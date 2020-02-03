package at.dalex.grape.sdk.scene;

import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.util.math.Vector2f;
import org.json.simple.JSONObject;

import java.io.File;

public class SceneSerializer {

    public void writeScene(Scene scene, File destination) {
        JSONObject root = new JSONObject();
        root.put("Name", scene.getName());


    }

    private JSONObject createNodeObject(NodeBase nodeInstance) {
        JSONObject node = new JSONObject();
        /* Save node class */
        node.put("NodeClass", nodeInstance.getClass().getName());

        /* Save position */
        JSONObject position = new JSONObject();
        Vector2f parentSpaceLocation = nodeInstance.getParentSpaceLocation();
        position.put("x", parentSpaceLocation.getX());
        position.put("y", parentSpaceLocation.getY());
        node.put("ParentSpaceLocation", position);


        return node;
    }
}
