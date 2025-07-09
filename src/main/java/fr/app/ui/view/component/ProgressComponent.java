package fr.app.ui.view.component;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ProgressComponent extends VBox {
    public final TextField pathField = new TextField();
    public final ProgressBar progressBar = new ProgressBar(0);

    public ProgressComponent() {
        setSpacing(10);

        pathField.setEditable(false);
        pathField.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-background-color: #ffffff; -fx-border-color: #cccccc;");
        pathField.setPrefWidth(400);

        getChildren().addAll(pathField, new Label("Progress:"), progressBar);
    }

    public void setPath(String path) {
        pathField.setText(path);
    }

    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }
}

