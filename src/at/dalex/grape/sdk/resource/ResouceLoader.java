package at.dalex.grape.sdk.resource;

import java.util.HashMap;

public class ResouceLoader {

    private static HashMap<String, Object> storage = new HashMap<String, Object>();

    public static void store(Object object, String key) {
        storage.put(key, object);
        System.out.println("Stored object with key '" + key + "'");
    }

    public static <T> T get(String key, Class<T> objectClass) {
        if (storage.containsKey(key)) {
            return objectClass.cast(storage.get(key));
        }
        else {
            System.err.println("Could not find an object with key '" + key + "' in storage!");
            return null;
        }
    }

    public static void removeObject(String key) {
        storage.remove(key);
    }
}
