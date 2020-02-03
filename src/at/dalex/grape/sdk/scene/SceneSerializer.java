package at.dalex.grape.sdk.scene;

import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.window.helper.DialogHelper;
import at.dalex.util.JSONUtil;
import at.dalex.util.math.Vector2f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SceneSerializer {

    /**
     * Serializes the scene object.
     * The result is a json file.
     *
     * @param scene The scene to serialize-
     * @param fqDestination The fully qualified destination path of the output file.
     */
    public static void writeScene(Scene scene, File fqDestination) {
        JSONObject root = new JSONObject();
        /* Store the scene name*/
        root.put("SceneName", scene.getName());
        /* Store the root node id */
        UUID rootNodeId = scene.getRootNode().getId();
        root.put("RootNodeId", rootNodeId.toString());

        /* Store nodes */
        JSONObject nodes = new JSONObject();
        HashMap<UUID, JSONObject> nodeObjects = new HashMap<>();
        nodeObjects.put(scene.getRootNode().getId(), createNodeObject(scene.getRootNode()));    //Collect root node
        collectNodeObjects(nodeObjects, scene.getRootNode());                                   //Collect other nodes
        for (UUID nodeId : nodeObjects.keySet()) {
            nodes.put(nodeId, nodeObjects.get(nodeId));
        }
        root.put("Nodes", nodes);

        try (FileWriter file = new FileWriter(fqDestination)) {
            file.write(JSONUtil.prettyPrintJSON(root.toJSONString()));
            file.flush();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error", "Write Error", "The scene could not be written to disk.\n" +
                    "Target destination: " + fqDestination.getAbsolutePath() + "\n\n" +
                    "Please check read and write permissions.");
            e.printStackTrace();
        }
    }

    private static void collectNodeObjects(HashMap<UUID, JSONObject> collection, NodeBase rootNode) {
        for (NodeBase childNode : rootNode.getChildren()) {
            collection.put(childNode.getId(), createNodeObject(childNode));
            collectNodeObjects(collection, childNode);
        }
    }

    private static JSONObject createNodeObject(NodeBase nodeInstance) {
        JSONObject node = new JSONObject();
        /* Save node class */
        node.put("NodeClass", nodeInstance.getClass().getName());

        /* Save position */
        JSONObject position = new JSONObject();
        Vector2f parentSpaceLocation = nodeInstance.getParentSpaceLocation();
        position.put("x", parentSpaceLocation.getX());
        position.put("y", parentSpaceLocation.getY());
        node.put("ParentSpaceLocation", position);

        /* Save child ids */
        ArrayList<NodeBase> nodeChildren = nodeInstance.getChildren();
        JSONArray childrenIds = new JSONArray();
        for (NodeBase nodeChild : nodeChildren) {
            childrenIds.add(nodeChild.getId());
        }
        node.put("Children", childrenIds);

        return node;
    }
}
