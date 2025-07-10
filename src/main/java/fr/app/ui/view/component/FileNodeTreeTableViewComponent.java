package fr.app.ui.view.component;

import fr.app.domain.FileNode;
import fr.app.utils.SizeFormatter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.ProgressBarTreeTableCell;

public class FileNodeTreeTableViewComponent extends TreeTableView<FileNode> {

    public FileNodeTreeTableViewComponent() {
        getStyleClass().add("tree-table-view");
        setupColumns();
    }

    private void setupColumns() {
        // Colonne Nom
        TreeTableColumn<FileNode, String> nameColumn = new TreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getName()));

        // Colonne Taille
        TreeTableColumn<FileNode, String> sizeColumn = new TreeTableColumn<>("Size");
        sizeColumn.setCellValueFactory(param ->
                new SimpleStringProperty(SizeFormatter.format(param.getValue().getValue().getSize())));

        // Colonne Pourcentage
        TreeTableColumn<FileNode, Double> percentColumn = new TreeTableColumn<>("%");
        percentColumn.setCellValueFactory(param ->
                new SimpleDoubleProperty(param.getValue().getValue().getPercentage()).asObject());

        // ✅ Cellule custom : ProgressBar sauf pour la racine
        percentColumn.setCellFactory(column -> new TreeTableCell<>() {
            private final ProgressBarTreeTableCell<FileNode> progressCell = new ProgressBarTreeTableCell<>();

            @Override
            protected void updateItem(Double percentage, boolean empty) {
                super.updateItem(percentage, empty);
                if (empty || percentage == null) {
                    setGraphic(null);
                } else {
                    boolean isRoot = getTreeTableRow().getTreeItem() == getTreeTableView().getRoot();
                    if (isRoot) {
                        setGraphic(null); // racine : rien
                    } else {
                        ProgressBarTreeTableCell<FileNode> pbCell = new ProgressBarTreeTableCell<>();
                        pbCell.updateItem(percentage, empty);
                        // Version simple : ou bien créer une ProgressBar manuelle
                        setGraphic(pbCell.getGraphic());
                    }
                }
            }
        });

        getColumns().addAll(nameColumn, sizeColumn, percentColumn);

        // Responsive bindings
        widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            nameColumn.setPrefWidth(totalWidth * 0.55);
            sizeColumn.setPrefWidth(totalWidth * 0.20);
            percentColumn.setPrefWidth(totalWidth * 0.25);
        });
    }
}
