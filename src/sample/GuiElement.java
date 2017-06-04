package sample;

import javafx.scene.layout.BorderPane;
import org.marc4j.marc.Record;

public interface GuiElement {

    BorderPane create();

    void update(Record record);
}
