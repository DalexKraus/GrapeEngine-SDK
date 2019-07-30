package at.dalex.grape.sdk.window.propertypanel;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MapPropertyPanel extends SplitPane {

    private static final String TAB_TITLE = "Properties";
    private TreeView nodeTree;
    private NodeTreeBar nodeTreeBar;

    public MapPropertyPanel() {
        /* Set the title and add a split-pane for content (vertical orientation) */
        setOrientation(Orientation.VERTICAL);

        /* Set preferred dimensions */
        setPrefHeight(Double.MAX_VALUE);

        /* Create Scene-Nodes-Box */
        VBox sceneNodesBox = new VBox();
        sceneNodesBox.setPadding(new Insets(0, 0, 0, 0));
        this.nodeTree = new TreeView();
        this.nodeTreeBar = new NodeTreeBar();
        sceneNodesBox.getChildren().add(nodeTree);
        sceneNodesBox.getChildren().add(nodeTreeBar);
        getItems().add(new TitledPane("Scene Nodes", sceneNodesBox));
        getItems().add(new TitledPane("Properties", new Pane()));

        setDividerPosition(0, 0.5f);
    }
}
