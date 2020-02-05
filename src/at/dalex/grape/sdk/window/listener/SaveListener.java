package at.dalex.grape.sdk.window.listener;

import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.scene.SceneUtil;
import at.dalex.util.ViewportUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SaveListener implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
        //Save the current scene if open
        saveScene();
    }

    private void saveScene() {
        Scene currentScene = ViewportUtil.getEditingScene();
        if (currentScene == null)
            return;

        SceneUtil.writeSceneFile(currentScene);
    }
}
