package at.dalex.grape.sdk.window.filebrowser;

import javafx.scene.image.Image;

public class FilterRule {

    private Visibility visibility;
    private Image customIcon;
    private String customName;

    public FilterRule(Visibility visibility) {
        this.visibility = visibility;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Image getCustomIcon() {
        return customIcon;
    }

    public String getCustomName() {
        return customName;
    }

    public static enum Visibility { VISIBLE, HIDDEN };

}
