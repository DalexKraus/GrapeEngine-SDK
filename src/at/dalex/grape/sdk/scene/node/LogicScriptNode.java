package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.window.viewport.ViewportCanvas;
import javafx.scene.canvas.GraphicsContext;

public class LogicScriptNode extends NodeBase {

    public LogicScriptNode(Scene parentScene) {
        super(parentScene, "LogicScript", "image.icon.node.logicscript");
        setWidth(16);
        setHeight(16);
    }

    @Override
    public void draw(ViewportCanvas canvas, GraphicsContext g) {

    }
}
