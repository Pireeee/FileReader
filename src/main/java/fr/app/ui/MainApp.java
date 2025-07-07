package fr.app.ui;

import fr.app.application.DiskScannerService;
import fr.app.domain.FileNode;
import fr.app.infrastructure.FileSystemScanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp extends Application {

    private DiskScannerService scannerService;

    @Override
    public void start(Stage primaryStage) {
        FileSystemScanner fileSystemScanner = new FileSystemScanner();
        scannerService = new DiskScannerService(fileSystemScanner);

        TreeView<String> treeView = new TreeView<>();
        Button scanButton = new Button("Scan Disk");

        VBox root = new VBox(10,scanButton, treeView);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Disk Scanner");
        primaryStage.setScene(scene);
        primaryStage.show();

        MainController controller = new MainController(treeView, scannerService);

        scanButton.setOnAction(e-> controller.startScan());
        primaryStage.setOnCloseRequest(e -> stop());
    }

    @Override
    public void stop() {
        if (scannerService != null) {
            scannerService.shutdown();
        }
    }

}

