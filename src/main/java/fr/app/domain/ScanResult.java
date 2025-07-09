package fr.app.domain;

import java.time.Duration;

public class ScanResult {
    private final FileNode rootNode;
//    private final Duration duration;

    public ScanResult(FileNode rootNode) {
        this.rootNode = rootNode;
//        this.duration = duration;
    }

    public FileNode getRootNode() {
        return rootNode;
    }

//    public Duration getDuration() {
//        return duration;
//    }
}

