package fr.app.application;

import java.util.concurrent.atomic.AtomicBoolean;

public class ScanHandle {

    private final AtomicBoolean cancelled;

    ScanHandle(AtomicBoolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        cancelled.set(true);
    }
}
