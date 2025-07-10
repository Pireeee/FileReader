package fr.app.infrastructure;

import fr.app.domain.FileNode;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class FileNodeFactory {

    public FileNode createFileNode(File file) {
        return new FileNode(
                file.getName(),
                file.toPath(),
                file.length(),
                0,
                false,
                Instant.ofEpochMilli(file.lastModified()),
                getFileExtension(file.getName()),
                new ArrayList<>()
        );
    }

    public FileNode createEmptyNode(String name, Path path, boolean isDirectory) {
        return new FileNode(
                name,
                path,
                0,
                0,
                isDirectory,
                Instant.now(),
                "",
                new ArrayList<>()
        );
    }

    public FileNode createDirectoryNode(File directory, List<FileNode> children) {
        long totalSize = children.stream().mapToLong(FileNode::getSize).sum();
        List<FileNode> updated = updateChildrenPercentages(children, totalSize);

        return new FileNode(
                directory.getName(),
                directory.toPath(),
                totalSize,
                0,
                true,
                Instant.ofEpochMilli(directory.lastModified()),
                "",
                updated
        );
    }

    private List<FileNode> updateChildrenPercentages(List<FileNode> children, long totalSize) {
        List<FileNode> updated = new ArrayList<>();
        for (FileNode child : children) {
            double percentOfParent = totalSize > 0 ? (double) child.getSize() / totalSize * 100 : 0;
            updated.add(child.withUpdatedPercentOfParent(percentOfParent));
        }
        return updated;
    }

    private String getFileExtension(String name) {
        int dot = name.lastIndexOf(".");
        return (dot > 0 && dot < name.length() - 1) ? name.substring(dot + 1) : "";
    }
}
