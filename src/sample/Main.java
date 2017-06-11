package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.marc4j.marc.Record;

import java.util.ArrayList;

public class Main extends Application {


    private ArrayList<Record> records = new ArrayList<>();
    private Node editorFocusedNode;

    private Viewer viewer = new Viewer();
    private Editor editor = new Editor();
    private Search search = new Search();


    @Override
    public void start(Stage primaryStage) throws Exception {
        UnimarcHandler unimarcHandler = new UnimarcHandler();
        Thread unimarcThread = new Thread(unimarcHandler);
        unimarcThread.start();
        primaryStage.setMaximized(true);

        TableView<Void> foundTable = new TableView<>();
        String[] columnsName = new String[]{"Мн", "Запись"};
        foundTable.setEditable(true);
        for (String s : columnsName) {
            TableColumn<Void, Void> column = new TableColumn<>(s);
            foundTable.getColumns().add(column);
            double size = columnsName.length;
            column.prefWidthProperty().bind(foundTable.widthProperty().multiply(1 / size));
        }


//rootSplitPane
        SplitPane splitPaneH1 = new SplitPane(editor.create());
        splitPaneH1.setDividerPositions(0.2);
        splitPaneH1.setOrientation(Orientation.HORIZONTAL);
        SplitPane splitPaneH2 = new SplitPane(foundTable, viewer.create());
        splitPaneH2.setOrientation(Orientation.HORIZONTAL);
        splitPaneH2.setDividerPositions(0.35);
        SplitPane splitPaneV = new SplitPane(splitPaneH1, splitPaneH2);
        splitPaneV.setOrientation(Orientation.VERTICAL);


        BorderPane root = new BorderPane();
        root.setTop(getMenuBar());
        root.setCenter(splitPaneV);

        primaryStage.setTitle("Library Client");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        editor.editorTable.getSelectionModel().select(0,editor.editorTable.getColumns().get(1));
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W && event.isAltDown()) {
                editor.editorTable.requestFocus();
            }
            if (event.getCode() == KeyCode.F && event.isControlDown()) {
                search.create(editor, viewer, unimarcHandler.getRecords(), unimarcHandler.getSearchMap());
            }
        });
    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> Platform.exit());
        exit.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
        file.getItems().addAll(save, exit);

        Menu settings = new Menu("Settings");
        MenuItem fonts = new MenuItem("Fonts");
        settings.getItems().addAll(fonts);

        menuBar.getMenus().addAll(file, settings);
        return menuBar;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
