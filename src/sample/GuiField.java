package sample;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.*;



class GuiField {



    private int fieldNumber;
    private CheckBox checkbox;
//    private Button button = new Button();
    private Label fieldNameLabel;
    private TextField valueTextField;
    private HBox hBox;


    GuiField(int number, String fieldName) {
        this.fieldNumber = number;
        this.checkbox = new CheckBox();

        Font font = new Font("Tahoma",13);

        this.fieldNameLabel = new Label(fieldName);
        this.fieldNameLabel.setFont(font);
        this.fieldNameLabel.setMinWidth(400);
        this.fieldNameLabel.setMaxWidth(400);

        this.valueTextField = new TextField();
        this.valueTextField.setMinWidth(1150);
        this.valueTextField.setFont(font);


        this.hBox = new HBox();
        this.hBox.getChildren().addAll(this.checkbox, fieldNameLabel, this.valueTextField);
        this.hBox.setMargin(this.checkbox, new Insets(5, 0, 0, 10));
        this.hBox.setMargin(fieldNameLabel, new Insets(5, 0, 0, 5));
//        this.hBox.setMargin(this.button, new Insets(5, 0, 0, 5));
        this.hBox.setMargin(this.valueTextField, new Insets(5, 0, 0, 5));
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public Label getFieldNameLabel() {
        return fieldNameLabel;
    }
//    public Button getButton() {
//        return button;
//    }

    public TextField getValueTextField() {
        return valueTextField;
    }
    int getFieldNumber() {
        return this.fieldNumber;
    }

    boolean isChecked() {
        return this.checkbox.isSelected();
    }

    void setValue(String value) {
        this.valueTextField.setText(value);
    }

    HBox getHBox() {
        return this.hBox;
    }

//    void setButtonNum(int num) {
//        this.button.setText(String.valueOf(num));
//    }

    String getValue() {
        return this.valueTextField.getText();
    }
}
