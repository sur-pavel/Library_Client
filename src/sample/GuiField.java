package sample;

import org.marc4j.marc.DataField;
import org.marc4j.marc.Subfield;

import java.util.List;

class GuiField {


    private boolean check =false;
    private int fieldNum;
    private String name;
    private byte buttonNum;
    private String value;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getFieldNum() {
        return fieldNum;
    }

    public void setFieldNum(int fieldNum) {
        this.fieldNum = fieldNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getButtonNum() {
        return buttonNum;
    }

    public void setButtonNum(byte buttonNum) {
        this.buttonNum = buttonNum;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
//    Map<key fieldNum, Field(boolean check, String name, byte[] buttonNum, ArrayList <String tag, String value>)>



    GuiField(){
        this.check = false;
        this.fieldNum = 910;
        this.name = "Экземпляры";
        this.buttonNum = 1;
        this.value = "$a183471";
    }
    GuiField(DataField field) {
        List subFields = field.getSubfields();
        StringBuilder builder = new StringBuilder();
        for (Object subField : subFields) {
            Subfield subfield = (Subfield) subField;
            char code = subfield.getCode();
            String data = subfield.getData();
            builder.append("$").append(code).append(data);
        }
        this.check = false;
        this.fieldNum = Integer.parseInt(field.getTag());
        this.buttonNum = 1;
        this.name = "название";
        this.value = builder.toString();
    }
}
