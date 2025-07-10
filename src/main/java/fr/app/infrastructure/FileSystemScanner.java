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
        long startTimeNanos = System.nanoTime();
        ProgressReporter reporter = new ProgressReporter(progressCallback, startTimeNanos);
        FileNodeFactory nodeFactory = new FileNodeFactory();
        DirectoryScanTask scanTask = new DirectoryScanTask(
                root,
                this,
                nodeFactory,
                reporter
        );
        FileNode rootNode = pool.invoke(scanTask);

        return new ScanResult(rootNode);
    }

}
