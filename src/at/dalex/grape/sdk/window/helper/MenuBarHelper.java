package at.dalex.grape.sdk.window.helper;

import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.listener.CloseProjectListener;
import at.dalex.grape.sdk.window.listener.HelpAboutListener;
import at.dalex.grape.sdk.window.listener.NewProjectListener;
import at.dalex.grape.sdk.window.listener.OpenProjectListener;
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
        Menu helpMenu = new Menu("Help");

        /* File */
        Menu file_new = new Menu("New");
        MenuItem file_new_project = new MenuItem("Project...");
        file_new_project.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        file_new_project.setOnAction(new NewProjectListener());
        file_new.getItems().add(file_new_project);

        MenuItem file_openProject = new MenuItem("Open Project...");
        file_openProject.setOnAction(new OpenProjectListener(Window.getPrimaryStage()));

        MenuItem file_close = new MenuItem("Close Project");
        file_close.setOnAction(new CloseProjectListener());

        fileMenu.getItems().add(file_new);
        fileMenu.getItems().add(file_openProject);
        fileMenu.getItems().add(file_close);

        /* About */
        MenuItem help_about = new MenuItem("About");
        help_about.setOnAction(new HelpAboutListener());
        helpMenu.getItems().add(help_about);

        //Add menus
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        //Apply application name (for macOS devices)
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "GrapeEngine SDK");
    }
}
