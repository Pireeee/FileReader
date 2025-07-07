package fr.app.ui;

import fr.app.application.DiskScannerService;
import fr.app.domain.FileNode;
import fr.app.infrastructure.FileSystemScanner;
import fr.app.utils.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp extends Application {

    private DiskScannerService scannerService;

    @Override
    public void start(Stage primaryStage) {
        Pane treemapPane = new Pane(); // Create treemapPane here
        TreeView<String> treeView = new TreeView<>();
        MainController controller = new MainController(treeView, treemapPane); // Pass treemapPane to MainController
        initializeUI(primaryStage, controller, treemapPane, treeView); // Pass treemapPane to initializeUI
    }

    private Button createButton(String text, String style) {
        Button button = new Button(text);
        button.setStyle(style);
        return button;
    }

    private void initializeUI(Stage primaryStage, MainController controller, Pane treemapPane, TreeView<String> treeView) {
        Button scanButton = createButton("Scan Disk", "-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        Button selectPathButton = createButton("Select Path", "-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #2196F3; -fx-text-fill: white;");

        treeView.setPrefHeight(400);

        VBox sidebar = new VBox(10, scanButton, selectPathButton, treeView);
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-padding: 10; -fx-background-color: #e0e0e0;");

        treemapPane.setStyle("-fx-background-color: #f0f0f0;");
        treemapPane.setPrefSize(800, 768);

        HBox rootLayout = new HBox(sidebar, treemapPane);

        Scene scene = new Scene(rootLayout, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pire's Disk Space Analyzer");
        primaryStage.show();

        scanButton.setOnAction(e -> {
            Logger.info("Starting disk scan...");
            Path rootPath = Paths.get("C:/Users/pierr/Documents/BankHeist");
            controller.startScan(rootPath);
        });
        primaryStage.setOnCloseRequest(e -> stop());
    }

    @Override
    public void stop() {
        if (scannerService != null) {
            scannerService.shutdown();
        }
    }

}

