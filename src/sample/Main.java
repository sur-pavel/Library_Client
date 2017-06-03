package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private TableView<SearchValue> searchTableView = new TableView<>();
    private final ObservableList<SearchValue> titles = FXCollections.observableArrayList();
    private List<DataField> dataFields;
    private ArrayList<Record> records = new ArrayList<>();
    private Record currentRecord;
    //    private ObservableList<SearchValue> searchValues = FXCollections.observableArrayList();
    private ArrayList<GuiField> guiFields;
    private GridPane editorGridPane;
    private MarcReader reader;
    private WebEngine webEngine;


    @Override
    public void start(Stage primaryStage) throws Exception {
        unimarcGet();
        primaryStage.setMaximized(true);
        currentRecord = records.get(0);

// viewBorderPane
        WebView webView = new WebView();
        webView.autosize();
        webView.setPrefSize(950, 220);
        webEngine = webView.getEngine();
        StringBuilder builder = new StringBuilder();
        dataFields = currentRecord.getDataFields();
        viewerBuilder(builder);
        webEngine.loadContent(builder.toString());
        BorderPane viewBorderPane = new BorderPane();
        viewBorderPane.setCenter(webView);
        viewBorderPane.setPadding(new Insets(10));

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

// editorTabPane
        // editorGridPane
        guiFields = new ArrayList<>();
        //      List fields = records.get(1).getVariableFields(tags);
        dataFields = currentRecord.getDataFields();
        for (DataField dataField : dataFields) {
            guiFields.add(new GuiField(dataField));
        }
        editorGridPane = new GridPane();
        //        gridPane.setAlignment(Pos.BASELINE_CENTER);
        editorGridPane.setVgap(5);
        editorGridPane.setHgap(5);
        editorGridPane.setPadding(new Insets(10));
        editorGridPane.setAlignment(Pos.CENTER);
        for (int i = 0, size = guiFields.size(); i < size; i++) {
            editorGridPane.add(new CheckBox(), 0, i + 1);

            Label label = new Label(guiFields.get(i).getName());
            label.setMinWidth(200);
            editorGridPane.add(label, 1, i + 1);

            editorGridPane.add(new Button(String.valueOf(guiFields.get(i).getButtonNum())), 2, i + 1);
            TextField field = new TextField(guiFields.get(i).getValue());
            field.setPrefWidth(980);
            editorGridPane.add(field, 3, i + 1);
        }
        //tabTech
        Tab tabTech = new Tab("Технология");
        ScrollPane scrollPane1 = new ScrollPane();
        scrollPane1.setFitToHeight(true);
        ScrollBar scrollBar1 = new ScrollBar();
        scrollBar1.setMax(500);
        scrollBar1.setMin(0);
        scrollBar1.setValue(100);
        scrollBar1.setUnitIncrement(30);
        scrollBar1.setBlockIncrement(35);
//        String[] tags1 = {"010", "100", "245", "250", "260", "300"};
        scrollPane1.setContent(editorGridPane);
        tabTech.setContent(scrollPane1);

/*        // tab Экземпляры
        Tab tab2 = new Tab("Экземпляры");
        ScrollPane scrollPane2 = new ScrollPane();
        scrollPane2.setFitToHeight(true);
        ScrollBar scrollBar2 = new ScrollBar();
        scrollBar2.setMax(500);
        scrollBar2.setMin(0);
        scrollBar2.setValue(100);
        scrollBar2.setUnitIncrement(30);
        scrollBar2.setBlockIncrement(35);
//        String[] tags2 = {"010", "100", "245", "250", "260", "300"};
        scrollPane2.setContent(editorGridPane);
        tabTech.setContent(scrollPane2);*/


        TabPane editorTabPane = new TabPane();
        editorTabPane.getTabs().add(tabTech);
//        editorTabPane.getTabs().add(tab2);
        // tabS


//searchVBox

        //searchTableView
        searchTableView.setEditable(true);
        TableColumn countValueColumn = new TableColumn("Кол-во");
        countValueColumn.prefWidthProperty().bind(searchTableView.widthProperty().multiply(0.2));
        countValueColumn.setCellValueFactory(
                new PropertyValueFactory<>("countValue"));
        // SIC! NB!  PropertyValueFactory<>("countValue") => get(set)CountValue()

        TableColumn searchValueColumn = new TableColumn("Значение");
        searchValueColumn.prefWidthProperty().bind(searchTableView.widthProperty().multiply(0.8));
        searchValueColumn.setCellValueFactory(
                new PropertyValueFactory<>("searchValue"));
        searchTableView.setItems(titles);
        searchTableView.getColumns().add(countValueColumn);
        searchTableView.getColumns().add(searchValueColumn);

        VBox searchVBox = new VBox();
        HBox hBox = new HBox();
        searchVBox.setSpacing(5);
        searchVBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(10, 10, 0, 10));
        Label label = new Label("Ключ:");
        TextField field = new TextField();
        hBox.getChildren().addAll(label, field);
        searchVBox.getChildren().addAll(searchTableView, hBox);


//rootSplitPane
        SplitPane splitPaneH1 = new SplitPane(searchVBox, editorTabPane);
        splitPaneH1.setDividerPositions(0.2);
        splitPaneH1.setOrientation(Orientation.HORIZONTAL);
        SplitPane splitPaneH2 = new SplitPane(foundGPane, viewBorderPane);
        splitPaneH2.setOrientation(Orientation.HORIZONTAL);
        splitPaneH2.setDividerPositions(0.35);
        SplitPane splitPaneV = new SplitPane(splitPaneH1, splitPaneH2);
        splitPaneV.setOrientation(Orientation.VERTICAL);
        searchTableView.getSelectionModel().getSelectedItems().addListener(onItemSelected);

        Scene scene = new Scene(splitPaneV);
        primaryStage.setTitle("Library Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ListChangeListener<SearchValue> onItemSelected = itemSelected -> {
        for (SearchValue searchValue : itemSelected.getList())

            for (Record record : records) {
                List recordFields = record.find("200", searchValue.getSearchValue());
                for (Object recordField : recordFields) {
                    String foundedString = recordField.toString();
                    if (foundedString.contains(searchValue.getSearchValue())) {
                        guiFields = new ArrayList<>();
                        dataFields = record.getDataFields();
                        for (DataField dataField : dataFields) {
                            guiFields.add(new GuiField(dataField));
                        }
                        editorGridPane.getChildren().removeAll();
                        for (int i = 0, size = guiFields.size(); i < size; i++) {
                            editorGridPane.add(new CheckBox(), 0, i + 1);

                            Label label = new Label(guiFields.get(i).getName());
                            label.setMinWidth(200);
                            editorGridPane.add(label, 1, i + 1);

                            editorGridPane.add(new Button(String.valueOf(guiFields.get(i).getButtonNum())), 2, i + 1);
                            TextField newField = new TextField(guiFields.get(i).getValue());
                            newField.setPrefWidth(980);
                            editorGridPane.add(newField, 3, i + 1);


                            StringBuilder builder = new StringBuilder();
                            dataFields = record.getDataFields();
                            viewerBuilder(builder);
                            webEngine.loadContent(builder.toString());
                        }

                    }
                }
            }

    };

    private void viewerBuilder(StringBuilder builder) {
        for (DataField dataField : dataFields) {
            String tag = dataField.getTag();
            builder.append(tag);
            List subFields = dataField.getSubfields();
            for (Object subField : subFields) {
                Subfield subfield = (Subfield) subField;
                char code = subfield.getCode();
                String data = subfield.getData();
                builder.append("$").append(code).append(data);
            }
            builder.append("<br>");
        }
    }

    private void unimarcGet() throws FileNotFoundException {
        InputStream in = new FileInputStream("ManyRecords.ISO");
        reader = new MarcStreamReader(in, "UTF8");

        // fill records
        while (reader.hasNext()) {
            Record record = reader.next();
            records.add(record);
        }

        // fill titles for searchTableView
        for (Record record : records) {
            DataField field = (DataField) record.getVariableField("200");
            List subfields = field.getSubfields();
            for (Object subField : subfields) {
                Subfield subfield = (Subfield) subField;
                char code = subfield.getCode();
                String s = subfield.getData();
                if (code == 'a')
                    titles.add(new SearchValue(1, s));
            }
        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}
