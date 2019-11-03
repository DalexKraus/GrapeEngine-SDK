package at.dalex.util;

import at.dalex.grape.sdk.project.Project;
import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.viewport.ViewportPanel;
import at.dalex.grape.sdk.window.viewport.renderer.GridRenderer;

public class ViewportUtil {

    /**
     * Retrieves the Scene object which is currently being edited in the viewport.
     * For this function to successfully return the scene object, a project
     * as well as a viewport must be opened.
     *
     * @return The current scene or null if no scene could be found.
     */
    public static Scene getEditingScene() {
        ViewportPanel currentViewport = getEditingViewport();
        Project currentProject = ProjectUtil.getCurrentProject();

        //Get the current scene by searching for it in the currently opened
        //project, as the viewport title equals the scene file without the extension.
        return currentProject.getSceneByName(currentViewport.getText());
    }

    /**
     * Retrieves the Viewport object which is currently visible to the user.
     * @return The visible {@link ViewportPanel}
     */
    public static ViewportPanel getEditingViewport() {
        Project currentProject = ProjectUtil.getCurrentProject();

        //Throw an exception of no project is opened
        if (currentProject == null)
            throw new IllegalStateException("A project must be opened to retrieve the current scene.");

        ViewportPanel currentViewport = Window.getSelectedViewport();

        //Throw an exception if no viewport is opened
        if (currentViewport == null)
            throw new IllegalStateException("A viewport must be opened to retrieve the current scene.");

        return currentViewport;
    }

    /**
     * Changes the tile size.
     * @param deltaSize The tilesize to add to the current one.
     */
    private static void changeTileSize(int deltaSize) {
        ViewportPanel currentViewport = getEditingViewport();
        if (currentViewport != null) {
            GridRenderer renderer = (GridRenderer) currentViewport.getViewportCanvas().getTickCallback("GridRenderer");
            int currentTileSize = renderer.getTileSize();
            renderer.setTileSize(currentTileSize + deltaSize);
        }
    }

    /**
     * Increases the tile size of the current viewport by 16 pixels.
     */
    public static void increaseTileSize() {
        changeTileSize(+16);
    }

    /**
     * Decreases the tile size of the current viewport by 16 pixels.
     */
    public static void decreaseTileSize() {
        changeTileSize(-16);
    }
}
