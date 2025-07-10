package fr.app.ui.view.component;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatisticsComponent extends VBox {

    public final Label pathLabel = new Label();
    public final Label foldersCountLabel = new Label("Dossiers analysés : 0");
    public final Label filesCountLabel = new Label("Fichiers analysés : 0");
    public final Label sizeTotalLabel = new Label("Taille cumulée : 0 B");
    public final Label filesSpeedLabel = new Label("Vitesse fichiers : 0 f/s");
    public final Label bytesSpeedLabel = new Label("Vitesse lecture : 0 B/s");
    public final Label durationLabel = new Label("Durée : 00:00:00");

    public StatisticsComponent() {
        this.setSpacing(10);
        getStyleClass().add("statistics-component");
        this.getChildren().addAll(
                pathLabel,
                foldersCountLabel,
                filesCountLabel,
                sizeTotalLabel,
                filesSpeedLabel,
                bytesSpeedLabel,
                durationLabel
        );
    }

    public void setPath(String path) {
        Platform.runLater(() -> pathLabel.setText("Chemin : " + path));
    }

    public void updateStats(
            String foldersText,
            String filesText,
            String sizeText,
            String filesSpeedText,
            String bytesSpeedText,
            String durationText
    ) {
        Platform.runLater(() -> {
            foldersCountLabel.setText(foldersText);
            filesCountLabel.setText(filesText);
            sizeTotalLabel.setText(sizeText);
            filesSpeedLabel.setText(filesSpeedText);
            bytesSpeedLabel.setText(bytesSpeedText);
            durationLabel.setText(durationText);
        });
    }
}
