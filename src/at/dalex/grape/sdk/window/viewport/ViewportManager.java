package at.dalex.grape.sdk.window.viewport;

import at.dalex.grape.sdk.window.viewport.renderer.GridRenderer;
import java.awt.Point;

/**
 * This class is used to manage the viewport.
 */
public class ViewportManager {

    private static ViewportCanvas viewportCanvas;
    private static float viewportScale = 0.25f;
    private static Point viewportOrigin = new Point(0, 0);

    /* Boundaries for scaling */
    public static final float MIN_SCALE = 0.05f;
    public static final float MAX_SCALE = 12.0f;

    /**
     * Initializes the entire viewport.
     */
    private static void init() {
        viewportCanvas = new ViewportCanvas();
        viewportCanvas.registerTickCallback(new GridRenderer());
    }

    /**
     * Returns the {@ViewportCanvas} of the viewport.
     * If the {@link ViewportCanvas} has not been set yet,
     * it will be created.
     *
     * @return The {@ViewportCanvas} of the viewport.
     */
    public static ViewportCanvas getViewportCanvas() {
        if (viewportCanvas == null)
            init();
        return viewportCanvas;
    }

    /**
     * @return The origin of the viewport. (or the world)
     */
    public static Point getViewportOrigin() {
        return viewportOrigin;
    }

    /**
     * Sets the origin of the viewport.
     * (Or the world)
     *
     * @param x The x-coordinate of the origin
     * @param y The y-coordinate of the origin
     */
    public static void setViewportOrigin(int x, int y) {
        viewportOrigin.setLocation(x, y);
    }

    /**
     * @return The scale of the viewport.
     */
    public static float getViewportScale() {
        return viewportScale;
    }

    /**
     * Sets the scale of the viewport.
     * Any passed values outside of {@link ViewportManager#MIN_SCALE} and {@link ViewportManager#MAX_SCALE}
     * are going to be clamped to the boundaries.
     *
     * @param scale The desired scale of the viewport
     */
    public static void setViewportScale(float scale) {
        // Clamp viewport scale between MIN_SCALE and MAX_SCALE
        viewportScale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
    }
}
