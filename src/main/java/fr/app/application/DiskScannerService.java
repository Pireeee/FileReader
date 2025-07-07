package fr.app.application;

import fr.app.domain.DiskScanner;
import fr.app.domain.FileNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiskScannerService {
    private final ExecutorService executor;
    private final DiskScanner scanner;

    public DiskScannerService(DiskScanner scanner) {
        this.scanner = scanner;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public CompletableFuture<FileNode> scanAsync(Path root) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return scanner.scan(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void shutdown() {
        executor.shutdown();
    }
}

