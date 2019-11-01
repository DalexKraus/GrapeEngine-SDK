package at.dalex.util;

public class OSUtil {

    private static boolean runningWindows;

    /* Running os cannot change while the editor is running, so we only need to grab once */
    static {
        runningWindows = System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * @return Whether or not the editor is currently running on windows.
     */
    public static boolean isRunningWindows() {
        return runningWindows;
    }

    /**
     * <b>When running on windows</b>,
     * replaces windows path separators with unix ones.
     *
     * @return The given path separated with unix separators
     */
    public static String replaceSeparators(String path) {
        return isRunningWindows() ? path.replaceAll("\\\\", "/") : path;
    }
}
