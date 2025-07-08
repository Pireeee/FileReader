package fr.app.ui.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView {

    public final Button scanButton = new Button("Scan Disk");
    public final Button chooseFolderButton = new Button("Select Path");
    public final TextField pathField = new TextField();
    public final TreeView<String> treeView = new TreeView<>();
    public final Pane treemapPane = new Pane();

    public MainView() {
        scanButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        chooseFolderButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #2196F3; -fx-text-fill: white;");

        pathField.setEditable(false);
        pathField.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-background-color: #ffffff; -fx-border-color: #cccccc;");
    }

    public void show(Stage stage) {
        pathField.setEditable(false);
        treeView.setPrefHeight(400);
        VBox sidebar = new VBox(10, scanButton, chooseFolderButton, treeView);
        sidebar.setPrefWidth(250);
        treemapPane.setPrefSize(800, 600);
        treemapPane.setScaleShape(true);
        VBox mainContainer = new VBox(10, pathField, treemapPane);
        HBox root = new HBox(sidebar, mainContainer);

        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.setTitle("Disk Space Analyzer");
        stage.show();
    }
}
