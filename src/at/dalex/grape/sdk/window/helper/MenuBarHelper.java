package at.dalex.grape.sdk.window.helper;

import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.dialog.NewSceneDialog;
import at.dalex.grape.sdk.window.listener.*;
import at.dalex.grape.sdk.window.viewport.ViewportCanvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * This class is used to create the menu bar for the main window.
 */
public class MenuBarHelper {

    /**
     * Inflates the given menubar.
     * @param menuBar The menubar which should be inflated
     */
    public static void inflateMenuBar(MenuBar menuBar) {
        menuBar.getMenus().clear();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu viewMenu = new Menu("View");
        Menu helpMenu = new Menu("Help");

        /* File */
        Menu file_new = new Menu("New");
        //new:project
        MenuItem file_new_project = new MenuItem("Project...");
        file_new_project.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        file_new_project.setOnAction(new NewProjectListener());
        file_new.getItems().add(file_new_project);
        //new:scene
        MenuItem file_new_scene = new MenuItem("Scene...");
        file_new_scene.setOnAction(listener -> NewSceneDialog.showDialog());
        file_new.getItems().add(file_new_scene);

        MenuItem file_openProject = new MenuItem("Open Project...");
        file_openProject.setOnAction(new OpenProjectListener(Window.getPrimaryStage()));

        MenuItem file_close = new MenuItem("Close Project");
        file_close.setOnAction(new CloseProjectListener());

        fileMenu.getItems().add(file_new);
        fileMenu.getItems().add(file_openProject);
        fileMenu.getItems().add(file_close);

        /* View */
        //view:togglegrid
        MenuItem view_toggle_grid = new MenuItem("ToggleTile Grid");
        view_toggle_grid.setOnAction(handler -> ViewportCanvas.toggleTileGrid());
        view_toggle_grid.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.SHIFT_DOWN, KeyCombination.ALT_DOWN));
        viewMenu.getItems().add(view_toggle_grid);
        //view:refreshfilebrowser
        MenuItem view_refresh_file_browser = new MenuItem("Refresh File Browser");
        view_refresh_file_browser.setOnAction(handler -> Window.refreshFileBrowser());
        viewMenu.getItems().add(view_refresh_file_browser);

        /* About */
        MenuItem help_about = new MenuItem("About");
        help_about.setOnAction(new HelpAboutListener());
        helpMenu.getItems().add(help_about);

        //Add menus
        menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, helpMenu);

        //Apply application name (for macOS devices)
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "GrapeEngine SDK");
    }
}
