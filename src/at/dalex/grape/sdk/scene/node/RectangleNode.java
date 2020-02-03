package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.window.viewport.ViewportCanvas;
import at.dalex.util.math.Vector2f;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RectangleNode extends NodeBase {

//    @Property(display = "Width")
//    private int width;

//    @Property(display = "Height")
//    private int height;

    /**
     * Create a new {@link RectangleNode}.
     */
    public RectangleNode(Scene parentScene) {
        super(parentScene, "Rectangle", "image.icon.node.rectangle");
        //Apply some default values
        setWidth(16);
        setHeight(16);
    }

    @Override
    public void draw(ViewportCanvas canvas, GraphicsContext g) {
        //Get the viewport origin and viewport scale
        Vector2f viewportOrigin = canvas.getViewportOrigin();
        float viewportScale = canvas.getViewportScale();

        Vector2f worldPosition = getWorldPosition();
        worldPosition.add(viewportOrigin);
        //double x = viewportOrigin.x + worldPosition.x;
        //double y = viewportOrigin.y + worldPosition.y;

        g.setStroke(Color.RED);
        g.strokeRect(worldPosition.x * viewportScale + 0.5f, worldPosition.y * viewportScale + 0.5f, getWidth() * viewportScale + 0.5f, getHeight() * viewportScale + 0.5f);
    }
}
