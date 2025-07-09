package fr.app.ui;

import fr.app.application.DiskScannerService;
import fr.app.infrastructure.FileSystemScanner;
import fr.app.ui.controller.MainController;
import fr.app.ui.view.MainView;
import fr.app.utils.NamedThreadFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    MainController controller;

    @Override
    public void start(Stage primaryStage) {

        ForkJoinPool forkJoinPool = new ForkJoinPool(
                Runtime.getRuntime().availableProcessors()
        );

        FileSystemScanner scanner = new FileSystemScanner(forkJoinPool);
        DiskScannerService service = new DiskScannerService(scanner, forkJoinPool);

        MainView view = new MainView();
        this.controller = new MainController(service, view, primaryStage);

        controller.init();
        view.show(primaryStage);
    }

    @Override
    public void stop() {
        System.out.println("Closing app...");
        controller.shutdown();
    }

}
