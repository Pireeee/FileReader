package fr.app.domain;

import fr.app.domain.FileNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface DiskScanner {
    ScanResult scan(Path rootPath, Consumer<ProgressInfo> progressCallback) throws IOException;
}