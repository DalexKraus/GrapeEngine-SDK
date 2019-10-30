package at.dalex.grape.sdk.resource;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class loads the default resources which
 * the editor needs to function correctly.
 */
public class DefaultResources {

    public static void loadDefaultResources() {
        //Images

        /* Application icon */
        ResourceLoader.store(loadImage("resources/images/grape.png"), "image.app-icon");

        /* Folders */
        ResourceLoader.store(loadImage("resources/images/folder_black32x32.png"), "image.folder.black32");
        ResourceLoader.store(loadImage("resources/images/folder_black16x16.png"), "image.folder.black16");
        ResourceLoader.store(loadImage("resources/images/project_icon.png"), "image.folder.project");

        /* Files */
        ResourceLoader.store(loadImage("resources/images/map_icon.png"), "image.file.scene");
        ResourceLoader.store(loadImage("resources/images/file_scene40x40.png"), "image.file.scene2");
        ResourceLoader.store(loadImage("resources/images/lua_icon.png"), "image.file.lua");

        /* Icons */
        ResourceLoader.store(loadImage("resources/images/map.png"), "image.icon.scene");
        ResourceLoader.store(loadImage("resources/images/node/resize_knob.png"), "image.icon.node.resizeknob");

        /* Node Icons*/
        ResourceLoader.store(loadImage("resources/images/node/rectangle.png"), "image.icon.node.rectangle");
        ResourceLoader.store(loadImage("resources/images/node/light.png"), "image.icon.node.light");
        ResourceLoader.store(loadImage("resources/images/node/logic_autosave.png"), "image.icon.node.logicautosave");
        ResourceLoader.store(loadImage("resources/images/node/logic_script.png"), "image.icon.node.logicscript");

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
            File executableDir = ResourceLoader.getEditorExecutableDirectory();
            File resourceFile = new File(executableDir.getAbsolutePath() + "/" + resourcePath);
            System.out.println("FILE: " +  resourceFile.getAbsolutePath());
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
