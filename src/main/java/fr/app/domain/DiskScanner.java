package fr.app.domain;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface DiskScanner {
    ScanResult scan(Path root, Consumer<ProgressInfo> progressCallback) throws IOException;
}
