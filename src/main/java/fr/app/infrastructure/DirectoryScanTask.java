package fr.app.infrastructure;

import fr.app.domain.FileNode;
import fr.app.utils.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;


class DirectoryScanTask extends RecursiveTask<FileNode> {

    private final Path path;
    private final FileSystemScanner scanner;
    private final FileNodeFactory nodeFactory;
    private final ProgressReporter progressReporter;

    public DirectoryScanTask(
            Path path,
            FileSystemScanner scanner,
            FileNodeFactory nodeFactory,
            ProgressReporter progressReporter
    ) {
        this.path = path;
        this.scanner = scanner;
        this.nodeFactory = nodeFactory;
        this.progressReporter = progressReporter;
    }

    @Override
    protected FileNode compute() {
        if (Files.isSymbolicLink(path)) {
            Logger.info("Skipping symlink: " + path);
            return nodeFactory.createEmptyNode(path.getFileName().toString(), path, false);
        }

        Logger.info("Scanning path: " + path);
        File file = path.toFile();

        if (file.isDirectory()) {
            return processDirectory(file);
        } else {
            return processFile(file);
        }
    }

    private FileNode processDirectory(File directory) {
        progressReporter.incrementFolders();

        if (!directory.canRead()) {
            Logger.warn("Cannot read directory: " + directory.getPath());
            return nodeFactory.createEmptyNode(directory.getName(), directory.toPath(), true);
        }

        File[] files = directory.listFiles();
        if (files == null) {
            Logger.warn("Permission denied or inaccessible: " + directory.getPath());
            return nodeFactory.createEmptyNode(directory.getName(), directory.toPath(), true);
        }

        List<FileNode> children = scanChildren(files);
        return nodeFactory.createDirectoryNode(directory, children);
    }

    private List<FileNode> scanChildren(File[] files) {
        List<FileNode> children = new ArrayList<>();
        List<DirectoryScanTask> subtasks = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory()) {
                DirectoryScanTask subtask = new DirectoryScanTask(
                        file.toPath(), scanner, nodeFactory, progressReporter);
                subtask.fork();
                subtasks.add(subtask);
            } else {
                children.add(nodeFactory.createFileNode(file));
                progressReporter.incrementFiles(file.length());
            }
        }

        for (DirectoryScanTask subtask : subtasks) {
            children.add(subtask.join());
        }

        return children;
    }

    private FileNode processFile(File file) {
        long fileSize = file.length();
        progressReporter.incrementFiles(fileSize);
        return nodeFactory.createFileNode(file);
    }
}
