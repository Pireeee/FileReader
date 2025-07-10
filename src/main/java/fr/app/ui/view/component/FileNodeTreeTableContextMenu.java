package fr.app.ui.view.component;

import fr.app.domain.FileNode;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileNodeTreeTableContextMenu extends ContextMenu {

    public FileNodeTreeTableContextMenu(TreeTableView<FileNode> treeTableView) {
        MenuItem deleteItem = new MenuItem("Supprimer");
        deleteItem.setOnAction(event -> {
            TreeItem<FileNode> selectedItem = treeTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.getParent() != null) {

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                        "Supprimer définitivement ?", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait();
                if (confirm.getResult() != ButtonType.YES) return;

                Task<Void> deleteTask = new Task<>() {
                    @Override protected Void call() throws Exception {
                        Path path = selectedItem.getValue().getPath();
                        if (Files.isDirectory(path)) {
                            Files.walk(path).sorted((p1, p2) -> p2.compareTo(p1))
                                    .forEach(p -> { try { Files.delete(p); } catch (Exception e) { e.printStackTrace(); } });
                        } else {
                            Files.delete(path);
                        }

                        Platform.runLater(() -> {
                            TreeItem<FileNode> parentItem = selectedItem.getParent();
                            parentItem.getChildren().remove(selectedItem);

                            long deletedSize = selectedItem.getValue().getSize();
                            TreeItem<FileNode> current = parentItem;
                            while (current != null) {
                                FileNode node = current.getValue();
                                long sum = current.getChildren().stream()
                                        .mapToLong(child -> child.getValue().getSize()).sum();
                                node.setSize(sum);

                                long total = current.getChildren().stream()
                                        .mapToLong(child -> child.getValue().getSize()).sum();
                                for (TreeItem<FileNode> child : current.getChildren()) {
                                    double newPercent = total == 0 ? 0 : ((double) child.getValue().getSize() * 100) / total;
                                    child.getValue().setPercentOfParent(newPercent);
                                }
                                current = current.getParent();
                            }
                            treeTableView.refresh();
                        });
                        return null;
                    }
                };
                new Thread(deleteTask).start();
            }
        });

        // Ouvrir dans l’explorateur
        MenuItem openInExplorerItem = new MenuItem("Ouvrir dans l’explorateur");
        openInExplorerItem.setOnAction(event -> {
            TreeItem<FileNode> selectedItem = treeTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                try {
                    Desktop.getDesktop().open(selectedItem.getValue().getPath().toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Copier chemin
        MenuItem copyPathItem = new MenuItem("Copier le chemin");
        copyPathItem.setOnAction(event -> {
            TreeItem<FileNode> selectedItem = treeTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(selectedItem.getValue().getPath().toString());
                clipboard.setContent(content);
            }
        });

        getItems().addAll(deleteItem, openInExplorerItem, copyPathItem, new SeparatorMenuItem());

        // Visibilité colonnes
        for (TreeTableColumn<FileNode, ?> col : treeTableView.getColumns()) {
            CheckMenuItem item = new CheckMenuItem(col.getText());
            item.setSelected(true);
            item.selectedProperty().addListener((obs, oldVal, newVal) -> col.setVisible(newVal));
            getItems().add(item);
        }
    }
}
