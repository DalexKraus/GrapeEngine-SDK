package at.dalex.grape.sdk.window.filebrowser;

import at.dalex.grape.sdk.resource.ResouceLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class FileBrowserItem extends TreeItem<BrowserFile> {

    private boolean isLeaf;
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;

    public FileBrowserItem(BrowserFile file, Node graphic) {
        super(new BrowserFile(file.getPath()), graphic);
    }

    public FileBrowserItem(BrowserFile file) {
        super(new BrowserFile(file.getPath()));
    }

    @Override
    public ObservableList<TreeItem<BrowserFile>> getChildren() {
        if (isFirstTimeChildren) {
            isFirstTimeChildren = false;
            super.getChildren().setAll(buildChildren(this));
        }
        return super.getChildren();
    }

    private ObservableList<TreeItem<BrowserFile>> buildChildren(TreeItem<BrowserFile> TreeItem) {
        BrowserFile file = TreeItem.getValue();
        if (file == null || file.isFile())
            return FXCollections.emptyObservableList();

        File[] files = file.listFiles();
        if (files != null) {
            ObservableList<TreeItem<BrowserFile>> children = FXCollections.observableArrayList();
            for (File childFile : files) {
                Image folderIcon = ResouceLoader.get("image.folder.black16", Image.class);
                children.add(new FileBrowserItem(new BrowserFile(childFile.getPath()), new ImageView(folderIcon)));
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
