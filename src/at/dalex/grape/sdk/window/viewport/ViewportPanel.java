package at.dalex.grape.sdk.window.viewport;

import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.awt.Point;

/**
 * This class derives from {@link TitledPane}, containing all
 * vieport components, such as the {@link ViewportCanvas}.
 */
public class ViewportPanel extends TitledPane {

    /* Title of the tab in the window */
    private static final String TAB_TITLE = "Viewport";
    /* The 'vector' from the component's origin to the mouse*/
    private static Point gridDragOffset = new Point(0, 0);

    /**
     * Creates a new {@link ViewportPanel}.
     */
    public ViewportPanel() {
        super(TAB_TITLE, ViewportManager.getViewportCanvas());

        //Set event listeners
        setOnMousePressed(this::mousePressedEvent);
        setOnMouseDragged(this::mouseDragEvent);
        setOnScroll(this::gridScaleEvent);
    }

    /**
     * Callback for mouse clicks.
     * This is used to set the initial drag position to calculate
     * the {@link ViewportPanel#gridDragOffset} vector from.
     * @param e The MouseEvent
     */
    private void mousePressedEvent(MouseEvent e) {
        Point previousGridOrigin = ViewportManager.getViewportOrigin();
        float scale = ViewportManager.getViewportScale();
        gridDragOffset.x = (int) (previousGridOrigin.getX() - e.getX() / scale);
        gridDragOffset.y = (int) (previousGridOrigin.getY() - e.getY() / scale);
    }

    /**
     * Callback for mouse dragging.
     * This is used to calculate the new position of the dragged component.
     * @param e The MouseEvent
     */
    private void mouseDragEvent(MouseEvent e) {
        float scale = ViewportManager.getViewportScale();
        int newGridOriginX = (int) (e.getX() / scale + gridDragOffset.x);
        int newGridOriginY = (int) (e.getY() / scale + gridDragOffset.y);
        ViewportManager.setViewportOrigin(newGridOriginX, newGridOriginY);
    }

    /**
     * Handles mouse scroll event and scales the view canvas accordingly
     * @param e Mouse scroll-event
     */
    private void gridScaleEvent(ScrollEvent e) {
        float previousScale = ViewportManager.getViewportScale();

        float speedUp = ViewportManager.MAX_SCALE / previousScale;
        speedUp = 240.0f / speedUp;

        float scale = previousScale + (float) (e.getDeltaY() / 75.0f * (speedUp / 240.0f));
        ViewportManager.setViewportScale(scale);
    }
}
