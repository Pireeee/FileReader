package fr.app.ui;

import fr.app.application.DiskScannerService;
import fr.app.domain.FileNode;
import fr.app.infrastructure.FileSystemScanner;
import fr.app.utils.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp extends Application {

    private MainController controller;

    private Stage stage;

    private static final String DEFAULT_ROOT_PATH = "C:/";

    private String path = DEFAULT_ROOT_PATH; // Default path to scan

    private TextField pathTextField;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Pane treemapPane = new Pane();
        TreeView<String> treeView = new TreeView<>();
        this.controller = new MainController(treeView, treemapPane);

        initializeUI(treemapPane, treeView);
    }

    private Button createButton(String text, String style) {
        Button button = new Button(text);
        button.setStyle(style);
        return button;
    }

    private void initializeUI(Pane treemapPane, TreeView<String> treeView) {
        Button scanButton = createButton("Scan Disk", "-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        Button choseFolderButton = createButton("Select Path", "-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        choseFolderButton.setOnAction(e -> onChooseFolderClicked());
        treeView.setPrefHeight(400);

        VBox sidebar = new VBox(10, scanButton, choseFolderButton, treeView);
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-padding: 10; -fx-background-color: #e0e0e0;");


        treemapPane.setStyle("-fx-background-color: #f0f0f0;");
        treemapPane.setPrefSize(800, 768);

        pathTextField = new TextField(path);
        pathTextField.setEditable(false);
        pathTextField.setPrefWidth(400);

        VBox mainContainer = new VBox(10, pathTextField, treemapPane);

        HBox rootLayout = new HBox(sidebar, mainContainer);

        Scene scene = new Scene(rootLayout, 1024, 768);
        stage.setScene(scene);
        stage.setTitle("Pire's Disk Space Analyzer");
        stage.show();

        scanButton.setOnAction(e -> {
            Logger.info("Starting disk scan...");
            Path rootPath = Paths.get(path);
            controller.startScan(rootPath);
        });
        stage.setOnCloseRequest(e -> stop());
    }

    private void onChooseFolderClicked() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choisir un dossier à scanner");

        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            path = selectedDirectory.getAbsolutePath(); // Mets à jour la variable
            pathTextField.setText(path); // Mets à jour le champ visuel !
        }
    }


    @Override
    public void stop() {
        controller.shutdown();
    }

}

