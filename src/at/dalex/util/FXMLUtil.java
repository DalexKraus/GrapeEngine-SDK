package at.dalex.util;

import at.dalex.grape.sdk.resource.ResourceLoader;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FXMLUtil {

    /**
     * Loads the given FXML file.
     *
     * @param relativeFilePath The path of the fxml file relative to the editor
     * @return The loaded FXML file as {@link Parent}.
     */
    public static Parent loadRelativeFXML(String relativeFilePath, Initializable controllerInstance) {
        File fxmlFile = ResourceLoader.getAbsoluteFilePath(relativeFilePath);
        Parent fxmlParent = null;

        /* Try to read fxml file */
        try {
            /* Create new FXML loader and apply controller instance */
            FXMLLoader fxmlLoader  = new FXMLLoader();
            if (controllerInstance != null)
                fxmlLoader.setController(controllerInstance);

            fxmlParent = fxmlLoader.load(new FileInputStream(ResourceLoader.getAbsoluteFilePath(relativeFilePath)));
        } catch (IOException e) {
            System.out.println("Unable to load FXML file '" + fxmlFile.getAbsolutePath() + "'!");
            e.printStackTrace();
        }

        return fxmlParent;
    }

    /**
     * Loads a given stylesheet to a {@link Parent} object.
     * @param parent The JavaFX node to apply the stylesheet to
     * @param relativeFilePath The relative path of the css file.
     */
    public static void addStyleSheet(Parent parent, String relativeFilePath) {
        StringBuilder builder = new StringBuilder();
        builder.append("file:");
        builder.append(ResourceLoader.getAbsoluteFilePath(relativeFilePath));

        parent.getStylesheets().add(builder.toString());
    }
}
