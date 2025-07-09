package fr.app.domain;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface DiskScanner {

    long countFiles(Path root, Consumer<Double> countingCallback) throws IOException;

    ScanResult scan(Path root,
                    Consumer<ProgressInfo> progressCallback,
                    long totalFilesCount) throws IOException;

}
