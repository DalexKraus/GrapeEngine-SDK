package at.dalex.grape.sdk.window.helper;

import javafx.scene.control.Alert;

public class DialogHelper {

    public static void showErrorDialog(String title, String header, String message) {
        Alert alertDialog = new Alert(Alert.AlertType.ERROR);
        alertDialog.setTitle(title);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(message);
        alertDialog.showAndWait();
    }
}
