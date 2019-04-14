package at.dalex.grape.sdk.project;

import java.io.File;

public class ProjectUtil {

    public static File getDefaultProjectDirectory() {
        return new File(getUserHomePath() + "/GrapeEngineProjects/");
    }

    private static String getUserHomePath() {
        return System.getProperty("user.home");
    }
}
