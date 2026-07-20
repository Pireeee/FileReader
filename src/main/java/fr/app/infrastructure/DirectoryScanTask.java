package fr.app.infrastructure;

import fr.app.domain.FileNode;
import fr.app.domain.ScanCancelledException;
import fr.app.utils.Logger;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Scans one directory. The caller (parent task, or FileSystemScanner for the root)
 * has already read this path's attributes and ruled out symlinks and junctions,
 * so no filesystem call is repeated here.
 */
class DirectoryScanTask extends RecursiveTask<FileNode> {

    private final Path path;
    private final BasicFileAttributes attributes;
    private final FileNodeFactory nodeFactory;
    private final ProgressReporter progressReporter;
    private final AtomicBoolean cancelled;

    public DirectoryScanTask(
            Path path,
            BasicFileAttributes attributes,
            FileNodeFactory nodeFactory,
            ProgressReporter progressReporter,
            AtomicBoolean cancelled
    ) {
        this.path = path;
        this.attributes = attributes;
        this.nodeFactory = nodeFactory;
        this.progressReporter = progressReporter;
        this.cancelled = cancelled;
    }

    @Override
    protected FileNode compute() {
        if (cancelled.get()) {
            throw new ScanCancelledException();
        }

        Logger.debug(() -> "Scanning directory: " + path);
        progressReporter.incrementFolders();

        List<FileNode> children;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            children = scanChildren(stream);
        } catch (IOException | DirectoryIteratorException e) {
            Logger.warn("Permission denied or inaccessible: " + path);
            return nodeFactory.createEmptyNode(path, true);
        }

        return nodeFactory.createDirectoryNode(path, attributes, children);
    }

    private List<FileNode> scanChildren(DirectoryStream<Path> stream) {
        List<FileNode> children = new ArrayList<>();
        List<DirectoryScanTask> subtasks = new ArrayList<>();

        for (Path child : stream) {
            if (cancelled.get()) {
                throw new ScanCancelledException();
            }

            BasicFileAttributes attrs;
            try {
                attrs = Files.readAttributes(
                        child, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            } catch (IOException e) {
                Logger.warn("Cannot read attributes: " + child);
                children.add(nodeFactory.createEmptyNode(child, false));
                continue;
            }

            if (attrs.isSymbolicLink() || attrs.isOther()) {
                Logger.debug(() -> "Skipping symlink or junction: " + child);
                children.add(nodeFactory.createEmptyNode(child, false));
            } else if (attrs.isDirectory()) {
                DirectoryScanTask subtask = new DirectoryScanTask(
                        child, attrs, nodeFactory, progressReporter, cancelled);
                subtask.fork();
                subtasks.add(subtask);
            } else {
                children.add(nodeFactory.createFileNode(child, attrs));
                progressReporter.incrementFiles(attrs.size());
            }
        }

        for (DirectoryScanTask subtask : subtasks) {
            children.add(subtask.join());
        }

        return children;
    }
}