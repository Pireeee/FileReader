package fr.app.ui.view;

import fr.app.ui.view.component.MainContainerComponent;
import fr.app.ui.view.component.SidebarComponent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView {
    private final SidebarComponent sidebar = new SidebarComponent();
    private final MainContainerComponent mainContainer = new MainContainerComponent();

    private static final String APP_NAME = "Pire's File Reader";
    private static final String GLOBAL_CSS = "/style.css";
    private static final int SCENE_WIDTH = 1024;
    private static final int SCENE_HEIGHT = 768;

    public void show(Stage stage) {
        HBox root = new HBox(10, sidebar, mainContainer);
        root.getStyleClass().add("root");
        root.setPadding(new Insets(20));
        HBox.setHgrow(mainContainer, Priority.ALWAYS);

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(getClass().getResource(GLOBAL_CSS).toExternalForm());

        stage.setScene(scene);
        stage.setTitle(APP_NAME);
        stage.setMinWidth(SCENE_WIDTH);
        stage.setMinHeight(SCENE_HEIGHT);
        stage.show();

    }

    public SidebarComponent getSidebar() {
        return sidebar;
    }

    public MainContainerComponent getMainContainer() {
        return mainContainer;
    }
}
