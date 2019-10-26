package at.dalex.grape.sdk.scene;

import at.dalex.grape.sdk.scene.node.RootNode;

import java.io.Serializable;

public class Scene implements Serializable {

    private String name;
    private RootNode rootNode;

    public Scene(String name) {
        this.name = name;
        this.rootNode = new RootNode();
    }

    public boolean isAnyNodeSelected() {
        return rootNode.isAnyChildrenSelected();
    }

    public String getName() {
        return name;
    }

    public RootNode getRootNode() {
        return this.rootNode;
    }
}
