package sample;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Search {

    private Viewer viewer;
    private Editor editor;
    private Record currentRecord;
    private TableView<SearchValue> searchTableView;
    private ArrayList<Record> records = new ArrayList<>();
    private final ObservableList<SearchValue> titles = FXCollections.observableArrayList();


    public void create(Editor ed, Viewer v, ArrayList<Record> recs) {
        viewer = v;
        editor = ed;
        records = recs;
        Stage searchStage = new Stage();
        searchTableView = new TableView<>();
        searchTableView.setEditable(true);
/*
        TableColumn countValueColumn = new TableColumn("Кол-во");
        countValueColumn.prefWidthProperty().bind(searchTableView.widthProperty().multiply(0.2));
        countValueColumn.setCellValueFactory(
                new PropertyValueFactory<>("countValue"));
*/
        // SIC! NB!  PropertyValueFactory<>("countValue") => get(set)CountValue()

        TableColumn searchValueColumn = new TableColumn("Значение");
        searchValueColumn.prefWidthProperty().bind(searchTableView.widthProperty().multiply(0.8));
        searchValueColumn.setCellValueFactory(
                new PropertyValueFactory<>("searchValue"));

        searchTableView.setItems(titles);
//        searchTableView.getColumns().add(countValueColumn);
        searchTableView.getColumns().add(searchValueColumn);
        searchValueColumn.setSortType(TableColumn.SortType.ASCENDING);
        searchTableView.getSortOrder().add(searchValueColumn);
        searchValueColumn.setSortable(true);
        searchTableView.sort();
        searchTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        searchTableView.getSelectionModel().getSelectedItems().addListener(onItemSelected);
        searchTableView.setOnKeyPressed(event -> {
            switch (event.getCode()){
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

        final ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Заглавие", "Автор");
        comboBox.setValue("Заглавие");

        hBox.getChildren().addAll(comboBox, field);
        Label notification = new Label();
        searchVBox.getChildren().addAll(hBox, notification, searchTableView);

        Scene searchScene = new Scene(searchVBox);

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
                case DOWN:
                    searchTableView.requestFocus();
                    searchTableView.getSelectionModel().select(0);
                    searchTableView.getFocusModel().focus(0);
                    break;



            }
        });


    }

    private ListChangeListener<SearchValue> onItemSelected = itemSelected -> {
        for (SearchValue searchValue : itemSelected.getList())
            for (Record record : records) {
                List recordFields = record.find("200", searchValue.getSearchValue());
                for (Object recordField : recordFields) {
                    String foundedString = recordField.toString();
                    if (foundedString.contains(searchValue.getSearchValue())) {
                        currentRecord = record;
                        editor.update(currentRecord);
                        viewer.update(currentRecord);

                    }
                }
            }

    };


    private void update(String string, String text) {
        HashSet<Object> set = new HashSet<>();

        String tag = "200";
        char code = 'a';
        titles.clear();
        switch (string) {
            case "Заглавие":
                tag = "200";
                code = 'a';
                break;
            case "Автор":
                tag = "700";
                code = 'a';
                break;
        }

        for (Record record : records) {
            if (record.getVariableField(tag) != null) {
                DataField field = (DataField) record.getVariableField(tag);
                if (field.getSubfields() != null) {
                    List subfields = field.getSubfields();
                    for (Object subField : subfields) {
                        Subfield subfield = (Subfield) subField;
                        char subfieldCode = subfield.getCode();
                        String s = subfield.getData();
                        String value = s.toLowerCase();
                        if (subfieldCode == code && value.contains(text)) {
                            set.add(s);
                        }

                    }
                }
            }


        }

        for (Object o : set) {
            String s = (String) o;
            titles.add(new SearchValue(s));
        }

        searchTableView.setItems(titles);
    }
}