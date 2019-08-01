package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.window.viewport.ViewportCanvas;
import at.dalex.util.math.Vector2f;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class LightNode extends NodeBase {

    public LightNode() {
        super("Light", "image.icon.node.light");
    }

    @Override
    public void draw(ViewportCanvas canvas, GraphicsContext g) {
        Image iconImage = getTreeIcon();

        Vector2f origin = canvas.getViewportOrigin();
        float scale = canvas.getViewportScale();

        g.drawImage(iconImage, (origin.x + 64) * scale, (origin.y + 64) * scale, 16 * scale, 16 * scale);
        g.setStroke(Color.ORANGE);
        g.strokeRect((origin.x + 64) * scale, (origin.y + 64) * scale, 16 * scale, 16 * scale);
    }
}
