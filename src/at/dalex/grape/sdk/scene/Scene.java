package at.dalex.grape.sdk.scene;

import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.scene.node.RootNode;
import at.dalex.util.math.Vector2f;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Scene implements Serializable {

    private String name;
    private RootNode rootNode;

    private ArrayList<UUID> registeredNodeIDs = new ArrayList<>();

    public Scene(String name) {
        this.name = name;
        this.rootNode = new RootNode(this);
    }

    public UUID generateNodeId() {
        UUID generatedId = UUID.randomUUID();
        return generatedId;
    }

    public boolean registerNodeId(UUID toRegister) {
        //Check if uuid is not present
        boolean isPresent = false;
        for (UUID registeredId : registeredNodeIDs) {
            if (registeredId.equals(toRegister)) {
                isPresent = true;
                break;
            }
        }

        if (!isPresent)
            registeredNodeIDs.add(toRegister);

        return isPresent;
    }

    public void deselectAllNodes() {
        deselectChildren(rootNode);
    }

    public void deselectChildren(NodeBase node) {
        node.setSelected(false);
        node.getChildren().forEach(this::deselectChildren);
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

    /**
     * Removes the given node instance from the scene,
     * updating the children as needed.
     *
     * @param toRemove The instance of the {@link NodeBase} to remove.
     */
    public void removeNode(NodeBase toRemove) {
        removeNodeImpl(rootNode, toRemove);
    }

    private void getNodesAtLocationImpl(NodeBase node, Vector2f worldLocation, ArrayList<NodeBase> nodes) {
        for (NodeBase childNode : node.getChildren()) {
            if (childNode.intersectsWithWorldCoordinates(worldLocation)) {
                nodes.add(childNode);
            }
            getNodesAtLocationImpl(childNode, worldLocation, nodes);
        }
    }

    private void removeNodeImpl(NodeBase currentNode, NodeBase toRemove) {
        ArrayList<NodeBase> removedNodes = new ArrayList<>();
        for (NodeBase childNode : currentNode.getChildren()) {
            if (childNode.equals(toRemove)) removedNodes.add(childNode);
            removeNodeImpl(childNode, toRemove);
        }
        currentNode.getChildren().removeAll(removedNodes);
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

    /**
     * Replaces the root node.
     * NOTE: The node tree needs to be refreshed after replacing the root node!
     *
     * @param rootNode The new root node of the scene.
     */
    public void setRootNode(RootNode rootNode) {
        System.err.println("[WARN] Scene root node has been replaced!");
        System.err.println("The Node tree needs to be rebuilt!");
        this.rootNode = rootNode;
    }
}
