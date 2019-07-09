package at.dalex.grape.sdk.window.viewport;

import at.dalex.util.math.Vector2f;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import static at.dalex.grape.sdk.window.viewport.ViewportManager.MIN_SCALE;
import static at.dalex.grape.sdk.window.viewport.ViewportManager.MAX_SCALE;

/**
 * This class derives from {@link TitledPane}, containing all
 * viewport components, such as the {@link ViewportCanvas}.
 */
public class ViewportPanel extends TitledPane {

    /* Title of the tab in the window */
    private static final String TAB_TITLE = "Viewport";
    /* The 'vector' from the component's origin to the mouse*/
    private static Vector2f gridDragOffset = new Vector2f();
    private static Vector2f pivotPoint = new Vector2f();

    /**
     * Creates a new {@link ViewportPanel}.
     */
    public ViewportPanel() {
        super(TAB_TITLE, ViewportManager.getViewportCanvas());

        //Drag listeners
        setOnMousePressed(this::mousePressedEvent);
        setOnMouseDragged(this::mouseDragEvent);

        //Scroll listeners
        setOnMouseMoved(this::mouseMoveEvent);
        setOnScroll(this::gridScaleEvent);
    }

    /**
     * Callback for mouse clicks.
     * This is used to set the initial drag position to calculate
     * the {@link ViewportPanel#gridDragOffset} vector from.
     * @param e The MouseEvent
     */
    private void mousePressedEvent(MouseEvent e) {
        gridDragOffset = translateMouseToWorldCoordinates(e);
    }

    /**
     * Callback for mouse movement.
     * This is used to
     * @param e
     */
    private void mouseMoveEvent(MouseEvent e) {
        pivotPoint = translateMouseToWorldCoordinates(e);
    }

    /**
     * Callback for mouse dragging.
     * This is used to calculate the new position of the world's origin.
     * @param e The MouseEvent
     */
    private void mouseDragEvent(MouseEvent e) {
        float scale = ViewportManager.getViewportScale();
        float newGridOriginX = (float) (e.getX() / scale + gridDragOffset.x);
        float newGridOriginY = (float) ((e.getY() - getHeaderHeight()) / scale + gridDragOffset.y);
        ViewportManager.setViewportOrigin(newGridOriginX, newGridOriginY);
    }

    public Vector2f translateMouseToWorldCoordinates(double mouseX, double mouseY) {
        Vector2f previousGridOrigin = ViewportManager.getViewportOrigin();
        float scale = ViewportManager.getViewportScale();
        float xPos = (float) (previousGridOrigin.getX() - mouseX / scale);
        float yPos = (float) (previousGridOrigin.getY() - (mouseY - getHeaderHeight()) / scale);
        return new Vector2f(xPos, yPos);
    }

    /**
     * Translates the screen coordinates of the mous to world coordinates.
     * @param e The mouse event
     * @return The vector representing the position of the mous in the world.
     */
    public Vector2f translateMouseToWorldCoordinates(MouseEvent e) {
        return translateMouseToWorldCoordinates(e.getX(), e.getY());
    }

    /**
     * Handles mouse scroll event and scales the view canvas accordingly
     * @param e Mouse scroll-event
     */
    private void gridScaleEvent(ScrollEvent e) {
        float previousScale = ViewportManager.getViewportScale();
        double scale = previousScale * Math.pow(1.00525, e.getDeltaY());

        //Clamp scale in-between boundaries
        scale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
        ViewportManager.setViewportScale((float) scale);

        float newGridOriginX = (float) (e.getX() / scale + pivotPoint.x);
        float newGridOriginY = (float) ((e.getY() - getHeaderHeight()) / scale + pivotPoint.y);
        ViewportManager.setViewportOrigin(newGridOriginX, newGridOriginY);
    }

    /**
     * @return the size (the height) of the header of the pane.
     */
    public double getHeaderHeight() {
        return getHeight() - ViewportManager.getViewportCanvas().getHeight();
    }
}
