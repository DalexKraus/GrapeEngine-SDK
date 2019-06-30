package at.dalex.grape.sdk.project;

import at.dalex.grape.sdk.Main;
import at.dalex.grape.sdk.resource.ResouceLoader;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.filebrowser.*;
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

    //The names of the default directories for each project.
    private static final String[] DEFAULT_SUB_DIRS = {
            "maps", "resource", "scripts", "shaders", "sounds", "textures"
    };

    //The currently opened project
    private static Project currentProject;

    /**
     * Opens the given @{link Project} in the editor.
     * @param project A project instance which was previously read or created
     * @return Whether or not the project was successfully opened
     */
    public static boolean openProject(Project project) {
        if (project == null)
            return false;

        //Warn the user if a different project is opened
        if (currentProject != null) {
            Alert openAlert = DialogHelper.createDialog(Alert.AlertType.WARNING, "Warning", null, "Any unsaved changes"
                    + " will be lost when opening a different project!");

            ButtonType cancelButton = new ButtonType("Cancel");
            ButtonType continueButton = new ButtonType("Open Project");
            openAlert.getButtonTypes().setAll(continueButton, cancelButton);

            Optional<ButtonType> result = openAlert.showAndWait();
            //Cancel the opening process if the user presses the 'cancel' button
            if (result.get() == cancelButton)
                return false;
        }

        //Create project's sub directories if non existent
        createDefaultSubDirectories(project);

        //Create project file if not present
        File projectFile = new File(project.getProjectDirectory() + "/.sdk/project.json");
        if (!projectFile.exists())
            writeProjectFile(project, projectFile);

        //Change the current project
        currentProject = project;

        //Update fileBrowser filter rule for sdk folder
        FileBrowserFilter.setFileRule(projectFile.getParentFile(), new FilterRule(FilterRule.Visibility.HIDDEN));

        //Update file browser
        //TODO: Make this cleaner
        SplitPane centerSplitPane = (SplitPane) Window.getMainScene().lookup("#centerSplitPane");
        ImageView rootImage = new ImageView(ResouceLoader.get("image.folder.project", Image.class));
        FileBrowserItem rootItem = new FileBrowserItem(new BrowserFile(project.getProjectDirectory().getPath()), rootImage);
        centerSplitPane.getItems().clear();
        TitledPane projectPane = new TitledPane("Project", new TreeView<>(rootItem));
        projectPane.setPrefHeight(Double.MAX_VALUE);
        centerSplitPane.getItems().add(projectPane);
        centerSplitPane.getItems().add(new BorderPane());
        centerSplitPane.setDividerPosition(0, 0.25f);

        //Project opening process succeeded.
        return true;
    }

    /**
     * Closes any currently opened project.
     * Calling this method will also display an
     * information text at the center of the window.
     */
    public static void closeProject() {
        currentProject = null;
        SplitPane centerSplitPane = (SplitPane) Window.getMainScene().lookup("#centerSplitPane");
        centerSplitPane.getItems().clear();
        /* Create Label */
        Label noProjectLabel = new Label("No project is currently opened");
        //--- Ultra mega extreme cheat to change font size, wtf?!(Because there is no setSize method)
        Font labelFont = noProjectLabel.getFont();
        noProjectLabel.setFont(new Font(labelFont.getName(), 25));
        //--- End of cheating session
        noProjectLabel.setAlignment(Pos.CENTER);
        noProjectLabel.setMaxWidth(Double.MAX_VALUE);
        /* Add to SplitPane */
        centerSplitPane.getItems().add(noProjectLabel);
    }

    /**
     * Creates the default subdirectories for every project.
     * Any folders are only created if they don't exist.
     *
     * @param project The project for which the folders should be created.
     */
    private static void createDefaultSubDirectories(Project project) {
        String absProjectPath = project.getProjectDirectory().getAbsolutePath();
        for (String dirName : DEFAULT_SUB_DIRS) {
            File folderDirectory = new File(absProjectPath + "/" + dirName);
            if (!folderDirectory.mkdirs())
                System.out.println("[Info] Project subdir '" + dirName + "' already present. Ignoring ...");
        }
    }

    /**
     * Writes a JSON-File, which contains information about the given project.
     * This file is later used to identify a project and it's settings.
     *
     * Additional resource directories are also read from this files.
     *
     * @param project The project for which to create this file
     * @param projectFile The destination where the file should be written to
     */
    private static void writeProjectFile(Project project, File projectFile) {
        JSONObject root = new JSONObject();
        root.put("projectName", project.getProjectName());
        root.put("projectSDKVersion", Main.VERSION);

        JSONObject windowSettings = new JSONObject();
        WindowSettings settings = project.getWindowSettings();
        windowSettings.put("windowTitle",   settings.getWindowTitle());
        windowSettings.put("windowWidth",   settings.getWindowWidth());
        windowSettings.put("windowHeight",  settings.getWindowHeight());
        windowSettings.put("isResizable",   settings.isResizable());
        root.put("windowSettings", windowSettings);

        projectFile.getParentFile().mkdirs();

        try (FileWriter file = new FileWriter(projectFile)) {

            file.write(JSONUtil.prettyPrintJSON(root.toJSONString()));
            file.flush();

        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error", "Write Error", "Unable to write the project file.\n" +
                    "Target destination: " + projectFile.getAbsolutePath() + "\n\n" +
                    "Please check read and write permissions.");
            e.printStackTrace();
        }
    }

    /**
     * Reads a JSON-File, which contains information about a project.
     *
     * @param projectDirectory The root directory of the project
     * @param projectFile The JSON-File which contains the project information
     * @return The read project if successful
     */
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

            //Parse and apply window settings
            JSONObject windowSettings =  (JSONObject) root.get("windowSettings");
            String windowTitle  = (String)  windowSettings.get("windowTitle");
            int windowWidth     = (int)     windowSettings.get("windowWidth");
            int windowHeight    = (int)     windowSettings.get("windowHeight");
            boolean isResizable = (boolean) windowSettings.get("isResizable");
            WindowSettings settings = new WindowSettings(windowTitle, windowWidth, windowHeight, isResizable);

            return new Project(projectName, projectDirectory, settings);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error", "Read Error", "Unable to read the project file.\n" +
                    "Target destination: " + projectFile.getAbsolutePath() + "\n\n" +
                    "Please check read permissions.");
        } catch (ParseException e) {
            DialogHelper.showErrorDialog("Error", "Parse Error", "Could not parse the project file correctly.");
        }
        return null;
    }

    /**
     * @return The currently opened project
     */
    public static Project getCurrentProject() {
        return currentProject;
    }

    /**
     * @return Whether or not any project is opened
     */
    public static boolean isAnyProjectOpened() {
        return getCurrentProject() != null;
    }

    /**
     * @return The default directory for creating new projects
     */
    public static File getDefaultProjectDirectory() {
        return new File(getUserHomePath() + "/GrapeEngineProjects/");
    }

    /**
     * @return The path of the user's home directory
     */
    private static String getUserHomePath() {
        return System.getProperty("user.home");
    }
}
