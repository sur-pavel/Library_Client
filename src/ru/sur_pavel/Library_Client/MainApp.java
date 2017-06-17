package ru.sur_pavel.Library_Client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.marc4j.marc.Leader;
import org.marc4j.marc.Record;
import ru.sur_pavel.Library_Client.model.SearchValue;
import ru.sur_pavel.Library_Client.util.UnimarcHandler;
import ru.sur_pavel.Library_Client.view.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 *  Application for viewing and editing records in unimarc format.
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private SplitPane vSplitPane;
    private SplitPane hSplitPane;
    private ViewerController viewerController;
    private EditorController editorController;
    private ArrayList<Record> records = new ArrayList<>();
    private Map<Leader, String> searchMap;
    private ObservableList<SearchValue> titles = FXCollections.observableArrayList(new SearchValue("","",""));
    private UnimarcHandler unimarcHandler;

    /**
     * @return HashMap with short records for search created in UnimarcHandler
     */
    public Map<Leader, String> getSearchMap() {
        return this.searchMap;
    }

    /**
     * @return list of all unimarc records from UnimarcHandler
     */
    public ArrayList<Record> getRecords() {
        return this.records;
    }

    /**
     * @return object of viewerController
     */
    public ViewerController getViewerController() {
        return this.viewerController;
    }

    /**
     * @return object of editorController
     */
    public EditorController getEditorController() {
        return this.editorController;
    }

    /**
     * Called when the application start
     * @param primaryStage root stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Library. Client");
        this.unimarcHandler = new UnimarcHandler();
        Thread unimarcThread = new Thread(this.unimarcHandler);
        unimarcThread.start();
        primaryStage.setMaximized(true);

        initRoot();
        showEditor();
        showFound();
        showViewer();
    }

    /**
     * Load root layout from Root.fxml
     * and gets reference to verticalSplit
     */
    private void initRoot() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Root.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            RootController controller = loader.getController();
            controller.setMainApp(this);
            vSplitPane = controller.getVerticalSplit();

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Load editor layout from Editor.fxml
     * and adds it to root verticalSplit
     */

    private void showEditor() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Editor.fxml"));
            AnchorPane editor = loader.load();

            vSplitPane.getItems().add(editor);

            editorController = loader.getController();
            editorController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Load found layout from Found.fxml
     * create horizontalSplit in root verticalSplit
     * and adds found layout to verticalSplit
     */
    private void showFound() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Found.fxml"));
            AnchorPane found = loader.load();
            hSplitPane = new SplitPane();
            hSplitPane.setOrientation(Orientation.HORIZONTAL);
            vSplitPane.getItems().add(hSplitPane);
            hSplitPane.getItems().add(found);

            FoundController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Load viewer layout from Viewer.fxml
     * and adds it to horizontalSplit next to found layout
     */
    private void showViewer() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Viewer.fxml"));
            AnchorPane viewer = loader.load();

            hSplitPane.getItems().add(viewer);

            viewerController = loader.getController();
            viewerController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Load search layout from Search.fxml
     * create separate stage and scene
     * adds search vBox on scene
     * transfers the data necessary for the search
     */
    public void showSearch() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Search.fxml"));
            VBox vBox = loader.load();

            Stage searchStage = new Stage();
            searchStage.setTitle("Search");
            searchStage.initModality(Modality.NONE);
            searchStage.initOwner(primaryStage);
            Scene scene = new Scene(vBox);
            searchStage.setScene(scene);

            SearchController controller = loader.getController();
            controller.setSearchStage(searchStage);
            controller.setMainApp(this);
            controller.setTitles(titles);

            searchStage.show();

            records = unimarcHandler.getRecords();
            searchMap = unimarcHandler.getSearchMap();
            searchStage.focusedProperty().addListener((ov, t, t1) -> searchStage.close());
            controller.sceneKeys();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
