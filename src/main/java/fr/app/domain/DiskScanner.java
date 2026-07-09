package fr.app.domain;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public interface DiskScanner {
    ScanResult scan(Path root, Consumer<ProgressInfo> progressCallback, AtomicBoolean cancelled) throws IOException;
}
