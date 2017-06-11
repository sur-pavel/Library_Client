package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.util.ArrayList;
import java.util.List;

class Editor {

    TableView<FieldData> editorTable;
    private ArrayList<TableView> editorTables = new ArrayList<>();
    private ObservableList<FieldData> fieldsData = FXCollections.observableArrayList();

    TableView create() {

        editorTable = new TableView<FieldData>();
        editorTable.setEditable(true);
        TableView.TableViewSelectionModel<FieldData> tsm = editorTable.getSelectionModel();
        tsm.setSelectionMode(SelectionMode.MULTIPLE);
//        editorTable.getStylesheets().add("styleSheet.css");
        TableColumn fieldNameColumn = new TableColumn("Название");
        fieldNameColumn.prefWidthProperty().bind(editorTable.widthProperty().multiply(0.2));
        fieldNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("fieldName"));
//        fieldNameColumn.setStyle( "-fx-background-color:rgb(192,192,192)");
        fieldNameColumn.setEditable(false);

        TableColumn fieldDataColumn = new TableColumn("Значение");
        fieldDataColumn.prefWidthProperty().bind(editorTable.widthProperty().multiply(0.8));
        fieldDataColumn.setCellValueFactory(
                new PropertyValueFactory<>("fieldData"));
        fieldDataColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        for (String fieldsName : Constants.rusMarcfields) {
            fieldsData.add(new FieldData(fieldsName, ""));

        }

        editorTable.setItems(fieldsData);

        editorTable.getColumns().addAll(fieldNameColumn, fieldDataColumn);
        editorTable.getSelectionModel().setCellSelectionEnabled(true);

        return editorTable;
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

