package at.dalex.grape.sdk.window.dialog;

import at.dalex.grape.sdk.resource.ResourceLoader;
import at.dalex.grape.sdk.scene.Scene;
import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.scene.node.NodeReader;
import at.dalex.grape.sdk.scene.node.NodeTreeItem;
import at.dalex.grape.sdk.window.Window;
import at.dalex.util.FXMLUtil;
import at.dalex.util.ThemeUtil;
import at.dalex.util.ViewportUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class NewNodeDialog extends Stage {

    /* The classes of all registered nodes  */
    private static ArrayList<Class>     nodeClasses;
    private static ArrayList<NodeBase>  nodeInstances   = new ArrayList<>();
    //Dummy scene is needed to instantiate nodes
    private static Scene                dummyScene      = new Scene("Dummy-Scene");

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
        File executableDir = ResourceLoader.getEditorExecutableDirectory();
        File nodeFile = new File(executableDir + "/resources/editor_nodes.json");
        nodeClasses = NodeReader.readNodeFile(nodeFile);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(""));
        fxmlLoader.setController(this);

        /* Load dialog FXML */
        Parent root = FXMLUtil.loadRelativeFXML("/resources/javafx/dialog_new_node.fxml", null);
        FXMLUtil.addStyleSheet(root, "/resources/javafx/theme_dark/new_node_dialog.css");
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
        javafx.scene.Scene dialogScene = new javafx.scene.Scene(root, 600, 390);
        setScene(dialogScene);

        //Make dialog stay in foreground
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);
        show();
    }

    /**
     * Handles the process of adding the user selected node
     * to the node tree in the editor.
     * @param selectedNodeInstance The selected node instance (selected by the user)
     */
    private void handleNodeSelection(NodeBase selectedNodeInstance) {
        //Get the selected node in the node tree
        NodeTreeItem selectedNodeTreeItem = Window.getScenePropertyPanel().getSelectedNode();
        Scene currentlyOpenScene = ViewportUtil.getEditingScene();

        //Create a new instance of the node, so the stored instance stays unaffected.
        try {
            Constructor<?> constructor = selectedNodeInstance.getClass().getConstructor(Scene.class);
            NodeBase newInstance = (NodeBase) constructor.newInstance(currentlyOpenScene);

            //Add new instance to the children of the selected node in the tree
            //and then refresh the children of the selected tree node in the editor
            selectedNodeTreeItem.getValue().addChild(newInstance);
            selectedNodeTreeItem.setExpanded(true);
            selectedNodeTreeItem.refreshChildren();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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
        nodeListView.setCellFactory(param -> new ListCell<>() {
            @Override
            public void updateItem(String name, boolean isEmpty) {
                super.updateItem(name, isEmpty);
                setText(name);

                //Create a image view and resize it,
                //since the resolutions of the actual icons vary.
                ImageView nodeIconView = new ImageView(nodeIcons.get(name));
                nodeIconView.setFitWidth(16);
                nodeIconView.setFitHeight(16);
                setGraphic(nodeIconView);
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
                //Get default node constructor
                Constructor<?> constructor = nodeClass.getConstructor(Scene.class);
                NodeBase nodeInstance = (NodeBase) constructor.newInstance(dummyScene);
                nodeIcons.put(nodeInstance.getTitle(), nodeInstance.getTreeIcon());
                nodeInstances.add(nodeInstance);

            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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
