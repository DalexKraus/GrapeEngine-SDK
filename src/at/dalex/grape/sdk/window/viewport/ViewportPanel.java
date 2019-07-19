package at.dalex.grape.sdk.window.viewport;

import at.dalex.grape.sdk.window.Window;
import at.dalex.util.math.Vector2f;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import static at.dalex.grape.sdk.window.viewport.ViewportManager.MIN_SCALE;
import static at.dalex.grape.sdk.window.viewport.ViewportManager.MAX_SCALE;

/**
 * This class derives from {@link TitledPane}, containing all
 * viewport components, such as the {@link ViewportCanvas}.
 */
public class ViewportPanel extends Tab {

    /* Title of the tab in the window */
    private static final String TAB_TITLE = "Viewport";
    private static final String CSS_ID = "viewport";

    /* The 'vector' from the component's origin to the mouse*/
    private static Vector2f gridDragOffset = new Vector2f();
    private static Vector2f pivotPoint = new Vector2f();

    /**
     * Creates a new {@link ViewportPanel}.
     */
    public ViewportPanel() {
        super(TAB_TITLE, ViewportManager.getViewportCanvas());
        //Apply an id to the panel for use in css
        setStyle("id=\"" + CSS_ID + "\"");

        //Drag listeners
        Node content = getContent();
        content.setOnMousePressed(this::mousePressedEvent);
        content.setOnMouseDragged(this::mouseDragEvent);

        //Scroll listeners
        content.setOnMouseMoved(this::mouseMoveEvent);
        content.setOnScroll(this::gridScaleEvent);

        //Tab close listener
        setOnCloseRequest(this::onTabClose);
    }

    /**
     * Callback for mouse clicks.
     * This is used to set the initial drag position to calculate
     * the {@link ViewportPanel#gridDragOffset} vector from.
     * @param e The MouseEvent
     */
    private void mousePressedEvent(MouseEvent e) {
        gridDragOffset = translateMouseToWorldSpace(e);
    }

    /**
     * Callback for mouse movement.
     * This is used to
     * @param e
     */
    private void mouseMoveEvent(MouseEvent e) {
        pivotPoint = translateMouseToWorldSpace(e);
    }

    /**
     * Callback for mouse dragging.
     * This is used to calculate the new position of the world's origin.
     * @param e The MouseEvent
     */
    private void mouseDragEvent(MouseEvent e) {
        float scale = ViewportManager.getViewportScale();
        float newGridOriginX = (float) (e.getX() / scale + gridDragOffset.x);
        float newGridOriginY = (float) (e.getY() / scale + gridDragOffset.y);
        ViewportManager.setViewportOrigin(newGridOriginX, newGridOriginY);
    }

    /**
     * Translates the given (window!) mouse coordinates to world space,
     * 0 0 being the world's origin, as seen by the red and green lines.
     * @param mouseX The mouse x location in the window
     * @param mouseY The mouse y location in the window
     * @return The translated mouse position in world space.
     */
    public Vector2f translateMouseToWorldSpace(double mouseX, double mouseY) {
        Vector2f previousGridOrigin = ViewportManager.getViewportOrigin();
        float scale = ViewportManager.getViewportScale();
        float xPos = (float) (previousGridOrigin.getX() - mouseX / scale);
        float yPos = (float) (previousGridOrigin.getY() - mouseY / scale);
        return new Vector2f(xPos, yPos);
    }

    /**
     * Translates the screen coordinates of the mous to world coordinates.
     * @param e The mouse event
     * @return The vector representing the position of the mous in the world.
     */
    public Vector2f translateMouseToWorldSpace(MouseEvent e) {
        return translateMouseToWorldSpace(e.getX(), e.getY());
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
        float newGridOriginY = (float) (e.getY() / scale + pivotPoint.y);
        ViewportManager.setViewportOrigin(newGridOriginX, newGridOriginY);
    }

    /**
     * Callback for closing requests of this tab.
     * Usually invoked by the user when pressing the 'x' of the tab.
     *
     * This callback is used for closing the maps's property panel.
     *
     * @param event The corresponding event
     */
    public void onTabClose(Event event) {
        Window.removePropertyPanel();
    }
}
