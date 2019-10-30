package at.dalex.grape.sdk.scene;

import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.scene.node.RootNode;
import at.dalex.grape.sdk.window.viewport.ViewportPanel;

import java.io.Serializable;

public class Scene implements Serializable {

    private String name;
    private RootNode rootNode;

    public Scene(String name) {
        this.name = name;
        this.rootNode = new RootNode();
    }

    public void deselectAllNodes() {
        deselectChildren(rootNode);
    }

    public void deselectChildren(NodeBase node) {
        node.setSelected(false);
        node.getChildren().forEach(this::deselectChildren);
    }

    /**
     * Registers all nodes in this scene as listener in the viewport instance.
     * @param viewportInstance The viewport to register the nodes in.
     */
    public void registerListenersToViewport(ViewportPanel viewportInstance) {

    }

    public void registerNodeToViewport(NodeBase node, ViewportPanel viewportPanel) {
        node.getChildren().forEach(child -> viewportPanel.register);
    }

    public boolean isAnyNodeSelected() {
        return rootNode.isAnyChildrenSelected();
    }

    public String getName() {
        return name;
    }

    public RootNode getRootNode() {
        return this.rootNode;
    }
}
