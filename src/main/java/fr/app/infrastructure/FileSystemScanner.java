package fr.app.infrastructure;

import fr.app.domain.DiskScanner;
import fr.app.domain.FileNode;
import fr.app.domain.ProgressInfo;
import fr.app.domain.ScanResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class FileSystemScanner implements DiskScanner {

    private final ForkJoinPool pool;

    public FileSystemScanner(ForkJoinPool pool) {
        this.pool = pool;
    }

    @Override
    public ScanResult scan(Path root, Consumer<ProgressInfo> progressCallback) throws IOException {
        AtomicLong scannedFilesCount = new AtomicLong(0);
        AtomicLong scannedFoldersCount = new AtomicLong(0);
        AtomicLong totalSizeBytes = new AtomicLong(0);
        long startTimeNanos = System.nanoTime();

        FileNode rootNode = pool.invoke(new DirectoryScanTask(
                root,
                this,
                progressCallback,
                scannedFilesCount,
                scannedFoldersCount,
                totalSizeBytes,
                startTimeNanos
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
