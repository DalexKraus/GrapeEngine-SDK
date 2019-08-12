package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.resource.ResourceLoader;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.viewport.ViewportCanvas;
import at.dalex.util.math.Vector2f;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents the foundation for every node usable in the editor.
 */
public abstract class NodeBase implements Serializable {

    /* Generic */
    private String title;
    private String treeIconStorageKey;

    /* Positioning */
    private Vector2f parentSpaceLocation;
    private int width;
    private int height;

    /* Node management */
    private ArrayList<NodeBase> children = new ArrayList<>();
    private NodeBase parent;

    /**
     * Create a new {@link NodeBase} using a given title.
     * This title will be shown in the scene's node list.
     *
     * The title vibible in the editor's node-creation window
     * is parsed from the corresponding json-file!
     *
     * @param title The title of this node
     * @param treeIconStorageKey The icon of the node in the tree,
     *                           in form of a storage key.
     */
    public NodeBase(String title, String treeIconStorageKey) {
        this.title = title;
        this.treeIconStorageKey = treeIconStorageKey;
    }

    public void drawNode(ViewportCanvas canvas, GraphicsContext g) {
        //RootNodes don't need to draw themselves.
        if (this instanceof RootNode)
            return;

        //Draw resize knobs
        Image knob = ResourceLoader.get("image.icon.node.resizeknob", Image.class);

        ViewportCanvas  currentCanvas   = Window.getSelectedViewport().getViewportCanvas();
        //The origin needs to be cloned in order to prevent modifications to the reference object
        Vector2f        viewportOrigin  = currentCanvas.getViewportOrigin().clone();
        Vector2f        worldPosition   = viewportOrigin.add(this.getWorldPosition());
        float           viewportScale   = currentCanvas.getViewportScale();

        Vector2f[] cornerPositions = getBoundaryCorners();
        float[] knobOffsets = { -4, -4, 4, -4, -4, 4, 4, 4 };

        for (int i = 0; i < knobOffsets.length; i += 2) {
            Vector2f knobPosition = worldPosition.add(cornerPositions[i / 2]);
            g.drawImage(knob,
                    knobPosition.x * viewportScale,
                    knobPosition.y * viewportScale,
                    4, 4);
        }

        //Finally draw the actual node
        draw(canvas, g);
    }

    /**
     * Every node must be able to draw itself (onto the viewport).
     * @param g The {@link GraphicsContext} from the {@link at.dalex.grape.sdk.window.viewport.ViewportCanvas}
     */
    protected abstract void draw(ViewportCanvas canvas, GraphicsContext g);

    /**
     * @return The title of this node visible in the scene's node list.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return The {@link Image} of this node visible in the scene's node list.
     */
    public Image getTreeIcon() {
        return ResourceLoader.get(this.treeIconStorageKey, Image.class);
    }

    /**
     * Sets the title of the node visible in the scene's node list.
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
     * @return The position of this node in the world.
     */
    public Vector2f getWorldPosition() {
        if (parentSpaceLocation == null) {
            this.parentSpaceLocation = new Vector2f();
        }
        return parent != null ? parent.getWorldPosition().add(parentSpaceLocation) : parentSpaceLocation;
    }

    /**
     * Sets the position of the node relative to it's parent.
     * @param parentSpaceLocation The position vector
     */
    public void setParentSpaceLocation(Vector2f parentSpaceLocation) {
        this.parentSpaceLocation = parentSpaceLocation;
    }

    /**
     * @return The width of this node in the world
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Sets the width of this node in the world.
     * @param width The new width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return The height of this node in the world
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Sets the height of this node in the world.
     * @param height The new height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return The corner positions of this node (in parent space).
     */
    public Vector2f[] getBoundaryCorners() {
        return new Vector2f[] {
                new Vector2f(parentSpaceLocation.x,         parentSpaceLocation.y),
                new Vector2f(parentSpaceLocation.x + width, parentSpaceLocation.y),
                new Vector2f(parentSpaceLocation.x,         parentSpaceLocation.y + height),
                new Vector2f(parentSpaceLocation.x + width, parentSpaceLocation.y + height)
        };
    }

    /**
     * Add a child node to this node.
     * @param child The child node to add
     */
    public void addChild(NodeBase child) {
        child.parent = this;
        this.children.add(child);
    }

    /**
     * @return The child {@link NodeBase}(s) that this node contains
     */
    public ArrayList<NodeBase> getChildren() {
        return this.children;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
