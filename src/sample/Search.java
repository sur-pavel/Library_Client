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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringJoiner;

class Search {

    private Viewer viewer;
    private Editor editor;
    private Record currentRecord;
    private Porter porter = new Porter();
    private TableView<SearchValue> searchTableView;
    private TableColumn searchValueColumn;
    private TableColumn countValueColumn;
    private ArrayList<Record> records = new ArrayList<>();
    private final ObservableList<SearchValue> titles = FXCollections.observableArrayList();


    void create(Editor ed, Viewer v, ArrayList<Record> recs) {
        viewer = v;
        editor = ed;
        records = recs;
        Stage searchStage = new Stage();
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

        VBox searchVBox = new VBox();
        HBox hBox = new HBox();
        searchVBox.setSpacing(5);
        searchVBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(10, 10, 0, 10));

        TextField field = new TextField();
        field.setPrefWidth(400);

        final ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Заглавие", "Автор");
        comboBox.setValue("Заглавие");


        hBox.getChildren().add(field);
        hBox.setAlignment(Pos.BASELINE_CENTER);
        Label notification = new Label();
        searchVBox.getChildren().addAll(hBox, notification, searchTableView);

        Scene searchScene = new Scene(searchVBox, 800, 300);
        searchStage.setTitle("Search");
        searchStage.setScene(searchScene);

        searchStage.initModality(Modality.NONE);
        searchStage.initOwner(editor.editorPane.getScene().getWindow());
        searchStage.show();
        searchStage.focusedProperty().addListener((ov, t, t1) -> searchStage.close());
        field.requestFocus();
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (comboBox.getValue() != null &&
                    !comboBox.getValue().toString().isEmpty()) {
                String s = comboBox.getValue().toString();
                String text = field.getText().toLowerCase();
                update(s, text);
            }
        });

        searchScene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.F && event.isControlDown()) {
                field.requestFocus();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                searchStage.close();
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
                    searchTableView.requestFocus();
                    searchTableView.getSelectionModel().select(0);
                    searchTableView.getFocusModel().focus(0);
                    break;


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
                System.out.println(searchValue.getLeader());
                if (leader.toString().equals(searchValue.getLeader())) {
                    if (!searchValue.getEditionValue().equals("")) {
/*                    String foundedString;
                    String foundedString2;
                    List recordFields = record.find("200");
                    List recordFields2 = record.find("210");

                    foundedString = recordFields.toString();
                    foundedString2 = recordFields2.toString();
                    System.out.println("Поиск значения" + foundedString + " " + foundedString2);
                    String[] strings = searchValue.getSearchValue().split(" ");

                    if (containsAllWords(foundedString, strings) && foundedString2.contains(searchValue.getCountValue())) {
  */
                        currentRecord = record;
                        editor.update(currentRecord);
                        viewer.update(currentRecord);


                    } else {
                        currentRecord = record;
                        editor.update(currentRecord);
                        viewer.update(currentRecord);
                    }
                }
            }
/*            for (Record record : records) {
                String foundedString;
                String foundedString2;
                List recordFields = record.find("200", searchValue.getSearchValue());
                List recordFields2 = record.find("210", searchValue.getCountValue());
                for (Object recordField : recordFields) {
                    for (Object recordField2 : recordFields2) {
                        foundedString = recordField.toString();
                        foundedString2 = recordField2.toString();
                        System.out.println(foundedString + " " + foundedString2);
                        if (foundedString.contains(searchValue.getSearchValue()) && foundedString2.contains(searchValue.getCountValue())) {
                            currentRecord = record;
                            editor.update(currentRecord);
                            viewer.update(currentRecord);
                        }
                    }
                }
            }*/


    };


    private void update(String string, String text) {
        HashSet<SearchValue> set = new HashSet<>();
        String[] strings = text.split(" ");
        String tag = "200";
        char code = 'a';
        String tag2 = "210";
        char code2 = 'd';


        for (Record record : records) {
            if (record.getVariableField(tag) != null) {
                DataField field = (DataField) record.getVariableField(tag);
                if (containsAllWords(field.toString(), strings)) {
                    if (field.getSubfields() != null) {
                        List subfields = field.getSubfields();
                        for (Object subField : subfields) {
                            Subfield subfield = (Subfield) subField;
                            char subfieldCode = subfield.getCode();
                            String s = subfield.getData();
                            String value = s.toLowerCase();
                            String s2 = "";
                            String s3 = "";
                            StringJoiner joiner = new StringJoiner(". ").add(s);
                            if (subfieldCode == code && containsAllWords(value, strings)) {
                                if (record.getVariableField(tag2) != null) {
                                    DataField field2 = (DataField) record.getVariableField(tag2);
                                    if (field2.getSubfield(code2) != null) {
                                        s2 = field2.getSubfield(code2).getData();
                                    }
                                }
                                if (record.getVariableField("205") != null) {
                                    DataField field2 = (DataField) record.getVariableField("205");
                                    if (field2.getSubfield('a') != null) {
                                        s3 = field2.getSubfield('a').getData();
                                    }
                                }

                                for (Object subField2 : subfields) {
                                    Subfield volSubfield = (Subfield) subField2;
                                    char subfieldCode2 = volSubfield.getCode();
                                    if (subfieldCode2 == 'h')
                                        joiner.add(volSubfield.getData());
                                }

                                Leader leader = record.getLeader();
                                set.add(new SearchValue(leader.toString(), s2, joiner.toString(), s3));
                            }

                        }
                    }
                }
            }

        }
        titles.clear();
        for (Object o : set) {
            SearchValue s = (SearchValue) o;
            titles.add(s);
            System.out.println("Добавление в лист:" + s.getCountValue() + " " + s.getSearchValue() + " " + s.getEditionValue());
        }

        searchTableView.setItems(titles);
        autoSort(countValueColumn);
        autoSort(searchValueColumn);
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