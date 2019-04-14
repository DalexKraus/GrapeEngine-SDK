package at.dalex.grape.sdk;

import at.dalex.grape.sdk.resource.DefaultResources;
import at.dalex.grape.sdk.window.filebrowser.FileBrowserItem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private static Main instance;

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;

        Parent root = FXMLLoader.load(getClass().getResource("/at/dalex/grape/sdk/window/mainwindow.fxml"));
        Scene mainScene = new Scene(root, 1280, 720);
        SplitPane centerSplitPane = (SplitPane) mainScene.lookup("#centerSplitPane");
        centerSplitPane.getItems().set(0, new TreeView<>(new FileBrowserItem(new File("/Users"))));
        centerSplitPane.setDividerPosition(0, 0.25);

        mainScene.setFill(Color.TRANSPARENT);
        primaryStage.setTitle("GrapeEngine Software Development Kit");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        DefaultResources.loadDefaultResources();
        launch(args);
    }

    public static Main getInstance() {
        return instance;
    }
}
