package at.dalex.grape.sdk.resource;

import at.dalex.grape.sdk.window.filebrowser.FileBrowserItem;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

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
        ResouceLoader.store(loadImage("resources/images/folder_black32x32.png"), "image.folder.black32");
        ResouceLoader.store(loadImage("resources/images/folder_black16x16.png"), "image.folder.black16");

        /* Files */
        ResouceLoader.store(loadImage("resources/images/file_black16x16.png"), "image.file.generic.black16");
    }

    private static Image loadImage(String resourcePath) {
        return readImage(new File(editor_executable_directory.getAbsolutePath() + "/" + resourcePath));
    }

    private static Image readImage(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            return SwingFXUtils.toFXImage(bufferedImage, null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
