package at.dalex.grape.sdk.scene;

import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.window.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SceneUtil {

    public static final String SCENEFILE_EXT = ".scene";

    public static void createNewScene(String sceneName) {
        String sceneFilePath = ProjectUtil.getCurrentProject().getProjectDirectory() + "/scenes/" + sceneName + SCENEFILE_EXT;
        try {
            FileWriter writer = new FileWriter(new File(sceneFilePath));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Window.refreshFileBrowser();
    }
}
