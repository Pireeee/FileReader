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
import java.util.function.Consumer;

public class DiskScannerService {

    private final ExecutorService executor;
    private final DiskScanner scanner;

    public DiskScannerService(DiskScanner scanner, ExecutorService executor) {
        this.scanner = scanner;
        this.executor = executor;
    }

    public void scan(Path rootPath,
                     Consumer<ProgressInfo> progressCallback,
                     Consumer<Double> countingCallback,
                     Consumer<ScanResult> completionCallback,
                     Consumer<Throwable> errorCallback) {

        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return scanner.countFiles(rootPath, countingCallback);
                    } catch (IOException e) {
                        throw new CompletionException(e);
                    }
                }, executor)
                .thenCompose(totalCount -> {
                    Logger.info("Total files counted: " + totalCount);
                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            return scanner.scan(rootPath, progressCallback, totalCount);
                        } catch (IOException e) {
                            throw new CompletionException(e);
                        }
                    }, executor);
                })
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
