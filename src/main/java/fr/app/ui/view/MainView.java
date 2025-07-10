package fr.app.ui.view;

import fr.app.ui.view.component.MainContainerComponent;
import fr.app.ui.view.component.SidebarComponent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class MainView {
    private final SidebarComponent sidebar = new SidebarComponent();
    private final MainContainerComponent mainContainer = new MainContainerComponent();

    private static final String APP_NAME = "Pire's File Reader";
    private static final String GLOBAL_CSS = "/style.css";
    private static final int SCENE_WIDTH = 1024;
    private static final int SCENE_HEIGHT = 768;

    public void show(Stage stage) {
        SplitPane corePane = new SplitPane();
        corePane.getItems().addAll(sidebar, mainContainer);
        corePane.setDividerPositions(0.4); // Set initial divider position
        corePane.getStyleClass().add("root");
        corePane.setPadding(new Insets(20));

        Scene scene = new Scene(corePane, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(getClass().getResource(GLOBAL_CSS).toExternalForm());

        stage.setScene(scene);
        stage.setTitle(APP_NAME);
        stage.show();
    }

    public SidebarComponent getSidebar() {
        return sidebar;
    }

    public MainContainerComponent getMainContainer() {
        return mainContainer;
    }
}
