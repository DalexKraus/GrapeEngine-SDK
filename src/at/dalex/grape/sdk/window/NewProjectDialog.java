package at.dalex.grape.sdk.window;

import at.dalex.grape.sdk.project.Project;
import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.project.WindowSettings;
import at.dalex.grape.sdk.window.helper.DialogHelper;
import at.dalex.grape.sdk.window.helper.NumberTextFieldFilter;
import at.dalex.util.ThemeUtil;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class represents the new project dialog.
 */
public class NewProjectDialog extends Stage implements Initializable {

    private Button browse_button;
    private Button create_button;
    private Button cancel_button;
    private TextField field_projectName;
    private TextField field_windowTitle;
    private TextField field_windowWidth;
    private TextField field_windowHeight;
    private TextField field_projectLocation;
    private CheckBox checkBox_resizable;
    private CheckBox checkBox_defaultLocation;

    public NewProjectDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/javafx/dialog_new_project.fxml"));
        fxmlLoader.setController(this);

        try {
            Parent root = fxmlLoader.load();
            ThemeUtil.applyThemeToParent(root);
            Scene dialogScene = new Scene(root, 450, 445);
            this.browse_button = (Button) dialogScene.lookup("#button_browse");
            this.create_button = (Button) dialogScene.lookup("#button_create");
            this.cancel_button = (Button) dialogScene.lookup("#button_cancel");
            browse_button.setOnAction(handler -> openFileBrowser(this, field_projectLocation));
            create_button.setOnAction(handler -> tryCreateProject());
            cancel_button.setOnAction(handler -> close());

            this.field_projectName = (TextField) dialogScene.lookup("#field_projectName");
            this.field_windowTitle = (TextField) dialogScene.lookup("#field_windowTitle");
            this.field_windowWidth = (TextField) dialogScene.lookup("#field_windowWidth");
            this.field_windowHeight = (TextField) dialogScene.lookup("#field_windowHeight");
            this.field_projectLocation = (TextField) dialogScene.lookup("#field_projectLocation");

            this.checkBox_resizable = (CheckBox) dialogScene.lookup("#checkbox_isResizable");
            this.checkBox_defaultLocation = (CheckBox) dialogScene.lookup("#checkbox_defaultLocation");

            field_windowTitle.setText("Another Game");
            field_windowWidth.setText("1280");
            field_windowHeight.setText("720");
            field_projectLocation.setDisable(true);
            field_projectLocation.setText(ProjectUtil.getDefaultProjectDirectory().getPath());

            field_windowWidth.textProperty().addListener(new NumberTextFieldFilter(field_windowWidth));
            field_windowHeight.textProperty().addListener(new NumberTextFieldFilter(field_windowHeight));

            checkBox_resizable.setSelected(true);
            checkBox_defaultLocation.setSelected(true);
            browse_button.setDisable(true);

            checkBox_defaultLocation.setOnAction(handler ->
            {
                boolean disable = checkBox_defaultLocation.isSelected();
                field_projectLocation.setDisable(disable);
                browse_button.setDisable(disable);
            });

            //Cursor jump to first field
            Platform.runLater(() -> field_projectName.requestFocus());

            setScene(dialogScene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Make this stage stay in foreground
        initModality(Modality.APPLICATION_MODAL);
    }

    /**
     * Check if a textfield contains text
     * @param textField The TextField to check on
     * @return Whether or not the text field contains text
     */
    private boolean containsText(TextField textField) {
        return textField != null && textField.getText().trim().length() > 0;
    }

    /**
     * Checks if a project can be created at the given location (which the user has entered).
     * @return The status of the given location
     */
    private PathStatus getGameLocationStatus() {
        if (!checkBox_defaultLocation.isSelected()) {
            if (containsText(field_projectLocation)) {
                if (!new File(field_projectLocation.getText()).isDirectory())
                    return PathStatus.INVALID_DIR;
            }
            else return PathStatus.EMPTY_GAME_LOC;
        }
        return PathStatus.PATH_VALID;
    }

    /**
     * @return Checks if every field is filled, otherwise the user shall not pass.
     */
    private boolean validateFields() {
        return      containsText(field_projectName)
                &&  containsText(field_windowTitle)
                &&  containsText(field_windowWidth)
                &&  containsText(field_windowHeight);
    }

    /**
     * Try to create the project with the given information the user has entered.
     */
    private void tryCreateProject() {
        if (!validateFields()) {
            DialogHelper.showErrorDialog("Error", "Fields Empty", "Some fields are still empty,\nplease fill them.");
            return;
        }

        PathStatus gamePathStatus = getGameLocationStatus();
        if (gamePathStatus != PathStatus.PATH_VALID) {
            if (gamePathStatus == PathStatus.EMPTY_GAME_LOC)
                DialogHelper.showErrorDialog("Error", "Game Location Empty",
                        "A path has to be specified for the project!");
            else if (gamePathStatus == PathStatus.INVALID_DIR)
                DialogHelper.showErrorDialog("Error", "Game Location invalid",
                        "The entered path for the project does not seem to be a valid directory!");
        }
        else {
            //Get project's path
            File projectDirectory = checkBox_defaultLocation.isSelected()
                    ? new File(ProjectUtil.getDefaultProjectDirectory().getPath() + "/"
                    + field_projectName.getText().replaceAll(" ", "_") + "/")
                    : new File(field_projectLocation.getText());

            if (projectDirectory.exists()) {
                DialogHelper.showErrorDialog("Error", null, "A project with the same name already exists " +
                        "at the given location!");
                return;
            }

            //Create WindowSettings and project instance
            WindowSettings windowSettings = new WindowSettings(field_windowTitle.getText(),
                    Integer.parseInt(field_windowWidth.getText()),
                    Integer.parseInt(field_windowHeight.getText()),
                    checkBox_resizable.isSelected());

            Project newProject = new Project(field_projectName.getText(), projectDirectory, windowSettings);
            ProjectUtil.openProject(newProject);
            close();
        }
    }

    /**
     * Opens a filebrowser and applys the selected path to the textfield
     */
    private void openFileBrowser(Stage anchor, TextField pathField) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedFolder = directoryChooser.showDialog(anchor);
        if (selectedFolder != null) {
            pathField.setText(selectedFolder.getPath());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    /**
     * This is used to determine the status of the project directory at creation time.
     */
    private enum PathStatus { PATH_VALID, INVALID_DIR, EMPTY_GAME_LOC }
}
