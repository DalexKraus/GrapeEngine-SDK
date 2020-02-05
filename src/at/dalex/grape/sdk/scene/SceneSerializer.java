package at.dalex.grape.sdk.scene;

import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.scene.node.RootNode;
import at.dalex.grape.sdk.window.helper.DialogHelper;
import at.dalex.util.JSONUtil;
import at.dalex.util.math.Vector2f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

class SceneSerializer {

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
            childrenIds.add(nodeChild.getId().toString());
        }
        node.put("Children", childrenIds);

        return node;
    }

    public static Scene loadScene(File fqSceneFile) {
        Scene scene = null;
        JSONParser parser = new JSONParser();
        try {
            JSONObject root  = (JSONObject) parser.parse(new FileReader(fqSceneFile));
            //Parse generic scene information
            String sceneName = (String) root.get("SceneName");
            UUID rootNodeId  = UUID.fromString((String) root.get("RootNodeId"));
            //Create scene instance
            scene = new Scene(sceneName);

            //Parse scene nodes
            JSONObject nodes = (JSONObject) root.get("Nodes");
            HashMap<UUID, NodeBase> nodeInstances = new HashMap<>();
            HashMap<UUID, UUID[]> nodeChildren = new HashMap<>();
            for (Object nodeKey : nodes.keySet()) {
                UUID nodeId = UUID.fromString((String) nodeKey);
                JSONObject node = (JSONObject) nodes.get(nodeKey);
                String nodeClass = (String) node.get("NodeClass");

                //Parse Location
                double xPos, yPos;
                JSONObject parentSpaceNode = (JSONObject) node.get("ParentSpaceLocation");
                xPos = (double) parentSpaceNode.get("x");
                yPos = (double) parentSpaceNode.get("y");

                //Parse children ids
                JSONArray childNodes = (JSONArray) node.get("Children");
                UUID[] childrenIds = new UUID[childNodes.size()];
                for (int i = 0; i < childNodes.size(); i++) {
                    childrenIds[i] = UUID.fromString((String) childNodes.get(i));
                }
                nodeChildren.put(nodeId, childrenIds);

                //Create node instance
                NodeBase nodeInstance = instantiateNode(scene, nodeClass);
                if (nodeInstance == null)
                    continue;

                nodeInstance.setParentSpaceLocation(new Vector2f(xPos, yPos));
                nodeInstances.put(nodeId, nodeInstance);
            }

            for (UUID nodeId : nodeChildren.keySet()) {
                NodeBase nodeInstance = nodeInstances.get(nodeId);
                if (nodeInstance == null)
                    continue;

                //Collect children instances
                UUID[] childrenIds = nodeChildren.get(nodeId);
                for (UUID childId : childrenIds) {
                    NodeBase childInstance = nodeInstances.get(childId);
                    if (childInstance == null)
                        continue;
                    nodeInstance.addChild(childInstance);
                }
            }

            NodeBase rootNode = nodeInstances.get(rootNodeId);
            scene.setRootNode((RootNode) rootNode);
        } catch (ParseException | IOException e) {
            DialogHelper.showErrorDialog("Error", "Read Error", "The scene file could not be read.\n" +
                    "Target destination: " + fqSceneFile.getAbsolutePath() + "\n\n" +
                    "Please check read permissions.");
            e.printStackTrace();
        }

        return scene;
    }

    private static NodeBase instantiateNode(Scene sceneInstance, String nodeClass) {
        try {
            Class<?> instanceClass = Class.forName(nodeClass);
            Constructor<?> constructor = instanceClass.getConstructor(Scene.class);
            return (NodeBase) constructor.newInstance(sceneInstance);
        } catch (ClassNotFoundException     | NoSuchMethodException
                | InstantiationException    | IllegalAccessException
                | InvocationTargetException e) {
            System.err.println("Unable to instantiate node class!");
            e.printStackTrace();
        }
        return null;
    }
}
