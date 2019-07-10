package at.dalex.grape.sdk.window.listener;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class FileBrowserListener implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        //Redirect the event to the right method handling the event
        if      (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY)    handleDoubleClick(event);
        else if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY)  handleRightClick(event);
    }

    /**
     * Handles all double-clicks
     * @param event The MouseEvent responsible for the event
     */
    private void handleDoubleClick(MouseEvent event) {

    }

    /**
     * Handles all right-clicks
     * @param event The MouseEvent responsible for the event
     */
    private void handleRightClick(MouseEvent event) {

    }
}
