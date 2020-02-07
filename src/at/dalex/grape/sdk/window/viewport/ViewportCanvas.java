package at.dalex.grape.sdk.window.viewport;

import at.dalex.grape.sdk.window.viewport.renderer.GridRenderer;
import at.dalex.grape.sdk.window.viewport.renderer.NodeRenderer;
import at.dalex.util.Disposable;
import at.dalex.util.math.Vector2f;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.LinkedHashMap;

/**
 * This class represents the actual canvas where stuff gets drawn.
 */
public class ViewportCanvas extends Canvas implements Disposable {

    private final int MAX_WIDTH = 8192;
    private final int MAX_HEIGHT = 4608;

    /* Boundaries for scaling */
    public static final float MIN_SCALE = 0.05f;
    public static final float MAX_SCALE = 12.0f;
    private float viewportScale = 1.0f;

    private Vector2f viewportOrigin = new Vector2f(480, 480);
    private static boolean showTileGrid = true;

    private GraphicsContext graphicsContext;
    private LinkedHashMap<String, ITickCallback> tickCallbacks = new LinkedHashMap<>();

    private AnimationTimer animationTimer;

    public ViewportCanvas() {
        this.graphicsContext = getGraphicsContext2D();
        registerTickCallback("GridRenderer", new GridRenderer(this));
        registerTickCallback("NodeRenderer", new NodeRenderer(this));

        /*
         * Animation Loop
         */
        this.animationTimer = new AnimationTimer() {
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
        };
        animationTimer.start();
    }

    /**
     * Centers the world's origin
     */
    public void centerOrigin() {
        setViewportOrigin((int) (getWidth() / 2.0f), (int) (getHeight() / 2.0f));
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
        GraphicsContext g = getGraphicsContext();

        for (String callbackKey : this.tickCallbacks.keySet()) {
            ITickCallback callback = tickCallbacks.get(callbackKey);
            callback.update(deltaTime);
            callback.draw(g);
        }
    }

    /**
     * Register a new {@link ITickCallback} which
     * gets invoked when a new frame gets to be drawn.
     *
     * @param callbackKey The key to store the object with.
     * @param callback The renderer which implements the {@link ITickCallback} interface.
     */
    public void registerTickCallback(String callbackKey, ITickCallback callback) {
        tickCallbacks.put(callbackKey, callback);
    }

    /**
     * Unregisters a previously registered {@link ITickCallback}.
     *
     * @param callbackKey The key of the {@link ITickCallback} to unregister
     */
    public void unregisterTickCallback(String callbackKey) {
        tickCallbacks.remove(callbackKey);
    }

    /**
     * Retrieves a previously registered {@link ITickCallback} using the key
     * with which the callback was previously stored.
     *
     * @param callbackKey The key of the callback
     * @return The callback or null if none found using the given key.
     */
    public ITickCallback getTickCallback(String callbackKey) {
        return tickCallbacks.get(callbackKey);
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

    /**
     * @return The origin of the viewport. (or the world)
     */
    public Vector2f getViewportOrigin() {
        return viewportOrigin;
    }

    /**
     * Sets the origin of the viewport.
     * (Or the world)
     *
     * @param x The x-coordinate of the origin
     * @param y The y-coordinate of the origin
     */
    public void setViewportOrigin(float x, float y) {
        viewportOrigin.set(x, y);
    }

    /**
     * Sets the origin of the viewport.
     * @param origin The vector representing the origin
     */
    public void setViewportOrigin(Vector2f origin) {
        viewportOrigin = origin;
    }

    /**
     * @return The scale of the viewport.
     */
    public float getViewportScale() {
        return viewportScale;
    }

    /**
     * Sets the scale of the viewport.
     * Any passed values outside of {@link ViewportCanvas#MIN_SCALE} and {@link ViewportCanvas#MAX_SCALE}
     * are going to be clamped to the boundaries.
     *
     * @param scale The desired scale of the viewport
     */
    public void setViewportScale(float scale) {
        // Clamp viewport scale between MIN_SCALE and MAX_SCALE
        viewportScale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
    }

    /**
     * @return Whether or not the grid is currently drawn
     */
    public static boolean isTileGridShowing() {
        return showTileGrid;
    }

    /**
     * Toggles grid drawing
     */
    public static void toggleTileGrid() {
        showTileGrid = !showTileGrid;
    }

    @Override
    public void dispose() {
        tickCallbacks.clear();
        animationTimer.stop();
        animationTimer = null;
    }
}
