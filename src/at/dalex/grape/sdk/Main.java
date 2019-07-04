package at.dalex.grape.sdk;

import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.resource.DefaultResources;
import at.dalex.grape.sdk.window.Window;
import javafx.application.Application;

import java.io.File;

public class Main {

    private static Main instance;
    private static boolean useDarkTheme = true;
    public  static final double VERSION = 1.3D;

    public Main(String[] args) {
        instance = this;

        //Load resources
        DefaultResources.loadDefaultResources();

        //Create default project directory
        File projectsDirectory = ProjectUtil.getDefaultProjectDirectory();
        if (!projectsDirectory.exists())
             projectsDirectory.mkdirs();

        //Launch main frame
        Application.launch(Window.class, args);
    }

    public static boolean useDarkTheme() {
        return useDarkTheme;
    }

    public static Main getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        new Main(args);
    }
}
