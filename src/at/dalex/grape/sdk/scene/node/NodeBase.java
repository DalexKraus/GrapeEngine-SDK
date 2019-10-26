package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.resource.ResourceLoader;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.event.*;
import at.dalex.grape.sdk.window.viewport.ViewportCanvas;
import at.dalex.grape.sdk.window.viewport.ViewportPanel;
import at.dalex.util.ViewportUtil;
import at.dalex.util.math.Vector2f;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the foundation for every node usable in the editor.
 */
public abstract class NodeBase implements EventListener, Serializable {

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

    /* Editing */
    private boolean isSelected;
    private Rectangle[] resizeKnobHitboxes;

    /**
     * Create a new {@link NodeBase} using a given title.
     * This title will be shown in the scene's node list.
     *
     * The title visible in the editor's node-creation window
     * is parsed from the corresponding json-file!
     *
     * @param title The title of this node
     * @param treeIconStorageKey The icon of the node in the tree,
     *                           in form of a storage key.
     */
    public NodeBase(String title, String treeIconStorageKey) {
        this.title = title;
        this.treeIconStorageKey = treeIconStorageKey;

        if (!(this instanceof RootNode))
            ViewportUtil.getEditingViewport().addInteractionListener(this);
    }

    /**
     * Draws the node onto the viewport canvas currently visible to the user.
     * If the node is selected, resize-knobs are rendered here.
     *
     * @param canvas The current canvas visible to the user
     * @param g The {@link GraphicsContext} of that canvas.
     */
    public void drawNode(ViewportCanvas canvas, GraphicsContext g) {
        //RootNodes don't need to draw themselves.
        if (this instanceof RootNode)
            return;

        /* Draw resize knobs */
        if (isSelected) {
            Image knob = ResourceLoader.get("image.icon.node.resizeknob", Image.class);

            ViewportCanvas currentCanvas = Window.getSelectedViewport().getViewportCanvas();
            //The origin needs to be cloned in order to prevent modifications to the reference object
            Vector2f viewportOrigin = currentCanvas.getViewportOrigin().clone();
            Vector2f worldPosition = viewportOrigin.add(this.getWorldPosition());
            float viewportScale = currentCanvas.getViewportScale();

            int knobSize = 8;
            Vector2f[] cornerPositions = getBoundaryCorners();
            float[] knobOffsets = {-2, -2, 2, -2, -2, 2, 2, 2};
            for (int i = 0; i < knobOffsets.length; i++)
                if (knobOffsets[i] < 0) knobOffsets[i] -= knobSize;

            for (int i = 0; i < knobOffsets.length; i += 2) {
                Vector2f knobPosition = cornerPositions[i / 2].add(worldPosition);
                g.drawImage(knob,
                        knobPosition.x * viewportScale + knobOffsets[i],
                        knobPosition.y * viewportScale + knobOffsets[i + 1],
                        knobSize, knobSize);
            }
        }
        /* --- --- --- --- --- */

        //Finally draw the actual node
        draw(canvas, g);
    }

    /**
     * Every node must be able to draw itself (onto the viewport).
     * @param g The {@link GraphicsContext} from the {@link at.dalex.grape.sdk.window.viewport.ViewportCanvas}
     */
    protected abstract void draw(ViewportCanvas canvas, GraphicsContext g);

    /**
     * Checks if the given coordinates intersect
     * with the boundaries of this node.
     * The passed coordinates must be in screen-space.
     *
     * @return Intersects with the boundaries
     */
    public boolean intersectsWithScreenCoordinates(Vector2f screenCoordinates) {
        //Transform the screen-coordinates into world-coordinates
        ViewportCanvas currentCanvas = ViewportUtil.getEditingViewport().getViewportCanvas();
        screenCoordinates.scale(1.0f / currentCanvas.getViewportScale());
        screenCoordinates.add(currentCanvas.getViewportOrigin().clone().negate());

        //Check if coordinate is inside the boundaries of this node.
        //(Basic AABB collision check)
        Vector2f worldPos = getWorldPosition();
        return (screenCoordinates.x >= worldPos.x && screenCoordinates.y >= worldPos.y
                && screenCoordinates.x <= (worldPos.x + width)
                && screenCoordinates.y <= (worldPos.y + height));
    }

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
        return parent != null ? parent.getWorldPosition().clone().add(parentSpaceLocation) : parentSpaceLocation;
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
     * @return The corner positions of this node (in local space).
     */
    public Vector2f[] getBoundaryCorners() {
        return new Vector2f[] {
                new Vector2f(0, 0),
                new Vector2f(width, 0),
                new Vector2f(0, height),
                new Vector2f(width, height)
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

    /**
     * @return Whether or not this node is currently selected in the scene.
     */
    public boolean isSelected() {
        return this.isSelected;
    }

    /**
     * @return Whether or not any children are currently selected in the scene.
     */
    public boolean isAnyChildrenSelected() {
        for (NodeBase child : children) {
            if (child.isSelected() || child.isAnyChildrenSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * This node needs to be able to represent itself
     * in the node tree.
     * @return The name of this node.
     */
    @Override
    public String toString() {
        return this.title;
    }

    /**
     * Handle selection using interaction events
     * from the {@link ViewportCanvas} currently visible.
     * @param event
     */
    @EventHandler
    public void NodeInteractionHandler(InteractionEvent event) {
        MouseEvent mouseEvent = event.getMouseEventInstance();
        Vector2f mouseScreenPosition = new Vector2f(mouseEvent.getX(), mouseEvent.getY());

        // Selection logic
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (!isSelected() && intersectsWithScreenCoordinates(mouseScreenPosition)) {
                this.isSelected = true;

                //Invoke NodeSelectEvent
                ViewportPanel nodeViewport = ViewportUtil.getEditingViewport();
                NodeSelectEvent eventInstance = new NodeSelectEvent(this, nodeViewport);

            } else isSelected = false;
        }
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if (isSelected && intersectsWithScreenCoordinates(mouseScreenPosition)) {
                //setParentSpaceLocation(mouseScreenPosition);
            }
        }
    }
}
