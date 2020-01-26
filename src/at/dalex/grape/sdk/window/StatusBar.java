package at.dalex.grape.sdk.window;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StatusBar {

    private HBox container = new HBox();

    /* Ram Usage */
    private Label ram_usage;
    private Thread updateThread;

    public StatusBar(HBox container) {
        this.ram_usage      = new Label();
        this.updateThread   = new Thread(() -> {
            Runtime runtime = Runtime.getRuntime();
            while (true) {
                Platform.runLater(() -> ram_usage.setText(runtime.totalMemory() + " / " + runtime.maxMemory()));
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();

        container.getChildren().add(ram_usage);
    }
}
