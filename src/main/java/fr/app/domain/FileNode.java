package fr.app.domain;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileNode {
    private final String name;
    private final Path path;
    private final long size;
    private final List<FileNode> children;

    public FileNode(String name, Path path, long size) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.children = new ArrayList<>();
    }

    public void addChild(FileNode child) {
        children.add(child);
    }

    public String getName() {
        return this.name;
    }

    public List<FileNode> getChildren() {
        return this.children;
    }

    public long getSize() {
        return this.size;
    }

    public Path getPath() {
        return this.path;
    }
}
