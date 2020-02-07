package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.window.viewport.ViewportCanvas;
import at.dalex.util.math.Vector2f;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class LightNode extends NodeBase {

    public LightNode(Scene parentScene) {
        super(parentScene, "Light", "image.icon.node.light");
        setWidth(16);
        setHeight(16);
    }

    @Override
    public void draw(ViewportCanvas canvas, GraphicsContext g) {
        Image iconImage = getTreeIcon();
        Vector2f position = getWorldPosition();
        float scale = canvas.getViewportScale();
        position.add(canvas.getViewportOrigin());
        position.scale(scale);

        g.drawImage(iconImage, position.x, position.y, getWidth() * scale, getHeight() * scale);
    }
}
