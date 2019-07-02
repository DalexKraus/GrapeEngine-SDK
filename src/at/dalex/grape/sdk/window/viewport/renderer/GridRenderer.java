package at.dalex.grape.sdk.window.viewport.renderer;

import at.dalex.grape.sdk.window.viewport.ITickCallback;
import at.dalex.grape.sdk.window.viewport.ViewportManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GridRenderer implements ITickCallback {

    private int viewportWidth;
    private int viewportHeight;
    private int tileSize = 32;

    private static final float LINE_WIDTH = 1.0f;

    @Override
    public void update(double delta) {
        Canvas canvas = ViewportManager.getViewportCanvas();
        this.viewportWidth  = (int) canvas.getWidth();
        this.viewportHeight = (int) canvas.getHeight();
    }

    @Override
    public void draw(GraphicsContext g) {
        g.setLineWidth(LINE_WIDTH);

        //Horizontal lines
        for (int y = 0; y <= viewportHeight / tileSize; y++)
            g.strokeLine(0, y * tileSize, viewportWidth, y * tileSize);

        //Vertical lines
        for (int x = 0; x <= viewportWidth / tileSize; x++)
            g.strokeLine(x * tileSize, 0, x * tileSize, viewportHeight);
    }

    public int getTileSize() {
        return this.tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
}
