package at.dalex.grape.sdk.scene;

import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.window.Window;

import java.io.*;

public class SceneUtil {

    public static final String SCENEFILE_EXT = ".scene";

    /**
     * Creates a new scene and updates the file browser.
     * The created scene is also saved.
     * @param sceneName
     */
    public static void createNewScene(String sceneName) {
        Scene scene = new Scene(sceneName);
        writeSceneFile(scene);
        //Update file browser, so that the created scene file is visible
        Window.refreshFileBrowser();
    }

    /**
     * Writes the given scene to the scene directory
     * of the currently opened project.
     * @param scene The scene to write
     */
    public static void writeSceneFile(Scene scene) {
        if (getSceneFolder() == null)
            throw new IllegalStateException("A map must be open to write a scene to disk!");

        String sceneFilePath = getSceneFolder().getAbsolutePath() + "/" + scene.getName() + SCENEFILE_EXT;
        SceneSerializer.writeScene(scene, new File(sceneFilePath));
    }

    /**
     * Loads a {@link Scene} from the given location.
     * @param sceneFile The location of the scene to load
     * @return The loaded {@link Scene}
     */
    public static Scene loadScene(File sceneFile) {
        if (!sceneFile.exists() || sceneFile.isDirectory())
            throw new IllegalStateException("Scene file must exist and cannot be a directory!");

        return SceneSerializer.loadScene(sceneFile);
    }

    /**
     * @return The folder of the currently opened project containing all scenes.
     */
    public static File getSceneFolder() {
        if (!ProjectUtil.isAnyProjectOpened())
            return null;
        return new File(ProjectUtil.getCurrentProject().getProjectDirectory().getAbsolutePath() + "/scenes/");
    }
}
