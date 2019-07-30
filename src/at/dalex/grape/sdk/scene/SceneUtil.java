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
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(sceneFilePath));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(scene);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            System.err.println("Unable to write scene file.");
            e.printStackTrace();
        }
    }

    /**
     * Loads a {@link Scene} from the given location.
     * @param sceneFile The location of the scene to load
     * @return The loaded {@link Scene}
     */
    public static Scene loadScene(File sceneFile) {
        if (!sceneFile.exists() || sceneFile.isDirectory())
            throw new IllegalStateException("Scene file must exist and cannot be a directory!");

        Scene loadedScene = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(sceneFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            loadedScene = (Scene) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return loadedScene;
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Unable to load scene from file '" + sceneFile.getAbsolutePath() + "'-");
            e.printStackTrace();
        }
        return loadedScene;
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
