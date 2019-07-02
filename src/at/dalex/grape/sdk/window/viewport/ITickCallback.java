package at.dalex.grape.sdk.window.viewport;

import javafx.scene.canvas.GraphicsContext;

public interface ITickCallback {

    void update(double delta);
    void draw(GraphicsContext g);
}
