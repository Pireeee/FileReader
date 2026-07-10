package fr.app.application;

import fr.app.domain.DiskScanner;
import fr.app.domain.ProgressInfo;
import fr.app.domain.ScanCancelledException;
import fr.app.domain.ScanResult;
import fr.app.utils.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class DiskScannerService {

    private final ExecutorService executor;
    private final DiskScanner scanner;

    public DiskScannerService(DiskScanner scanner, ExecutorService executor) {
        this.scanner = scanner;
        this.executor = executor;
    }

    public ScanHandle scan(Path rootPath,
                     Consumer<ProgressInfo> progressCallback,
                     Consumer<ScanResult> completionCallback,
                     Consumer<Throwable> errorCallback,
                     Runnable cancelledCallback) {

        AtomicBoolean cancelled = new AtomicBoolean(false);

        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return scanner.scan(rootPath, progressCallback, cancelled);
                    } catch (IOException e) {
                        throw new CompletionException(e);
                    }
                }, executor)
                .thenAccept(completionCallback)
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    if (cause instanceof ScanCancelledException) {
                        cancelledCallback.run();
                    } else {
                        Logger.error("Error during scan: " + cause.getMessage(), cause);
                        errorCallback.accept(cause);
                    }
                    return null;
                });

        return new ScanHandle(cancelled);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
