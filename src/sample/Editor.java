package sample;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Editor {
    private int currentColumn = 3;
    private int currentRow = 0;


    public int getCurrentColumn() {
        return currentColumn;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    TabPane editorPane;
    private ArrayList<GuiField> guiFieldArrayList = new ArrayList<>();
    private StringBuilder builder;



    TabPane create() {

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
                new ArrayList<>(Collections.singletonList(801))));
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
        return editorPane;
    }

    private Tab getТab(String name, ArrayList<Integer> tags) {
        Tab tab = new Tab(name);
        ScrollPane scrollPane = new ScrollPane();

        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(10));
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints(0, 300, 300);
        ColumnConstraints column3 = new ColumnConstraints();

        ColumnConstraints column4 = new ColumnConstraints(1, 970, 970);
        column4.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(column1, column2, column3, column4);

        fillGridPane(tags, scrollPane, gridPane);
        scrollPane.setContent(gridPane);
        tab.setContent(scrollPane);
        return tab;
    }

    private void fillGridPane(ArrayList<Integer> tags, ScrollPane scrollPane, GridPane gridPane) {
        int rowNum = 0;
        for (Integer integer : tags) {
            for (String s : Constants.fieldsName) {
                int num = Integer.parseInt(s.split(":")[0]);
                if (integer == num) {
                    GuiField guiField = new GuiField(num, s);
                    guiFieldArrayList.add(guiField);

                    guiField.getCheckbox().setMaxWidth(Double.MAX_VALUE);


                    guiField.getFieldNameLabel().setFocusTraversable(false);
                    guiField.getFieldNameLabel().setFont(new Font("Arial" ,13));

                    guiField.getButton().setText("1");
                    guiField.getButton().setMaxWidth(Double.MAX_VALUE);


                    textFieldKeys(scrollPane, gridPane, guiField.getValueTextField());

                    guiField.getValueTextField().setFont(new Font("Arial Bold", 13));

                    gridPane.addRow(rowNum, guiField.getCheckbox(), guiField.getFieldNameLabel(), guiField.getButton(), guiField.getValueTextField());
                    GridPane.setFillWidth(guiField.getValueTextField(), true);
                    rowNum += 1;
                }
            }
        }
    }

    private void textFieldKeys(ScrollPane scrollPane, GridPane gridPane, TextField textField) {
        textField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DOWN:
                    ObservableList<Node> children = gridPane.getChildren();
                    for (int i = 0, size = children.size(); i < size; i++) {
                        if (children.get(i).isFocused()) {
                            if (i + 2 > size) {
                                children.get(3).requestFocus();
                                scrollPane.setVvalue(0);
                                break;
                            } else {
                                int nextTextFieldId = i + 4;
                                children.get(nextTextFieldId).requestFocus();
                                nodeInScrollPane(scrollPane, children.get(nextTextFieldId), 0.95);
                                break;
                            }
                        }
                    }
                    break;
                case UP:
                    children = gridPane.getChildren();
                    for (int i = 0, size = children.size(); i < size; i++) {
                        if (children.get(i).isFocused()) {
                            if (i - 4 < 0) {
                                children.get(size - 1).requestFocus();
                                scrollPane.setVvalue(1);
                                break;
                            } else {
                                int previousTextFieldId = i - 4;
                                children.get(previousTextFieldId).requestFocus();
                                nodeInScrollPane(scrollPane, children.get(previousTextFieldId), 0.05);
                                break;
                            }
                        }
                    }
                    break;

            }
            if (event.getCode() == KeyCode.END && event.isControlDown()) {
                editorPane.getSelectionModel().selectLast();
            }
            if (event.getCode() == KeyCode.HOME && event.isControlDown()) {
                editorPane.getSelectionModel().selectFirst();
            }
            if (event.getCode() == KeyCode.RIGHT && event.isAltDown()) {
                editorPane.getSelectionModel().selectNext();
            }
            if (event.getCode() == KeyCode.LEFT && event.isAltDown()) {
                editorPane.getSelectionModel().selectPrevious();
            }
            if (event.getCode() == KeyCode.Q && event.isControlDown()) {
                EditorCom editorCom = new EditorCom();
                editorCom.create(this);
            }
        });
    }

    void focusOnTextField(int num) {
        for (GuiField guiField : guiFieldArrayList) {
            if(guiField.getFieldNumber() == num){
                guiField.getValueTextField().getParent().getParent().getParent().requestFocus();
                guiField.getValueTextField().requestFocus();
            }
        }

    }

    private void nodeInScrollPane(ScrollPane scrollPane, Node node, double k) {
        double h = scrollPane.getContent().getBoundsInLocal().getHeight();
        double y = (node.getBoundsInParent().getMaxY() +
                node.getBoundsInParent().getMinY()) / 2.0;
        double v = scrollPane.getViewportBounds().getHeight();
        if (y > v || y < v)
            scrollPane.setVvalue(scrollPane.getVmax() * ((y - k * v) / (h - v)));
    }


    void update(Record record) {
        List<DataField> dataFields = record.getDataFields();
        for (DataField dataField : dataFields) {
            for (GuiField guiField : guiFieldArrayList) {
                if (Integer.parseInt(dataField.getTag()) == guiField.getFieldNumber()) {
                    builder = new StringBuilder();
                    setStringBuilder(dataField);
                    guiField.setValue(builder.toString());

                }
            }
        }
    }

    private void setStringBuilder(DataField dataField) {
        List subFields = dataField.getSubfields();
        for (Object subField : subFields) {
            Subfield subfield = (Subfield) subField;
            char code = subfield.getCode();
            String data = subfield.getData();
            builder.append("$").append(code).append(data);
        }
    }
}




