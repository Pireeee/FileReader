package fr.app.ui.view;

import fr.app.ui.view.component.MainContainerComponent;
import fr.app.ui.view.component.SidebarComponent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView {
    private final SidebarComponent sidebar = new SidebarComponent();
    private final MainContainerComponent mainContainer = new MainContainerComponent();

    private static final Integer SCENE_WIDTH = 1024;
    private static final Integer SCENE_HEIGHT = 768;

    public void show(Stage stage) {
        HBox root = new HBox(sidebar, mainContainer);
        HBox.setHgrow(mainContainer, Priority.ALWAYS);

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Disk Space Analyzer");
        stage.show();
    }

    public SidebarComponent getSidebar() {
        return sidebar;
    }

    public MainContainerComponent getMainContainer() {
        return mainContainer;
    }
}