package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.scene.node.annotation.Property;

public class RectangleNode extends NodeBase {

    @Property(display = "Width")
    private int width;

    @Property(display = "Height")
    private int height;

    /**
     * Create a new {@link RectangleNode}.
     */
    public RectangleNode() {
        super("Rectangle", "image.icon.node.rectangle");
    }
}
