package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
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
    private Record record;

    @Override
    public void start(Stage primaryStage) throws Exception {


        InputStream in = new FileInputStream("oneRec.ISO");
        MarcReader reader = new MarcStreamReader(in, "UTF8");
        while (reader.hasNext()) {
            record = reader.next();
            dataFields = record.getDataFields();
            System.out.println(record.toString());
        }

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        Group root = new Group();
        Scene scene = new Scene(root, Color.WHITE);
        root.getChildren().add(getFirstGridPane(scene));

        primaryStage.setTitle("Library Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane getFirstGridPane(Scene scene) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);
//        gridPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT); // Default width and height
        gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setPadding(new Insets(10));
        gridPane.maxHeight(100);
        Tab tab = getTab("Техонология");
        Tab tab2 = getTab("Экземпляры");
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(tab);
        tabPane.getTabs().add(tab2);
        ScrollPane scrollPane = getRecordViewer();
        gridPane.add(getSearchBox(), 0, 0);
        gridPane.add(tabPane, 1, 0);
        gridPane.add(getFoundGPane(), 0, 1);
        gridPane.add(scrollPane, 1, 1);
        return gridPane;
    }

    private ScrollPane getRecordViewer() {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
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
        ScrollPane scrollPane = new ScrollPane(browser);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;
    }

    private Tab getTab(String name) {
        Tab tab = new Tab(name);
        GridPane gridPane = getEditorGPane();
        tab.setContent(gridPane);
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
        gridPane.setGridLinesVisible(true);
        ColumnConstraints column1 = new ColumnConstraints(10,10,Double.MAX_VALUE);
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints(10,10,Double.MAX_VALUE);
        column2.setHgrow(Priority.ALWAYS);
        ColumnConstraints column3 = new ColumnConstraints(10,10,Double.MAX_VALUE);
        column3.setHgrow(Priority.ALWAYS);
        ColumnConstraints column4 = new ColumnConstraints(100,100,Double.MAX_VALUE);
        column4.setHgrow(Priority.ALWAYS);
        for (int i = 1, size = guiFields.size(); i < size; i++) {
            gridPane.add(new CheckBox(), 0, i);
            String fieldTitle = String.join(": ", String.valueOf(guiFields.get(i).getFieldNum()), guiFields.get(i).getName());

            gridPane.add(new Label(fieldTitle), 1, i);
            gridPane.add(new Button(String.valueOf(guiFields.get(i).getButtonNum())), 2, i);

            TextField field = new TextField(guiFields.get(i).getValue());
            field.prefWidth(100);
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
