package at.dalex.grape.sdk.window.viewport;

import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.scene.node.RootNode;
import at.dalex.grape.sdk.window.event.EventHandler;
import at.dalex.grape.sdk.window.event.EventListener;
import at.dalex.grape.sdk.window.event.ViewportInteractionEvent;
import at.dalex.grape.sdk.window.viewport.renderer.GridRenderer;
import at.dalex.util.ViewportUtil;
import at.dalex.util.math.Vector2f;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.LinkedList;

public class NodeSelectionHandler implements EventListener {

    private LinkedList<NodeBase> selectedNodes = new LinkedList<>();
    private Vector2f draggingOffset;
    private boolean isDragging;
    private boolean leftMouseDown;

    NodeSelectionHandler(ViewportPanel panel) {
        panel.addInteractionListener(this);
    }

    @EventHandler
    public void onViewportInteraction(ViewportInteractionEvent e) {
        //Only left click events are allowed
        if (e.getMouseEventInstance().getButton() != MouseButton.PRIMARY)
            return;

        ViewportCanvas currentCanvas        = ViewportUtil.getEditingViewport().getViewportCanvas();
        Scene currentScene                  = ViewportUtil.getEditingScene();
        float viewportScale                 = currentCanvas.getViewportScale();
        Vector2f viewportOrigin             = currentCanvas.getViewportOrigin();

        MouseEvent mouseEvent               = e.getMouseEventInstance();
        Vector2f cursorScreenCoordinates    = new Vector2f(mouseEvent.getX(), mouseEvent.getY());
        double cursorWorldX                 = (cursorScreenCoordinates.x / viewportScale) - viewportOrigin.x;
        double cursorWorldY                 = (cursorScreenCoordinates.y / viewportScale) - viewportOrigin.y;
        Vector2f cursorWorldPos             = new Vector2f(cursorWorldX, cursorWorldY);

        ArrayList<NodeBase> intersectingNodes = currentScene.getNodesAtLocation(cursorWorldPos);

        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            leftMouseDown = true;
        }

        /* Selection and deselection */
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            leftMouseDown = false;

            //Clear node selection if clicked into void
            if (intersectingNodes.size() == 0) {
                selectedNodes.forEach(node -> node.setSelected(false));
                selectedNodes.clear();

                isDragging = false;
            }
            else if (!isDragging) {
                NodeBase firstNode = intersectingNodes.get(0);
                if (!firstNode.isSelected()) {
                     firstNode.setSelected(true);
                     selectedNodes.add(firstNode);
                }
            }
        }

        /* Node dragging */
        else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
            isDragging = selectedNodes.size() > 0;

            //Move node to new position when dragging
            Vector2f newPosition = cursorWorldPos.clone();

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
            for (NodeBase selectedNode : selectedNodes) {
                NodeBase parent = selectedNode.getParent();
                if (!(parent instanceof RootNode)) {
                    Vector2f targetWorldSpace = newPosition;
                    Vector2f nodeParentSpace = parent.getWorldPosition();
                    newPosition = targetWorldSpace.sub(nodeParentSpace);
                }

                selectedNode.setParentSpaceLocation(newPosition);
            }
        }
    }
}

