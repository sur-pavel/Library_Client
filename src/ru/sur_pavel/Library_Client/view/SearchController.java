package ru.sur_pavel.Library_Client.view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import ru.sur_pavel.Library_Client.MainApp;
import ru.sur_pavel.Library_Client.model.Porter;
import ru.sur_pavel.Library_Client.model.SearchValue;
import ru.sur_pavel.Library_Client.util.AutoCompleteComboBoxListener;
import ru.sur_pavel.Library_Client.util.Constants;

import java.util.*;

/**
 * Controller for search layout
 */
public class SearchController {

    @FXML
    private TabPane tabPane;
    @FXML
    private TextField searchField;
    @FXML
    private TextField specField;
    @FXML
    private TableView<SearchValue> searchTable;
    @FXML
    private TableColumn<SearchValue, String> yearColumn;
    @FXML
    private TableColumn<SearchValue, String> titleColumn;
    @FXML
    private ComboBox<String> comboBox;

    private Stage searchStage;
    private int currentRow = 0;
    private MainApp mainApp;
    private Record currentRecord;
    private ArrayList<Record> records = new ArrayList<>();
    private ObservableList<SearchValue> titles = FXCollections.observableArrayList();
    private Date start;
    private Date finish;

    /**
     * Sets MainApp object for controller
     * @param mainApp object
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Sets stage for search layout
     * @param searchStage search stage
     */
    public void setSearchStage(Stage searchStage) {
        this.searchStage = searchStage;
    }

    /**
     * Sets list of searchTable items
     * @param titles list of items
     */
    public void setTitles(ObservableList<SearchValue> titles) {
        this.titles = titles;
        searchTable.setItems(titles);
    }

    /**
     * Sets keys for search scene
     * and request focus for searchField in first Tab
     */
    public void sceneKeys() {
        records = mainApp.getRecords();

        searchField.requestFocus();

        searchStage.getScene().setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.F && event.isControlDown()) {
                currentRow = searchTable.getSelectionModel().getFocusedIndex();
//                tabPane.requestFocus();
                int tabNum = tabPane.getSelectionModel().getSelectedIndex();
                if (tabNum == 0)
                    searchField.requestFocus();
                if (tabNum == 1)
                    specField.requestFocus();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                searchStage.close();
            }
            if (event.getCode() == KeyCode.TAB && event.isControlDown()) {
                tabPane.requestFocus();
                tabPane.getSelectionModel().selectNext();
            }
        });
    }

    @FXML
    private void initialize() {
        searchTable.setEditable(true);
        yearColumn.setCellValueFactory(
                cellData -> cellData.getValue().countValueProperty());

        titleColumn.setCellValueFactory(
                cellData -> cellData.getValue().searchValueProperty());
        autoSort(titleColumn);
        searchTable.getSelectionModel().getSelectedItems().addListener(onItemSelected);
        searchTable.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    searchStage.close();
            }
        });
        for (String fieldsName : Constants.getRusMarcfields())
            comboBox.getItems().add(fieldsName);
        new AutoCompleteComboBoxListener<>(comboBox);

        fieldListeners(searchField);
        fieldListeners(specField);
    }

    private ListChangeListener<SearchValue> onItemSelected = itemSelected -> {

        for (SearchValue searchValue : itemSelected.getList()) {
            for (Record record : records) {
                Leader leader = record.getLeader();
                if (leader.toString().equals(searchValue.getLeader())) {
                    currentRecord = record;
                    mainApp.getEditorController().update(currentRecord);
                    mainApp.getViewerController().update(currentRecord);
                }
            }
        }
    };

    private void autoSort(TableColumn searchValueColumn) {
        searchValueColumn.setSortType(TableColumn.SortType.ASCENDING);
        searchTable.getSortOrder().add(searchValueColumn);
        searchValueColumn.setSortable(true);
        searchTable.sort();
    }


    private void fieldListeners(TextField textField) {
/*        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (comboBox.getValue() != null &&
                    !comboBox.getValue().toString().isEmpty()) {
                String fieldName = comboBox.getValue().toString();
                String text = field.getText().toLowerCase();
                if (text.length() > 4) search(fieldName, text);
            }
        });*/
        textField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    String text = textField.getText().toLowerCase();
                    if (text.length() > 4) search(text);

                case DOWN:
                    searchTable.requestFocus();
                    searchTable.getSelectionModel().select(currentRow);
                    searchTable.getFocusModel().focus(0);
            }
        });
        specField.setOnAction(event -> {
            if (comboBox.getValue() != null &&
                    !comboBox.getValue().toString().isEmpty()) {
                String fieldName = comboBox.getValue().toString();
                String text = specField.getText().toLowerCase();
                if (text.length() > 4) search(fieldName, text);
            }
        });
    }

    private void search(String input) {
        String[] splitInput = input.split(" ");
        ArrayList<Leader> leaders = new ArrayList<>();
        Map<Leader, String> searchMap = mainApp.getSearchMap();

        for (Map.Entry<Leader, String> entry : searchMap.entrySet()) {
            if (containsAllWords(entry.getValue(), splitInput)) {
                leaders.add(entry.getKey());
            }
        }
        updateTableView(leaders);
    }

    private void search(String fieldName, String input) {
        String[] splitInput = input.split(" ");
        String tag = fieldName.split(":")[0];
        ArrayList<Leader> leaders = new ArrayList<>();
        for (Record record : records) {
            if (record.getVariableField(tag) != null) {
                DataField field = (DataField) record.getVariableField(tag);
                if(containsAllWords(field.toString(), splitInput)){
                    leaders.add(record.getLeader());
                }
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
            List<Subfield> subfields = field.getSubfields();
            for (Subfield subField : subfields) {
                Subfield subF = subField;
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
