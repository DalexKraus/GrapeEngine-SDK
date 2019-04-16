package at.dalex.grape.sdk.project;

import java.io.File;

public class Project {

    private String projectName;
    private File projectDirectory;

    private String windowTitle;
    private int windowWidth;
    private int windowHeight;
    private boolean isResizeable;

    public Project(String projectName, File projectDirectory, String windowTitle, int windowWidth, int windowHeight, boolean isResizeable) {
        this.projectName = projectName;
        this.projectDirectory = projectDirectory;
        this.windowTitle = windowTitle;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.isResizeable = isResizeable;
    }

    public String getProjectName() {
        return projectName;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public boolean isResizeable() {
        return isResizeable;
    }
}
