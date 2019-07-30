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
            String nodeTitle = node.getTitle();
            Image nodeIcon = node.getTreeIcon();

            super.getChildren().add(new NodeTreeItem(node, new ImageView(nodeIcon)));
        }
    }

    @Override
    public boolean isLeaf() {
        return super.isLeaf();
    }
}
