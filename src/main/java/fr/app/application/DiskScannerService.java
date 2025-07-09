package fr.app.application;

import fr.app.domain.DiskScanner;
import fr.app.domain.ProgressInfo;
import fr.app.domain.ScanResult;
import fr.app.utils.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
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

    public void scan(Path rootPath,
                     Consumer<ProgressInfo> progressCallback,
                     Consumer<ScanResult> completionCallback,
                     Consumer<Throwable> errorCallback) {

        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return scanner.scan(rootPath, progressCallback);
                    } catch (IOException e) {
                        throw new CompletionException(e);
                    }
                }, executor)
                .thenAccept(completionCallback)
                .exceptionally(ex -> {
                    Logger.error("Erreur durant le scan : " + ex.getMessage(), ex);
                    errorCallback.accept(ex.getCause());
                    return null;
                });
    }

    public void shutdown() {
        executor.shutdown();
    }
}
