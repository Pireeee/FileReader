package fr.app.ui.view.component;

import fr.app.domain.FileNode;
import fr.app.ui.model.FileNodeTreeCell;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SidebarComponent extends VBox {
    public final Text title = new Text("Pire's File Reader");
    public final Button chooseButton = new Button("Choose Folder");
    public final Button scanButton = new Button("Scan Folder");
    public final TreeView<FileNode> treeView = new TreeView<>();

    public SidebarComponent() {
        setSpacing(10);
        setMaxHeight(Double.MAX_VALUE);
        getStyleClass().add("sidebar-component");

        title.getStyleClass().add("title");

        chooseButton.getStyleClass().addAll("button", "choose-folder");
        chooseButton.setMaxWidth(Double.MAX_VALUE);

        scanButton.getStyleClass().addAll("button", "scan-folder");
        scanButton.setMaxWidth(Double.MAX_VALUE);

        treeView.getStyleClass().add("tree-view");
        treeView.setCellFactory(tv -> new FileNodeTreeCell());
        setVgrow(treeView, Priority.ALWAYS);

        getChildren().addAll(title, chooseButton, scanButton, treeView);
    }
}
