package ru.sur_pavel.Library_Client.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class of objects for editorTable
 */
public class FieldData {

    private final SimpleStringProperty fieldName;
    private final SimpleStringProperty fieldData;

    public SimpleStringProperty fieldNameProperty() {
        return fieldName;
    }

    public SimpleStringProperty fieldDataProperty() {
        return fieldData;
    }

    public FieldData(String fName, String fData) {
        this.fieldName = new SimpleStringProperty(fName);
        this.fieldData = new SimpleStringProperty(fData);

    }

    public String getFieldName() {
        return fieldName.get();
    }

    public String getFieldData() {
        return fieldData.get();
    }

    public void setFieldData(String searchVal) {
        fieldData.set(searchVal);
    }

}


