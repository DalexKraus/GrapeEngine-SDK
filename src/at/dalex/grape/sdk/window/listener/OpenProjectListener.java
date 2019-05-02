package at.dalex.grape.sdk.window.listener;

import at.dalex.grape.sdk.project.Project;
import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.window.helper.DialogHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class OpenProjectListener implements EventHandler<ActionEvent> {

    private Stage anchor;
    public OpenProjectListener(Stage anchor) {
        this.anchor = anchor;
    }

    @Override
    public void handle(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(ProjectUtil.getDefaultProjectDirectory());
        File selectedFolder = chooser.showDialog(anchor);
        if (selectedFolder == null)
            return;

        //Validate selected folder
        File projectFile = new File(selectedFolder.getAbsolutePath() + "/.sdk/project.json");
        if (!projectFile.exists())
            DialogHelper.showErrorDialog("Error", null, "The selected folder does not seem to contain " +
                                                        "a valid project!");

        Project toOpen = ProjectUtil.readProjectFile(selectedFolder, projectFile);
        ProjectUtil.closeProject();
        ProjectUtil.openProject(toOpen);
    }
}
