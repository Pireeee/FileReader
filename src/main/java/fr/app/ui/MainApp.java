package fr.app.ui;

import fr.app.application.DiskScannerService;
import fr.app.infrastructure.FileSystemScanner;
import fr.app.ui.controller.MainController;
import fr.app.ui.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DiskScannerService service = new DiskScannerService(new FileSystemScanner());
        MainView view = new MainView();
        MainController controller = new MainController(service, view, primaryStage);

        controller.init(); // configure listeners
        view.show(primaryStage); // build scene and show
    }

    @Override
    public void stop() {
        System.out.println("Closing app...");
        // Ton shutdown ici
    }
}
