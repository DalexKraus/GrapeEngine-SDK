package at.dalex.grape.sdk.window.dialog;

import at.dalex.grape.sdk.resource.ResourceLoader;
import at.dalex.grape.sdk.scene.node.NodeBase;
import at.dalex.grape.sdk.scene.node.NodeReader;
import at.dalex.grape.sdk.window.Window;
import at.dalex.grape.sdk.window.filebrowser.BrowserFile;
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
    private Label nodeTitle;
    private Label nodeClass;

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
            this.nodeTitle      = (Label) root.lookup("#node_title");
            this.nodeClass      = (Label) root.lookup("#node_class");

            //Fill the node list
            populateListView();

            //Add selection listener to the node list
            nodeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //Restrict to single selection
            nodeListView.addEventFilter(MouseEvent.MOUSE_CLICKED, handler -> {
                ObservableList selectedItems = nodeListView.getSelectionModel().getSelectedItems();
                String selectedNodeName = selectedItems.size() > 0 ? (String) selectedItems.get(0) : null;
                nodeTitle.setText(selectedNodeName);
                nodeClass.setText(getNodeByTitle(selectedNodeName).getClass().getCanonicalName());
            });

            /* Create and set dialog scene */
            Scene dialogScene = new Scene(root, 600, 400);
            setScene(dialogScene);

            //Make dialog stay in foreground
            initModality(Modality.APPLICATION_MODAL);
            show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fills the node list view with entries.
     * Every entry represents a available node which can be used in the editor.
     */
    private void populateListView() {
        nodeIcons.clear();
        nodeInstances.clear();

        /* Custom cell factory for displaying the node's icon */
        nodeListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            public void updateItem(String name, boolean isEmpty) {
                super.updateItem(name, isEmpty);
                setText(name);
                setGraphic(new ImageView(nodeIcons.get(name)));
            }
        });

        /* Read nodes, create new instance and extract title and image for list view */
        for (Class<?> nodeClass : nodeClasses) {
            try {
                NodeBase nodeInstance = (NodeBase) nodeClass.newInstance();
                nodeIcons.put(nodeInstance.getTitle(), nodeInstance.getTreeIcon());
                nodeInstances.add(nodeInstance);
                nodeListView.getItems().add(nodeInstance.getTitle());
            } catch (InstantiationException | IllegalAccessException e) {
                System.err.println("[Error] Unable to create instance of node class.");
                e.printStackTrace();
            }
        }
    }

    private NodeBase getNodeByTitle(String nodeTitle) {
        for (NodeBase nodeBase : nodeInstances) {
            if (nodeBase.getTitle().equals(nodeTitle)) {
                return nodeBase;
            }
        }
        return null;
    }
}
