package at.dalex.grape.sdk.window.filebrowser;

import at.dalex.grape.sdk.resource.ResouceLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class FileBrowserItem extends TreeItem<File> {

    private boolean isLeaf;
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;

    public FileBrowserItem(File file, Node graphic) {
        super(new File(file.getPath()), graphic);
    }

    public FileBrowserItem(File file) {
        super(new File(file.getPath()));
    }

    @Override
    public ObservableList<TreeItem<File>> getChildren() {
        if (isFirstTimeChildren) {
            isFirstTimeChildren = false;
            super.getChildren().setAll(buildChildren(this));
        }
        return super.getChildren();
    }

    private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
        File file = TreeItem.getValue();
        if (file == null || file.isFile())
            return FXCollections.emptyObservableList();

        File[] files = file.listFiles();
        if (files != null) {
            ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
            for (File childFile : files) {
                Image folderIcon = ResouceLoader.get("image.folder.black16", Image.class);
                children.add(new FileBrowserItem(childFile, new ImageView(folderIcon)));
            }
            return children;
        }
        return FXCollections.emptyObservableList();
    }

    @Override
    public boolean isLeaf() {
        if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            isLeaf = getValue().isFile();
        }
        return isLeaf;
    }
}
