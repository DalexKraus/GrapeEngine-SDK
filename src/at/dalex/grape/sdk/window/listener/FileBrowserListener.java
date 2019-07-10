package at.dalex.grape.sdk.window.listener;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class FileBrowserListener implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        if (event.getClickCount() < 2)
            return;

        /* Handlers go here */
        System.out.println("Double clicked");
    }
}
