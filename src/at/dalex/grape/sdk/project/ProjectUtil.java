package at.dalex.grape.sdk.project;

import at.dalex.grape.sdk.window.helper.DialogHelper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.util.Optional;

public class ProjectUtil {

    private static Project currentProject;

    public static boolean openProject(Project project) {
        if (currentProject != null) {
            Alert openAlert = DialogHelper.createDialog(Alert.AlertType.WARNING, "Warning", null, "Any unsaved changes"
                    + " will be lost when opening a different project!");

            ButtonType cancelButton = new ButtonType("Cancel");
            ButtonType continueButton = new ButtonType("Continue");
            openAlert.getButtonTypes().setAll(continueButton, cancelButton);

            Optional<ButtonType> result = openAlert.showAndWait();
            if (result.get() == cancelButton)
                return false;
        }
        currentProject = project;
        return true;
    }

    public static File getDefaultProjectDirectory() {
        return new File(getUserHomePath() + "/GrapeEngineProjects/");
    }

    private static String getUserHomePath() {
        return System.getProperty("user.home");
    }
}
