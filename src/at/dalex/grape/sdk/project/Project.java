package at.dalex.grape.sdk.project;

import java.io.File;

/**
 * This class contains information about a project, which can be loaded into the editor.
 */
public class Project {

    private String projectName;
    private File projectDirectory;
    private WindowSettings windowSettings;

    public Project(String projectName, File projectDirectory, WindowSettings windowSettings) {
        this.projectName = projectName;
        this.projectDirectory = projectDirectory;
        this.windowSettings = windowSettings;
    }

    public String getProjectName() {
        return projectName;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

    public WindowSettings getWindowSettings() {
        return windowSettings;
    }
}
