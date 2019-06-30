package at.dalex.grape.sdk.project;

/**
 * This class contains information about the game's window.
 * E.g.: window width and height, the title of the window, etc.
 */
public class WindowSettings {

    private String windowTitle;
    private int windowWidth;
    private int windowHeight;
    private boolean isResizable;

    public WindowSettings(String windowTitle, int windowWidth, int windowHeight, boolean isResizable) {
        this.windowTitle = windowTitle;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.isResizable = isResizable;
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

    public boolean isResizable() {
        return isResizable;
    }
}
