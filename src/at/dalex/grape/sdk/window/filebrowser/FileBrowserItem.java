package at.dalex.grape.sdk.window.filebrowser;

import at.dalex.grape.sdk.resource.ResourceLoader;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class FileBrowserItem extends TreeItem<BrowserFile> {

    //This is used to remember which nodes were expanded.
    //When the tree is rebuilt, those values are read.
    private ArrayList<String> previousExpandedPaths = new ArrayList<>();

    /**
     * Creates a new {@link FileBrowserItem} with an icon
     * @param file The file that represents this item
     * @param graphic The icon which should be displayed.
     */
    public FileBrowserItem(BrowserFile file, Node graphic) {
        super(new BrowserFile(file.getPath()), graphic);
        refreshChildren(this);
    }

    @Override
    public ObservableList<TreeItem<BrowserFile>> getChildren() {
        return super.getChildren();
    }

    private void collectExpandedPaths() {
        for (TreeItem<BrowserFile> child : getChildren()) {
            if (child.isExpanded()) previousExpandedPaths.add(child.getValue().getPath());
        }
    }

    public void refreshChildren(TreeItem<BrowserFile> treeItem) {
        collectExpandedPaths();     //Save previous expaneded branches
        this.getChildren().clear(); //Remove all nodes from the tree

        BrowserFile browserFile = treeItem.getValue();
        if (browserFile == null || browserFile.isFile())
            return;

        if (getValue().listFiles() == null)
            return;

        //Build the tree
        for (File file : browserFile.listFiles()) {
            FilterRule filterRule = FileBrowserFilter.getRuleFor(file);
            FileBrowserFilter.FilterStatus filterStatus = FileBrowserFilter.getFilterStatus(filterRule);

            String nodeContent = filterStatus == FileBrowserFilter.FilterStatus.NO_MATCH ? file.getPath()
                    : (filterRule.getCustomName() != null ? filterRule.getCustomName() : file.getName());

            if (containsChildNode(nodeContent) || filterStatus == FileBrowserFilter.FilterStatus.APPLY_HIDE)
                continue;

            Image nodeIcon = ResourceLoader.get(
                    file.isDirectory() ? "image.folder.black16" : "image.file.generic.black16", Image.class);
            if (filterStatus == FileBrowserFilter.FilterStatus.APPLY_CUSTOMS && filterRule.getCustomIcon() != null)
                nodeIcon = filterRule.getCustomIcon();

            BrowserFile childBrowserFile = new BrowserFile(nodeContent);
            FileBrowserItem childItem = new FileBrowserItem(childBrowserFile, new ImageView(nodeIcon));

            //If the node was previously expanded, do it again
            if (previousExpandedPaths.contains(file.getPath()) && Objects.requireNonNull(file.list()).length > 0)
                childItem.setExpanded(true);

            super.getChildren().add(childItem);
        }
    }

    private boolean containsChildNode(String nodeTitle) {
        for (TreeItem<BrowserFile> child : super.getChildren()) {
            if (child.getValue().getName().equals(new File(nodeTitle).getName())) {
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
