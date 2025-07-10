package fr.app.ui.view.component;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ProgressComponent extends VBox {

    public final Label pathLabel = new Label();
    public final Label foldersCountLabel = new Label("Dossiers analys\u00E9s : 0");
    public final Label filesCountLabel = new Label("Fichiers analys\u00E9s : 0");
    public final Label sizeTotalLabel = new Label("Taille cumul\u00E9e : 0 B");
    public final Label speedLabel = new Label("Vitesse : 0 f/s");
    public final Label durationLabel = new Label("Dur\u00E9e : 00:00");

    private long startTimeMillis;

    public ProgressComponent() {
        this.setSpacing(10);
        getStyleClass().add("progress-component");
        this.getChildren().addAll(
                pathLabel,
                foldersCountLabel,
                filesCountLabel,
                sizeTotalLabel,
                speedLabel,
                durationLabel
        );
    }

    public void setPath(String path) {
        Platform.runLater(() -> pathLabel.setText("Chemin : " + path));
    }

    public void updateStats(long folders, long files, long totalSizeBytes, long startTimeMillis) {
        long elapsedSeconds = Math.max((System.currentTimeMillis() - startTimeMillis) / 1000, 1);
        double speed = files / (double) elapsedSeconds;

        Platform.runLater(() -> {
            foldersCountLabel.setText("Dossiers analys\u00E9s : " + folders);
            filesCountLabel.setText("Fichiers analys\u00E9s : " + files);
            sizeTotalLabel.setText("Taille cumul\u00E9e : " + formatSize(totalSizeBytes));
            speedLabel.setText(String.format("Vitesse : %.2f f/s", speed));
            durationLabel.setText("Dur\u00E9e : " + formatDuration(elapsedSeconds));
        });
    }

    public void startTimer() {
        startTimeMillis = System.currentTimeMillis();
    }

    public void stopTimer() {
        // rien à faire ici si tu n’as pas besoin
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String unit = "KMGTPE".charAt(exp - 1) + "o";
        return String.format("%.1f %s", bytes / Math.pow(1024, exp), unit);
    }

    private String formatDuration(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }
}
