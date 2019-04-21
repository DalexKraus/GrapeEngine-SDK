package at.dalex.grape.sdk.project;

import at.dalex.grape.sdk.Main;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.filebrowser.BrowserFile;
import at.dalex.grape.sdk.window.filebrowser.FileBrowserItem;
import at.dalex.grape.sdk.window.helper.DialogHelper;
import at.dalex.util.JSONUtil;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import org.json.simple.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

        //Create project file if not present
        File projectFile = new File(project.getProjectDirectory() + "/.sdk/project.json");
        if (!projectFile.exists())
            createProjectFile(project, projectFile);

        currentProject = project;

        //Update file browser
        SplitPane centerSplitPane = (SplitPane) Window.getMainScene().lookup("#centerSplitPane");
        centerSplitPane.getItems().set(0, new TreeView<>(new FileBrowserItem(new BrowserFile(project.getProjectDirectory().getPath()))));
        centerSplitPane.getItems().add(new BorderPane());
        centerSplitPane.setDividerPosition(0, 0.25f);

        return true;
    }

    public static void closeProject() {
        currentProject = null;
        SplitPane centerSplitPane = (SplitPane) Window.getMainScene().lookup("#centerSplitPane");
        centerSplitPane.getItems().clear();
        /* Create Label */
        Label noProjectLabel = new Label("No project is currently opened");
        //--- Ultra mega extreme cheat to change font size, wtf?!
        Font labelFont = noProjectLabel.getFont();
        noProjectLabel.setFont(new Font(labelFont.getName(), 25));
        //--- End of cheating session
        noProjectLabel.setAlignment(Pos.CENTER);
        noProjectLabel.setMaxWidth(Double.MAX_VALUE);
        /* Add to SplitPane */
        centerSplitPane.getItems().add(noProjectLabel);
    }

    private static void createDefaultSubDirectories(Project project) {
        String absProjectPath = project.getProjectDirectory().getAbsolutePath();
        for (String dirName : DEFAULT_SUB_DIRS) {
            File folderDirectory = new File(absProjectPath + "/" + dirName);
            if (!folderDirectory.exists())
                folderDirectory.mkdirs();
        }
    }

    private static void createProjectFile(Project project, File projectFile) {
        JsonObject root = new JsonObject();
        root.put("projectName", project.getProjectName());
        root.put("projectSDKVersion", Main.VERSION);

        projectFile.getParentFile().mkdirs();

        try (FileWriter file = new FileWriter(projectFile)) {

            file.write(JSONUtil.prettyPrintJSON(root.toJson()));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Project getCurrentProject() {
        return currentProject;
    }

    public static boolean isAnyProjectOpened() {
        return getCurrentProject() != null;
    }

    public static File getDefaultProjectDirectory() {
        return new File(getUserHomePath() + "/GrapeEngineProjects/");
    }

    private static String getUserHomePath() {
        return System.getProperty("user.home");
    }
}
