package at.dalex.grape.sdk.window.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class DialogHelper {

    public static Optional<ButtonType> showErrorDialog(String title, String header, String message) {
        return createDialog(Alert.AlertType.ERROR, title, header, message).showAndWait();
    }

    public static Optional<ButtonType> showAlertDialog(String title, String header, String message) {
        return createDialog(Alert.AlertType.WARNING, title, header, message).showAndWait();
    }

    public static Alert createDialog(Alert.AlertType alertType, String title, String header, String message) {
        Alert alertDialog = new Alert(alertType);
        alertDialog.setTitle(title);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(message);
        return alertDialog;
    }
}
