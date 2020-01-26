package at.dalex.grape.sdk.scene.node;

import at.dalex.grape.sdk.resource.ResourceLoader;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.event.*;
import at.dalex.grape.sdk.window.propertypanel.ScenePropertyPanel;
import at.dalex.grape.sdk.window.viewport.ViewportCanvas;
import at.dalex.grape.sdk.window.viewport.ViewportPanel;
import at.dalex.grape.sdk.window.viewport.renderer.GridRenderer;
import at.dalex.util.ViewportUtil;
import at.dalex.util.math.Vector2f;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

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
     * Every node must be able to draw itself (onto the viewport).
     * @param g The {@link GraphicsContext} from the {@link at.dalex.grape.sdk.window.viewport.ViewportCanvas}
     */
    protected abstract void draw(ViewportCanvas canvas, GraphicsContext g);

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

        //Register the newly created node as listener to the viewport currently opened
        if (!(this instanceof RootNode)) { //RootNode is created when scene is created, therefore no viewport is open.
            ViewportPanel currentViewport = ViewportUtil.getEditingViewport();
            currentViewport.addInteractionListener(this);
        }
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

            Vector2f[] cornerPositions = getBoundaryCorners();
            ViewportCanvas currentCanvas = Window.getSelectedViewport().getViewportCanvas();
            //The origin needs to be cloned in order to prevent modifications to the reference object
            Vector2f viewportOrigin = currentCanvas.getViewportOrigin().clone();
            Vector2f worldPosition = viewportOrigin.add(this.getWorldPosition());
            float viewportScale = currentCanvas.getViewportScale();

            //Draw selection boundary box
            g.setStroke(Color.INDIANRED);
            g.setLineDashes(8);
            g.strokeRoundRect(
                    worldPosition.x * viewportScale - 2,
                    worldPosition.y * viewportScale - 2,
                    width * viewportScale + 4,
                    height * viewportScale + 4,
                    8, 8);
            g.setLineDashes(0);

            int knobSize = 8;
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
     * Checks if the given coordinates intersect
     * with the boundaries of this node.
     * The passed coordinates must be in screen-space.
     *
     * @return Intersects with the boundaries
     */
    public boolean intersectsWithScreenCoordinates(Vector2f screenCoordinates) {
        //Transform the screen-coordinates into world-coordinates
        ViewportCanvas currentCanvas = ViewportUtil.getEditingViewport().getViewportCanvas();
        Vector2f transformedCoordinates = screenCoordinates.clone();
        transformedCoordinates.scale(1.0f / currentCanvas.getViewportScale());
        transformedCoordinates.add(currentCanvas.getViewportOrigin().clone().negate());

        return intersectsWithWorldCoordinates(transformedCoordinates);
    }

    /**
     * Checks if the given coordinates intersect
     * with the boundaries of this node.
     * The passed coordinates must be in world-space.
     *
     * @return Intersects with the boundaries
     */
    public boolean intersectsWithWorldCoordinates(Vector2f worldCoordinates) {
        //Check if coordinate is inside the boundaries of this node.
        //(Basic AABB collision check)
        Vector2f worldPos = getWorldPosition();
        return (worldCoordinates.x >= worldPos.x && worldCoordinates.y >= worldPos.y
                && worldCoordinates.x <= (worldPos.x + width)
                && worldCoordinates.y <= (worldPos.y + height));
    }

    /* Fields used for selection determination */
    private boolean wasDragged;     //To avoid selection when cursor was dragged in node's boundaries if not sel.
    private boolean leftButtonHeld; //To avoid node teleportation to cursor when clicked somewhere else
    private Vector2f draggingOffset;
    /**
     * Handles the interaction event and decides whether or not
     * the node should be selected.
     *
     * @param mouseEvent The mouse event responsible for the invocation
     * @param canvas The canvas the interaction occurred on
     */
    public boolean handleInteractionEvent(MouseEvent mouseEvent, ViewportCanvas canvas) {
        //RootNodes cannot be selected anyway
        if (this instanceof RootNode)
            return false;

        //Viewport variables
        Vector2f viewportOrigin = canvas.getViewportOrigin();
        float viewportScale = canvas.getViewportScale();
        Vector2f mouseScreenPosition = new Vector2f(mouseEvent.getX(), mouseEvent.getY());

        // Selection logic
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
            // use wasDragged to prevent selection when node was dragged but not selected previously
            if (!isSelected) {
                this.isSelected = !wasDragged && !ViewportUtil.getEditingScene().isAnyNodeSelected();
            }
            wasDragged = false;
        }

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            leftButtonHeld = intersectsWithScreenCoordinates(mouseScreenPosition);

            //Calculate dragging offset
            Vector2f nodeWorldPos = getWorldPosition();
            draggingOffset = new Vector2f(
                    (mouseEvent.getX() / viewportScale) - nodeWorldPos.getX() - viewportOrigin.x,
                    (mouseEvent.getY() / viewportScale) - nodeWorldPos.getY() - viewportOrigin.y
            );
        }

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            leftButtonHeld = false;
        }

        //if the node is now selected, select the node in the node tree as well
        if (isSelected && mouseEvent.getEventType() != MouseEvent.MOUSE_MOVED) {
            ScenePropertyPanel propertyPanel = Window.getScenePropertyPanel();
            NodeTreeItem correspondingItem = propertyPanel.getNodeTreeReferences().get(this);
            TreeView nodeTree = propertyPanel.getNodeTree();

            int rowIndex = nodeTree.getRow(correspondingItem);
            nodeTree.getSelectionModel().select(rowIndex);
        }

        return isSelected;
    }

    /**
     * Used for deselecting using a click outside of the node's boundaries
     */
    @EventHandler
    public void onViewportInteraction(ViewportInteractionEvent e) {
        MouseEvent mouseEvent = e.getMouseEventInstance();
        Vector2f cursorScreenCoordinates = new Vector2f(mouseEvent.getX(), mouseEvent.getY());

        ViewportCanvas currentCanvas = ViewportUtil.getEditingViewport().getViewportCanvas();
        float viewportScale     = currentCanvas.getViewportScale();
        Vector2f viewportOrigin = currentCanvas.getViewportOrigin();

        boolean intersecting = intersectsWithScreenCoordinates(cursorScreenCoordinates);
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && !intersecting)
            this.isSelected = false;

        //Drag node
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if (isSelected && leftButtonHeld) {
                double cursorWorldX = (cursorScreenCoordinates.x / viewportScale) - viewportOrigin.x;
                double cursorWorldY = (cursorScreenCoordinates.y / viewportScale) - viewportOrigin.y;
                Vector2f newPosition = new Vector2f(cursorWorldX, cursorWorldY);

                if (ViewportUtil.shouldSnapToGrid()) {
                    //Calculate cell coordinates in which the cursor is currently in
                    GridRenderer gridRenderer = (GridRenderer) currentCanvas.getTickCallback("GridRenderer");
                    int tileSize = gridRenderer.getTileSize();
                    float tempX = ((int) (cursorWorldX / tileSize)) * tileSize;
                    float tempY = ((int) (cursorWorldY / tileSize)) * tileSize;

                    //When the cursor is inside negative bounds, subtract one cell more
                    newPosition.x = cursorWorldX < 0 ? -(tileSize + Math.abs(tempX)) : tempX;
                    newPosition.y = cursorWorldY < 0 ? -(tileSize + Math.abs(tempY)) : tempY;
                }
                else newPosition.sub(draggingOffset);

                //We need to set a different parent space location if the node we drag
                //is a child of some node, as the screen position isn't in parent space.
                if (!(parent instanceof RootNode)) {
                    Vector2f targetWorldSpace = newPosition;
                    Vector2f nodeParentSpace = parent.getWorldPosition();
                    Vector2f targetParentSpaceLocation = targetWorldSpace.sub(nodeParentSpace);
                    setParentSpaceLocation(targetParentSpaceLocation);
                }
                else setParentSpaceLocation(newPosition);
            }
            else wasDragged = true;
        }
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
    public Iterator<NodeBase> getChildren() {
        return this.children.iterator();
    }

    /**
     * @return Whether or not this node is currently selected in the scene.
     */
    public boolean isSelected() {
        return this.isSelected;
    }

    /**
     * Change the selection state of this node. (Used in the viewport)
     * @param selected Whether or not the node should be selected.
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected;

        //Reset fields used for node movement on the editor grid
        if (!selected) {
            this.wasDragged = false;
            this.leftButtonHeld = false;
            this.draggingOffset = null;
        }
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
}
