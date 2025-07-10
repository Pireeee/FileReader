package fr.app.ui.view.component;

import fr.app.domain.FileNode;
import fr.app.utils.SizeFormatter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileNodeTreeTableViewComponent extends TreeTableView<FileNode> {

    public FileNodeTreeTableViewComponent() {
        getStyleClass().add("tree-table-view");
        setupColumns();
    }

    private void setupColumns() {
        // Nom avec tooltip sur le chemin
        TreeTableColumn<FileNode, String> nameColumn = new TreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getName()));
        nameColumn.setCellFactory(column -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty || name == null) {
                    setText(null);
                    setGraphic(null);
                    setTooltip(null);
                } else {
                    setText(name);
                    FileNode node = getTreeTableRow().getItem();
                    if (node != null) {
                        Tooltip tooltip = new Tooltip(node.getPath().toString());
                        setTooltip(tooltip);
                    }
                }
            }
        });

        // Taille formatée
        TreeTableColumn<FileNode, String> sizeColumn = new TreeTableColumn<>("Size");
        sizeColumn.setCellValueFactory(param ->
                new SimpleStringProperty(SizeFormatter.format(param.getValue().getValue().getSize()))
        );

        // Pourcentage du parent
        TreeTableColumn<FileNode, Double> percentColumn = new TreeTableColumn<>("%");
        percentColumn.setCellValueFactory(param ->
                new SimpleDoubleProperty(param.getValue().getValue().getPercentOfParent() / 100).asObject());
        percentColumn.setCellFactory(col -> new ColoredProgressBarTreeTableCell<>());

        // Nombre d'éléments
        TreeTableColumn<FileNode, Integer> countColumn = new TreeTableColumn<>("Items");
        countColumn.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getChildrenCount()).asObject());

        // Date de dernière modification
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TreeTableColumn<FileNode, String> modifiedColumn = new TreeTableColumn<>("Last Modified");
        modifiedColumn.setCellValueFactory(param -> {
            Instant instant = param.getValue().getValue().getLastModified();
            String formatted = dateFormatter.withZone(ZoneId.systemDefault()).format(instant);
            return new SimpleStringProperty(formatted);
        });

        // Extension
        TreeTableColumn<FileNode, String> extensionColumn = new TreeTableColumn<>("Extension");
        extensionColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getExtension()));

        getColumns().addAll(
                nameColumn,
                sizeColumn,
                percentColumn,
                countColumn,
                modifiedColumn,
                extensionColumn
        );

        // ContextMenu
        setContextMenu(new FileNodeTreeTableContextMenu(this));

        // Bindings responsive
        widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            nameColumn.setPrefWidth(totalWidth * 0.30);
            sizeColumn.setPrefWidth(totalWidth * 0.15);
            percentColumn.setPrefWidth(totalWidth * 0.15);
            countColumn.setPrefWidth(totalWidth * 0.10);
            modifiedColumn.setPrefWidth(totalWidth * 0.20);
            extensionColumn.setPrefWidth(totalWidth * 0.10);
        });
    }
}
