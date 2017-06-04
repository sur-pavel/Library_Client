package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
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

    private final ObservableList<SearchValue> titles = FXCollections.observableArrayList();
    private List<DataField> dataFields;
    private ArrayList<Record> records = new ArrayList<>();
    private ArrayList<GuiField> guiFieldArrayList = new ArrayList<>();
    private Record currentRecord;
    private TabPane editorPane = new TabPane();
    private WebEngine webEngine;
    private StringBuilder builder;


    @Override
    public void start(Stage primaryStage) throws Exception {
        unimarcGet();
        createGuiFields();
        primaryStage.setMaximized(true);
//        primaryStage.setFullScreen(true);
        currentRecord = records.get(0);

// viewBorderPane
        WebView webView = new WebView();
        webView.autosize();
        webView.setPrefSize(950, 220);
        webEngine = webView.getEngine();
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

// editorPane
        setEditorPane();
        setViewer();
//searchVBox

        //searchTableView
        TableView<SearchValue> searchTableView = new TableView<>();
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
        searchValueColumn.setSortType(TableColumn.SortType.ASCENDING);
        searchTableView.getSortOrder().add(searchValueColumn);
        searchValueColumn.setSortable(true);
        searchTableView.sort();

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
        SplitPane splitPaneH1 = new SplitPane(searchVBox, editorPane);
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

    private void setEditorPane() {

        int[] tags = {200, 210, 10, 700};
        editorPane.getTabs().clear();
        editorPane.getTabs().add(getТab("Основное БО", tags));
        int[] tags2 = {910};
        editorPane.getTabs().add(getТab("Экземпляры", tags2));
    }

    private Tab getТab(String name, int[] tags) {
        Tab tab = new Tab(name);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setMax(500);
        scrollBar.setMin(0);
        scrollBar.setValue(100);
        scrollBar.setUnitIncrement(30);
        scrollBar.setBlockIncrement(35);
        VBox vbox = new VBox();
        for (GuiField guiField : guiFieldArrayList) {
            for (int tag : tags) {
                if (tag == guiField.getFieldNumber()) {
                    vbox.getChildren().add(guiField.getHBox());
                }
            }
        }
        scrollPane.setContent(vbox);
        tab.setContent(scrollPane);

        dataFields = currentRecord.getDataFields();
        for (DataField dataField : dataFields) {
            for (GuiField guiField : guiFieldArrayList) {
                if (Integer.parseInt(dataField.getTag()) == guiField.getFieldNumber()) {
                    builder = new StringBuilder();
                    setStringBuilder(dataField);
                    guiField.setValue(builder.toString());

                }
            }
        }
        return tab;
    }

    private void setStringBuilder(DataField dataField) {
        List subFields = dataField.getSubfields();
        for (Object subField : subFields) {
            Subfield subfield = (Subfield) subField;
            char code = subfield.getCode();
            String data = subfield.getData();
            builder.append("$").append(code).append(data);
        }
    }

    private StringBuilder getViewerBuilder() {
        StringBuilder builder = new StringBuilder();

        builder.append("<b>")
                .append(getData(675, 'a',"<br>"))
                .append(getData(899, 'm',"<br>"))
                .append(getData(700, 'a',", "))
                .append(getData(700, 'g',"<br>"))
                .append("</b>")
                .append(getData(200, 'a'," / "))
                .append(getData(700, 'b'," "))
                .append(getData(700, 'a',". - "))
                .append(getData(210, 'a'," : "))
                .append(getData(210, 'c',", "))
                .append(getData(210, 'd',"."))


        ;
        return builder;
    }

    private void setViewer() {
        webEngine.loadContent(getViewerBuilder().toString());
    }

    private ListChangeListener<SearchValue> onItemSelected = itemSelected -> {
        for (SearchValue searchValue : itemSelected.getList())
            for (Record record : records) {
                List recordFields = record.find("200", searchValue.getSearchValue());
                for (Object recordField : recordFields) {
                    String foundedString = recordField.toString();
                    if (foundedString.contains(searchValue.getSearchValue())) {
                        currentRecord = record;
                        setEditorPane();
                        setViewer();

                    }
                }
            }

    };


    private void unimarcGet() throws FileNotFoundException {
        InputStream in = new FileInputStream("ManyRecords.ISO");
        MarcReader reader = new MarcStreamReader(in, "UTF8");

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

    private void createGuiFields() {
        for (String s : Constanst.fieldsName) {
            int num = Integer.parseInt(s.split(":")[0]);
            guiFieldArrayList.add(new GuiField(num, s));
        }
    }

    private String getFieldData(int number, char code) {
        StringBuilder builder = new StringBuilder();
        for (DataField dataField : dataFields) {
            int tag = Integer.parseInt(dataField.getTag());
            if (tag == number) {
                List subFields = dataField.getSubfields(code);
                for (Object subField : subFields) {
                    Subfield subfield = (Subfield) subField;
                    builder.append(subfield.getData());
                }
            }
        }
        return builder.toString();
    }

    private String getData(int number, char code, String nextString){
        StringBuilder builder = new StringBuilder();
        String string = getFieldData(number, code);
        if (!string.equals("")){
            builder.append(string)
                    .append(nextString);
        }
        return builder.toString();
    }

    private String getFieldData(int number, char code, int subFNum) {
        return getString(number, code, subFNum - 1);
    }

    private String getString(int number, char code, int subFNum) {
        String data = "";
        DataField field = (DataField) currentRecord.getVariableField(String.valueOf(number));
        List subfields = field.getSubfields(code);
        Subfield subfield = (Subfield) subfields.get(subFNum);
        data = subfield.getData();
        return data;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
