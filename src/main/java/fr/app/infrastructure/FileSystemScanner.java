package fr.app.infrastructure;

import fr.app.domain.DiskScanner;
import fr.app.domain.FileNode;
import fr.app.domain.ProgressInfo;
import fr.app.domain.ScanResult;
import fr.app.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class FileSystemScanner implements DiskScanner {

    private final ForkJoinPool pool;

    public FileSystemScanner(ForkJoinPool pool) {
        this.pool = pool;
    }

    public long countFiles(Path root, Consumer<Double> countingCallback) throws IOException {
        long total = countFilesRecursive(root, countingCallback, new AtomicLong(0));
        Logger.info("Comptage terminé : " + total + " fichiers.");
        return total;
    }

    private long countFilesRecursive(Path path, Consumer<Double> countingCallback, AtomicLong counter) {
        File file = path.toFile();
        if (!file.exists()) return 0;

        if (file.isFile()) {
            long c = counter.incrementAndGet();
            countingCallback.accept(0.5 * (c / 100_000.0)); // progress approx pour phase 1
            return 1;
        }

        long count = 0;
        File[] files = file.listFiles();
        if (files != null) {
            for (File child : files) {
                count += countFilesRecursive(child.toPath(), countingCallback, counter);
            }
        }
        return count;
    }

    public ScanResult scan(Path root, Consumer<ProgressInfo> progressCallback, long totalFilesCount) throws IOException {
        AtomicLong scannedFilesCount = new AtomicLong(0);
        AtomicLong totalElementsCount = new AtomicLong(totalFilesCount);

        FileNode rootNode = pool.invoke(new DirectoryScanTask(
                root, this, progressCallback, totalFilesCount, scannedFilesCount, totalElementsCount
        ));

        return new ScanResult(rootNode);
    }

    FileNode createFileNode(File file) {
        return new FileNode(file.getName(), file.toPath(), file.length());
    }

    void setChildrenPercentages(FileNode parent, long totalSize) {
        for (FileNode child : parent.getChildren()) {
            double percent = totalSize == 0 ? 0.0 : (double) child.getSize() / totalSize;
            child.setPercentage(percent);
        }
    }
}
