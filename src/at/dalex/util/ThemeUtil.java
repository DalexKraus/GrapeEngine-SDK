package at.dalex.util;

import at.dalex.grape.sdk.Main;
import javafx.scene.Parent;

/**
 * This class is used for applying themes to windows or {@link Parent} in general.
 *
 * @author David Kraus
 */
public class ThemeUtil {

    /**
     * Applies the current theme to the given {@link Parent}.
     * @param node The {@link Parent} you want to apply the theme to
     */
    public static void applyThemeToParent(Parent node) {
        //Different themes will be added in the future
        if (Main.useDarkTheme())
            node.getStylesheets().add("/resources/javafx/theme_dark/theme.css");
    }
}
