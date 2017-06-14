package ru.sur_pavel.Library_Client.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import ru.sur_pavel.Library_Client.MainApp;
import ru.sur_pavel.Library_Client.model.FieldData;
import ru.sur_pavel.Library_Client.util.Constants;

import java.util.List;

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


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        editorTable.setEditable(true);
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
                                    btn.setOnAction((ActionEvent event) ->
                                    {
                                        FieldData fieldData = getTableView().getItems().get(getIndex());
                                        fieldsData.add(new FieldData(fieldData.getFieldName(), ""));
//                                        autoSort(nameColumn);
                                        System.out.println(fieldData.getFieldName() + "   " + fieldData.getFieldData());
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        actionColumn.setCellFactory(cellFactory);


        for (String fieldsName : Constants.getRusMarcfields()) {
            fieldsData.add(new FieldData(fieldsName, ""));
        }

        editorTable.setItems(fieldsData);
        editorTable.getSelectionModel().setCellSelectionEnabled(true);

    }


    private void autoSort(TableColumn nameColumn) {
        nameColumn.setSortType(TableColumn.SortType.ASCENDING);
        editorTable.getSortOrder().add(nameColumn);
        nameColumn.setSortable(true);
        editorTable.sort();
    }


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

}



