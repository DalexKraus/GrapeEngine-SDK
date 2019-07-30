package at.dalex.grape.sdk.window.listener;

import at.dalex.grape.sdk.map.MapUtil;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.editor.CodeEditorManager;
import at.dalex.grape.sdk.window.filebrowser.BrowserFile;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.io.File;

public class FileBrowserListener implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        //Redirect the event to the right method handling the event
        if      (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY)    handleDoubleClick(event);
        else if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY)  handleRightClick(event);
    }

    /**
     * Handles all double-clicks
     * @param event The MouseEvent responsible for the event
     */
    private void handleDoubleClick(MouseEvent event) {
        TreeItem<BrowserFile> selectedItem = getClickedBrowserItem();
        File clickedFile = selectedItem.getValue();
        String fileName = clickedFile.getName();

        if (fileName.endsWith(MapUtil.MAPFILE_EXT))
            Window.createViewport(fileName.replaceFirst("[.][^.]+$", ""));

        if (fileName.endsWith(".lua")) {

            Tab editor = new Tab("Code Editor", new StackPane(new VirtualizedScrollPane<>(CodeEditorManager.getCodeArea())));
            Window.getViewportTabPane().getTabs().add(editor);
        }
    }

    /**
     * Handles all right-clicks
     * @param event The MouseEvent responsible for the event
     */
    private void handleRightClick(MouseEvent event) {

    }

    private TreeItem<BrowserFile> getClickedBrowserItem() {
        ObservableList selectedItems = Window.getFileBrowser().getSelectionModel().getSelectedItems();
        return selectedItems.size() > 0 ? (TreeItem<BrowserFile>) selectedItems.get(0) : null;
    }
}
