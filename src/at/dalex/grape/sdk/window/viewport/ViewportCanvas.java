package at.dalex.grape.sdk.window.viewport;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * This class represents the actual canvas where stuff gets drawn.
 */
public class ViewportCanvas extends Canvas {

    private final int MAX_WIDTH = 8192;
    private final int MAX_HEIGHT = 4608;

    private GraphicsContext graphicsContext;
    private ArrayList<ITickCallback> tickCallbacks = new ArrayList<>();

    public ViewportCanvas() {
        this.graphicsContext = getGraphicsContext2D();

        /**
         * Animation Loop
         */
        new AnimationTimer() {
            final long startTime = System.nanoTime();
            double lastLoopDistance = -1;

            @Override
            public void handle(long currentTime) {
                double timeDistance = (currentTime - startTime) / 1000000000.0f;
                double deltaTime = 0.0f;
                if (lastLoopDistance > 0)
                    deltaTime = timeDistance - lastLoopDistance;
                lastLoopDistance = timeDistance;
                draw(deltaTime);
            }
        }.start();
    }

    /**
     * Draws everything needed onto the canvas
     * by calling each registered {@link ITickCallback}.
     *
     * @param deltaTime The time it took the last frame to render.
     */
    private void draw(double deltaTime) {
        //Clear canvas
        graphicsContext.clearRect(0, 0, getWidth(), getHeight());
        for (ITickCallback callback : this.tickCallbacks) {
            callback.update(deltaTime);
            callback.draw(getGraphicsContext());
        }
    }

    /**
     * Register a new {@link ITickCallback} which
     * gets invoked when a new frame gets to be drawn.
     *
     * @param callback The renderer which implements the {@link ITickCallback} interface.
     */
    public void registerTickCallback(ITickCallback callback) {
        if (!tickCallbacks.contains(callback))
            tickCallbacks.add(callback);
    }

    /**
     * Unregisters a previously registered {@link ITickCallback}.
     *
     * @param callback The {@link ITickCallback} to unregister
     */
    public void unregisterTickCallback(ITickCallback callback) {
        tickCallbacks.remove(callback);
    }

    @Override
    public double maxWidth(double height) {
        return MAX_WIDTH;
    }

    @Override
    public double maxHeight(double width) {
        return MAX_HEIGHT;
    }

    @Override
    public double prefHeight(double width) {
        return minHeight(width);
    }

    @Override
    public double minWidth(double height) {
        return getWidth();
    }

    @Override
    public double minHeight(double width) {
        return getHeight();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        super.setWidth(width);
        super.setHeight(height);
    }

    /**
     * @return The {@link GraphicsContext} of this canvas.
     */
    public GraphicsContext getGraphicsContext() {
        return this.graphicsContext;
    }
}
