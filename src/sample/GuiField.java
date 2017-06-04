package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


class GuiField {

    private int fieldNumber;
    private CheckBox checkbox;
    private Button button;
    private TextField valueTextField;
    private HBox hBox;

    GuiField(int number, String fieldName) {
        this.fieldNumber = number;
        Label fieldNameLabel = new Label(fieldName);
        fieldNameLabel.setMinWidth(200);
        fieldNameLabel.setMaxWidth(200);
        this.checkbox = new CheckBox();
        this.button = new Button();
        this.valueTextField = new TextField();
        this.valueTextField.setMinWidth(810);
        this.hBox = new HBox();
        this.hBox.getChildren().addAll(this.checkbox, fieldNameLabel,this.button, this.valueTextField);
        this.hBox.setMargin(this.checkbox, new Insets(5, 0, 0 ,10));
        this.hBox.setMargin(fieldNameLabel, new Insets(5, 0, 0 ,5));
        this.hBox.setMargin(this.button, new Insets(5, 0, 0 ,5));
        this.hBox.setMargin(this.valueTextField, new Insets(5, 0, 0 ,5));
    }


    int getFieldNumber(){
        return this.fieldNumber;
    }

    boolean isChecked(){
        return this.checkbox.isSelected();
    }

     void setValue(String value) {
        this.valueTextField.setText(value);
    }
     HBox getHBox(){
        return this.hBox;
    }
     void setButtonNum (int num){
        this.button.setText(String.valueOf(num));
    }
     String getValue(){
        return this.valueTextField.getText();
    }
}
