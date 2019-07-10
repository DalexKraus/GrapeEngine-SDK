package at.dalex.grape.sdk.window.filebrowser;

import at.dalex.grape.sdk.resource.ResourceLoader;
import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;

/**
 * This class is used to modify which items should be displayed
 * in the filebrowser.
 */
public class FileBrowserFilter {

    private static HashMap<File, FilterRule> fileRules = new HashMap<>();
    private static HashMap<String, FilterRule> extensionRules = new HashMap<>();

    /* Apply default rules here */
    //TODO: Maybe load from a fixed json file in the editor's resource folder?
    static {
        //Map files
        FilterRule mapFileRule = new FilterRule(FilterRule.Visibility.VISIBLE);
        mapFileRule.setCustomIcon(ResourceLoader.get("image.file.map", Image.class));
        //Lua files
        FilterRule luaFileRule = new FilterRule(FilterRule.Visibility.VISIBLE);
        luaFileRule.setCustomIcon(ResourceLoader.get("image.file.lua", Image.class));
        setExtensionRule(".lua", luaFileRule);
    }

    /**
     * Applies a new rule for a file.
     * @param file The file to apply the rule on
     * @param rule The rule for the file
     */
    public static void setFileRule(File file, FilterRule rule) {
        fileRules.put(file, rule);
    }

    /**
     * Removes a rule for a specific file
     * @param folder The Folder on which any rules were applied
     */
    public static void removeFileRule(File folder) {
        fileRules.remove(folder);
    }

    /**
     * Applies a rule only to a specific file extension.
     * This is pretty useful to change the icon for a filetype.
     * @param extension The file extension to getFilterStatus
     * @param rule The rule for those file(s)
     */
    public static void setExtensionRule(String extension, FilterRule rule) {
        extensionRules.put(extension, rule);
    }

    /**
     * @return the {@link FilterStatus} for a specific rule.
     */
    public static FilterStatus getFilterStatus(FilterRule rule) {
        if (rule != null) {
            if (rule.getVisibility() == FilterRule.Visibility.HIDDEN)
                return FilterStatus.APPLY_HIDE;
            else return FilterStatus.APPLY_CUSTOMS;
        }
        return FilterStatus.NO_MATCH;
    }

    /**
     * @return the {@link FilterStatus} for the given file
     */
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

    /**
     * What should be done with the file or folder?
     */
    enum FilterStatus { NO_MATCH, APPLY_HIDE, APPLY_CUSTOMS }
}
