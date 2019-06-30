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

    /**
     * Creates a new {@link FileBrowserItem} with an icon
     * @param file The file that represents this item
     * @param graphic The icon which should be displayed.
     */
    public FileBrowserItem(BrowserFile file, Node graphic) {
        super(new BrowserFile(file.getPath()), graphic);
    }

    /**
     * Creates a new {@link FileBrowserItem}.
     * @param file The file that represents this item
     */
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
                BrowserFile childBrowserFile = null;
                Image itemIcon = ResouceLoader.get(
                        childFile.isDirectory() ? "image.folder.black16" : "image.file.generic.black16", Image.class);

                //Check getFilterStatus before adding next item
                FilterRule filterRule = FileBrowserFilter.getRuleFor(childFile);
                FileBrowserFilter.FilterStatus filterStatus = FileBrowserFilter.getFilterStatus(filterRule);

                //Skip child if it should be hidden
                if (filterStatus == FileBrowserFilter.FilterStatus.APPLY_HIDE)
                    continue;

                if (filterStatus == FileBrowserFilter.FilterStatus.NO_MATCH) {
                    childBrowserFile = new BrowserFile(childFile.getPath());
                }
                else if (filterStatus == FileBrowserFilter.FilterStatus.APPLY_CUSTOMS) {
                    childBrowserFile = new BrowserFile(
                            (filterRule.getCustomName() != null) ? filterRule.getCustomName() : childFile.getName());
                    if (filterRule.getCustomIcon() != null)
                        itemIcon = filterRule.getCustomIcon();
                }

                children.add(new FileBrowserItem(childBrowserFile, new ImageView(itemIcon)));
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
