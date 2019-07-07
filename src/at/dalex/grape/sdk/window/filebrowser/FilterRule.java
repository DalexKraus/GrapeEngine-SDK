package at.dalex.grape.sdk.window.filebrowser;

import javafx.scene.image.Image;

/**
 * Contains information about what to do with a file
 * for which this rule applies.
 */
public class FilterRule {

    private Visibility visibility;
    private Image customIcon;
    private String customName;

    public FilterRule(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * @return Should the file be visible?
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * @return Get the custom icon for this file (if set)
     */
    public Image getCustomIcon() {
        return customIcon;
    }

    /*
     * Sets the custom icon for this rule
     */
    public void setCustomIcon(Image icon) {
        this.customIcon = icon;
    }

    /**
     * @return Should the filename be replaced?
     */
    public String getCustomName() {
        return customName;
    }

    /**
     * Sets the custom name for this rule
     * @param customName
     */
    public void setCustomName(String customName) {
        this.customName = customName;
    }

    /**
     * The visibility for a file
     */
    public static enum Visibility { VISIBLE, HIDDEN };
}
