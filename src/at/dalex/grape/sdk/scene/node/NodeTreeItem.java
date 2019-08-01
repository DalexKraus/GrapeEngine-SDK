package at.dalex.grape.sdk.scene.node;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NodeTreeItem extends TreeItem<NodeBase> {

    public NodeTreeItem(NodeBase node, ImageView iconImage) {
        super(node, iconImage);
    }

    /**
     * Refreshes the children of this item.
     */
    public void refreshChildren() {
        super.getChildren().clear();

        NodeBase node = getValue();
        if (node == null)
            return;

        for (NodeBase child : node.getChildren()) {
            Image nodeIcon = child.getTreeIcon();

            //Create new tree node for child and rebuild it's children
            NodeTreeItem childTreeNode = new NodeTreeItem(child, new ImageView(nodeIcon));
            childTreeNode.refreshChildren();

            //Finally add the populated child node to the list of children (of this node)
            super.getChildren().add(childTreeNode);
        }
    }
}
