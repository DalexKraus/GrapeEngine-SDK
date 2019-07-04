package at.dalex.grape.sdk.window.listener;

import at.dalex.grape.sdk.window.helper.DialogHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class HelpAboutListener implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        DialogHelper.showInformationDialog("About", "Credits", "Main developer: David Kraus\n" +
                                                    "\n" +
                                                    "© 2019 David Kraus. All rights reserved.");
    }
}
