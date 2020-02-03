package at.dalex.grape.sdk.window;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

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
                try {
                    Platform.runLater(() -> ram_usage.setText(runtime.totalMemory() + " / " + runtime.maxMemory()));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();

        container.getChildren().add(ram_usage);
    }
}
