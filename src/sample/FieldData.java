package sample;

import javafx.beans.property.SimpleStringProperty;

public class FieldData {

    private final SimpleStringProperty fieldName;
    private final SimpleStringProperty fieldData;


    FieldData(String fName, String fData) {
        this.fieldName = new SimpleStringProperty(fName);
        this.fieldData = new SimpleStringProperty(fData);

    }

    public String getFieldName() {
        return fieldName.get();
    }

    public String getFieldData() {
        return fieldData.get();
    }

    public void setFielData(String searchVal) {
        fieldData.set(searchVal);
    }

}


