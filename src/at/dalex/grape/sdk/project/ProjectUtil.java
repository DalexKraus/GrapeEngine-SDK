package at.dalex.grape.sdk.project;

import at.dalex.grape.sdk.Main;
import at.dalex.grape.sdk.resource.ResouceLoader;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.filebrowser.BrowserFile;
import at.dalex.grape.sdk.window.filebrowser.FileBrowserFilter;
import at.dalex.grape.sdk.window.filebrowser.FileBrowserItem;
import at.dalex.grape.sdk.window.filebrowser.FilterRule;
import at.dalex.grape.sdk.window.helper.DialogHelper;
import at.dalex.util.JSONUtil;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Optional;

public class ProjectUtil {

    private static final String[] DEFAULT_SUB_DIRS = {
            "maps", "resource", "scripts", "shaders", "sounds", "textures"
    };

    private static Project currentProject;

    public static boolean openProject(Project project) {
        if (project == null)
            return false;

        if (currentProject != null) {
            Alert openAlert = DialogHelper.createDialog(Alert.AlertType.WARNING, "Warning", null, "Any unsaved changes"
                    + " will be lost when opening a different project!");

            ButtonType cancelButton = new ButtonType("Cancel");
            ButtonType continueButton = new ButtonType("Open Project");
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
            writeProjectFile(project, projectFile);

        currentProject = project;

        //Update fileBrowser filter rule for sdk folder
        FileBrowserFilter.setFileRule(projectFile.getParentFile(), new FilterRule(FilterRule.Visibility.HIDDEN));

        //Update file browser
        SplitPane centerSplitPane = (SplitPane) Window.getMainScene().lookup("#centerSplitPane");
        ImageView rootImage = new ImageView(ResouceLoader.get("image.folder.project", Image.class));
        FileBrowserItem rootItem = new FileBrowserItem(new BrowserFile(project.getProjectDirectory().getPath()), rootImage);
        centerSplitPane.getItems().set(0, new TreeView<>(rootItem));
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

    private static void writeProjectFile(Project project, File projectFile) {
        JSONObject root = new JSONObject();
        root.put("projectName", project.getProjectName());
        root.put("projectSDKVersion", Main.VERSION);

        JSONObject gameConfig = new JSONObject();
        gameConfig.put("windowTitle", project.getWindowTitle());
        gameConfig.put("windowWidth", project.getWindowWidth());
        gameConfig.put("windowHeight", project.getWindowHeight());
        gameConfig.put("isResizable", project.isResizable());
        root.put("gameConfig", gameConfig);

        projectFile.getParentFile().mkdirs();

        try (FileWriter file = new FileWriter(projectFile)) {

            file.write(JSONUtil.prettyPrintJSON(root.toJSONString()));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Project readProjectFile(File projectDirectory, File projectFile) {
        JSONParser parser = new JSONParser();

        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(projectFile));
            String projectName = (String) root.get("projectName");
            double projectSDKVersion = (double) root.get("projectSDKVersion");

            //Check if SDK versions match
            if (projectSDKVersion != Main.VERSION) {
                Alert openAlert = DialogHelper.createDialog(Alert.AlertType.WARNING, "Warning", null, "The project's" +
                        " version differs from the SDK's version,\n" +
                        "opening may result in irreversible damage!");

                ButtonType cancelButton = new ButtonType("Cancel");
                ButtonType continueButton = new ButtonType("Continue");
                openAlert.getButtonTypes().setAll(continueButton, cancelButton);

                Optional<ButtonType> result = openAlert.showAndWait();
                if (result.get() == cancelButton)
                    return null;
            }

            JSONObject gameConfig = (JSONObject) root.get("gameConfig");
            String windowTitle = (String) gameConfig.get("windowTitle");
            int windowWidth = (int) ((long) gameConfig.get("windowWidth"));
            int windowHeight = (int) ((long) gameConfig.get("windowHeight"));
            boolean isResizable = (boolean) gameConfig.get("isResizable");

            return new Project(projectName, projectDirectory, windowTitle, windowWidth, windowHeight, isResizable);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
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
