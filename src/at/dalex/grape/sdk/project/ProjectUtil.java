package at.dalex.grape.sdk.project;

import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.filebrowser.FileBrowserItem;
import at.dalex.grape.sdk.window.helper.DialogHelper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.Optional;

public class ProjectUtil {

    private static final String[] DEFAULT_SUB_DIRS = {
            "maps", "resource", "scripts", "shaders", "sounds", "textures"
    };

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

        //Create project's sub directories if non existent
        createDefaultSubDirectories(project);
        currentProject = project;

        //Update file browser
        SplitPane centerSplitPane = (SplitPane) Window.getMainScene().lookup("#centerSplitPane");
        centerSplitPane.getItems().set(0, new TreeView<>(new FileBrowserItem(project.getProjectDirectory())));

        return true;
    }

    private static void  createDefaultSubDirectories(Project project) {
        String absProjectPath = project.getProjectDirectory().getAbsolutePath();
        for (String dirName : DEFAULT_SUB_DIRS) {
            File folderDirectory = new File(absProjectPath + "/" + dirName);
            if (!folderDirectory.exists())
                folderDirectory.mkdirs();
        }
    }

    public static File getDefaultProjectDirectory() {
        return new File(getUserHomePath() + "/GrapeEngineProjects/");
    }

    private static String getUserHomePath() {
        return System.getProperty("user.home");
    }
}
