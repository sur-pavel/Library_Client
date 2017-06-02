package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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
    ArrayList<Record> records = new ArrayList<Record>();


    @Override
    public void start(Stage primaryStage) throws Exception {


        InputStream in = new FileInputStream("OneRecord.ISO");
        MarcReader reader = new MarcStreamReader(in, "UTF8");
        while (reader.hasNext()) {
            Record record = reader.next();
            dataFields = record.getDataFields();
            records.add(record);
        }
        primaryStage.setMaximized(true);

        Scene scene = new Scene(getRootSplitPane());
        primaryStage.setTitle("Library Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private SplitPane getRootSplitPane() {
        SplitPane splitPaneH1 = new SplitPane(getSearchBox(), getEditorTabPane());
        splitPaneH1.setDividerPositions(0.2);
        splitPaneH1.setOrientation(Orientation.HORIZONTAL);
        SplitPane splitPaneH2 = new SplitPane(getFoundGPane(), getViewer());
        splitPaneH2.setOrientation(Orientation.HORIZONTAL);
        splitPaneH2.setDividerPositions(0.35);
        SplitPane splitPaneV = new SplitPane(splitPaneH1, splitPaneH2);
        splitPaneV.setOrientation(Orientation.VERTICAL);
        return splitPaneV;
    }

    private TabPane getEditorTabPane() {
        Tab tab = getEditorTab("Техонология");
        Tab tab2 = getEditorTab("Экземпляры");
        TabPane tabPane = new TabPane();
//        tabPane.setPrefWidth(1267);
        tabPane.getTabs().add(tab);
        tabPane.getTabs().add(tab2);
        return tabPane;
    }

    private BorderPane getViewer() {
        WebView webView = new WebView();
        webView.autosize();
        webView.setPrefSize(950, 220);
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
        borderPane.setCenter(webView);
        borderPane.setPadding(new Insets(10));
        return borderPane;
    }

    private Tab getEditorTab(String name) {
        Tab tab = new Tab(name);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        ScrollBar s1 = new ScrollBar();
        s1.setMax(500);
        s1.setMin(0);
        s1.setValue(100);
        s1.setUnitIncrement(30);
        s1.setBlockIncrement(35);
        String[] tags = {"010", "100", "245", "250", "260", "300"};
        scrollPane.setContent(getEditorGPane(tags));
        tab.setContent(scrollPane);
        return tab;
    }

    private VBox getSearchBox() {
        VBox vbox = new VBox();
        HBox hBox = new HBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(10, 10, 0, 10));
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

        TableView<Void> table = new TableView<>();
        table.setEditable(true);
        for (String s : columnsName) {
            TableColumn<Void, Void> column = new TableColumn<>(s);
            table.getColumns().add(column);
            double size = columnsName.length;
            column.prefWidthProperty().bind(table.widthProperty().multiply(1/size));
        }
        return table;
    }

    private GridPane getEditorGPane(String[] tags) {
        ArrayList<GuiField> guiFields = new ArrayList<>();
        List fields = records.get(1).getVariableFields(tags);
        for (DataField dataField : dataFields) {
            guiFields.add(new GuiField(dataField));
        }
        GridPane gridPane = getGridPane();
        gridPane.setAlignment(Pos.CENTER);
        for (int i = 0, size = guiFields.size(); i < size; i++) {
            gridPane.add(new CheckBox(), 0, i+1);

            Label label = new Label(guiFields.get(i).getName());
            label.setMinWidth(200);
            gridPane.add(label, 1, i+1);

            gridPane.add(new Button(String.valueOf(guiFields.get(i).getButtonNum())), 2, i+1);
            TextField field = new TextField(guiFields.get(i).getValue());
            field.setPrefWidth(980);
            gridPane.add(field, 3, i+1);
        }
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
