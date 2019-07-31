package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.window.helper.DialogHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NodeReader {

    public static ArrayList<NodeBase> readNodeFile(File nodeFile) {
        ArrayList<NodeBase> readNodes = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject rootObject = (JSONObject) parser.parse(new FileReader(nodeFile));
            for (Object nodeName : rootObject.keySet()) {
                JSONObject nodeObject = (JSONObject) rootObject.get(nodeName);
                String className = (String) nodeObject.get("class");
                System.out.println("ClassName: " + className);
            }
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error", "Read Error", "Unable to read node file.\n" +
                    "Target destination: " + nodeFile.getAbsolutePath() + "\n\n" +
                    "Please check read permissions.");
        } catch (ParseException e) {
            DialogHelper.showErrorDialog("Error", "Parse Error", "Could not parse the node file correctly.");
        }
        return readNodes;
    }
}
