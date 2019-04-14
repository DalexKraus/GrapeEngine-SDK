package at.dalex.grape.sdk.window.listener;

import at.dalex.grape.sdk.window.NewProjectDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class NewProjectListener implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        NewProjectDialog dialog = new NewProjectDialog();
        dialog.showAndWait();
    }
}
