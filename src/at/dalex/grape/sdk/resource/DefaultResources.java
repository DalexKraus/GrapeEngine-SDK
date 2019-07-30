package at.dalex.grape.sdk.resource;

import at.dalex.grape.sdk.window.filebrowser.FileBrowserItem;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * This class loads the default resources which
 * the editor needs to function correctly.
 */
public class DefaultResources {

    private static File editor_executable_directory;

    public static void loadDefaultResources() {
        try {
            editor_executable_directory = new File(FileBrowserItem.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //Images
        /* Folders */
        ResourceLoader.store(loadImage("resources/images/folder_black32x32.png"), "image.folder.black32");
        ResourceLoader.store(loadImage("resources/images/folder_black16x16.png"), "image.folder.black16");
        ResourceLoader.store(loadImage("resources/images/project_icon.png"), "image.folder.project");

        /* Files */
        ResourceLoader.store(loadImage("resources/images/map_icon.png"), "image.file.map");
        ResourceLoader.store(loadImage("resources/images/lua_icon.png"), "image.file.lua");

        /* Icons */
        ResourceLoader.store(loadImage("resources/images/map.png"), "image.icon.map");

        /* Node Icons*/
        ResourceLoader.store(loadImage("resources/images/node/rectangle.png"), "image.icon.node.rectangle");
        ResourceLoader.store(loadImage("resources/images/node/add.png"), "image.icon.node.add");
        ResourceLoader.store(loadImage("resources/images/node/root.png"), "image.icon.node.root");

        /* Files */
        ResourceLoader.store(loadImage("resources/images/file_black16x16.png"), "image.file.generic.black16");
    }

    /**
     * Loads an image from the resource folder of the editor
     * @param resourcePath The path of the image resource
     * @return The loaded image if successful.
     */
    private static Image loadImage(String resourcePath) {
        try {
            File resourceFile = new File(editor_executable_directory.getAbsolutePath() + "/" + resourcePath);
            BufferedImage bufferedImage = ImageIO.read(resourceFile);
            return SwingFXUtils.toFXImage(bufferedImage, null);
        }
        catch (IOException e) {
            System.err.println("Unable to read resource '" + resourcePath + "'!");
            e.printStackTrace();
        }
        return null;
    }
}
