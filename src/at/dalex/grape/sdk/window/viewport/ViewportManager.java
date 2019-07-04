package at.dalex.grape.sdk.window.viewport;

import at.dalex.grape.sdk.window.viewport.renderer.GridRenderer;

public class ViewportManager {

    private static ViewportCanvas viewportCanvas;
    private static float viewportScale = 0.25f;

    public static final float MIN_SCALE = 0.05f;
    public static final float MAX_SCALE = 12.0f;

    public static void init() {
        viewportCanvas = new ViewportCanvas();
        viewportCanvas.registerTickCallback(new GridRenderer());
    }

    public static ViewportCanvas getViewportCanvas() {
        return viewportCanvas;
    }

    public static float getViewportScale() {
        return viewportScale;
    }

    public static void setViewportScale(float scale) {
        // Clamp viewport scale between MIN_SCALE and MAX_SCALE
        viewportScale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
    }
}
