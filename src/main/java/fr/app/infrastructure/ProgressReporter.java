package fr.app.infrastructure;

import fr.app.domain.ProgressInfo;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

class ProgressReporter {

    private static final int BATCH_SIZE = 100;

    private final Consumer<ProgressInfo> callback;
    private final AtomicLong files = new AtomicLong(0);
    private final AtomicLong folders = new AtomicLong(0);
    private final AtomicLong bytes = new AtomicLong(0);
    private final long startTimeNanos;
    private final AtomicInteger batchCounter = new AtomicInteger(0);

    public ProgressReporter(Consumer<ProgressInfo> callback, long startTimeNanos) {
        this.callback = callback;
        this.startTimeNanos = startTimeNanos;
    }

    public void incrementFiles(long size) {
        files.incrementAndGet();
        bytes.addAndGet(size);
        maybeReport();
    }

    public void incrementFolders() {
        folders.incrementAndGet();
        maybeReport();
    }

    private void maybeReport() {
        if (batchCounter.incrementAndGet() >= BATCH_SIZE) {
            batchCounter.set(0);
            callback.accept(createProgressInfo());
        }
    }

    private ProgressInfo createProgressInfo() {
        return new ProgressInfo(
                files.get(),
                folders.get(),
                Duration.ofNanos(System.nanoTime() - startTimeNanos),
                bytes.get()
        );
    }
}
