package fr.app.infrastructure;

import fr.app.domain.FileNode;
import fr.app.domain.ProgressInfo;
import fr.app.utils.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

class DirectoryScanTask extends RecursiveTask<FileNode> {

    private static final int PROGRESS_BATCH_SIZE = 100; // Threshold for batching progress updates
    private int progressBatchCounter = 0; // Counter to track progress updates

    private final Path path;
    private final FileSystemScanner scanner;
    private final Consumer<ProgressInfo> progressCallback;
    private final AtomicLong scannedFilesCount;
    private final AtomicLong scannedFoldersCount;
    private final AtomicLong totalSizeBytes;
    private final long startTimeNanos;

    public DirectoryScanTask(
            Path path,
            FileSystemScanner scanner,
            Consumer<ProgressInfo> progressCallback,
            AtomicLong scannedFilesCount,
            AtomicLong scannedFoldersCount,
            AtomicLong totalSizeBytes,
            long startTimeNanos
    ) {
        this.path = path;
        this.scanner = scanner;
        this.progressCallback = progressCallback;
        this.scannedFilesCount = scannedFilesCount;
        this.scannedFoldersCount = scannedFoldersCount;
        this.totalSizeBytes = totalSizeBytes;
        this.startTimeNanos = startTimeNanos;
    }

    @Override
    protected FileNode compute() {
        if (Files.isSymbolicLink(path)) {
            return handleSymbolicLink();
        }

        Logger.info("Scanning path: " + path);

        File file = path.toFile();
        FileNode node = new FileNode(file.getName(), path, 0);

        if (file.isDirectory()) {
            return processDirectory(file, node);
        } else {
            return processFile(file, node);
        }
    }

    private FileNode handleSymbolicLink() {
        Logger.info("Skipping symlink: " + path);
        return new FileNode(path.getFileName().toString(), path, 0);
    }

    private FileNode processDirectory(File file, FileNode node) {
        scannedFoldersCount.incrementAndGet();
        batchUpdateProgress();

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
                subtasks.add(createSubtask(child));
            } else {
                size += processChildFile(child, node);
            }
        }

        size += processSubtasks(subtasks, node);
        scanner.setChildrenPercentages(node, size);
        node.setSize(size);

        return node;
    }

    private DirectoryScanTask createSubtask(File child) {
        DirectoryScanTask subtask = new DirectoryScanTask(
                child.toPath(), scanner, progressCallback,
                scannedFilesCount, scannedFoldersCount, totalSizeBytes, startTimeNanos
        );
        subtask.fork();
        return subtask;
    }

    private long processChildFile(File child, FileNode node) {
        FileNode childNode = scanner.createFileNode(child);
        node.addChild(childNode);

        long fileSize = childNode.getSize();
        scannedFilesCount.incrementAndGet();
        totalSizeBytes.addAndGet(fileSize);

        progressBatchCounter++;
        if (progressBatchCounter >= PROGRESS_BATCH_SIZE) {
            batchUpdateProgress();
        }

        return fileSize;
    }

    private long processSubtasks(ArrayList<DirectoryScanTask> subtasks, FileNode node) {
        long size = 0;
        for (DirectoryScanTask subtask : subtasks) {
            FileNode childNode = subtask.join();
            node.addChild(childNode);
            size += childNode.getSize();

            progressBatchCounter++;
            if (progressBatchCounter >= PROGRESS_BATCH_SIZE) {
                batchUpdateProgress();
            }
        }
        return size;
    }

    private FileNode processFile(File file, FileNode node) {
        long fileSize = file.length();
        node.setSize(fileSize);

        scannedFilesCount.incrementAndGet();
        totalSizeBytes.addAndGet(fileSize);

        progressBatchCounter++;
        if (progressBatchCounter >= PROGRESS_BATCH_SIZE) {
            batchUpdateProgress();
        }

        return node;
    }

    private void batchUpdateProgress() {
        progressBatchCounter = 0; // Reset the counter
        Duration duration = Duration.ofNanos(System.nanoTime() - startTimeNanos);
        progressCallback.accept(new ProgressInfo(
                scannedFilesCount.get(),
                scannedFoldersCount.get(),
                duration,
                totalSizeBytes.get()
        ));
    }
}