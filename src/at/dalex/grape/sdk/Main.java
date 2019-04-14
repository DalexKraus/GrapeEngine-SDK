package at.dalex.grape.sdk;

import at.dalex.grape.sdk.resource.DefaultResources;
import at.dalex.grape.sdk.window.Window;
import javafx.application.Application;

public class Main {

    private static Main instance;
    public static final double VERSION = 1.2D;

    public Main(String[] args) {
        instance = this;

        DefaultResources.loadDefaultResources();
        Application.launch(Window.class, args);
    }

    public static Main getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        new Main(args);
    }
}
