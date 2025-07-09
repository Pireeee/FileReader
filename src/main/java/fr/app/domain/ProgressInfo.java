package fr.app.domain;

public class ProgressInfo {
    private final double progress;          // ex: 0.0 - 1.0 (barre)
    private final long filesScanned;        // fichiers traités
    private final long totalFiles;          // fichiers totaux
    private final long totalElements;       // fichiers + dossiers

    public ProgressInfo(double progress, long filesScanned, long totalFiles, long totalElements) {
        this.progress = progress;
        this.filesScanned = filesScanned;
        this.totalFiles = totalFiles;
        this.totalElements = totalElements;
    }

    public double getProgress() { return progress; }
    public long getFilesScanned() { return filesScanned; }
    public long getTotalFiles() { return totalFiles; }
    public long getTotalElements() { return totalElements; }
}

