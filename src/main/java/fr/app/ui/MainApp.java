package fr.app.ui;

import fr.app.application.DiskScannerService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp extends Application {

    private DiskScannerService scannerService;

    @Override
    public void start(Stage primaryStage) {
        TreeView<String> treeView = new TreeView<>();
        Button scanButton = new Button("Scan Disk");
        VBox root = new VBox(10, scanButton, treeView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Disk Scanner");
        primaryStage.show();

        MainController controller = new MainController(treeView);

        scanButton.setOnAction(e -> {
            Path rootPath = Paths.get("C:/"); // à adapter
            controller.startScan(rootPath);
        });
    }

}

