package at.dalex.grape.sdk.window.event;

import javafx.scene.input.MouseEvent;

public class ViewportInteractionEvent extends EventBase {

    private MouseEvent mouseEventInstance;

    public ViewportInteractionEvent(MouseEvent mouseEventInstance) {
        this.mouseEventInstance = mouseEventInstance;
    }

    public MouseEvent getMouseEventInstance() {
        return this.mouseEventInstance;
    }
}
