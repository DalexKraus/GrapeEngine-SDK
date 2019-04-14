package at.dalex.grape.sdk;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;

public class Controller {

    private boolean isFullscreen;
    private double dragSceneX, dragSceneY;
    private Dimension screenSize;

    public Controller() {
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }

    @FXML
    public void minimizeWindow(MouseEvent e) {
        getStage(e).setIconified(true);
    }

    @FXML
    public void maximizeWindow(MouseEvent e) {
        getStage(e).setFullScreen(isFullscreen = !isFullscreen);
    }

    @FXML
    public void closeWindow(MouseEvent e) {
        getStage(e).close();
    }

    @FXML
    public void onMouseDragged(MouseEvent e) {
        Stage stage = getStage(e);
        double newX = e.getScreenX() - dragSceneX;
        double newY = e.getScreenY() - dragSceneY;
        if (newX >= 0 && newX <= (screenSize.getWidth() - stage.getWidth()))
            stage.setX(newX);
        if (newY >= 0 && newY <= (screenSize.getHeight() - 30))
            stage.setY(newY);
    }

    @FXML
    public void onMouseClicked(MouseEvent e) {
        dragSceneX = e.getSceneX();
        dragSceneY = e.getSceneY();
    }

    private Stage getStage(Event e) {
        return (Stage) ((Node) e.getSource()).getScene().getWindow();
    }
}
