package at.dalex.grape.sdk.scene.node;

import java.io.Serializable;

/**
 * This class represents the root node for the scene.
 */
public class RootNode extends NodeBase implements Serializable {

    public RootNode() {
        super("Scene Root", "image.icon.node.root");
    }
}
