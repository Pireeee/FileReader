package fr.app.infrastructure;

import fr.app.domain.DiskScanner;
import fr.app.domain.FileNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import fr.app.utils.Logger;

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
                if (child.isHidden()) {
                    Logger.info("Fichier ou dossier caché ignoré : " + child.getName());
                    continue; // Ignore hidden files and directories
                }
                FileNode childNode = scanRec(child.toPath());
                node.addChild(childNode);
                size += childNode.getSize();
                Logger.info("Scanned: " + child.getName() + " - Size: " + childNode.getSize());
            }
        } else {
            size = file.length();
        }

        node.setSize(size);

        return node;
    }

}
