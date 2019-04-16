package at.dalex.grape.sdk.window.listener;

import at.dalex.grape.sdk.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

public class HelpAboutListener implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        Alert aboutDialogue = new Alert(Alert.AlertType.INFORMATION);
        aboutDialogue.setTitle("About");
        aboutDialogue.setHeaderText("GrapeEngine Software Development Kit\n" +
                                    "v" + Main.VERSION + " (Apr. 2019)");
        aboutDialogue.setContentText("Main developer: David Kraus\n" +
                                    "\n" +
                                    "© 2019 David Kraus. All rights reserved.");
        aboutDialogue.showAndWait();
    }
}
