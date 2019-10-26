package at.dalex.grape.sdk.window.event;

import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.window.viewport.ViewportPanel;
import at.dalex.util.ViewportUtil;

public class NodeSelectEvent extends EventBase {

    private NodeBase nodeInstance;
    private ViewportPanel viewportInstance;
    private Scene occurringScene;

    public NodeSelectEvent(NodeBase nodeInstance, ViewportPanel viewportInstance) {
        this.nodeInstance = nodeInstance;
        this.viewportInstance = viewportInstance;
        this.occurringScene = ViewportUtil.getEditingScene();
    }

    public NodeBase getNodeInstance() {
        return nodeInstance;
    }

    public ViewportPanel getViewportInstance() {
        return viewportInstance;
    }

    public Scene getOccurringScene() {
        return occurringScene;
    }
}
