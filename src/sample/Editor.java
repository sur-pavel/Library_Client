package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
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
        editorTable.getStylesheets().add("styleSheet.css");
        TableView.TableViewSelectionModel<FieldData> tsm = editorTable.getSelectionModel();
        tsm.setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn fieldNameColumn = new TableColumn("Поле");
        fieldNameColumn.prefWidthProperty().bind(editorTable.widthProperty().multiply(0.2));
        fieldNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("fieldName"));

        fieldNameColumn.setStyle( "-fx-font-weight:bold; -fx-font-size:13; -fx-text-origin: baseline;");
        fieldNameColumn.setEditable(false);

        TableColumn fieldDataColumn = new TableColumn("Значение");
        fieldDataColumn.prefWidthProperty().bind(editorTable.widthProperty().multiply(0.77));
        fieldDataColumn.setCellValueFactory(
                new PropertyValueFactory<>("fieldData"));
        fieldDataColumn.setCellFactory(TextFieldTableCell.forTableColumn());



        TableColumn actionCol = new TableColumn();
        actionCol.setStyle( "-fx-alignment: CENTER;");
        actionCol.setCellValueFactory(new PropertyValueFactory<>(""));

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
                                        autoSort(fieldNameColumn);
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

        actionCol.setCellFactory(cellFactory);

        for (String fieldsName : Constants.rusMarcfields) {
            fieldsData.add(new FieldData(fieldsName, ""));

        }

        editorTable.setItems(fieldsData);

        editorTable.getColumns().addAll(actionCol, fieldNameColumn, fieldDataColumn);
        editorTable.getSelectionModel().setCellSelectionEnabled(true);

        return editorTable;
    }

    private void autoSort(TableColumn fieldNameColumn) {
        fieldNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        editorTable.getSortOrder().add(fieldNameColumn);
        fieldNameColumn.setSortable(true);
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

