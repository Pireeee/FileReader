package fr.app.domain;

import fr.app.domain.FileNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface DiskScanner {
    FileNode scan(Path root, Consumer<Double> progressCallBack) throws IOException;
}