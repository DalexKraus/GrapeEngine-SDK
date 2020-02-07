package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.window.viewport.ViewportCanvas;
import javafx.scene.canvas.GraphicsContext;

public class LogicAutosaveNode extends NodeBase {

    public LogicAutosaveNode(Scene parentScene) {
        super(parentScene, "LogicAutosave", "image.icon.node.logicautosave");
        setWidth(16);
        setHeight(16);
    }

    @Override
    public void draw(ViewportCanvas canvas, GraphicsContext g) {

    }
}
