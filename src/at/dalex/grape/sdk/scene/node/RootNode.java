package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.window.viewport.ViewportCanvas;
import at.dalex.util.math.Vector2f;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

/**
 * This class represents the root node for the scene.
 */
public class RootNode extends NodeBase implements Serializable {

    public RootNode() {
        super("Scene Root", "image.icon.node.root");
    }

    @Override
    public void draw(ViewportCanvas canvas, GraphicsContext g) {

    }

    /* Override methods to avoid changes */
    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setParentSpaceLocation(Vector2f parentSpaceLocation) {

    }
}
