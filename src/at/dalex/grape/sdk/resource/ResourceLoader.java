package at.dalex.grape.sdk.resource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

/**
 * This class functions as "storage" for objects (in this case resources)
 */
public class ResourceLoader {

    private static File editor_executable_directory;

    //The 'storage'
    private static HashMap<String, Object> storage = new HashMap<>();

    /* Retrieve the location of the program in the file system */
    static {
        try {
            String executablePath = ResourceLoader.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath();

            int separatorIndex = executablePath.lastIndexOf("/");
            editor_executable_directory = new File(executablePath.substring(1, separatorIndex));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Store a new object in the storage using a unique key.
     * @param object The object you want to store
     * @param key The key with which you can later retrieve the object
     */
    public static void store(Object object, String key) {
        if (object == null) return;
        storage.put(key, object);
        System.out.println("Stored object with key '" + key + "'");
    }

    /**
     * Returns an object stored previously using a unique key.
     * The object is automatically cast to the given class type.
     *
     * Note: Calling this method does not remove the object from storage!
     * To do this, please call the ResourceLoader#dispose method.
     *
     * @param key The key with which the object was stored
     * @param objectClass The class type of the object.
     * @param <T>
     * @return
     */
    public static <T> T get(String key, Class<T> objectClass) {
        if (storage.containsKey(key)) {
            return objectClass.cast(storage.get(key));
        }
        else {
            System.err.println("Could not find an object with key '" + key + "' in storage!");
            return null;
        }
    }

    /**
     * Removes a previously stored object form storage
     * @param key The key with which the object was stored
     */
    public static void dispose(String key) {
        storage.remove(key);
        //Call garbage collector
        System.gc();
    }

    /**
     * @return The directory in which this program is located.
     */
    public static File getEditorExecutableDirectory() {
        return editor_executable_directory;
    }

    public static URL getResource(String path) {
        System.out.println("STAT: " + editor_executable_directory.getAbsolutePath() + "/" + path);
        URL url =  ResourceLoader.class.getResource(editor_executable_directory.getAbsolutePath().replaceAll(File.separator, "/") + "/" + path);
        System.out.println("URL: " + url.getPath());
        return url;
    }
}
