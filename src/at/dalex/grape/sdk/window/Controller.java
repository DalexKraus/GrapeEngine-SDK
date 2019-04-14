package at.dalex.grape.sdk.window;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Controller {

    private Stage getStage(Event e) {
        return (Stage) ((Node) e.getSource()).getScene().getWindow();
    }
}
