package at.dalex.grape.sdk.map;

import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.window.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MapUtil {

    public static final String MAPFILE_EXT = ".gmf";

    public static void createNewMap(String mapName) {
        String mapFilePath = ProjectUtil.getCurrentProject().getProjectDirectory() + "/maps/" + mapName + MAPFILE_EXT;
        try {
            FileWriter writer = new FileWriter(new File(mapFilePath));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Window.refreshFileBrowser();
    }
}
