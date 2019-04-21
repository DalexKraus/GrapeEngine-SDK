package at.dalex.grape.sdk.window.filebrowser;

import java.io.File;
import java.util.HashMap;

public class FileBrowserFilter {


    private static HashMap<File, FilterRule> fileRules = new HashMap<>();
    private static HashMap<String, FilterRule> extensionRules = new HashMap<>();

    /* Apply default rules here */
    static {

    }

    public static void setFolderRule(File folder, FilterRule rule) {
        if (folder.isDirectory()) {
            setFileRule(folder, rule);
        }
    }

    public static void setFileRule(File file, FilterRule rule) {
        fileRules.put(file, rule);
    }

    public static void removeFileRule(File folder) {
        fileRules.remove(folder);
    }

    public static void setExtensionRule(String extension, FilterRule rule) {
        extensionRules.put(extension, rule);
    }

    public static FilterStatus filter(FilterRule rule) {
        if (rule != null) {
            if (rule.getVisibility() == FilterRule.Visibility.HIDDEN)
                return FilterStatus.APPLY_HIDE;
            else return FilterStatus.APPLY_CUSTOMS;
        }
        return FilterStatus.NO_MATCH;
    }

    public static FilterRule getRuleFor(File toFilter) {
        //Check file rules first
        for (File file : fileRules.keySet()) {
            if (file.getAbsolutePath().equals(toFilter.getAbsolutePath())) {
                return fileRules.get(file);
            }
        }
        //Check extension rules
        for (String ext : extensionRules.keySet()) {
            if (toFilter.getName().endsWith(ext)) {
                return extensionRules.get(ext);
            }
        }
        return null;
    }

    enum FilterStatus { NO_MATCH, APPLY_HIDE, APPLY_CUSTOMS }
}
