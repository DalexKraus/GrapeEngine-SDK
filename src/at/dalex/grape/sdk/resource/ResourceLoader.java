package at.dalex.grape.sdk.resource;

import at.dalex.util.OSUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * This class functions as "storage" for objects (in this case resources)
 */
public class ResourceLoader {

    private static File editor_executable_directory;

    //The 'storage'
    private static HashMap<String, Object> storage = new HashMap<>();

    /* Retrieve the location of the program */
    static {
        try {
            String editorPath = new File(ResourceLoader.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI()).toString();

            editorPath = OSUtil.replaceSeparators(editorPath);

            int lastSeparatorIndex = editorPath.lastIndexOf('/');
            editor_executable_directory = new File(editorPath.substring(0, lastSeparatorIndex));

            System.out.println("Editor executable directory: " + editor_executable_directory.getAbsolutePath());
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

    /**
     * Returns the absolute path of the given file.
     * @param relativePath A file relative to the editor's directory
     * @return The absolute file on disk.
     */
    public static String getAbsoluteFilePath(String relativePath) {
        //Replace all backslashes with forward ones when the editor is running on windows.
        String translatedRelativePath = OSUtil.replaceSeparators(relativePath);
        return editor_executable_directory.getAbsolutePath() + translatedRelativePath;
    }
}
