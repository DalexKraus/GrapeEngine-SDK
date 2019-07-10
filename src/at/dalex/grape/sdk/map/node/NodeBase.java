package at.dalex.grape.sdk.map.node;

import java.util.ArrayList;

public abstract class NodeBase {

    private String title;

    private NodeBase parent;
    private ArrayList<NodeBase> children = new ArrayList<>();

    /**
     * Create a new {@link NodeBase} using a given title.
     * This title will be shown in the maps's node list.
     *
     * The title vibible in the editor's node-creation window
     * is parsed from the corresponding json-file!
     *
     * @param title The title of this node
     */
    public NodeBase(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title of the node visible in the maps's node list.
     *
     * Whitespaces and tabs will be removed.
     * If the given title only consists of whitespaces or tabs,
     * the given title will <b>NOT</b> be set!
     *
     * @param title The new title
     */
    public void setTitle(String title) {
        String trimmedTitle = title.trim();
        this.title = trimmedTitle.length() > 0 ? trimmedTitle : this.title;
    }

    /**
     * @return Returns the parent of this node in the tree
     */
    public NodeBase getParent() {
        return this.parent;
    }

    /**
     * @return The child {@link NodeBase}(s) that this node contains
     */
    public ArrayList<NodeBase> getChildren() {
        return this.children;
    }
}
