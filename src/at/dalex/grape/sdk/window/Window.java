package at.dalex.grape.sdk.window;

import at.dalex.grape.sdk.project.Project;
import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.resource.ResourceLoader;
import at.dalex.grape.sdk.window.filebrowser.BrowserFile;
import at.dalex.grape.sdk.window.filebrowser.FileBrowserItem;
import at.dalex.grape.sdk.window.helper.MenuBarHelper;
import at.dalex.grape.sdk.window.listener.FileBrowserListener;
import at.dalex.grape.sdk.window.propertypanel.ScenePropertyPanel;
import at.dalex.grape.sdk.window.viewport.ViewportPanel;
import at.dalex.util.ThemeUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.File;

public class Window extends Application {

    private static Scene mainScene;
    private static Stage stage;

    private static SplitPane mainSplitPane;
    private static TabPane viewportTabPane;
    private static TreeView fileBrowser;
    private static FileBrowserItem fileBrowserRoot;
    private static ScenePropertyPanel scenePropertyPanel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/javafx/mainwindow.fxml"));
        root.getStylesheets().add("/resources/javafx/theme_dark/theme.css");

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

        Project proj = ProjectUtil.readProjectFile(new File(ProjectUtil.getDefaultProjectDirectory() + "/Game01"));
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

        fileBrowser = new TreeView<>(fileBrowserRoot);
        fileBrowser.addEventHandler(MouseEvent.MOUSE_CLICKED, new FileBrowserListener());
        TitledPane projectPane = new TitledPane("Project", fileBrowser);
        projectPane.setCollapsible(false);
        projectPane.setPrefHeight(Double.MAX_VALUE);

        mainSplitPane.getItems().add(projectPane);
        mainSplitPane.setDividerPosition(0, 0.25f);
        mainSplitPane.getItems().add(new Pane()); //Otherwise the file-browser would cover the entire window.
    }

    /**
     * Removes the property panel from the center-split-pane.
     */
    public static void removePropertyPanel() {
        if (mainSplitPane.getItems().size() >= 3) {
            //Only close if no scene views are open
            //Decrement the tab count by one (the tab has not been removed yet)
            if ((viewportTabPane.getTabs().size() - 1) == 0) {
                mainSplitPane.getItems().remove(2);
            }
        }
    }

    /**
     * Prepares the property panel by adding it to the center-split-pane.
     */
    public static void preparePropertyPanel() {
        if (mainSplitPane.getItems().size() == 3)
            return;

        scenePropertyPanel = new ScenePropertyPanel();
        mainSplitPane.getItems().add(scenePropertyPanel);
        mainSplitPane.setDividerPosition(1, 0.75f);
    }

    /**
     * Prepares the viewport by adding the TabPane to the center-split-pane.
     */
    public static void prepareViewport() {
        viewportTabPane = new TabPane();
        mainSplitPane.getItems().set(1, viewportTabPane);
    }

    /**
     * Creates another viewport panel and adds it to the viewport tab pane,
     * which is contained by the center-split-pane.
     */
    public static void createViewport(String viewportTitle) {
        ViewportPanel viewportPanel = new ViewportPanel(viewportTitle);
        viewportPanel.getViewportCanvas().setViewportOrigin(480, 480);
        viewportPanel.getViewportCanvas().setViewportScale(1.0f);
        viewportTabPane.getTabs().add(viewportPanel);
        preparePropertyPanel();
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
     * @return The viewport's {@link TabPane} containing the individual tabs
     */
    public static TabPane getViewportTabPane() {
        return viewportTabPane;
    }

    /**
     * @return The {@link ViewportPanel} currently visible in the editor.
     */
    public static ViewportPanel getSelectedViewport() {
        Tab selectedTab = viewportTabPane.getSelectionModel().getSelectedItem();
        return (ViewportPanel) selectedTab;
    }

    /**
     * @return The {@link TreeView} object of the file-browser.
     */
    public static TreeView getFileBrowser() {
        return fileBrowser;
    }

    /**
     * @return The {@link ScenePropertyPanel}, the panel visible on the right hand side.
     */
    public static ScenePropertyPanel getScenePropertyPanel() {
        return scenePropertyPanel;
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
