package at.dalex.grape.sdk.window.viewport;

import javafx.scene.canvas.Canvas;

public class ViewportManager {

    private static ViewportCanvas viewportCanvas;

    public static void init() {
        viewportCanvas = new ViewportCanvas();
    }

    public static Canvas getViewportCanvas() {
        return viewportCanvas;
    }
}
