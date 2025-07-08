package fr.app.ui.model;

import fr.app.domain.FileNode;
import fr.app.utils.SizeFormatter;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class FileNodeTreeCell extends TreeCell<FileNode> {

    private final HBox content;
    private final Text name;
    private final Text size;
    private final ProgressBar progressBar;

    public FileNodeTreeCell() {
        name = new Text();
        size = new Text();
        progressBar = new ProgressBar();
        progressBar.setPrefWidth(100);
        content = new HBox(10, name, size, progressBar);
    }

    @Override
    protected void updateItem(FileNode item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            name.setText(item.getName());
            size.setText(SizeFormatter.format(item.getSize()));
            progressBar.setProgress(item.getPercentage());
            setText(null);
            setGraphic(content);
        }
    }
}
