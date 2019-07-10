package at.dalex.grape.sdk.window;

import at.dalex.grape.sdk.project.Project;
import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.resource.ResourceLoader;
import at.dalex.grape.sdk.window.filebrowser.BrowserFile;
import at.dalex.grape.sdk.window.filebrowser.FileBrowserItem;
import at.dalex.grape.sdk.window.helper.MenuBarHelper;
import at.dalex.grape.sdk.window.viewport.ViewportPanel;
import at.dalex.util.ThemeUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.beans.EventHandler;
import java.io.File;

public class Window extends Application {

    private static Scene mainScene;
    private static Stage stage;

    private static SplitPane mainSplitPane;
    private static FileBrowserItem fileBrowserRoot;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/javafx/mainwindow.fxml"));

        //Apply dark theme for this window
        ThemeUtil.applyThemeToParent(root);

        mainScene = new Scene(root, 1280, 720);
        stage = primaryStage;

        mainSplitPane = (SplitPane) root.lookup("#centerSplitPane");

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

        Project proj = ProjectUtil.readProjectFile(new File(ProjectUtil.getDefaultProjectDirectory() + "/Test"));
        ProjectUtil.openProject(proj);
    }

    /**
     * Creates the file browser and adds it to the main split pane.
     */
    public static void createFileBrowser() {
        Project currentProject = ProjectUtil.getCurrentProject();
        if (currentProject == null)
            return;

        ImageView rootImage = new ImageView(ResourceLoader.get("image.folder.project", Image.class));
        fileBrowserRoot = new FileBrowserItem(new BrowserFile(currentProject.getProjectDirectory().getPath()), rootImage);
        fileBrowserRoot.setExpanded(true);

        TreeView fileBrowser = new TreeView<>(fileBrowserRoot);
        fileBrowser.addEventHandler(MouseEvent.MOUSE_CLICKED, handler -> System.out.println("helloo"));
        TitledPane projectPane = new TitledPane("Project", fileBrowser);
        projectPane.setPrefHeight(Double.MAX_VALUE);

        mainSplitPane.getItems().add(projectPane);
        mainSplitPane.setDividerPosition(0, 0.25f);
        mainSplitPane.getItems().add(new Pane()); //Otherwise the filebrowser would cover the entire window.
    }

    /**
     * Creates the viewport panel and adds it to the main split pane
     */
    public static void createViewport() {
        TitledPane viewportPanel = new ViewportPanel();
        viewportPanel.setPrefHeight(Double.MAX_VALUE);
        mainSplitPane.getItems().add(viewportPanel);
    }

    /**
     * Refreshes the file browser to show created or delted files.
     */
    public static void refreshFileBrowser() {
        fileBrowserRoot.refreshChildren();
    }

    /**
     * @return The main {@link SplitPane} of this window
     */
    public static SplitPane getMainSplitPane() {
        return mainSplitPane;
    }

    /**
     * @return The root of the file-browser
     */
    public static FileBrowserItem getFileBrowserRoot() {
        return fileBrowserRoot;
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static Stage getPrimaryStage() {
        return stage;
    }
}
