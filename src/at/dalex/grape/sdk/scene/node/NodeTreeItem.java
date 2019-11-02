package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.window.Window;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;

public class NodeTreeItem extends TreeItem<NodeBase> {

    private final int TREE_ICON_SIZE = 24;

    public NodeTreeItem(NodeBase node, ImageView iconImage) {
        super(node, iconImage);
    }

    /**
     * Refreshes the children of this item.
     */
    public void refreshChildren() {
        //Collect expanded status of children
        HashMap<NodeBase, Boolean> statuses = collectNodeExpandStatuses();
        getChildren().clear();

        NodeBase node = getValue();
        if (node == null)
            return;

        for (NodeBase child : node.getChildren()) {
            Image nodeIcon = child.getTreeIcon();

            //Create new tree node for child and rebuild it's children
            ImageView nodeIconView = new ImageView(nodeIcon);
            nodeIconView.setFitWidth(TREE_ICON_SIZE);
            nodeIconView.setFitHeight(TREE_ICON_SIZE);

            //TODO: (Optimisation) avoid tree rebuilding, only create new instances for new nodes
            NodeTreeItem childTreeNode = new NodeTreeItem(child, nodeIconView);

            childTreeNode.refreshChildren();

            //Expand node if it was previously expanded
            if (statuses.containsKey(child) && statuses.get(child)) {
                childTreeNode.setExpanded(true);
            }

            //Finally add the populated child node to the list of children (of this node)
            getChildren().add(childTreeNode);

            //Refresh the reference in the reference list
            Window.getScenePropertyPanel().getNodeTreeReferences().put(child, childTreeNode);
        }
    }

    /**
     * Returns the index of a child of this tree item
     * by searching for it's value.
     *
     * @param childValue The value to search for
     * @return The index of the child, -1 if none found.
     */
    public int getChildIndexByValue(NodeBase childValue) {
        ObservableList<TreeItem<NodeBase>> children = getChildren();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getValue().equals(childValue)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return A {@link HashMap} containing a boolean whether or not the node (which is also the key value)
     *         is expanded or not.
     */
    private HashMap<NodeBase, Boolean> collectNodeExpandStatuses() {
        HashMap<NodeBase, Boolean> statuses = new HashMap<>();
        for (TreeItem<NodeBase> child : getChildren()) {
            statuses.put(child.getValue(), child.isExpanded());
        }
        return statuses;
    }
}