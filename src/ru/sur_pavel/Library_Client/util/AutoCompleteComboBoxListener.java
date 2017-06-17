package ru.sur_pavel.Library_Client.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;

/**
 * Class to make autoComplete comboBox
 * @param <T>
 */
public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent> {

    private ComboBox comboBox;
    private StringBuilder sb;
    private ObservableList<T> data;
    private boolean moveCaretToPos = false;
    private int caretPos;

    /**
     * Constructor to add comboBox
     * @param comboBox with any data type
     */
    public AutoCompleteComboBoxListener(final ComboBox<T> comboBox) {
        this.comboBox = comboBox;
        sb = new StringBuilder();
        data = comboBox.getItems();

        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed(t -> comboBox.hide());
        this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
    }

    /**
     * Override default EventHandler method
     * handle some keys in comboBox
     * create list for comboBox with filtered items
     * @param event OnKeyPressed and OnKeyReleased
     */
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()){
            case UP:
                caretPos = -1;
                moveCaret(comboBox.getEditor().getText().length());
                return;
            case DOWN:
                if(!comboBox.isShowing()) {
                    comboBox.show();
                }
                caretPos = -1;
                moveCaret(comboBox.getEditor().getText().length());
                return;
            case BACK_SPACE:
                moveCaretToPos = true;
                caretPos = comboBox.getEditor().getCaretPosition();
                break;
            case DELETE:
                moveCaretToPos = true;
                caretPos = comboBox.getEditor().getCaretPosition();
                break;
            case ESCAPE:
                comboBox.getEditor().setText("");
                break;
            case HOME:
                moveCaretToPos = true;
                caretPos = 0;
                break;
            case END:
                moveCaretToPos = false;
                break;
            case RIGHT:
            case LEFT:
            case TAB:
                return;
        }

        ObservableList list = FXCollections.observableArrayList();
        for (T aData : data) {
            if (aData.toString().toLowerCase().contains(
                    AutoCompleteComboBoxListener.this.comboBox
                            .getEditor().getText().toLowerCase())) {
                list.add(aData);
            }
        }
        String t = comboBox.getEditor().getText();

        comboBox.setItems(list);
        comboBox.getEditor().setText(t);
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(t.length());
        if(!list.isEmpty()) {
            comboBox.show();
        }
    }

    /**
     * Moves the caret to desired position
     * @param textLength length of string in comboBox
     */
    private void moveCaret(int textLength) {
        if(caretPos == -1) {
            comboBox.getEditor().positionCaret(textLength);
        } else {
            comboBox.getEditor().positionCaret(caretPos);
        }
        moveCaretToPos = false;
    }

}