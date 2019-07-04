package at.dalex.grape.sdk.window.viewport;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class ViewportCanvas extends Canvas implements Runnable {

    private final int MAX_WIDTH = 8192;
    private final int MAX_HEIGHT = 4608;

    private Thread updateThread;
    private GraphicsContext graphicsContext;
    private ArrayList<ITickCallback> tickCallbacks = new ArrayList<>();

    public ViewportCanvas() {
        this.graphicsContext = getGraphicsContext2D();
        //Update the canvas in a different thread to increase performance
        new Thread(this, "Viewport Update/Draw").start();
    }

    private void draw() {
        //Clear canvas
        graphicsContext.clearRect(0, 0, getWidth(), getHeight());
        for (ITickCallback callback : this.tickCallbacks) {
            callback.update(0.0D);
            callback.draw(getGraphicsContext());
        }
    }

    @Override
    public void run() {
        while (true) {
            draw();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerTickCallback(ITickCallback callback) {
        if (!tickCallbacks.contains(callback))
            tickCallbacks.add(callback);
    }

    public void unregisterTickCallback(ITickCallback callback) {
        tickCallbacks.remove(callback);
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

    public GraphicsContext getGraphicsContext() {
        return this.graphicsContext;
    }
}
