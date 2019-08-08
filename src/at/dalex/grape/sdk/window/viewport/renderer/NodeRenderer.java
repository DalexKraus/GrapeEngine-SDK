package at.dalex.grape.sdk.window.viewport.renderer;

import at.dalex.grape.sdk.project.ProjectUtil;
import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.viewport.ITickCallback;
import at.dalex.grape.sdk.window.viewport.ViewportCanvas;
import at.dalex.grape.sdk.window.viewport.ViewportPanel;
import javafx.scene.canvas.GraphicsContext;

/**
 * This class is used to draw all nodes present in the current scene onto the viewport.
 * The scaling is controlled by the {@link ViewportCanvas#getViewportScale()} value.
 */
public class NodeRenderer implements ITickCallback {

    private ViewportCanvas canvasInstance;

    /**
     * Used later on for occlusion queries
     */
    @SuppressWarnings("unused")
    private int viewportWidth;
    @SuppressWarnings("unused")
    private int viewportHeight;

    public NodeRenderer(ViewportCanvas canvasInstance) {
        this.canvasInstance = canvasInstance;
    }

    @Override
    public void update(double delta) {
        this.viewportWidth  = (int) canvasInstance.getWidth();
        this.viewportHeight = (int) canvasInstance.getHeight();
    }

    @Override
    public void draw(GraphicsContext g) {
        ViewportPanel currentViewport = Window.getSelectedViewport();
        if (currentViewport != null) {
            String viewportName = currentViewport.getText();
            Scene currentScene = ProjectUtil.getCurrentProject().getSceneByName(viewportName);
            drawNode(currentScene.getRootNode(), g);
        }
    }

    /**
     * Recursively draw a node and it's children onto the viewport.
     * TODO: Add a occlusion query
     *
     * @param nodeInstance The node to draw
     */
    private void drawNode(NodeBase nodeInstance, GraphicsContext g) {
        //Draw the given node
        nodeInstance.drawNode(canvasInstance, g);

        //Draw children
        for (NodeBase child : nodeInstance.getChildren()) {
            drawNode(child, g);
        }
    }
}
