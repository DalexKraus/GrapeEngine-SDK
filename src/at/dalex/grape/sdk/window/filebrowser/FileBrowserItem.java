package at.dalex.grape.sdk.window.filebrowser;

import at.dalex.grape.sdk.resource.ResourceLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class FileBrowserItem extends TreeItem<BrowserFile> {

    /**
     * Creates a new {@link FileBrowserItem} with an icon
     * @param file The file that represents this item
     * @param graphic The icon which should be displayed.
     */
    public FileBrowserItem(BrowserFile file, Node graphic) {
        super(new BrowserFile(file.getPath()), graphic);
        refreshChildren(this);
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
        return super.getChildren();
    }

    public void refreshChildren(TreeItem<BrowserFile> treeItem) {
        BrowserFile browserFile = treeItem.getValue();
        if (browserFile == null || browserFile.isFile())
            return;

        if (getValue().listFiles() == null)
            return;

        for (File file : browserFile.listFiles()) {
            FilterRule filterRule = FileBrowserFilter.getRuleFor(file);
            FileBrowserFilter.FilterStatus filterStatus = FileBrowserFilter.getFilterStatus(filterRule);

            String nodeTitle = filterStatus == FileBrowserFilter.FilterStatus.NO_MATCH ? file.getPath()
                    : (filterRule.getCustomName() != null ? filterRule.getCustomName() : file.getName());

            if (containsChildNode(nodeTitle) || filterStatus == FileBrowserFilter.FilterStatus.APPLY_HIDE)
                continue;

            Image nodeIcon = ResourceLoader.get(
                    file.isDirectory() ? "image.folder.black16" : "image.file.generic.black16", Image.class);
            if (filterStatus == FileBrowserFilter.FilterStatus.APPLY_CUSTOMS && filterRule.getCustomIcon() != null)
                nodeIcon = filterRule.getCustomIcon();

            BrowserFile childBrowserFile = new BrowserFile(nodeTitle);
            FileBrowserItem childItem = new FileBrowserItem(childBrowserFile, new ImageView(nodeIcon));

            if (file.isDirectory())
                childItem.refreshChildren(childItem);

            super.getChildren().add(childItem);
        }
    }

    private boolean containsChildNode(String nodeTitle) {
        for (TreeItem<BrowserFile> child : getChildren()) {
            if (child.getValue().getName().equals(nodeTitle)) {
                return true;
            }
        }
        return false;
    }



    @Override
    public boolean isLeaf() {
        return !getValue().isDirectory();
    }
}
