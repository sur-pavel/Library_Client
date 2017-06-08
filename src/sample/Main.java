package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {


    private ArrayList<Record> records = new ArrayList<>();

    private Record currentRecord;
    private Viewer viewer = new Viewer();
    private Editor editor = new Editor();
    private Search search = new Search();
    private UnimarcHandler unimarcHandler = new UnimarcHandler();


    @Override
    public void start(Stage primaryStage) throws Exception {
        records = unimarcHandler.getUnimarc();
        primaryStage.setMaximized(true);
//        primaryStage.setFullScreen(true);
        currentRecord = records.get(0);
        List<DataField> dataFields = currentRecord.getDataFields();


// foundGPane
        // foundTable

        TableView<Void> foundTable = new TableView<>();
        String[] columnsName = new String[]{"Мн", "Запись"};
        foundTable.setEditable(true);
        for (String s : columnsName) {
            TableColumn<Void, Void> column = new TableColumn<>(s);
            foundTable.getColumns().add(column);
            double size = columnsName.length;
            column.prefWidthProperty().bind(foundTable.widthProperty().multiply(1 / size));
        }


        GridPane foundGPane = new GridPane();
        //        gridPane.setAlignment(Pos.BASELINE_CENTER);
        foundGPane.setVgap(5);
        foundGPane.setHgap(5);
        foundGPane.setPadding(new Insets(10));
        foundGPane.add(foundTable, 1, 1);

//rootSplitPane
        SplitPane splitPaneH1 = new SplitPane(editor.create());
        splitPaneH1.setDividerPositions(0.2);
        splitPaneH1.setOrientation(Orientation.HORIZONTAL);
        SplitPane splitPaneH2 = new SplitPane(foundGPane, viewer.create());
        splitPaneH2.setOrientation(Orientation.HORIZONTAL);
        splitPaneH2.setDividerPositions(0.35);
        SplitPane splitPaneV = new SplitPane(splitPaneH1, splitPaneH2);
        splitPaneV.setOrientation(Orientation.VERTICAL);
        splitPaneV.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W && event.isAltDown()) {
                editor.editorPane.requestFocus();
                editor.focusOnTextField();
            }
            if (event.getCode() == KeyCode.F && event.isControlDown()) {
                search.create(editor, viewer, records, unimarcHandler.getSearchMap());
            }
            if (event.getCode() == KeyCode.Q && event.isControlDown()) {
                primaryStage.close();
            }
/*
            if (event.getCode() == KeyCode.PLUS && event.isControlDown()) {

            }
*/

        });

        Scene scene = new Scene(splitPaneV);
        primaryStage.setTitle("Library Client");
        primaryStage.setScene(scene);
        primaryStage.show();
        editor.focusOnTextField();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
