package fr.app.domain;

public class ProgressInfo {
    private final double progress;      // entre 0 et 1
    private final long filesScanned;    // nombre de fichiers trouvés

    public ProgressInfo(double progress, long filesScanned) {
        this.progress = progress;
        this.filesScanned = filesScanned;
    }

    public double getProgress() {
        return progress;
    }

    public long getFilesScanned() {
        return filesScanned;
    }
}
