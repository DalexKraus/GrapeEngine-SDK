package at.dalex.grape.sdk.window.propertypanel;

import at.dalex.grape.sdk.resource.ResourceLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * This class holds the widgets below the node tree in the
 * {@link MapPropertyPanel}.
 */
public class NodeTreeBar extends HBox {

    private static final String STYLE_CLASS = "nodeTreeBar";
    private Button addNodeButton;

    public NodeTreeBar() {
        /* Create buttons*/
        addNodeButton = new Button("", new ImageView(ResourceLoader.get("image.icon.node.add", Image.class)));
        addNodeButton.setDisable(true);
        getChildren().add(addNodeButton);

        setPrefHeight(64);
        setPadding(new Insets(2, 2, 2, 2));
        getStyleClass().add(STYLE_CLASS);
    }
}
