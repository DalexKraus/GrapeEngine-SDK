package at.dalex.grape.sdk.window;

import at.dalex.grape.sdk.window.filebrowser.FileBrowserItem;
import at.dalex.grape.sdk.window.helper.MenuBarHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

public class Window extends Application {

    private static Scene mainScene;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/at/dalex/grape/sdk/window/mainwindow.fxml"));
        mainScene = new Scene(root, 1280, 720);

        /* *** Center Split-Pane *** */
        SplitPane centerSplitPane = (SplitPane) mainScene.lookup("#centerSplitPane");
        centerSplitPane.getItems().set(0, new TreeView<>(new FileBrowserItem(new File("/Users"))));
        centerSplitPane.setDividerPosition(0, 0.25);

        /* *** MenuBar *** */
        MenuBar menuBar = (MenuBar) mainScene.lookup("#menu_bar");
        menuBar.setUseSystemMenuBar(true);
        MenuBarHelper.inflateMenuBar(menuBar);

        mainScene.setFill(Color.TRANSPARENT);
        primaryStage.setTitle("GrapeEngine Software Development Kit");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static Scene getMainScene() {
        return mainScene;
    }
}
