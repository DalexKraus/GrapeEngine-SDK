package at.dalex.grape.sdk.scene.node;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;

public class NodeTreeItem extends TreeItem<NodeBase> {

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
            NodeTreeItem childTreeNode = new NodeTreeItem(child, new ImageView(nodeIcon));
            childTreeNode.refreshChildren();

            //Expand node if it was previously expanded
            if (statuses.containsKey(child) && statuses.get(child)) {
                childTreeNode.setExpanded(true);
            }

            //Finally add the populated child node to the list of children (of this node)
            getChildren().add(childTreeNode);
        }
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