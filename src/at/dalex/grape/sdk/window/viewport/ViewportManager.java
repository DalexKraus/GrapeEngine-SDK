package at.dalex.grape.sdk.window.viewport;

import at.dalex.grape.sdk.window.viewport.renderer.GridRenderer;
import java.awt.Point;

public class ViewportManager {

    private static ViewportCanvas viewportCanvas;
    private static float viewportScale = 0.25f;
    private static Point viewportOrigin = new Point(0, 0);

    public static final float MIN_SCALE = 0.05f;
    public static final float MAX_SCALE = 12.0f;

    private static void init() {
        viewportCanvas = new ViewportCanvas();
        viewportCanvas.registerTickCallback(new GridRenderer());
    }

    public static ViewportCanvas getViewportCanvas() {
        if (viewportCanvas == null)
            init();
        return viewportCanvas;
    }

    public static Point getViewportOrigin() {
        return viewportOrigin;
    }

    public static void setViewportOrigin(int x, int y) {
        viewportOrigin.setLocation(x, y);
    }

    public static float getViewportScale() {
        return viewportScale;
    }

    public static void setViewportScale(float scale) {
        // Clamp viewport scale between MIN_SCALE and MAX_SCALE
        viewportScale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
    }
}
