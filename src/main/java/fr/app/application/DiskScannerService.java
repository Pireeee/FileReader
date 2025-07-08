package fr.app.application;

import fr.app.domain.DiskScanner;
import fr.app.domain.FileNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class DiskScannerService {

    private final ExecutorService executor;
    private final DiskScanner scanner;

    public DiskScannerService(DiskScanner scanner) {
        this.scanner = scanner;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void scan(Path rootPath, Consumer<FileNode> callback) {
        executor.submit(() -> {
            try {
                FileNode result = scanner.scan(rootPath);
                callback.accept(result);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors du scan : " + e.getMessage(), e);
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}
