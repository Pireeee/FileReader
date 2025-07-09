package fr.app.infrastructure;

import fr.app.domain.DiskScanner;
import fr.app.domain.FileNode;
import fr.app.domain.ProgressInfo;
import fr.app.domain.ScanResult;
import fr.app.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

public class FileSystemScanner implements DiskScanner {

    private long totalFilesCount = 0;
    private long scannedFilesCount = 0;

    @Override
    public ScanResult scan(Path root, Consumer<ProgressInfo> progressCallback) throws IOException {
        totalFilesCount = countFiles(root);
        scannedFilesCount = 0;
        FileNode rootNode = new FileNode(root.getFileName().toString(), root, 0);
        Instant startTime = Instant.now();
        rootNode = scanRecursive(root, progress -> {
            scannedFilesCount++;
            double percent = totalFilesCount == 0 ? 0.0 : (double) scannedFilesCount / totalFilesCount;
            progressCallback.accept(new ProgressInfo(percent, totalFilesCount));
        });
        Instant endTime = Instant.now();
        return new ScanResult(rootNode, Duration.between(startTime, endTime));
    }

    private long countFiles(Path path) {
        File file = path.toFile();
        if (!file.exists()) return 0;

        if (file.isFile()) {
            return 1;
        }

        long count = 0;
        File[] files = file.listFiles();
        if (files != null) {
            for (File child : files) {
                count += countFiles(child.toPath());
            }
        }
        return count;
    }

    private FileNode scanRecursive(Path path, Consumer<Double> progressCallback) throws IOException {
        File file = path.toFile();
        long size = 0;
        FileNode node = new FileNode(file.getName(), path, 0);

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                throw new IOException("Impossible d'accéder au dossier : " + path);
            }
            for (File child : files) {
                FileNode childNode = scanRecursive(child.toPath(), progressCallback);
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

        // Mise à jour de la progression globale
        scannedFilesCount++;
        progressCallback.accept(scannedFilesCount / (double) totalFilesCount);

        node.setSize(size);
        return node;
    }
}