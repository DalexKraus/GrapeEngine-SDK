package at.dalex.grape.sdk.window.propertypanel;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;

public class MapPropertyPanel extends TitledPane {

    private static final String TAB_TITLE = "Properties";
    private SplitPane splitPane;
    private TreeView nodeTree;

    public MapPropertyPanel() {
        /* Set the title and add a split-pane for content (vertical orientation) */
        setText(TAB_TITLE);
        this.splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        setContent(splitPane);
        setCollapsible(false);

        /* Set preferred dimensions */
        setPrefHeight(Double.MAX_VALUE);

        /* Create Node-Tree */
        this.nodeTree = new TreeView();
        splitPane.getItems().add(nodeTree);
        splitPane.getItems().add(new Pane());
        splitPane.setDividerPosition(0, 0.5f);
    }

}
