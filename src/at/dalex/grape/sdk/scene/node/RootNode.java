package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.resource.ResourceLoader;
import javafx.scene.image.Image;

import java.io.Serializable;

/**
 * This class represents the root node for the scene.
 */
public class RootNode extends NodeBase implements Serializable {

    public RootNode() {
        super("Scene Root", ResourceLoader.get("image.icon.node.root", Image.class));
    }
}
