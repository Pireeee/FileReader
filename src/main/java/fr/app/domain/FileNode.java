package fr.app.domain;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

public class FileNode {

    private final String name;
    private final Path path;
    private final LongProperty size = new SimpleLongProperty();
    private final DoubleProperty percentOfParent = new SimpleDoubleProperty();
    private final boolean isDirectory;
    private final Instant lastModified;
    private final String extension;
    private final List<FileNode> children;

    public FileNode(String name,
                    Path path,
                    long size,
                    double percentOfParent,
                    boolean isDirectory,
                    Instant lastModified,
                    String extension,
                    List<FileNode> children) {
        this.name = name;
        this.path = path;
        this.size.set(size);
        this.percentOfParent.set(percentOfParent);
        this.isDirectory = isDirectory;
        this.lastModified = lastModified;
        this.extension = extension;
        this.children = children;
    }

    public String getName() { return name; }
    public Path getPath() { return path; }
    public long getSize() { return size.get(); }
    public void setSize(long size) { this.size.set(size); }
    public LongProperty sizeProperty() { return size; }

    public double getPercentOfParent() { return percentOfParent.get(); }
    public void setPercentOfParent(double percent) { this.percentOfParent.set(percent); }
    public DoubleProperty percentOfParentProperty() { return percentOfParent; }
    public boolean isDirectory() { return isDirectory; }
    public int getChildrenCount() {
        return children != null ? children.size() : 0;
    }
    public Instant getLastModified() { return lastModified; }
    public String getExtension() { return extension; }
    public List<FileNode> getChildren() { return children; }

    public FileNode withUpdatedPercentOfParent(double newPercent) {
        return new FileNode(
                name,
                path,
                getSize(),
                newPercent,
                isDirectory,
                lastModified,
                extension,
                children
        );
    }
    public void recalculateChildrenPercents() {
        long total = this.getChildren().stream()
                .mapToLong(FileNode::getSize).sum();

        for (FileNode child : this.getChildren()) {
            double newPercent = total == 0 ? 0 :
                    ((double) child.getSize() * 100) / total;
            child.setPercentOfParent(newPercent);
        }
    }

}
