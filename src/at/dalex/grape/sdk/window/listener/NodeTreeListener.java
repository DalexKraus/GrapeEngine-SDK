package at.dalex.grape.sdk.window.listener;

import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.scene.node.NodeTreeItem;
import at.dalex.grape.sdk.scene.node.RootNode;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.propertypanel.ScenePropertyPanel;
import at.dalex.util.ViewportUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class NodeTreeListener implements EventHandler<MouseEvent> {

    private ScenePropertyPanel propertyPanel;
    private static ContextMenu currentContextMenu;

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
        MouseButton pressedButton = event.getButton();
        if (pressedButton == MouseButton.PRIMARY) {
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
        else if (pressedButton == MouseButton.SECONDARY) {
            //Retrieve clicked tree item
            NodeBase clickedNodeInstance = getClickedNode();

            //We don't need a context menu for Root Nodes
            if (!(clickedNodeInstance instanceof RootNode)) {
                createContextMenu(event.getScreenX(), event.getScreenY());
            }
        }
    }

    /**
     * Handles all double-clicks
     * @param event The MouseEvent responsible for the event
     */
    private void handleDoubleClick(MouseEvent event) {

    }

    private void createContextMenu(double mouseX, double mouseY) {
        ContextMenu menu = new ContextMenu();
        MenuItem item_remove = new MenuItem("Remove");
        item_remove.setOnAction(this::removeSelectedNode);
        menu.getItems().add(item_remove);

        //Close other context menu if open
        if (currentContextMenu != null) {
            currentContextMenu.hide();
        }

        currentContextMenu = menu;

        //Show the new context menu
        currentContextMenu.show(Window.getMainScene().getFocusOwner(), mouseX, mouseY);
    }

    private void removeSelectedNode(ActionEvent actionEvent) {
        NodeBase selectedNode = getClickedNode();
        Scene currentScene = ViewportUtil.getEditingScene();
        currentScene.removeNode(selectedNode);

        //Update node tree item
        NodeTreeItem selectionRoot = (NodeTreeItem) getSelectedTreeNode();
        if (selectionRoot != null) {
            Window.getScenePropertyPanel().refreshNodeTree();
        }
    }

    private TreeItem<NodeBase> getSelectedTreeNode() {
        ObservableList selectedItems = propertyPanel.getNodeTree().getSelectionModel().getSelectedItems();
        return selectedItems.size() > 0 ? (NodeTreeItem) selectedItems.get(0) : null;
    }

    private NodeBase getClickedNode() {
        TreeItem<NodeBase> treeItem = getSelectedTreeNode();
        return treeItem != null ? treeItem.getValue() : null;
    }
}
