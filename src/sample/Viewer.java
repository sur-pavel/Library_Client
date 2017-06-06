package sample;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.util.List;

public class Viewer implements GuiElement{
     public WebEngine webEngine;
    private Record currentRecord;

    @Override
    public BorderPane create() {
        WebView webView = new WebView();
        webView.autosize();
        webView.setPrefSize(950, 220);
        webEngine = webView.getEngine();
        BorderPane viewBorderPane = new BorderPane();
        viewBorderPane.setCenter(webView);
        viewBorderPane.setPadding(new Insets(10));
        return viewBorderPane;
    }

    @Override
    public void update(Record record) {
        currentRecord = record;
        webEngine.loadContent(getViewerBuilder().toString());
    }

    private StringBuilder getViewerBuilder() {
        StringBuilder builder = new StringBuilder();

        builder.append("<b>")
                .append(getData(675, 'a', "<br>"))
                .append(getData(899, 'm', "<br>"))
                .append(getData(700, 'a'))
                .append(getData(", ", 700, 'g', 'b', "<br>"))
                .append("</b>")
                .append(getData(200, 'a', " / "))
                .append(getData(700, 'b', " "))
                .append(getData(700, 'a', ". - "))
                .append(getData(210, 'a'))
                .append(getData(": ", 210, 'c'))
                .append(getData(", ", 210, 'd'))
                .append('.')


        ;
        return builder;
    }


    private String getData(int number, char code) {
        StringBuilder builder = new StringBuilder();
        List<DataField> dataFields;
        dataFields = currentRecord.getDataFields();
        for (DataField dataField : dataFields) {
            int tag = Integer.parseInt(dataField.getTag());
            if (tag == number) {
                List subFields = dataField.getSubfields(code);
                for (Object subField : subFields) {
                    Subfield subfield = (Subfield) subField;
                    builder.append(subfield.getData());
                }
            }
        }
        return builder.toString();
    }

    private String getData(int number, char code, String next) {
        StringBuilder builder = new StringBuilder();
        String string = getData(number, code);
        if (!string.equals("")) {
            builder.append(string)
                    .append(next);
        }
        return builder.toString();
    }

    private String getData(String prev, int number, char code) {
        StringBuilder builder = new StringBuilder();
        String string = getData(number, code);
        if (!string.equals("")) {
            builder.append(prev)
                    .append(string);
        }
        return builder.toString();
    }

    private String getData(String prev, int number, char code, String next) {
        StringBuilder builder = new StringBuilder();
        String string = getData(number, code);
        if (!string.equals("")) {
            builder.append(prev)
                    .append(string)
                    .append(next);
        }
        return builder.toString();
    }

    private String getData(int number, char code, char code2) {
        String string = getData(number, code);
        if (string.equals("")) string = getData(number, code2);
        return string;
    }

    private String getData(int number, char code, char code2, String next) {
        StringBuilder builder = new StringBuilder();
        String string = getData(number, code);
        if (string.equals(""))
            string = getData(number, code2);
        if (!string.equals("")) {
            builder.append(string)
                    .append(next);
        }
        return builder.toString();
    }

    private String getData(String prev, int number, char code, char code2, String next) {
        StringBuilder builder = new StringBuilder();
        String string = getData(number, code);
        if (string.equals(""))
            string = getData(number, code2);
        if (!string.equals("")) {
            builder.append(prev)
                    .append(string)
                    .append(next);
        }
        return builder.toString();
    }

    private String getData(int number, char code, int subFNum) {

        return getSubString(number, code, subFNum - 1);
    }

    private String getSubString(int number, char code, int subFNum) {
        String data = "";
        DataField field = (DataField) currentRecord.getVariableField(String.valueOf(number));
        List subfields = field.getSubfields(code);
        Subfield subfield = (Subfield) subfields.get(subFNum);
        data = subfield.getData();
        return data;
    }

}