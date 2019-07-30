package at.dalex.grape.sdk.window.propertypanel;

import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.scene.node.NodeTreeItem;
import at.dalex.grape.sdk.scene.node.RootNode;
import at.dalex.grape.sdk.window.Window;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ScenePropertyPanel extends SplitPane {

    private static final String TAB_TITLE = "Properties";
    private TreeView nodeTree;
    private NodeTreeBar nodeTreeBar;

    public ScenePropertyPanel() {
        /* Set the title and add a split-pane for content (vertical orientation) */
        setOrientation(Orientation.VERTICAL);

        /* Set preferred dimensions */
        setPrefHeight(Double.MAX_VALUE);

        /* Create Scene-Nodes-Box */
        VBox sceneNodesBox = new VBox();
        sceneNodesBox.setPadding(new Insets(0, 0, 0, 0));

        createTreeView();
        this.nodeTreeBar = new NodeTreeBar();
        sceneNodesBox.getChildren().add(nodeTree);
        sceneNodesBox.getChildren().add(nodeTreeBar);

        getItems().add(new TitledPane("Scene Nodes", sceneNodesBox));
        getItems().add(new TitledPane(TAB_TITLE, new Pane()));

        setDividerPosition(0, 0.5f);
    }

    private void createTreeView() {
        String currentSceneName = Window.getSelectedViewport().getText();
        RootNode sceneRootNode = ProjectUtil.getCurrentProject().getSceneByName(currentSceneName).getRootNode();
        this.nodeTree = new TreeView<>(new NodeTreeItem(sceneRootNode, new ImageView(sceneRootNode.getTreeIcon())));
    }

    public void refreshNodeTree() {
        ((NodeTreeItem) nodeTree.getRoot()).refreshChildren();
    }

    public NodeTreeBar getNodeTreeBar() {
        return this.nodeTreeBar;
    }
}
