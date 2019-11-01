package at.dalex.grape.sdk.scene;

import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.scene.node.RootNode;
import at.dalex.grape.sdk.window.viewport.ViewportPanel;
import at.dalex.util.math.Vector2f;

import java.io.Serializable;
import java.util.ArrayList;

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
        registerNodeToViewport(rootNode, viewportInstance);
    }

    //TODO: Invoke somewhere
    public void registerNodeToViewport(NodeBase node, ViewportPanel viewportPanel) {
        for (NodeBase child : node.getChildren()) {
            viewportPanel.addInteractionListener(child);
            registerNodeToViewport(child, viewportPanel);
        }
    }

    /**
     * Returns all nodes in the scene whose boundaries intersect the given world location.
     * @param worldLocation The location in world space
     * @return An {@link ArrayList} containing all nodes present at the given location
     */
    public ArrayList<NodeBase> getNodesAtLocation(Vector2f worldLocation) {
        ArrayList<NodeBase> intersectingNodes = new ArrayList<>();
        getNodesAtLocationImpl(rootNode, worldLocation, intersectingNodes);
        return intersectingNodes;
    }

    private void getNodesAtLocationImpl(NodeBase node, Vector2f worldLocation, ArrayList<NodeBase> nodes) {
        for (NodeBase childNode : node.getChildren()) {
            if (childNode.intersectsWithWorldCoordinates(worldLocation)) {
                nodes.add(childNode);
            }
            getNodesAtLocationImpl(childNode, worldLocation, nodes);
        }
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
