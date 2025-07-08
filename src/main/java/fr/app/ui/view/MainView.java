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

    private static final Integer SCENE_WIDTH = 1024;
    private static final Integer SCENE_HEIGHT = 768;
    private static final Integer SIDEBAR_WIDTH = 250;

    public MainView() {
        treeView.setPrefHeight(400);
        scanButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        scanButton.setPrefWidth(SIDEBAR_WIDTH - 20);
        chooseFolderButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        chooseFolderButton.setPrefWidth(SIDEBAR_WIDTH - 20);
        pathField.setEditable(false);
        pathField.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-background-color: #ffffff; -fx-border-color: #cccccc;");
        pathField.setPrefWidth(400);
    }

    public void show(Stage stage) {
        VBox sidebar = new VBox(10, scanButton, chooseFolderButton, treeView);
        sidebar.setPrefWidth(SIDEBAR_WIDTH);
        VBox.setVgrow(treeView, Priority.ALWAYS); // Allow TreeView to grow

        treemapPane.setPrefSize(800, 600);
        treemapPane.setScaleShape(true);
        VBox.setVgrow(treemapPane, Priority.ALWAYS); // Allow Pane to grow

        VBox mainContainer = new VBox(10, pathField, treemapPane);
        HBox root = new HBox(sidebar, mainContainer);
        HBox.setHgrow(mainContainer, Priority.ALWAYS); // Allow main container to grow

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Disk Space Analyzer");
        stage.show();
    }
}
