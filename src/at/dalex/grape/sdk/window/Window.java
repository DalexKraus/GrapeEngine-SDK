package at.dalex.grape.sdk.window;

import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.window.helper.MenuBarHelper;
import at.dalex.grape.sdk.window.viewport.ViewportManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Window extends Application {

    private static Scene mainScene;
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/javafx/mainwindow.fxml"));
        root.getStylesheets().add("/resources/javafx/theme_dark.css");

        mainScene = new Scene(root, 1280, 720);
        stage = primaryStage;

        //Initialize ViewportManager
        ViewportManager.init();

        /* *** MenuBar *** */
        MenuBar menuBar = (MenuBar) mainScene.lookup("#menu_bar");
        menuBar.setUseSystemMenuBar(true);
        MenuBarHelper.inflateMenuBar(menuBar);

        /* *** Information Text *** */
        ProjectUtil.closeProject();

        mainScene.setFill(Color.TRANSPARENT);
        primaryStage.setTitle("GrapeEngine Software Development Kit");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);
        primaryStage.show();

        primaryStage.setOnCloseRequest(handler -> {
            System.out.println("Exiting ...");
            System.exit(0);
        });
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static Stage getPrimaryStage() {
        return stage;
    }
}
