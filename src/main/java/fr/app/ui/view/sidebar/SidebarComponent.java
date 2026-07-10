package fr.app.ui.view.sidebar;

import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SidebarComponent extends VBox {
    public final Button chooseButton = new Button("Choose Folder");
    public final Button scanButton = new Button("Scan Folder");
    public final PathBarComponent pathBarComponent = new PathBarComponent();
    public final StatisticsComponent statisticsComponent = new StatisticsComponent();
    public final DonutChartComponent donutChartComponent = new DonutChartComponent();

    public SidebarComponent() {
        setSpacing(10);
        setMaxHeight(Double.MAX_VALUE);
        // Without this, long content (e.g. a deep scanned path) inflates the
        // computed min width and the SplitPane divider refuses to shrink past it.
        setMinWidth(150);
        getStyleClass().add("sidebar-component");

        chooseButton.getStyleClass().addAll("button", "choose-folder");
        chooseButton.setMaxWidth(Double.MAX_VALUE);

        scanButton.getStyleClass().addAll("button", "scan-folder");
        scanButton.setMaxWidth(Double.MAX_VALUE);

        donutChartComponent.setPrefSize(200, 200);
        setVgrow(donutChartComponent, Priority.ALWAYS);

        getChildren().addAll(chooseButton, scanButton, pathBarComponent, donutChartComponent, statisticsComponent);
    }
}