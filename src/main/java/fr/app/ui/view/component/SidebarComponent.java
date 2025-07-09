package fr.app.ui.view.component;

import fr.app.domain.FileNode;
import fr.app.ui.model.FileNodeTreeCell;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SidebarComponent extends VBox {
    public final Button chooseButton = new Button("Choose Folder");
    public final Button scanButton = new Button("Scan Folder");
    public final TreeView<FileNode> treeView = new TreeView<>();

    private static final int SIDEBAR_WIDTH = 250;

    public SidebarComponent() {
        setSpacing(10);
        setPrefWidth(SIDEBAR_WIDTH);

        chooseButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        chooseButton.setPrefWidth(SIDEBAR_WIDTH - 20);

        scanButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        scanButton.setPrefWidth(SIDEBAR_WIDTH - 20);

        treeView.setPrefHeight(400);
        treeView.setCellFactory(tv -> new FileNodeTreeCell());

        getChildren().addAll(chooseButton, scanButton, treeView);
        VBox.setVgrow(treeView, Priority.ALWAYS);
    }
}
