package fr.app.infrastructure;

import fr.app.domain.FileNode;
import fr.app.domain.ProgressInfo;
import fr.app.utils.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

class DirectoryScanTask extends RecursiveTask<FileNode> {

    private final Path path;
    private final FileSystemScanner scanner;
    private final Consumer<ProgressInfo> progressCallback;
    private final long totalFilesCount;
    private final AtomicLong scannedFilesCount;
    private final AtomicLong totalElementsCount;

    public DirectoryScanTask(Path path,
                             FileSystemScanner scanner,
                             Consumer<ProgressInfo> progressCallback,
                             long totalFilesCount,
                             AtomicLong scannedFilesCount,AtomicLong totalElementsCount) {
        this.path = path;
        this.scanner = scanner;
        this.progressCallback = progressCallback;
        this.totalFilesCount = totalFilesCount;
        this.scannedFilesCount = scannedFilesCount;
        this.totalElementsCount = totalElementsCount;
    }

    @Override
    protected FileNode compute() {
        if (Files.isSymbolicLink(path)) {
            Logger.info("Skipping symlink: " + path);
            return new FileNode(path.getFileName().toString(), path, 0);
        }

        Logger.info("Scanning path: " + path);

        File file = path.toFile();
        FileNode node = new FileNode(file.getName(), path, 0);

        if (file.isDirectory()) {
            totalElementsCount.incrementAndGet();
            if (!file.canRead()) {
                Logger.warn("Cannot read directory: " + file.getPath());
                return node;
            }
            File[] files = file.listFiles();
            if (files == null) {
                Logger.warn("Permission denied or inaccessible: " + file.getPath());
                return node;
            }

            long size = 0;
            var subtasks = new ArrayList<DirectoryScanTask>();

            for (File child : files) {
                if (child.isDirectory()) {
                    totalElementsCount.incrementAndGet();
                    DirectoryScanTask subtask = new DirectoryScanTask(child.toPath(), scanner, progressCallback, totalFilesCount, scannedFilesCount, totalElementsCount);
                    subtask.fork();
                    subtasks.add(subtask);
                } else {
                    FileNode childNode = scanner.createFileNode(child);
                    node.addChild(childNode);
                    size += childNode.getSize();
                    updateProgress();
                }
            }

            for (DirectoryScanTask t : subtasks) {
                FileNode childNode = t.join();
                node.addChild(childNode);
                size += childNode.getSize();
            }

            scanner.setChildrenPercentages(node, size);
            node.setSize(size);
            updateProgress();

        } else {
            node.setSize(file.length());
            updateProgress();
        }

        return node;
    }

    private void updateProgress() {
        long scanned = scannedFilesCount.incrementAndGet();
        long elements = totalElementsCount.get();
        double percent = 0.5 + 0.5 * ((double) scanned / totalFilesCount);
        progressCallback.accept(new ProgressInfo(percent, scanned, totalFilesCount, elements));
    }
}
