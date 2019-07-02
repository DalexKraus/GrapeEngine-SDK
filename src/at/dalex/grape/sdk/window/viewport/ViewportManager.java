package at.dalex.grape.sdk.window.viewport;

import at.dalex.grape.sdk.window.viewport.renderer.GridRenderer;

public class ViewportManager {

    private static ViewportCanvas viewportCanvas;
    private static float viewportScale = 1.0f;

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
}
