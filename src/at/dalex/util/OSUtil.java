package at.dalex.util;

public class OSUtil {

    private static boolean runningWindows;

    static {
        runningWindows = System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static boolean isRunningWindows() {
        return runningWindows;
    }
}
