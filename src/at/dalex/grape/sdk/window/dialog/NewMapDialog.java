package at.dalex.grape.sdk.window.dialog;

import at.dalex.grape.sdk.map.MapUtil;
import at.dalex.grape.sdk.resource.ResourceLoader;
import at.dalex.grape.sdk.window.helper.DialogHelper;
import at.dalex.util.ThemeUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class NewMapDialog {

    public static void showDialog() {
        Dialog dialog = new Dialog();
        dialog.setTitle("Create");
        dialog.setHeaderText("New Map File");
        ThemeUtil.applyThemeToParent(dialog.getDialogPane());

        dialog.setGraphic(new ImageView(ResourceLoader.get("image.icon.map", Image.class)));
        ButtonType createButtonType = new ButtonType("Create Map");
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField mapName = new TextField();
        Platform.runLater(() -> mapName.requestFocus());

        mapName.setPrefWidth(128);
        grid.add(new Label("Map name:"), 0, 0);
        grid.add(mapName, 1, 0);

        Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
        mapName.textProperty().addListener(((observable, oldValue, newValue)
                -> createButton.setDisable(newValue.trim().isEmpty())));

        dialog.getDialogPane().setContent(grid);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get().equals(createButtonType)) {
            if (mapName.getText().isEmpty()) {
                DialogHelper.showErrorDialog("Error", "Invalid information", "The map name must not be empty!");
                showDialog();
            }
            else MapUtil.createNewMap(mapName.getText());
        }
    }
}
