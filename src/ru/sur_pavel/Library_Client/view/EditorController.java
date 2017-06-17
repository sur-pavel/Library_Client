package ru.sur_pavel.Library_Client.view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;
import javafx.util.Callback;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import ru.sur_pavel.Library_Client.MainApp;
import ru.sur_pavel.Library_Client.model.FieldData;
import ru.sur_pavel.Library_Client.util.AutoCompleteComboBoxListener;
import ru.sur_pavel.Library_Client.util.Constants;

import java.util.List;

/**
 * Controller to handle editor items
 */
public class EditorController {

    @FXML
    private TableColumn<FieldData, String> actionColumn;
    @FXML
    private TableColumn<FieldData, String> nameColumn;
    @FXML
    private TableColumn<FieldData, String> dataColumn;
    @FXML
    private TableView<FieldData> editorTable;

    private MainApp mainApp;
    private ObservableList<FieldData> fieldsData = FXCollections.observableArrayList();


    /**
     * set MainApp object
     * @param mainApp object
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Invokes when loading editor layer.
     * Adjusts the behavior of the editorTable
     * and its columns
     */
    @FXML
    private void initialize() {
        editorTable.setEditable(true);
        // setting focus on dataColumn
        editorTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super FieldData>) observable -> {
            editorTable.getFocusModel().focus(editorTable.getFocusModel().getFocusedCell().getRow(), dataColumn);
        });
        editorTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Q && event.isAltDown()) {

                Popup popup = new Popup();
                ComboBox<String> comboBox = new ComboBox<>();
                for (String fieldsName : Constants.getMarcFieldsIrbisName())
                    comboBox.getItems().add(fieldsName);
                new AutoCompleteComboBoxListener<>(comboBox);
                comboBox.setOnAction(e -> {
                        for (FieldData fieldData : fieldsData) {
                            if (fieldData.getFieldName().contains(comboBox.getValue())) {
                                popup.hide();
                                editorTable.getSelectionModel().clearSelection();
                                editorTable.getSelectionModel().select(fieldData);
                                editorTable.scrollTo(fieldData);
// after popup hide focus not on the dataColumn
                        }
                    }
                });
                popup.getContent().add(comboBox);
                popup.show(editorTable.getScene().getWindow());
            }
        });
        TableView.TableViewSelectionModel<FieldData> tsm = editorTable.getSelectionModel();
        tsm.setSelectionMode(SelectionMode.MULTIPLE);

        nameColumn.setCellValueFactory(
                cellData -> cellData.getValue().fieldNameProperty());
        nameColumn.setEditable(false);

        dataColumn.setCellValueFactory(
                cellData -> cellData.getValue().fieldDataProperty());
        dataColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        actionColumn.setCellValueFactory(new PropertyValueFactory<>(""));

        Callback<TableColumn<FieldData, String>, TableCell<FieldData, String>> cellFactory =
                new Callback<TableColumn<FieldData, String>, TableCell<FieldData, String>>() {
                    @Override
                    public TableCell call(final TableColumn<FieldData, String> param) {
                        final TableCell<FieldData, String> cell = new TableCell<FieldData, String>() {

                            Button btn = new Button();

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setMaxWidth(Double.MAX_VALUE);
                                    btn.setText("1");
                                    btn.setOnAction((ActionEvent event) ->
                                    {
                                        FieldData fieldData = getTableView().getItems().get(getIndex());
                                        fieldsData.add(new FieldData(fieldData.getFieldName(), ""));
                                        System.out.println(fieldData.getFieldName() + "   " + fieldData.getFieldData());
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        cell.setAlignment(Pos.CENTER);
                        return cell;
                    }
                };

        actionColumn.setCellFactory(cellFactory);


        for (String fieldsName : Constants.getMarcFieldsIrbisName()) {
            fieldsData.add(new FieldData(fieldsName, ""));
        }

        autoSort(nameColumn);
        editorTable.setItems(fieldsData);
//        editorTable.getSelectionModel().setCellSelectionEnabled(true);

    }

    /**
     * Sorts items in editorTable
     * @param nameColumn object of column
     */
    private void autoSort(TableColumn nameColumn) {
        nameColumn.setSortType(TableColumn.SortType.ASCENDING);
        editorTable.getSortOrder().add(nameColumn);
        nameColumn.setSortable(true);
        editorTable.sort();
    }

    /**
     * Create stringBuilder for
     * @param dataField
     * @return
     */
    private StringBuilder setStringBuilder(DataField dataField) {
        StringBuilder builder = new StringBuilder();
        List<Subfield> subFields = dataField.getSubfields();
        for (Subfield subField : subFields) {
            char code = subField.getCode();
            String data = subField.getData();
            builder.append("$").append(code).append(data);
        }
        return builder;
    }

    /**
     * Forms data for editorTable
     * @param record with necessary data
     */
    void update(Record record) {

        for (int i = 0; i < fieldsData.size(); i++) {
            List<DataField> dataFields = record.getDataFields();
            for (DataField dataField : dataFields) {
                if (dataField.getTag().equals(fieldsData.get(i).getFieldName().split(":")[0])) {
                    fieldsData.set(i, new FieldData(fieldsData.get(i).getFieldName(), setStringBuilder(dataField).toString()));
                }
            }
        }
    }

    /**
     * Scrolls scrollPane to make node visible
     * @param scrollPane object
     * @param node object
     * @param k coefficient for position of scrollPane
     */
    private void nodeInScrollPane(ScrollPane scrollPane, Node node, double k) {
        double h = scrollPane.getContent().getBoundsInLocal().getHeight();
        double y = (node.getBoundsInParent().getMaxY() +
                node.getBoundsInParent().getMinY()) / 2.0;
        double v = scrollPane.getViewportBounds().getHeight();
        if (y > v || y < v)
            scrollPane.setVvalue(scrollPane.getVmax() * ((y - k * v) / (h - v)));
    }
}



