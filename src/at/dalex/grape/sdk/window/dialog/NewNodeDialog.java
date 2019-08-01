package at.dalex.grape.sdk.window.dialog;

import at.dalex.grape.sdk.resource.ResourceLoader;
import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.scene.node.NodeReader;
import at.dalex.grape.sdk.scene.node.NodeTreeItem;
import at.dalex.grape.sdk.window.Window;
import at.dalex.util.ThemeUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NewNodeDialog extends Stage {

    /* The classes of all registered nodes  */
    private static ArrayList<Class>     nodeClasses;
    private static ArrayList<NodeBase>  nodeInstances = new ArrayList<>();
    /* HashMap containing every node's icon */
    private HashMap<String, Image> nodeIcons = new HashMap<>();

    /* Dialog widgets */
    private ListView<String> nodeListView;
    private TextField searchField;
    private Label nodeTitle;
    private Label nodeClass;

    private NodeBase lastSelectedNode;

    public NewNodeDialog() {
        /* Load node classes from resource file */
        File executableDir = ResourceLoader.getEditorExecutableDirectroy();
        File nodeFile = new File(executableDir + "/resources/editor_nodes.json");
        nodeClasses = NodeReader.readNodeFile(nodeFile);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/javafx/dialog_new_node.fxml"));
            fxmlLoader.setController(this);

            /* Load dialog FXML */
            Parent root = fxmlLoader.load();
            root.getStylesheets().add("/resources/javafx/theme_dark/new_node_dialog.css");
            ThemeUtil.applyThemeToParent(root);

            /* Retrieve fields from FXML */
            this.nodeListView   = (ListView<String>) root.lookup("#node_list");
            this.searchField    = (TextField) root.lookup("#node_search");
            this.nodeTitle      = (Label) root.lookup("#node_title");
            this.nodeClass      = (Label) root.lookup("#node_class");

            //Fill the node list
            loadNodeInstances();
            populateListView();

            //Add selection listener to the node list
            nodeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //Restrict to single selection
            nodeListView.addEventFilter(MouseEvent.MOUSE_CLICKED, handler -> {
                ObservableList selectedItems = nodeListView.getSelectionModel().getSelectedItems();
                String selectedNodeName = selectedItems.size() > 0 ? (String) selectedItems.get(0) : null;
                if (selectedNodeName != null) {
                    NodeBase selectedNodeInstance = getNodeByTitle(selectedNodeName);
                    nodeTitle.setText(selectedNodeName);
                    nodeClass.setText(selectedNodeInstance.getClass().getCanonicalName());
                    this.lastSelectedNode = selectedNodeInstance;

                    //User decides to create a node with the selected class (double click)
                    if (handler.getClickCount() == 2) {
                        handleNodeSelection(selectedNodeInstance);
                        close();
                    }
                } else this.lastSelectedNode = null;
            });

            //Add change listener to search field
            searchField.textProperty().addListener(changeListener -> populateListView());

            /* Create and set dialog scene */
            Scene dialogScene = new Scene(root, 600, 390);
            setScene(dialogScene);

            //Make dialog stay in foreground
            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the process of adding the user selected node
     * to the node tree in the editor.
     * @param selectedNodeInstance The selected node instance (selected by the user)
     */
    private void handleNodeSelection(NodeBase selectedNodeInstance) {
        //Get the selected node in the node tree
        NodeTreeItem selectedNodeTreeItem = Window.getScenePropertyPanel().getSelectedNode();

        //Create a new instance of the node, so the stored instance stays unaffected.
        try {
            NodeBase newInstance = selectedNodeInstance.getClass().newInstance();

            //Add new instance to the children of the selected node in the tree
            //and then refresh the children of the selected tree node in the editor
            selectedNodeTreeItem.getValue().getChildren().add(newInstance);
            selectedNodeTreeItem.setExpanded(true);
            selectedNodeTreeItem.refreshChildren();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println("[Error] Unable to create duplicate of node instance.");
            e.printStackTrace();
        }
    }

    /**
     * Fills the node list view with entries.
     * Every entry represents a available node which can be used in the editor.
     */
    private void populateListView() {
        nodeListView.getItems().clear();

        /* Custom cell factory for displaying the node's icon */
        nodeListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            public void updateItem(String name, boolean isEmpty) {
                super.updateItem(name, isEmpty);
                setText(name);
                setGraphic(new ImageView(nodeIcons.get(name)));
            }
        });

        /* Populating the list view, only including nodes matching the filter */
        String filterValue = searchField.getText().trim();
        boolean isFilterEnabled = filterValue.trim().length() > 0;
        boolean isSelectedNodeVisible = false; //Used to determine whether or not to change the information text
        for (NodeBase nodeInstance : nodeInstances) {
            if (isFilterEnabled && !nodeInstance.getTitle().toLowerCase().contains(filterValue.toLowerCase()))
                continue;

            //Add the current node instance to the list view
            nodeListView.getItems().add(nodeInstance.getTitle());

            //Check if the previously selected item is visible again
            if (lastSelectedNode != null && nodeInstance.getTitle().equals(lastSelectedNode.getTitle())) {
                nodeListView.getSelectionModel().select(nodeListView.getItems().size() - 1);
                isSelectedNodeVisible = true;
            }
        }

        /* Display default text if no node is selected */
        if (!isSelectedNodeVisible) {
            nodeTitle.setText("- No node selected -");
            nodeClass.setText("Select a node type to view additional information.");
        }
    }

    /**
     * Loads an instance for every registered node class.
     */
    private void loadNodeInstances() {
        nodeIcons.clear();
        nodeInstances.clear();

        for (Class<?> nodeClass : nodeClasses) {
            try {
                NodeBase nodeInstance = (NodeBase) nodeClass.newInstance();
                nodeIcons.put(nodeInstance.getTitle(), nodeInstance.getTreeIcon());
                nodeInstances.add(nodeInstance);

            } catch (InstantiationException | IllegalAccessException e) {
                System.err.println("[Error] Unable to create instance of node class.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns a registered node instance by it's title.
     * If no node instance with this title could be found,
     * null is returned.
     *
     * @param nodeTitle The title of the node to search for.
     * @return The instance of the node.
     */
    private NodeBase getNodeByTitle(String nodeTitle) {
        for (NodeBase nodeBase : nodeInstances) {
            if (nodeBase.getTitle().equals(nodeTitle)) {
                return nodeBase;
            }
        }
        return null;
    }
}
