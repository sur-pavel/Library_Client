package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private List<DataField> dataFields;
    double stageWidth;
    double stageHeight;

    @Override
    public void start(Stage primaryStage) throws Exception {


        InputStream in = new FileInputStream("oneRec.ISO");
        MarcReader reader = new MarcStreamReader(in, "UTF8");
        while (reader.hasNext()) {
            Record record = reader.next();
            dataFields = record.getDataFields();
        }

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        stageWidth = bounds.getWidth();
        primaryStage.setWidth(stageWidth);
        stageHeight = bounds.getHeight();
        primaryStage.setHeight(stageHeight);

        Group root = new Group();
        Scene scene = new Scene(root, Color.WHITE);
        root.getChildren().add(getRootSplitPane(scene));

        primaryStage.setTitle("Library Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private SplitPane getRootSplitPane(Scene scene) {
        SplitPane splitPaneH1 = new SplitPane(getSearchBox(), getEditorTabPane());
        splitPaneH1.setOrientation(Orientation.HORIZONTAL);
        SplitPane splitPaneH2 = new SplitPane(getFoundGPane(), getRecordViewer());
        splitPaneH2.setOrientation(Orientation.HORIZONTAL);
        SplitPane splitPaneV = new SplitPane(splitPaneH1, splitPaneH2);
        splitPaneV.setOrientation(Orientation.VERTICAL);
        return splitPaneV;
    }

    private TabPane getEditorTabPane() {
        Tab tab = getEditorTab("Техонология");
        Tab tab2 = getEditorTab("Экземпляры");
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(tab);
        tabPane.getTabs().add(tab2);
        return tabPane;
    }

    private BorderPane getRecordViewer() {
        WebView webView = new WebView();
        webView.autosize();
//        webView.setPrefSize(950, 220);
        WebEngine webEngine = webView.getEngine();
        StringBuilder builder = new StringBuilder();
        for (DataField dataField : dataFields) {
            List subFields = dataField.getSubfields();
            for (Object subField : subFields) {
                Subfield subfield = (Subfield) subField;
                char code = subfield.getCode();
                String data = subfield.getData();
                builder.append("$").append(code).append(data);
            }
            builder.append("<br>");
        }
        webEngine.loadContent(builder.toString());
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(webView);
        return borderPane;
    }

    private Tab getEditorTab(String name) {
        Tab tab = new Tab(name);
        tab.setContent(getEditorGPane());
        return tab;
    }

    private VBox getSearchBox() {
        VBox vbox = new VBox();
        HBox hBox = new HBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(10, 0, 0, 10));
        Label label = new Label("Ключ:");
        TextField field = new TextField();
        hBox.getChildren().addAll(label, field);
        String[] columnsName = new String[]{"Кол-во", "Термины"};
        TableView table = getTableView(columnsName);
        vbox.getChildren().addAll(table, hBox);

        return vbox;
    }

    private GridPane getFoundGPane() {
        GridPane gridPane = getGridPane();
        String[] columnsName = new String[]{"Мн", "Запись"};
        TableView table = getTableView(columnsName);
        gridPane.add(table, 1, 1);
        return gridPane;
    }

    private TableView getTableView(String[] columnsName) {
        TableView table = new TableView();
        table.setEditable(true);
        for (String s : columnsName) {
            table.getColumns().add(new TableColumn(s));
        }
        table.maxHeight(10);
        return table;
    }

    private GridPane getEditorGPane() {
        ArrayList<GuiField> guiFields = new ArrayList<>();
        for (DataField dataField : dataFields) {
            guiFields.add(new GuiField(dataField));
        }
        GridPane gridPane = getGridPane();
        gridPane.setAlignment(Pos.CENTER);
        for (int i = 1, size = guiFields.size(); i < size; i++) {
            gridPane.add(new CheckBox(), 0, i);

            String fieldTitle = String.join(": ", String.valueOf(guiFields.get(i).getFieldNum()), guiFields.get(i).getName());
            gridPane.add(new Label(fieldTitle), 1, i);

            gridPane.add(new Button(String.valueOf(guiFields.get(i).getButtonNum())), 2, i);
            TextField field = new TextField(guiFields.get(i).getValue());
            field.setPrefWidth(900);
            gridPane.add(field, 3, i);
        }
        gridPane.prefWidth(100);
        return gridPane;
    }

    private GridPane getGridPane() {
        GridPane gridPane = new GridPane();
//        gridPane.setAlignment(Pos.BASELINE_CENTER);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(10));
        return gridPane;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
