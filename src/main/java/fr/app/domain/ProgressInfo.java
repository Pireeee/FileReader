package fr.app.domain;

import fr.app.utils.SizeFormatter;

import java.time.Duration;

public class ProgressInfo {
    private final long filesScanned;        // fichiers traités
    private final long foldersScanned;   // répertoires traités
    private final Duration duration; // durée du scan
    private final long totalSizeBytes; // taille totale des fichiers scannés en octets



    public ProgressInfo( long filesScanned, long foldersScanned, Duration duration, long totalSizeBytes) {
        this.filesScanned = filesScanned;
        this.foldersScanned = foldersScanned;
        this.duration = duration;
        this.totalSizeBytes = totalSizeBytes;
    }

    public long getFilesScanned() { return filesScanned; }
    public long getFoldersScanned() { return foldersScanned; }
    public long getTotalScannedElements() { return filesScanned + foldersScanned; }
    public Duration getDuration() { return duration;}
    public String getDurationFormatted() {
        long millis = duration.toMillis();
        long hours = millis / (1000 * 60 * 60);
        millis %= (1000 * 60 * 60);
        long minutes = millis / (1000 * 60);
        millis %= (1000 * 60);
        long seconds = millis / 1000;
        millis %= 1000;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
        } else {
            return String.format("%02d:%02d.%03d", minutes, seconds, millis);
        }
    }

    public String getScanSpeed() {
        if (duration.isZero() || duration.getSeconds() == 0) {
            return "0 files/s";
        }
        long speed = filesScanned / duration.getSeconds();
        return speed + " files/s";
    }
    public long getTotalSizeBytes() {
        return totalSizeBytes;
    }
    public String getTotalSize() {
        return SizeFormatter.format(totalSizeBytes);
    }

    public String getBytesSpeed() {
        if (duration.isZero() || duration.getSeconds() == 0) {
            return "0 B/s";
        }
        String speed = SizeFormatter.format(totalSizeBytes / duration.getSeconds());
        return speed + "/s";
    }
}

