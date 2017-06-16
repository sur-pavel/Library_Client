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


    public Map<Leader, String> getSearchMap() {
        return this.searchMap;
    }

    public ArrayList<Record> getRecords() {
        return this.records;
    }
    public ViewerController getViewerController() {
        return this.viewerController;
    }

    public EditorController getEditorController() {
        return this.editorController;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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



    public void showSearch() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Search.fxml"));
            VBox page = loader.load();

            Stage searchStage = new Stage();
            searchStage.setTitle("Search");
            searchStage.initModality(Modality.NONE);
            searchStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
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
