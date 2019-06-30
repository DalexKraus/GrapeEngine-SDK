package at.dalex.grape.sdk.window.viewport;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ViewportCanvas extends Canvas implements Runnable {

    private final int MAX_WIDTH = 8192;
    private final int MAX_HEIGHT = 4608;

    private Thread updateThread;

    public ViewportCanvas() {
        //Update the canvas in a different thread to increase performance
        new Thread(this, "Viewport Update/Draw").start();
    }

    private void update() {

    }

    private void draw() {
        double width = getWidth();
        double height = getHeight();

        GraphicsContext g = getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.fillText("Canvas Render Test",  128, 64);
    }

    @Override
    public void run() {
        while (true) {
            update();
            draw();
        }
    }

    @Override
    public double maxWidth(double height) {
        return MAX_WIDTH;
    }

    @Override
    public double maxHeight(double width) {
        return MAX_HEIGHT;
    }

    @Override
    public double prefHeight(double width) {
        return minHeight(width);
    }

    @Override
    public double minWidth(double height) {
        return 64;
    }

    @Override
    public double minHeight(double width) {
        return 64;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        super.setWidth(width);
        super.setHeight(height);
    }
}
