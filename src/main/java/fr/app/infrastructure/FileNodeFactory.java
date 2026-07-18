package fr.app.infrastructure;

import fr.app.domain.FileNode;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class FileNodeFactory {

    public FileNode createFileNode(Path path, BasicFileAttributes attributes) {
        String name = nameOf(path);
        return new FileNode(
                name,
                path,
                attributes.size(),
                0,
                false,
                attributes.lastModifiedTime().toInstant(),
                getFileExtension(name),
                new ArrayList<>()
        );
    }

    public FileNode createEmptyNode(Path path, boolean isDirectory) {
        return new FileNode(
                nameOf(path),
                path,
                0,
                0,
                isDirectory,
                Instant.now(),
                "",
                new ArrayList<>()
        );
    }

    public FileNode createDirectoryNode(Path path, BasicFileAttributes attributes, List<FileNode> children) {
        long totalSize = children.stream().mapToLong(FileNode::getSize).sum();
        List<FileNode> updated = updateChildrenPercentages(children, totalSize);

        return new FileNode(
                nameOf(path),
                path,
                totalSize,
                0,
                true,
                attributes.lastModifiedTime().toInstant(),
                "",
                updated
        );
    }

    // getFileName() is null for filesystem roots like C:\
    private String nameOf(Path path) {
        Path fileName = path.getFileName();
        return fileName != null ? fileName.toString() : path.toString();
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