package at.dalex.grape.sdk.window.listener;

import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.scene.node.RootNode;
import at.dalex.grape.sdk.window.propertypanel.ScenePropertyPanel;
import at.dalex.util.ViewportUtil;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class NodeTreeListener implements EventHandler<MouseEvent> {

    private ScenePropertyPanel propertyPanel;

    public NodeTreeListener(ScenePropertyPanel propertyPanel) {
        this.propertyPanel = propertyPanel;
    }

    @Override
    public void handle(MouseEvent event) {
        //Redirect the event to the right method handling the event
        if      (event.getClickCount() == 1)                                                handleSingleClick(event);
        else if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY)    handleDoubleClick(event);
    }

    /**
     * Handles all single clicks (left or right).
     * @param event The MouseEvent responsible for the event
     */
    private void handleSingleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            //Retrieve clicked node instance
            NodeBase node = getClickedNode();

            //Enable 'add node' button
            propertyPanel.getNodeTreeBar().getAddNodeButton().setDisable(node == null);

            //Select node in viewport
            if (node != null && !(node instanceof RootNode)) {
                ViewportUtil.getEditingScene().deselectAllNodes();
                node.setSelected(true);
            }
        }
    }

    /**
     * Handles all double-clicks
     * @param event The MouseEvent responsible for the event
     */
    private void handleDoubleClick(MouseEvent event) {

    }

    private NodeBase getClickedNode() {
        ObservableList selectedItems = propertyPanel.getNodeTree().getSelectionModel().getSelectedItems();
        TreeItem<NodeBase> nodeTreeItem = selectedItems.size() > 0 ? (TreeItem<NodeBase>) selectedItems.get(0) : null;
        return nodeTreeItem != null ? nodeTreeItem.getValue() : null;
    }
}
