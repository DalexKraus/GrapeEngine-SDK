package at.dalex.grape.sdk.window.viewport.renderer;

import at.dalex.grape.sdk.window.viewport.ITickCallback;
import at.dalex.grape.sdk.window.viewport.ViewportManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridRenderer implements ITickCallback {

    private int viewportWidth;
    private int viewportHeight;
    private int tileSize = 32;

    private static final float LINE_WIDTH = 1.0f;
    private static final Color LINE_COLOR = Color.DARKGRAY;

    @Override
    public void update(double delta) {
        Canvas canvas = ViewportManager.getViewportCanvas();
        this.viewportWidth  = (int) canvas.getWidth();
        this.viewportHeight = (int) canvas.getHeight();
    }

    @Override
    public void draw(GraphicsContext g) {
        g.setLineWidth(LINE_WIDTH);
        g.setStroke(LINE_COLOR);

        float scale = ViewportManager.getViewportScale();

        //Horizontal lines
        for (int y = 0; y <= viewportHeight / (tileSize * scale); y++)
            g.strokeLine(0, y * tileSize * scale + 0.5f , viewportWidth, y * tileSize * scale + 0.5f);

        //Vertical lines
        for (int x = 0; x <= viewportWidth / (tileSize * scale); x++)
            g.strokeLine(x * tileSize * scale + 0.5f, 0, x * tileSize * scale + 0.5f, viewportHeight);
    }
}
