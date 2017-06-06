package sample;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Editor {

    TabPane editorPane;
    private ArrayList<TableView> editorTables = new ArrayList<>();


    BorderPane create() {

        editorPane = new TabPane();
        editorPane.getTabs().clear();

        editorPane.getTabs().add(getТab("Основное БО",
                new ArrayList<>(Arrays.asList(700, 701, 702, 711, 972, 200, 923, 922, 925, 205, 210, 215))));
        editorPane.getTabs().add(getТab("Коды",
                new ArrayList<>(Arrays.asList(900, 102, 101, 911, 919, 10, 11, 19, 904))));
        editorPane.getTabs().add(getТab("Расширенное",
                new ArrayList<>(Arrays.asList(300, 912, 320, 390, 314, 328, 225, 510, 517, 541, 924, 921, 503, 421, 422, 454, 451, 452, 481, 932, 488, 993))));
        editorPane.getTabs().add(getТab("Специфические",
                new ArrayList<>(Arrays.asList(230, 337, 135, 982, 916, 915, 115, 123, 509, 125, 36, 126, 130, 106, 239))));
        editorPane.getTabs().add(getТab("Экземпляры",
                new ArrayList<>(Collections.singletonList(910))));
        editorPane.getTabs().add(getТab("Технология",
                new ArrayList<>(Arrays.asList(905, 907, 951, 953, 902, 941, 940, 999, 933))));
        editorPane.getTabs().add(getТab("Систематизация",
                new ArrayList<>(Arrays.asList(675, 621, 686, 908, 903, 906, 60, 964, 606, 607, 690, 965, 610, 600, 601, 331, 619, 996, 995))));
        editorPane.getTabs().add(getТab("Содерж.",
                new ArrayList<>(Arrays.asList(327, 330, 926))));
        editorPane.getTabs().add(getТab("КО",
                new ArrayList<>(Arrays.asList(691, 61, 694, 692, 693, 699, 943))));
        editorPane.getTabs().add(getТab("Редкие",
                new ArrayList<>(Arrays.asList(391, 316, 317, 318, 398, 395, 396, 116, 140, 141, 399, 397, 929))));
        editorPane.getTabs().add(getТab("Краеведение",
                new ArrayList<>(Collections.singletonList(629))));

        BorderPane editorBorderPane = new BorderPane();
        editorBorderPane.setCenter(editorPane);
        editorBorderPane.setPadding(new Insets(0));
        return editorBorderPane;
    }

    private Tab getТab(String name, ArrayList<Integer> tags) {
        ObservableList<FieldData> fieldsData = FXCollections.observableArrayList();
        Tab tab = new Tab(name);
        TableView editorTable = new TableView();
        editorTable.setEditable(true);

        TableColumn actionCol = new TableColumn();
        actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<FieldData, String>, TableCell<FieldData, String>> cellFactory =
                new Callback<TableColumn<FieldData, String>, TableCell<FieldData, String>>() {
                    @Override
                    public TableCell call(final TableColumn<FieldData, String> param) {
                        final TableCell<FieldData, String> cell = new TableCell<FieldData, String>() {

                             Button btn = new Button("1");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setMinSize(15, 10);
                                    btn.setOnAction((ActionEvent event) ->
                                    {
                                        FieldData fieldData = getTableView().getItems().get(getIndex());
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


        TableColumn fieldNameColumn = new TableColumn("Название");
        fieldNameColumn.prefWidthProperty().bind(editorTable.widthProperty().multiply(0.2));
        fieldNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("fieldName"));


        TableColumn fieldDataColumn = new TableColumn("Значение");
        fieldDataColumn.prefWidthProperty().bind(editorTable.widthProperty().multiply(0.8));
        fieldDataColumn.setCellValueFactory(
                new PropertyValueFactory<>("fieldData"));
        fieldDataColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fieldDataColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<FieldData, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<FieldData, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setFielData(t.getNewValue());
                        editorTable.requestFocus();
                        editorTable.getSelectionModel().selectBelowCell();
                    }
                }
        );
//        ObservableList<String> list = FXCollections.observableArrayList();
//        list.add("name 1");
//        list.add("name 2");
//        list.add("name 3");
//        list.add("name 4");
//        Callback<TableColumn<FieldData, String>, TableCell<FieldData, String>> cbtc =
//                ComboBoxTableCell.forTableColumn(list);


        for (Integer tag : tags) {
            for (String fieldsName : Constants.fieldsName) {
                int num = Integer.parseInt(fieldsName.split(":")[0]);
                if (tag == num) {
                    fieldsData.add(new FieldData(fieldsName, ""));
                }
            }
        }

        editorTable.setItems(fieldsData);

        editorTable.getColumns().addAll(actionCol, fieldNameColumn, fieldDataColumn);
        editorTable.getSelectionModel().setCellSelectionEnabled(true);
        final ObservableList<TablePosition> selectedCells = editorTable.getSelectionModel().getSelectedCells();
        selectedCells.addListener((ListChangeListener<TablePosition>) change -> {
            for (TablePosition pos : selectedCells) {
                editorTable.edit(pos.getRow(),pos.getTableColumn());
            }
        });
        editorTables.add(editorTable);
        tab.setContent(editorTable);
        return tab;
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
        for (TableView editorTable : editorTables) {
            ObservableList<FieldData> tableFields = editorTable.getItems();
            ObservableList<FieldData> newTableFields = FXCollections.observableArrayList(tableFields);
            for (int i = 0; i < tableFields.size(); i++) {
                List<DataField> dataFields = record.getDataFields();
                for (DataField dataField : dataFields) {
                    if (dataField.getTag().equals(tableFields.get(i).getFieldName().split(":")[0])) {
                        newTableFields.set(i, new FieldData(tableFields.get(i).getFieldName(), setStringBuilder(dataField).toString()));
                    }
                }
            }
            editorTable.setItems(newTableFields);
        }
    }
}


//    GuiField getGuiField(int num) {
//        int field = 0;
//        for (int i = 0; i < guiFieldArrayList.size(); i++) {
//            if (guiFieldArrayList.get(i).getFieldNumber() == num) field = i;
//        }
//        return guiFieldArrayList.get(field);
//    }
//
//    private void setGuiFieldListener(int prev, int current, int next) {
//        getGuiField(current).getValueTextField().focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
//            if (newPropertyValue) {
//                currentField = current;
//            }
//        });
//        getGuiField(current).getValueTextField().setOnKeyPressed(event -> {
//            tabTextFields.put(editorPane.getSelectionModel().getSelectedItem().getText(), current);
//            switch (event.getCode()) {
//                case UP:
//                    if (prev != 0) {
//                        getGuiField(prev).getValueTextField().requestFocus();
//                    }
//                    scroll();
//                    break;
//
//                case DOWN:
//                    if (next != 0) {
//                        getGuiField(next).getValueTextField().requestFocus();
//                    }
////                    scroll();
//                    break;
//            }
//
//            if (event.getCode() == KeyCode.END && event.isControlDown()) {
//                editorPane.getSelectionModel().selectLast();
//                focusOnTextField();
//            }
//            if (event.getCode() == KeyCode.HOME && event.isControlDown()) {
//                editorPane.getSelectionModel().selectFirst();
//                focusOnTextField();
//            }
//            if (event.getCode() == KeyCode.RIGHT && event.isAltDown()) {
//
//                editorPane.getSelectionModel().selectNext();
//                focusOnTextField();
//            }
//            if (event.getCode() == KeyCode.LEFT && event.isAltDown()) {
//                editorPane.getSelectionModel().selectPrevious();
//                focusOnTextField();
//            }
//        });
//    }
//
//    private void scroll() {
//        HBox hbox = getGuiField(currentField).getHBox();
//        ScrollPane scrollPane = (ScrollPane) editorPane.getSelectionModel().getSelectedItem().getContent();
//
//
//    }
//
//
//    private void firstGuiFieldListener() {
//        getGuiField(700).getValueTextField().setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.DOWN) getGuiField(701).getValueTextField().requestFocus();
//            if (event.getCode() == KeyCode.END && event.isControlDown()) {
//                editorPane.getSelectionModel().selectLast();
//                focusOnTextField();
//            }
//            if (event.getCode() == KeyCode.HOME && event.isControlDown()) {
//                editorPane.getSelectionModel().selectFirst();
//                focusOnTextField();
//            }
//            if (event.getCode() == KeyCode.RIGHT && event.isAltDown()) {
//
//                editorPane.getSelectionModel().selectNext();
//                focusOnTextField();
//            }
//            if (event.getCode() == KeyCode.LEFT && event.isAltDown()) {
//                editorPane.getSelectionModel().selectPrevious();
//                focusOnTextField();
//            }
//        });
//    }
//
//    private void focusOnTextField() {
//        for (Map.Entry<String, Integer> entry : tabTextFields.entrySet()) {
//            if (editorPane.getSelectionModel().getSelectedItem().getText().equals(entry.getKey())) {
//                getGuiField(entry.getValue()).getValueTextField().requestFocus();
//            }
//        }
//    }
//
//
//}
//
//
/*ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setMax(500);
        scrollBar.setMin(0);
        scrollBar.setValue(100);
        scrollBar.setUnitIncrement(30);
        scrollBar.setBlockIncrement(35);
        VBox vbox = new VBox();
        for (Integer tag : tags) {
            for (String s : Constants.fieldsName) {
                int num = Integer.parseInt(s.split(":")[0]);
                if (tag == num) {
                    GuiField guiField = new GuiField(num, s);
                    guiFieldArrayList.add(guiField);
                    vbox.getChildren().add(guiField.getHBox());
                }
            }
        }

        for (int i = 0; i < tags.size(); i++) {
            int prev = 0;
            int next = 0;
            if (i != 0) prev = tags.get(i - 1);
            if (i != tags.size() - 1) next = tags.get(i + 1);
            setGuiFieldListener(prev, tags.get(i), next);
        }
        firstGuiFieldListener();

        scrollPane.setContent(vbox);
        tab.setContent(scrollPane);
*/