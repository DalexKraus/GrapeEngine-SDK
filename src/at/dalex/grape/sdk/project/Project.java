package at.dalex.grape.sdk.project;

import at.dalex.grape.sdk.scene.Scene;

import java.io.File;
import java.util.ArrayList;

/**
 * This class contains information about a project, which can be loaded into the editor.
 */
public class Project {

    private String projectName;
    private File projectDirectory;
    private WindowSettings windowSettings;

    private ArrayList<Scene> scenes = new ArrayList<>();

    public Project(String projectName, File projectDirectory, WindowSettings windowSettings) {
        this.projectName = projectName;
        this.projectDirectory = projectDirectory;
        this.windowSettings = windowSettings;
    }

    /**
     * @return The project's name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @return The root directory of the project
     */
    public File getProjectDirectory() {
        return projectDirectory;
    }

    /**
     * @return The window settings of this project.
     */
    public WindowSettings getWindowSettings() {
        return windowSettings;
    }

    /**
     * @return All {@link Scene}s in this project
     */
    public ArrayList<Scene> getScenes() {
        return scenes;
    }

    /**
     * Returns the first {@link Scene} in this project with the given name.
     * If no scene with this name could be found, null is returned.
     * @param sceneName The target scene name
     */
    public Scene getSceneByName(String sceneName) {
        for (Scene scene : this.scenes) {
            if (scene.getName().equals(sceneName)) {
                return scene;
            }
        }
        return null;
    }
}
