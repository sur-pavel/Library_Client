package sample;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.util.*;

public class Editor implements GuiElement {
    private int currentField;
    TabPane editorPane;
    private ArrayList<GuiField> guiFieldArrayList = new ArrayList<>();
    private StringBuilder builder;
    private Map<String, Integer> tabTextFields = new HashMap<>();

    @Override
    public BorderPane create() {
        createGuiFields();

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
        tabTextFields.put(name, tags.get(0));
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
        return tab;
    }

    @Override
    public void update(Record record) {
        List<DataField> dataFields;
        dataFields = record.getDataFields();
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


    private void createGuiFields() {
        for (String s : Constants.fieldsName) {
            int num = Integer.parseInt(s.split(":")[0]);
            guiFieldArrayList.add(new GuiField(num, s));
        }
    }

    private GuiField getGuiField(int num) {
        int field = 0;
        for (int i = 0; i < guiFieldArrayList.size(); i++) {
            if (guiFieldArrayList.get(i).getFieldNumber() == num) field = i;
        }
        return guiFieldArrayList.get(field);
    }

    private void setGuiFieldListener(int prev, int current, int next) {
        getGuiField(current).getValueTextField().focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                currentField = current;
            }
        });
        getGuiField(current).getValueTextField().setOnKeyPressed(event -> {
            tabTextFields.put(editorPane.getSelectionModel().getSelectedItem().getText(), current);
            switch (event.getCode()) {
                case UP:
                    if (prev != 0) {
                        getGuiField(prev).getValueTextField().requestFocus();
                        scroll();
                    }

                    break;

                case DOWN:
                    if (next != 0) {
                        getGuiField(next).getValueTextField().requestFocus();
                        scroll();
                    }

                    break;
            }

            if (event.getCode() == KeyCode.END && event.isControlDown()) {
                editorPane.getSelectionModel().selectLast();
                focusOnTextField();
            }
            if (event.getCode() == KeyCode.HOME && event.isControlDown()) {
                editorPane.getSelectionModel().selectFirst();
                focusOnTextField();
            }
            if (event.getCode() == KeyCode.RIGHT && event.isAltDown()) {
                editorPane.getSelectionModel().selectNext();
                focusOnTextField();
            }
            if (event.getCode() == KeyCode.LEFT && event.isAltDown()) {
                editorPane.getSelectionModel().selectPrevious();
                focusOnTextField();
            }
        });
    }

    private void scroll() {
        HBox hbox = getGuiField(currentField).getHBox();

        ScrollPane scrollPane = (ScrollPane) editorPane.getSelectionModel().getSelectedItem().getContent();

        double hboxHeight = hbox.getBoundsInParent().getMaxY();
        VBox vBox = (VBox) hbox.getParent();

        Bounds bounds = scrollPane.getViewportBounds();
        int lowestXPixelShown = -1 * (int) bounds.getMinX() + 1;
        int highestXPixelShown = -1 * (int) bounds.getMinX() + (int) bounds.getMaxX();
        double sPHeight = scrollPane.getHeight();
        double vBoxHeight = vBox.getHeight();
        double set;
        if (hboxHeight > sPHeight) {
            set = scrollPane.getVvalue() + sPHeight / vBoxHeight / 1.5;
            scrollPane.setVvalue(set);
            System.out.println(new StringJoiner(" ")
                    .add("highPixel ")
                    .add(String.valueOf(highestXPixelShown))
                    .add("lowPixel")
                    .add(String.valueOf(lowestXPixelShown))
                    .add("hbox")
                    .add(String.valueOf(hboxHeight))
                    .add("vbox")
                    .add(String.valueOf(vBoxHeight))
                    .add("sPHeight")
                    .add(String.valueOf(sPHeight)));
        }
    }









    private void firstGuiFieldListener() {
        getGuiField(700).getValueTextField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) getGuiField(701).getValueTextField().requestFocus();
            if (event.getCode() == KeyCode.END && event.isControlDown()) {
                editorPane.getSelectionModel().selectLast();
                focusOnTextField();
            }
            if (event.getCode() == KeyCode.HOME && event.isControlDown()) {
                editorPane.getSelectionModel().selectFirst();
                focusOnTextField();
            }
            if (event.getCode() == KeyCode.RIGHT && event.isAltDown()) {

                editorPane.getSelectionModel().selectNext();
                focusOnTextField();
            }
            if (event.getCode() == KeyCode.LEFT && event.isAltDown()) {
                editorPane.getSelectionModel().selectPrevious();
                focusOnTextField();
            }
        });
    }

    void focusOnTextField() {
        for (Map.Entry<String, Integer> entry : tabTextFields.entrySet()) {
            if (editorPane.getSelectionModel().getSelectedItem().getText().equals(entry.getKey())) {
                getGuiField(entry.getValue()).getValueTextField().requestFocus();

            }

        }
    }


}




