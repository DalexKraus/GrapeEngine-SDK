package at.dalex.grape.sdk.window.helper;

import at.dalex.grape.sdk.window.listener.CloseProjectListener;
import at.dalex.grape.sdk.window.listener.HelpAboutListener;
import at.dalex.grape.sdk.window.listener.NewProjectListener;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class MenuBarHelper {

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

        MenuItem file_close = new MenuItem("Close Project");
        file_close.setOnAction(new CloseProjectListener());
        fileMenu.getItems().add(file_new);
        fileMenu.getItems().add(file_close);

        /* About */
        MenuItem help_about = new MenuItem("About");
        help_about.setOnAction(new HelpAboutListener());
        helpMenu.getItems().add(help_about);

        //Add menus
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
        //Apply application name
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "GrapeEngine SDK");
    }
}
