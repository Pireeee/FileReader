package fr.app.infrastructure;

import fr.app.domain.DiskScanner;
import fr.app.domain.FileNode;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import fr.app.utils.Logger;

public class FileSystemScanner implements DiskScanner {

    @Override
    public FileNode scan(Path root) throws IOException {
        long size = 0;
        FileNode node = new FileNode(root.getFileName().toString(), root, size);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(root)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    FileNode child = scan(entry);
                    node.addChild(child);
                    size += child.getSize();
                    continue;
                }

                long fileSize = Files.size(entry);
                FileNode child = new FileNode(entry.getFileName().toString(), entry, fileSize);
                node.addChild(child);
                size += fileSize;
            }
        } catch (IOException e) {
            Logger.warn("Error scanning directory: " + e.getMessage());
            throw e;
        }
        return new FileNode(node.getName(), node.getPath(), size);
    }
}
