package at.dalex.grape.sdk.window;

import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.window.helper.NumberTextFieldFilter;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewProjectDialog extends Stage implements Initializable {

    private Button browse_button;
    private Button create_button;
    private Button cancel_button;
    private TextField field_projectName;
    private TextField field_windowTitle;
    private TextField field_windowWidth;
    private TextField field_windowHeight;
    private TextField field_projectLocation;
    private CheckBox checkBox_resizeable;
    private CheckBox checkBox_defaultLocation;

    public NewProjectDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/dalex/grape/sdk/window/dialog_new_project.fxml"));
        fxmlLoader.setController(this);

        try {
            Scene dialogScene = new Scene(fxmlLoader.load(), 450, 445);
            this.browse_button = (Button) dialogScene.lookup("#button_browse");
            this.create_button = (Button) dialogScene.lookup("#button_create");
            this.cancel_button = (Button) dialogScene.lookup("#button_cancel");
            browse_button.setOnAction(handler -> { openFileBrowser(this, field_projectLocation);    });
            create_button.setOnAction(handler -> { createProject();                                 });
            cancel_button.setOnAction(handler -> { close();                                         });

            this.field_projectLocation = (TextField) dialogScene.lookup("#field_projectName");
            this.field_windowTitle = (TextField) dialogScene.lookup("#field_windowTitle");
            this.field_windowWidth = (TextField) dialogScene.lookup("#field_windowWidth");
            this.field_windowHeight = (TextField) dialogScene.lookup("#field_windowHeight");
            this.field_projectLocation = (TextField) dialogScene.lookup("#field_projectLocation");

            this.checkBox_resizeable = (CheckBox) dialogScene.lookup("#checkbox_isResizeable");
            this.checkBox_defaultLocation = (CheckBox) dialogScene.lookup("#checkbox_defaultLocation");

            field_windowTitle.setText("Another Game");
            field_windowWidth.setText("1280");
            field_windowHeight.setText("720");
            field_windowWidth.setDisable(true);
            field_windowHeight.setDisable(true);
            field_projectLocation.setDisable(true);
            field_projectLocation.setText(ProjectUtil.getDefaultProjectDirectory().getPath());

            checkBox_resizeable.setSelected(true);
            checkBox_defaultLocation.setSelected(true);
            browse_button.setDisable(true);

            checkBox_resizeable.setOnAction(handler ->
            {
                boolean selected = checkBox_resizeable.isSelected();
                field_windowWidth.setDisable(selected);
                field_windowHeight.setDisable(selected);
                field_windowWidth.textProperty().addListener(new NumberTextFieldFilter(field_windowWidth));
                field_windowHeight.textProperty().addListener(new NumberTextFieldFilter(field_windowHeight));
            });

            checkBox_defaultLocation.setOnAction(handler ->
            {
                boolean disable = checkBox_defaultLocation.isSelected();
                field_projectLocation.setDisable(disable);
                browse_button.setDisable(disable);
            });

            setScene(dialogScene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Make this stage stay in foreground
        initModality(Modality.APPLICATION_MODAL);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void createProject() {

    }

    private void openFileBrowser(Stage anchor, TextField pathField) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedFolder = directoryChooser.showDialog(anchor);
        if (selectedFolder != null) {
            pathField.setText(selectedFolder.getPath());
        }
    }
}
