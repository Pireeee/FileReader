package fr.app.domain;

public class ScanCancelledException extends RuntimeException {
    public ScanCancelledException() {
        super("Scan cancelled");
    }
}
