package at.dalex.grape.sdk.map.node;

import at.dalex.grape.sdk.resource.ResourceLoader;
import javafx.scene.image.Image;

public class RectangleNode extends NodeBase {

    private int width;
    private int height;

    /**
     * Create a new {@link RectangleNode}.
     */
    public RectangleNode() {
        super("Rectangle", ResourceLoader.get("image.icon.node.rectangle", Image.class));
    }
}
