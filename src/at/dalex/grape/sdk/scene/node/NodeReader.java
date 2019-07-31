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

    public static ArrayList<Class> readNodeFile(File nodeFile) {
        ArrayList<Class> nodeClasses = new ArrayList<>();
        JSONParser parser = new JSONParser();
        String className = null;
        try {
            JSONObject rootObject = (JSONObject) parser.parse(new FileReader(nodeFile));
            for (Object nodeName : rootObject.keySet()) {
                JSONObject nodeObject = (JSONObject) rootObject.get(nodeName);
                className = (String) nodeObject.get("class");
                nodeClasses.add(Class.forName(className));
                System.out.println("[Info] Registered node class '" + className + "'");
            }
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error", "Read Error", "Unable to read node file.\n" +
                    "Target destination: " + nodeFile.getAbsolutePath() + "\n\n" +
                    "Please check read permissions.");
        } catch (ClassNotFoundException e) {
            DialogHelper.showErrorDialog("Error", "Runtime Error", "The class '" + className + "'\n" +
                    "Could not be found.");
        } catch (ParseException e) {
            DialogHelper.showErrorDialog("Error", "Parse Error", "Could not parse the node file correctly.");
        }
        return nodeClasses;
    }
}
