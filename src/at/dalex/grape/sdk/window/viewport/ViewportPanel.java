package at.dalex.grape.sdk.window.viewport;

import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.awt.Point;

public class ViewportPanel extends TitledPane {

    private static final String TAB_TITLE = "Viewport";
    private static Point dragOffset = new Point(0, 0);

    public ViewportPanel() {
        super(TAB_TITLE, ViewportManager.getViewportCanvas());

        //Set event listeners
        setOnMousePressed(this::mousePressedEvent);
        setOnMouseDragged(this::mouseDragEvent);
        setOnScroll(this::gridScaleEvent);
    }

    private void mousePressedEvent(MouseEvent e) {
        Point previousGridOrigin = ViewportManager.getViewportOrigin();
        dragOffset.x = (int) (previousGridOrigin.getX() - e.getX());
        dragOffset.y = (int) (previousGridOrigin.getY() - e.getY());
    }

    private void mouseDragEvent(MouseEvent e) {
        int newGridOriginX = (int) (e.getX() + dragOffset.x);
        int newGridOriginY = (int) (e.getY() + dragOffset.y);
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

        float scale = previousScale + (float) (e.getDeltaY() / 50.0f * ((speedUp / 240.0f)));
        ViewportManager.setViewportScale(scale);
    }
}
