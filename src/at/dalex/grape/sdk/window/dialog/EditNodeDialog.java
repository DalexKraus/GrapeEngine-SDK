package at.dalex.grape.sdk.window.dialog;

import at.dalex.util.FXMLUtil;
import at.dalex.util.ThemeUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditNodeDialog extends Stage {

    public EditNodeDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(""));
        fxmlLoader.setController(this);

        /* Load dialog FXML */
        Parent root = FXMLUtil.loadRelativeFXML("/resources/javafx/dialog_edit_node.fxml", null);
        //FXMLUtil.addStyleSheet(root, "/resources/javafx/theme_dark/new_node_dialog.css");
        ThemeUtil.applyThemeToParent(root);

        /* Create and set dialog scene */
        javafx.scene.Scene dialogScene = new javafx.scene.Scene(root, 700, 550);
        setScene(dialogScene);

        //Make dialog stay in foreground
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);
        show();
    }
}
