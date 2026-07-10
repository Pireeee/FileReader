package fr.app.ui.view.table;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ScanningOverlayComponent extends StackPane {

    private final RotateTransition rotateTransition;

    public ScanningOverlayComponent() {
        getStyleClass().add("scanning-overlay");
        setVisible(false);
        setManaged(false);
        setMouseTransparent(true);

        ImageView spinner = new ImageView(new Image(getClass().getResourceAsStream("/icon/file-reader-icon-256.png")));
        spinner.setFitWidth(64);
        spinner.setFitHeight(64);

        rotateTransition = new RotateTransition(Duration.seconds(1), spinner);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);

        Label label = new Label("Scanning...");
        label.getStyleClass().add("scanning-overlay-label");

        VBox content = new VBox(12, spinner, label);
        content.setAlignment(Pos.CENTER);

        getChildren().add(content);
    }

    public void setScanning(boolean scanning) {
        setVisible(scanning);
        setManaged(scanning);
        if (scanning) {
            rotateTransition.play();
        } else {
            rotateTransition.stop();
        }
    }
}
