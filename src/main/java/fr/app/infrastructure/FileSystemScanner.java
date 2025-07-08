package fr.app.infrastructure;

import fr.app.domain.DiskScanner;
import fr.app.domain.FileNode;
import fr.app.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class FileSystemScanner implements DiskScanner {

    @Override
    public FileNode scan(Path root) throws IOException {
        File file = root.toFile();
        return scanRec(file.toPath());
    }

    private FileNode scanRec(Path path) throws IOException {
        File file = path.toFile();
        long size = 0;
        FileNode node = new FileNode(file.getName(), path, 0);

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                throw new IOException("Impossible d'accéder au dossier : " + path);
            }
            for (File child : files) {
                FileNode childNode = scanRec(child.toPath());
                node.addChild(childNode);
                size += childNode.getSize();
                Logger.info("Scanned: " + child.getName() + " - Size: " + childNode.getSize());
            }
            // Calcule le pourcentage pour chaque enfant
            for (FileNode child : node.getChildren()) {
                double percent = size == 0 ? 0.0 : (double) child.getSize() / size;
                child.setPercentage(percent);
            }
        } else {
            size = file.length();
        }
        node.setSize(size);
        return node;
    }
}
