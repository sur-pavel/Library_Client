package sample;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditorCom {

    void create(Editor editor) {
        Stage stage = new Stage();
        TextField field = new TextField();

        Scene searchScene = new Scene(field);
        stage.setTitle("Commands");
        stage.setScene(searchScene);

        stage.initModality(Modality.NONE);
        stage.initOwner(editor.editorPane.getScene().getWindow());
        stage.show();
        field.requestFocus();
        field.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    String com = field.getText();
                    if (com != null && com.length() == 3) {
                        try {
                            editor.focusOnTextField(Integer.parseInt(com));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        });
    }
}
