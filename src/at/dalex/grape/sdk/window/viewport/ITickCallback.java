package at.dalex.grape.sdk.window.viewport;

import javafx.scene.canvas.GraphicsContext;

/**
 * This interface acts as a callback for the {@link ViewportCanvas}.
 * The methods get called when a new frame is about to be rendered.
 *
 * The deriving class can then use those callbacks to draw their own
 * stuff on the {@link ViewportCanvas}
 */
public interface ITickCallback {

    void update(double delta);
    void draw(GraphicsContext g);
}
