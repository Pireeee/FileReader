package fr.app.ui.view.table;

import fr.app.domain.FileNode;
import fr.app.ui.view.CategoryPalette;
import fr.app.utils.SizeFormatter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class FileNodeTreeTableViewComponent extends TreeTableView<FileNode> {

    public final FileNodeTreeTableContextMenu contextMenu = new FileNodeTreeTableContextMenu(this);

    private Map<String, Color> categoryColors = Map.of();

    public FileNodeTreeTableViewComponent() {
        getStyleClass().add("tree-table-view");
        setPlaceholder(new Label("Choose a folder and scan to see results"));
        setupColumns();
        setContextMenu(contextMenu);
    }

    /**
     * Maps a top-level entry's name to the color it's shown with in the donut
     * chart, so a row's category dot stays consistent with the chart.
     */
    public void setCategoryColors(Map<String, Color> categoryColors) {
        this.categoryColors = categoryColors != null ? categoryColors : Map.of();
        refresh();
    }

    public Color resolveColor(TreeItem<FileNode> item) {
        if (item == null) {
            return CategoryPalette.OTHER;
        }
        TreeItem<FileNode> topLevel = item;
        while (topLevel.getParent() != null && topLevel.getParent().getParent() != null) {
            topLevel = topLevel.getParent();
        }
        FileNode node = topLevel.getValue();
        if (node == null) {
            return CategoryPalette.OTHER;
        }
        return categoryColors.getOrDefault(node.getName(), CategoryPalette.OTHER);
    }

    private void setupColumns() {
        // Name, with a tooltip showing the full path
        TreeTableColumn<FileNode, String> nameColumn = new TreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));
        nameColumn.setCellFactory(column -> new TreeTableCell<>() {
            private final Circle categoryDot = new Circle(4);

            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (!empty && name != null) {
                    setText(name);
                    TreeItem<FileNode> treeItem = getTreeTableView().getTreeItem(getIndex());
                    FileNode node = treeItem != null ? treeItem.getValue() : null;
                    if (node != null) {
                        Tooltip tooltip = new Tooltip(node.getPath().toString());
                        setTooltip(tooltip);
                    }
                    categoryDot.setFill(resolveColor(treeItem));
                    setGraphic(categoryDot);
                    return;
                }
                setText(null);
                setGraphic(null);
                setTooltip(null);

            }
        });

        // Formatted size
        TreeTableColumn<FileNode, String> sizeColumn = new TreeTableColumn<>("Size");
        sizeColumn.setCellValueFactory(
                param -> new SimpleStringProperty(SizeFormatter.format(param.getValue().getValue().getSize())));

        // Percent of parent
        TreeTableColumn<FileNode, Double> percentColumn = new TreeTableColumn<>("% of Parent");
        percentColumn.setCellValueFactory(
                param -> new SimpleDoubleProperty(param.getValue().getValue().getPercentOfParent() / 100).asObject());
        percentColumn.setCellFactory(col -> new ColoredProgressBarTreeTableCell());

        // Item count
        TreeTableColumn<FileNode, Integer> countColumn = new TreeTableColumn<>("Items");
        countColumn.setCellValueFactory(
                param -> new SimpleIntegerProperty(param.getValue().getValue().getChildrenCount()).asObject());

        // Last modified date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TreeTableColumn<FileNode, String> modifiedColumn = new TreeTableColumn<>("Modified");
        modifiedColumn.setCellValueFactory(param -> {
            Instant instant = param.getValue().getValue().getLastModified();
            String formatted = dateFormatter.withZone(ZoneId.systemDefault()).format(instant);
            return new SimpleStringProperty(formatted);
        });

        getColumns().addAll(
                nameColumn,
                sizeColumn,
                percentColumn,
                countColumn,
                modifiedColumn);

        // Responsive column widths
        widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            nameColumn.setPrefWidth(totalWidth * 0.35);
            sizeColumn.setPrefWidth(totalWidth * 0.15);
            percentColumn.setPrefWidth(totalWidth * 0.20);
            countColumn.setPrefWidth(totalWidth * 0.10);
            modifiedColumn.setPrefWidth(totalWidth * 0.20);
        });
    }
}
