package sample;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.marc4j.marc.*;

import java.util.*;

class Search {

    private Viewer viewer;
    private Editor editor;
    private Record currentRecord;
    private Porter porter = new Porter();
    private TableView<SearchValue> searchTableView;
    private int currentRow = 0;
    private TableColumn searchValueColumn;
    private TableColumn countValueColumn;
    private String[] searchFields = {"200"};
    private ArrayList<Record> records = new ArrayList<>();
    private final ObservableList<SearchValue> titles = FXCollections.observableArrayList();


    void create(Editor ed, Viewer v, ArrayList<Record> recs, Map<Leader, String> sMap) {

        viewer = v;
        editor = ed;
        records = recs;

        Stage searchStage = new Stage();
        getSearchTableView(searchStage);
        final ComboBox comboBox = getComboBox();
        TextField field = getSearchField(comboBox);
        HBox hBox = gethBox(field);
        Label notification = new Label();
        VBox searchVBox = getvBox(hBox, notification);

        Scene searchScene = getScene(searchStage, field, searchVBox);
        searchStage.setTitle("Search");
        searchStage.setScene(searchScene);

        searchStage.initModality(Modality.NONE);
        searchStage.initOwner(editor.editorPane.getScene().getWindow());
        searchStage.show();
        searchStage.focusedProperty().addListener((ov, t, t1) -> searchStage.close());
        field.requestFocus();
    }

    private Scene getScene(Stage searchStage, TextField field, VBox searchVBox) {
        Scene searchScene = new Scene(searchVBox, 800, 300);
        searchScene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.F && event.isControlDown()) {
                currentRow = searchTableView.getSelectionModel().getFocusedIndex();
                field.requestFocus();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                searchStage.close();
            }
        });
        return searchScene;
    }

    private VBox getvBox(HBox hBox, Label notification) {
        VBox searchVBox = new VBox();
        searchVBox.setSpacing(5);
        searchVBox.setPadding(new Insets(10, 10, 10, 10));
        searchVBox.getChildren().addAll(hBox, notification, searchTableView);
        return searchVBox;
    }

    private HBox gethBox(TextField field) {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(10, 10, 0, 10));
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.getChildren().add(field);
        return hBox;
    }

    private ComboBox getComboBox() {
        final ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Заглавие", "Автор");
        comboBox.setValue("Заглавие");
        return comboBox;
    }

    private TextField getSearchField(ComboBox comboBox) {
        TextField field = new TextField();
        field.setPrefWidth(400);
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (comboBox.getValue() != null &&
                    !comboBox.getValue().toString().isEmpty()) {
                String fieldName = comboBox.getValue().toString();
                String text = field.getText().toLowerCase();
                if (text.length() > 3) search(fieldName, text);
            }
        });
        field.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                   /* if (comboBox.getValue() != null &&
                            !comboBox.getValue().toString().isEmpty()) {
                        String s = comboBox.getValue().toString();
                        String text = field.getText().toLowerCase();
                        update(s, text);
                    }*/
                case DOWN:
                    searchTableView.getFocusModel().focus(currentRow);
                    break;


            }
        });
        return field;
    }

    private void getSearchTableView(Stage searchStage) {
        searchTableView = new TableView<>();
        searchTableView.setEditable(true);
        countValueColumn = new TableColumn("Год");
        countValueColumn.prefWidthProperty().bind(searchTableView.widthProperty().multiply(0.1));
        countValueColumn.setCellValueFactory(
                new PropertyValueFactory<>("countValue"));
        // SIC! NB!  PropertyValueFactory<>("countValue") => get(set)CountValue()

        searchValueColumn = new TableColumn("Заглавие");
        searchValueColumn.prefWidthProperty().bind(searchTableView.widthProperty().multiply(0.9));
        searchValueColumn.setCellValueFactory(
                new PropertyValueFactory<>("searchValue"));

        searchTableView.setItems(titles);
        searchTableView.getColumns().add(countValueColumn);
        searchTableView.getColumns().add(searchValueColumn);
        autoSort(searchValueColumn);
//        searchTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        searchTableView.getSelectionModel().getSelectedItems().addListener(onItemSelected);
        searchTableView.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    searchStage.close();
            }
        });
    }

    private void autoSort(TableColumn searchValueColumn) {
        searchValueColumn.setSortType(TableColumn.SortType.ASCENDING);
        searchTableView.getSortOrder().add(searchValueColumn);
        searchValueColumn.setSortable(true);
        searchTableView.sort();
    }

    private ListChangeListener<SearchValue> onItemSelected = itemSelected -> {
        for (SearchValue searchValue : itemSelected.getList())
            for (Record record : records) {
                Leader leader = record.getLeader();
                System.out.println("search:" + searchValue.getLeader());
                if (leader.toString().equals(searchValue.getLeader())) {
                    System.out.println("record:" + leader.toString());
                    currentRecord = record;
                    editor.update(currentRecord);
                    viewer.update(currentRecord);
                }
            }
    };


    private void search(String fieldName, String input) {
        String[] splitInput = input.split(" ");

        for (Record record : records) {

            StringBuilder searchFieldsInRow = new StringBuilder();
            for (String searchField : searchFields) {
                if (record.getVariableField(String.valueOf(searchField)) != null) {
                    DataField field = (DataField) record.getVariableField(searchField);
                    searchFieldsInRow.append(field.toString().toLowerCase());
                    System.out.println(searchFieldsInRow.toString());
                }
            }
            if(containsAllWords(searchFieldsInRow.toString(), splitInput)){
                Leader leader = record.getLeader();
                    String title = new StringJoiner(". ")
                            .add(subFieldData(record, "200", 'a'))
                            .add(subFieldData(record, "200", 'h'))
                            .add(subFieldData(record, "205", 'a'))
                            .toString();
                    String year = subFieldData(record, "210", 'd');
                    titles.add(new SearchValue(leader.toString(), year, title));
            }
        }

        searchTableView.setItems(titles);
        autoSort(countValueColumn);
        autoSort(searchValueColumn);
    }

    private String subFieldData(Record record, String tag, char code) {
        StringJoiner joiner = new StringJoiner(". ");
        if (record.getVariableField(tag) != null) {
            DataField field = (DataField) record.getVariableField(tag);
            List subfields = field.getSubfields();
            for (Object subField : subfields) {
                Subfield subF = (Subfield) subField;
                if (subF.getCode() == code)
                    joiner.add(subF.getData());
            }
        }
        return joiner.toString();
    }

    private boolean containsAllWords(String word, String... keywords) {
        for (String k : keywords) {
            word = word.toLowerCase();
            k = k.toLowerCase();
            k = porter.stem(k);
            if (!word.contains(k)) return false;
        }

        return true;
    }
}