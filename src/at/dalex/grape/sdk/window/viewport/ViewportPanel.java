package at.dalex.grape.sdk.window.viewport;

import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.event.*;
import at.dalex.util.ViewportUtil;
import at.dalex.util.math.Vector2f;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static at.dalex.grape.sdk.window.viewport.ViewportCanvas.MIN_SCALE;
import static at.dalex.grape.sdk.window.viewport.ViewportCanvas.MAX_SCALE;

/**
 * This class derives from {@link Tab}, containing all
 * viewport components, such as the {@link ViewportCanvas}.
 */
public class ViewportPanel extends Tab {

    /* The instance of the viewport canvas */
    private ViewportCanvas viewportCanvas;

    /* The 'vector' from the component's origin to the mouse */
    private Vector2f gridDragOffset = new Vector2f();
    private Vector2f pivotPoint = new Vector2f();

    /* List of listeners responsible for interactions */
    private ArrayList<EventListener> interactionListeners = new ArrayList<>();
    private NodeSelectionHandler selectionHandler;

    /* List of keys currently held */
    private static ArrayList<KeyCode> pressedKeys = new ArrayList<>();

    /**
     * Creates a new {@link ViewportPanel}.
     */
    public ViewportPanel(String viewportTitle) {
        super(viewportTitle);
        this.viewportCanvas = new ViewportCanvas();
        setContent(viewportCanvas);

        //Mouse listeners
        Node content = getContent();
        content.setOnMouseClicked(this::onMouseEvent);
        content.setOnMousePressed(this::onMouseEvent);
        content.setOnMouseReleased(this::onMouseEvent);
        content.setOnMouseDragged(this::onMouseEvent);
        content.setOnMouseMoved(this::onMouseEvent);

        //Scroll listener
        content.setOnScroll(this::gridScaleEvent);

        //Key listener
        content.setOnKeyPressed(handler  -> pressedKeys.add(handler.getCode()));
        content.setOnKeyReleased(handler -> pressedKeys.remove(handler.getCode()));

        //Tab close listener
        setOnCloseRequest(this::onTabClose);

        //Create Selection Handler
        this.selectionHandler = new NodeSelectionHandler(this);
    }

    /* Interaction Listener management */
    public void addInteractionListener(EventListener listenerInstance) {
        if (!interactionListeners.contains(listenerInstance)) {
            interactionListeners.add(listenerInstance);
        }
    }

    public void removeInteractionListener(EventListener listenerInstance) {
        interactionListeners.remove(listenerInstance);
    }

    public void removeAllInteractionListeners() {
        interactionListeners.clear();
    }

    /**
     * This method is used for listening to mouse events.
     * The method will invoke a new InteractionEvent
     * for use in other locations of the editor.
     *
     * After that, this method will redirect the event
     * to the responsible sub-method.
     *
     * @param event The mouse event that was made by the user
     */
    private void onMouseEvent(MouseEvent event) {
        //Invoke new InteractionEvent first
        EventBase eventInstance = new ViewportInteractionEvent(event);
        for (EventListener listenerInstance : interactionListeners) {
            ArrayList<Method> handlerMethods = EventManager.getEventHandlerMethods(listenerInstance);
            EventManager.callHandlerMethods(listenerInstance, handlerMethods, eventInstance);
        }

        //Pass event to responsible sub components
        //if the event hasn't been cancelled by a handler
        if (!eventInstance.isCancelled()) {
            EventType<? extends MouseEvent> eventType = event.getEventType();
            if (eventType.equals(MouseEvent.MOUSE_PRESSED))
                mousePressEvent(event);
            if (eventType.equals(MouseEvent.MOUSE_CLICKED))
                mouseClickEvent(event);
            if (eventType.equals(MouseEvent.MOUSE_DRAGGED))
                mouseDragEvent(event);
            if (eventType.equals(MouseEvent.MOUSE_MOVED))
                mouseMoveEvent(event);
        }
    }

    /**
     * Callback for mouse mouse-presses.
     * This is used to set the initial drag position to calculate
     * the {@link ViewportPanel#gridDragOffset} vector from.
     * @param e The MouseEvent
     */
    private void mousePressEvent(MouseEvent e) {
        gridDragOffset = translateMouseToWorldSpace(e.getX(), e.getY());
    }

    /**
     * Callback for mouse clicks.
     * @param e The MouseEvent
     */
    private void mouseClickEvent(MouseEvent e) {

    }

    /**
     * Callback for mouse movement.
     * @param e The MouseEvent
     */
    private void mouseMoveEvent(MouseEvent e) {
        pivotPoint = translateMouseToWorldSpace(e.getX(), e.getY());
    }

    /**
     * Callback for mouse dragging.
     * This is used to calculate the new position of the world's origin.
     * @param e The MouseEvent
     */
    private void mouseDragEvent(MouseEvent e) {
        //Skip canvas movement if some nodes are selected
        Scene currentScene = ViewportUtil.getEditingScene();
        if (currentScene.isAnyNodeSelected())
            return;

        float scale = viewportCanvas.getViewportScale();
        float newGridOriginX = (float) (e.getX() / scale + gridDragOffset.x);
        float newGridOriginY = (float) (e.getY() / scale + gridDragOffset.y);
        viewportCanvas.setViewportOrigin(newGridOriginX, newGridOriginY);
    }

    /**
     * Translates the given (window!) mouse coordinates to world space,
     * 0 0 being the world's origin, as seen by the red and green lines.
     * @param mouseX The mouse x location in the window
     * @param mouseY The mouse y location in the window
     * @return The translated mouse position in world space.
     */
    public Vector2f translateMouseToWorldSpace(double mouseX, double mouseY) {
        Vector2f previousGridOrigin = viewportCanvas.getViewportOrigin();
        float scale = viewportCanvas.getViewportScale();
        float xPos = (float) (previousGridOrigin.getX() - mouseX / scale);
        float yPos = (float) (previousGridOrigin.getY() - mouseY / scale);
        return new Vector2f(xPos, yPos);
    }

    /**
     * Handles mouse scroll event and scales the view canvas accordingly
     * @param e Mouse scroll-event
     */
    private void gridScaleEvent(ScrollEvent e) {
        float previousScale = viewportCanvas.getViewportScale();
        double scale = previousScale * Math.pow(1.00525, e.getDeltaY());

        //Clamp scale in-between boundaries
        scale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
        viewportCanvas.setViewportScale((float) scale);

        float newGridOriginX = (float) (e.getX() / scale + pivotPoint.x);
        float newGridOriginY = (float) (e.getY() / scale + pivotPoint.y);
        viewportCanvas.setViewportOrigin(newGridOriginX, newGridOriginY);
    }

    /**
     * Callback for closing requests of this tab.
     * Usually invoked by the user when pressing the 'x' of the tab.
     *
     * This callback is used for closing the scene's property panel.
     *
     * @param event The corresponding event
     */
    public void onTabClose(Event event) {
        //Deselect any previously selected nodes
        ViewportUtil.getEditingScene().deselectAllNodes();

        Window.removePropertyPanel();
        //Clean-Up
        viewportCanvas.dispose();
        viewportCanvas = null;

        //TODO: Update property panel to other scene if there's any left.
    }

    /**
     * @return The {@link ViewportCanvas} of this {@link ViewportCanvas}.
     */
    public ViewportCanvas getViewportCanvas() {
        return this.viewportCanvas;
    }

    /**
     * Check if a key is held by the user.
     * @param keyCode The code of the key to check for
     * @return Whether or not the key is currently pressed.
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
