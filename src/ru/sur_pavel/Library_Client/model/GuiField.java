package ru.sur_pavel.Library_Client.model;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


class GuiField {


    private int fieldNumber;
    private CheckBox checkbox;
    private Button button = new Button();
    private Label fieldNameLabel;
    private TextField valueTextField;

    public int getFieldNumber() {
        return fieldNumber;
    }

    public void setFieldNumber(int fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Label getFieldNameLabel() {
        return fieldNameLabel;
    }

    public void setFieldNameLabel(Label fieldNameLabel) {
        this.fieldNameLabel = fieldNameLabel;
    }

    public TextField getValueTextField() {
        return valueTextField;
    }

    public void setValueTextField(TextField valueTextField) {
        this.valueTextField = valueTextField;
    }
    public void setValue(String v){
        this.valueTextField.setText(v);
    }

    GuiField(int number, String fieldName) {
        this.fieldNumber = number;
        this.checkbox = new CheckBox();
        this.fieldNameLabel = new Label(fieldName);
        this.valueTextField = new TextField();
    }

}
