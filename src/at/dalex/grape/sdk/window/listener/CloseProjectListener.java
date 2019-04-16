package at.dalex.grape.sdk.window.listener;

import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.window.helper.DialogHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class CloseProjectListener implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        if (ProjectUtil.isAnyProjectOpened()) {
            Alert warningDialog = DialogHelper.createDialog(Alert.AlertType.WARNING, "Warning", null, "Any unsafed " +
                    "changes will be lost when closing the current project!");
            ButtonType continueButton = new ButtonType("Continue");
            ButtonType cancelButton = new ButtonType("Cancel");
            warningDialog.getButtonTypes().setAll(continueButton, cancelButton);
            Optional<ButtonType> result = warningDialog.showAndWait();
            if (result.get() == continueButton) {
                ProjectUtil.closeProject();
            }
        }
    }
}