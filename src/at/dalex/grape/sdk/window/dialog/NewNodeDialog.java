package at.dalex.grape.sdk.window.dialog;

import at.dalex.util.ThemeUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NewNodeDialog extends Stage {

    public NewNodeDialog() {
        try {
            /* Load dialog FXML */
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/javafx/dialog_new_node.fxml"));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();
            root.getStylesheets().add("/resources/javafx/theme_dark/new_node_dialog.css");
            ThemeUtil.applyThemeToParent(root);

            Scene dialogScene = new Scene(root, 600, 400);
            setScene(dialogScene);

            //Make dialog stay in foreground
            initModality(Modality.APPLICATION_MODAL);
            show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
