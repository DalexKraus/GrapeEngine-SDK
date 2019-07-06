package at.dalex.grape.sdk.window.viewport.renderer;

import at.dalex.grape.sdk.window.viewport.ITickCallback;
import at.dalex.grape.sdk.window.viewport.ViewportManager;
import at.dalex.util.math.Vector2f;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.Point;

/**
 * This class is used to draw a Tile-Grid onto the canvas.
 * The scaling is controlled by the {@link ViewportManager#getViewportScale()} value.
 */
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
        Vector2f origin = ViewportManager.getViewportOrigin();

        double overlapDistanceX = calculateAxisOverlapDistance(origin.getX());
        double overlapDistanceY = calculateAxisOverlapDistance(origin.getY());

        //Horizontal lines
        for (int y = 0; y <= viewportHeight / (tileSize * scale); y++) {
            double yPos = 0.5f + (overlapDistanceY + (float) y * tileSize) * scale;
            g.strokeLine(0, yPos, viewportWidth, yPos);
        }

        //Vertical lines
        for (int x = 0; x <= viewportWidth / (tileSize * scale); x++) {
            double xPos = 0.5f + (overlapDistanceX + (float) x * tileSize) * scale;
            g.strokeLine(xPos, 0, xPos, viewportHeight);
        }

        //Draw colored axes
        g.setStroke(Color.RED);
        g.strokeLine(0, origin.y * scale, viewportWidth, origin.y * scale);
        g.setStroke(Color.LIME);
        g.strokeLine(origin.x * scale, 0, origin.x * scale, viewportHeight);
    }

    /**
     *
     * To understand the math going on here,
     * please consider looking up this screenshot: http://prntscr.com/ob4tlo
     * (x or the orange line is what this method actually calculates)
     * This applies to the given axis.
     *
     * @param originCoordinate The coordinate of the origin of the desired coordinate.
     * @return The calculated value seen in the screenshot
     */
    private double calculateAxisOverlapDistance(float originCoordinate) {
        int tileCountToOrigin = (int) Math.abs(originCoordinate / this.tileSize);
        double normalizedTilePosition = Math.abs(originCoordinate) - (tileCountToOrigin * this.tileSize);
        return originCoordinate < 0 ? Math.abs(this.tileSize - normalizedTilePosition) : normalizedTilePosition;
    }
}
