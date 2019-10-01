package at.dalex.grape.sdk.window.helper;

import at.dalex.grape.sdk.Main;
import at.dalex.util.ThemeUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * This class is used to create dialogs. Fast.
 */
public class DialogHelper {

    /**
     * Shows a new information dialog.
     *
     * @param title The window title
     * @param header The title of the information
     * @param message The main text
     * @return Pressed button type
     */
    public static Optional<ButtonType> showInformationDialog(String title, String header, String message) {
        return createDialog(Alert.AlertType.INFORMATION, title, header, message).showAndWait();
    }

    /**
     * Shows a new error dialog.
     *
     * @param title The window title
     * @param header The title of the error
     * @param message The main text
     * @return Pressed button type
     */
    public static Optional<ButtonType> showErrorDialog(String title, String header, String message) {
        return createDialog(Alert.AlertType.ERROR, title, header, message).showAndWait();
    }

    /**
     * Shows a new alert dialog.
     *
     * @param title The window title
     * @param header The title of the alert
     * @param message The main text
     * @return Pressed button type
     */
    public static Optional<ButtonType> showAlertDialog(String title, String header, String message) {
        return createDialog(Alert.AlertType.WARNING, title, header, message).showAndWait();
    }

    /**
     * Creates a new dialog of a specific type.
     *
     * @param alertType The type of the dialog
     * @param title The window title
     * @param header The title of the dialog
     * @param message The main text
     *
     * @return The created Dialog
     */
    public static Alert createDialog(Alert.AlertType alertType, String title, String header, String message) {
        Alert alertDialog = new Alert(alertType);
        alertDialog.setTitle(title);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(message);

        //Apply dark theme
        ThemeUtil.applyThemeToParent(alertDialog.getDialogPane());

        return alertDialog;
    }
}
