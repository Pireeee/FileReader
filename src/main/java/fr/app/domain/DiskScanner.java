package fr.app.domain;

import fr.app.domain.FileNode;

import java.io.IOException;
import java.nio.file.Path;

public interface DiskScanner {
    FileNode scan(Path root) throws IOException;
}