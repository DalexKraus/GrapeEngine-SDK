package at.dalex.grape.sdk.window.propertypanel;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class MapPropertyPanel extends TitledPane {

    private static final String TAB_TITLE = "Properties";
    private VBox contentPanel;

    public MapPropertyPanel() {
        /* Set the title and add a panel for content (vertical layout) */
        setText(TAB_TITLE);
        this.contentPanel = new VBox();
        setContent(contentPanel);
        setCollapsible(false);

        /* Set preferred dimensions */
        setPrefHeight(Double.MAX_VALUE);
    }

}
