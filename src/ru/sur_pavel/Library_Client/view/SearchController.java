package ru.sur_pavel.Library_Client.view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import ru.sur_pavel.Library_Client.MainApp;
import ru.sur_pavel.Library_Client.model.Porter;
import ru.sur_pavel.Library_Client.model.SearchValue;

import java.util.*;


public class SearchController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<SearchValue> searchTable;
    @FXML
    private TableColumn<SearchValue, String> yearColumn;
    @FXML
    private TableColumn<SearchValue, String> titleColumn;

    private Stage searchStage;



    private MainApp mainApp;
    private int currentRow = 0;
    private Record currentRecord;
    private Map<Leader, String> searchMap;
    private EditorController editorController;
    private ViewerController viewerController;

    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;
    }

    public void setViewerController(ViewerController viewerController) {
        this.viewerController = viewerController;
    }

    private ArrayList<Record> records = new ArrayList<>();
    private final ObservableList<SearchValue> titles = FXCollections.observableArrayList();
    private Date start;
    private Date finish;

    public SearchController(){
        titles.add(new SearchValue("", "", ""));
    }
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setSearchStage(Stage searchStage) {
        this.searchStage = searchStage;
    }

    @FXML
    private void initialize() {
        searchTable.setEditable(true);
        yearColumn.setCellValueFactory(
                cellData -> cellData.getValue().countValueProperty());

        titleColumn.setCellValueFactory(
                cellData -> cellData.getValue().searchValueProperty());

        searchTable.setItems(titles);
        autoSort(titleColumn);
        searchTable.getSelectionModel().getSelectedItems().addListener(onItemSelected);
        searchTable.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:

                    searchStage.close();
            }
        });

//        searchStage.focusedProperty().addListener((ov, t, t1) -> searchStage.close());
        fieldListeners();
        searchField.requestFocus();
//        sceneKeys();

    }

    private ListChangeListener<SearchValue> onItemSelected = itemSelected -> {
        for (SearchValue searchValue : itemSelected.getList())
            for (Record record : records) {
                Leader leader = record.getLeader();
                if (leader.toString().equals(searchValue.getLeader())) {
                    currentRecord = record;
                    editorController.update(currentRecord);
                    viewerController.update(currentRecord);
                }
            }
    };

    private void autoSort(TableColumn searchValueColumn) {
        searchValueColumn.setSortType(TableColumn.SortType.ASCENDING);
        searchTable.getSortOrder().add(searchValueColumn);
        searchValueColumn.setSortable(true);
        searchTable.sort();
    }

    private void sceneKeys() {
        searchStage.getScene().setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.F && event.isControlDown()) {
                currentRow = searchTable.getSelectionModel().getFocusedIndex();
                searchField.requestFocus();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                searchStage.close();
            }
        });
    }

    private void fieldListeners() {
/*        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (comboBox.getValue() != null &&
                    !comboBox.getValue().toString().isEmpty()) {
                String fieldName = comboBox.getValue().toString();
                String text = field.getText().toLowerCase();
                if (text.length() > 4) search(fieldName, text);
            }
        });*/
        searchField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    String text = searchField.getText().toLowerCase();
                    if (text.length() > 4) search(text);

                case DOWN:
                    searchTable.requestFocus();
                    searchTable.getSelectionModel().select(currentRow);
                    searchTable.getFocusModel().focus(0);
            }
        });
    }

    private void search(String input) {

        String[] splitInput = input.split(" ");
        ArrayList<Leader> leaders = new ArrayList<>();

        for (Map.Entry<Leader, String> entry : searchMap.entrySet()) {
            if (containsAllWords(entry.getValue(), splitInput)){
                leaders.add(entry.getKey());
            }
        }
        updateTableView(leaders);
    }

    private void updateTableView(ArrayList<Leader> leaders) {
        start = new Date();
        titles.clear();

        for (Record record : records) {
            for (Leader leader : leaders) {
                if (record.getLeader().toString().equals(leader.toString())) {
                    String title = new StringJoiner(" ")
                            .add(subFieldData(record, "200", 'a'))
                            .add(subFieldData(record, "200", 'h'))
                            .add(subFieldData(record, "205", 'a'))
                            .toString();
                    String year = subFieldData(record, "210", 'd');
                    titles.add(new SearchValue(leader.toString(), year, title));
                }
            }
        }


//        searchTableView.setItems(titles);
        autoSort(yearColumn);
        autoSort(titleColumn);

        finish = new Date();
        long searchTime = finish.getTime() - start.getTime();
        System.out.println("TableView update time: " + searchTime);
    }

    private String subFieldData(Record record, String tag, char code) {
        StringJoiner joiner = new StringJoiner(" ");
        if (record.getVariableField(tag) != null) {
            DataField field = (DataField) record.getVariableField(tag);
            List subfields = field.getSubfields();
            for (Object subField : subfields) {
                Subfield subF = (Subfield) subField;
                if (subF.getCode() == code && !subF.getData().equals("")) {
                    joiner.add(subF.getData());
                }

            }
        }
        return joiner.toString();
    }

    private boolean containsAllWords(String word, String... keywords) {
        Porter porter = new Porter();
        for (String k : keywords) {
            word = word.toLowerCase();
            k = porter.stem(k);
            if (!word.contains(k)) return false;
        }

        return true;
    }
}
