package fr.app.infrastructure;

import fr.app.domain.DiskScanner;
import fr.app.domain.FileNode;
import fr.app.domain.ProgressInfo;
import fr.app.domain.ScanResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class FileSystemScanner implements DiskScanner {

    private final ForkJoinPool pool;

    public FileSystemScanner(ForkJoinPool pool) {
        this.pool = pool;
    }

    @Override
    public ScanResult scan(Path root, Consumer<ProgressInfo> progressCallback, AtomicBoolean cancelled) throws IOException {
        long startTimeNanos = System.nanoTime();
        ProgressReporter reporter = new ProgressReporter(progressCallback, startTimeNanos);
        FileNodeFactory nodeFactory = new FileNodeFactory();

        BasicFileAttributes rootAttributes = Files.readAttributes(
                root, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);

        FileNode rootNode;
        if (rootAttributes.isDirectory() && !rootAttributes.isOther()) {
            DirectoryScanTask scanTask = new DirectoryScanTask(
                    root,
                    rootAttributes,
                    nodeFactory,
                    reporter,
                    cancelled
            );
            rootNode = pool.invoke(scanTask);
        } else if (rootAttributes.isRegularFile()) {
            reporter.incrementFiles(rootAttributes.size());
            rootNode = nodeFactory.createFileNode(root, rootAttributes);
        } else {
            rootNode = nodeFactory.createEmptyNode(root, false);
        }

        return new ScanResult(rootNode);
    }

}