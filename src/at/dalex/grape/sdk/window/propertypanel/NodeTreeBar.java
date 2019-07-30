package at.dalex.grape.sdk.window.propertypanel;

import at.dalex.grape.sdk.resource.ResourceLoader;
import at.dalex.grape.sdk.window.dialog.NewNodeDialog;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * This class holds the widgets below the node tree in the
 * {@link ScenePropertyPanel}.
 */
public class NodeTreeBar extends HBox {

    private static final String STYLE_CLASS = "nodeTreeBar";
    private Button addNodeButton;

    public NodeTreeBar() {
        /* Create buttons*/
        addNodeButton = new Button("", new ImageView(ResourceLoader.get("image.icon.node.add", Image.class)));
        addNodeButton.setOnAction(handler -> new NewNodeDialog());
        addNodeButton.setDisable(true);
        getChildren().add(addNodeButton);

        setMaxHeight(28);
        setPadding(new Insets(2, 2, 2, 2));
        getStyleClass().add(STYLE_CLASS);
    }

    public Button getAddNodeButton() {
        return this.addNodeButton;
    }
}
