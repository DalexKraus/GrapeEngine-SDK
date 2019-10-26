package at.dalex.grape.sdk.window.event;

import javafx.scene.input.MouseEvent;

public class InteractionEvent extends EventBase {

    private MouseEvent mouseEventInstance;

    public InteractionEvent(MouseEvent mouseEventInstance) {
        this.mouseEventInstance = mouseEventInstance;
    }

    public MouseEvent getMouseEventInstance() {
        return this.mouseEventInstance;
    }
}
