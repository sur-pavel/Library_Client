package sample;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.util.ArrayList;
import java.util.List;

public class Editor implements GuiElement{
    private Record currentRecord;
    private ArrayList<GuiField> guiFieldArrayList = new ArrayList<>();
    private StringBuilder builder;
    @Override
    public BorderPane create() {
        createGuiFields();

        TabPane editorPane = new TabPane();
        int[] tags = {200, 210, 10, 700};
        editorPane.getTabs().clear();
        editorPane.getTabs().add(getТab("Основное БО", tags));
        int[] tags2 = {910};
        editorPane.getTabs().add(getТab("Экземпляры", tags2));

        BorderPane editorBorderPane = new BorderPane();
        editorBorderPane.setCenter(editorPane);
        editorBorderPane.setPadding(new Insets(0));
        return editorBorderPane;
    }

    @Override
    public void update(Record record) {
        List<DataField> dataFields;
        currentRecord = record;
        dataFields = currentRecord.getDataFields();
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
    private Tab getТab(String name, int[] tags) {
        Tab tab = new Tab(name);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setMax(500);
        scrollBar.setMin(0);
        scrollBar.setValue(100);
        scrollBar.setUnitIncrement(30);
        scrollBar.setBlockIncrement(35);
        VBox vbox = new VBox();
        for (GuiField guiField : guiFieldArrayList) {
            for (int tag : tags) {
                if (tag == guiField.getFieldNumber()) {
                    vbox.getChildren().add(guiField.getHBox());
                }
            }
        }
        scrollPane.setContent(vbox);
        tab.setContent(scrollPane);
        return tab;
    }

    private void createGuiFields() {
        for (String s : Constanst.fieldsName) {
            int num = Integer.parseInt(s.split(":")[0]);
            guiFieldArrayList.add(new GuiField(num, s));
        }
    }
}
