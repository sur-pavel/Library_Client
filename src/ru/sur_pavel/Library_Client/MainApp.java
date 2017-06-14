package ru.sur_pavel.Library_Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.sur_pavel.Library_Client.view.*;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private SplitPane vSplitPane;
    private SplitPane hSplitPane;
    private ViewerController viewerController;
    private EditorController editorController;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Library. Client");

/*
        UnimarcHandler unimarcHandler = new UnimarcHandler();
        Thread unimarcThread = new Thread(unimarcHandler);
        unimarcThread.start();
        primaryStage.setMaximized(true);
*/

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
            AnchorPane page = loader.load();

            Stage searchStage = new Stage();
            searchStage.setTitle("Search");
            searchStage.initModality(Modality.NONE);
            searchStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            searchStage.setScene(scene);
            SearchController controller = loader.getController();
            controller.setSearchStage(searchStage);

            controller.setEditorController(editorController);
            controller.setViewerController(viewerController);

            searchStage.show();
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
