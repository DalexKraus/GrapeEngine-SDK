package at.dalex.grape.sdk.window.viewport;

import javafx.scene.control.TitledPane;
import javafx.scene.input.ScrollEvent;

public class ViewportPanel extends TitledPane {

    private static final String TAB_TITLE = "Viewport";

    public ViewportPanel() {
        super(TAB_TITLE, ViewportManager.getViewportCanvas());
        setOnScroll(this::gridScaleEvent);
    }

    private void gridScaleEvent(ScrollEvent e) {
        float previousScale = ViewportManager.getViewportScale();

        float speedUp = ViewportManager.MAX_SCALE / previousScale;
        speedUp = 240.0f / speedUp;

        float scale = previousScale + (float) (e.getDeltaY() / 50.0f * ((speedUp / 240.0f)));
        ViewportManager.setViewportScale(scale);
    }
}
