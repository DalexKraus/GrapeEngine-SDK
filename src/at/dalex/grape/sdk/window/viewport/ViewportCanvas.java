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

    private double x = 0;
    private void draw() {
        GraphicsContext g = getGraphicsContext2D();
        g.clearRect(0, 0, getWidth(), getHeight());

        g.setFill(Color.RED);
        g.fillRect(0, 0, x, getHeight());

        g.setFill(Color.BLACK);
        g.fillText("Canvas Render Test", 64, 64);
    }

    @Override
    public void run() {
        while (true) {
            draw();
            x += 8D;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        return getWidth();
    }

    @Override
    public double minHeight(double width) {
        return getHeight();
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
