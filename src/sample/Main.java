package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
List dataFields;
    @Override
    public void start(Stage primaryStage) throws Exception{

        InputStream in = new FileInputStream("oneRec.ISO");
        MarcReader reader = new MarcStreamReader(in, "UTF8");
        while (reader.hasNext()) {
            Record record = reader.next();
            dataFields = record.getDataFields();
            System.out.println(record.toString());
        }

        Group root = new Group();
        Scene scene = new Scene(root, 400, 250, Color.WHITE);
        BorderPane borderPane = getBorderPane(scene);
        root.getChildren().add(borderPane);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane getBorderPane(Scene scene) {
        TabPane tabPane = new TabPane();
        BorderPane borderPane = new BorderPane();
        Tab tab = getTab("Техонология");
        tabPane.getTabs().add(tab);
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        borderPane.setCenter(tabPane);
        return borderPane;
    }

    private Tab getTab(String name) {
        Tab tab = new Tab(name);
        GridPane gridPane = getGridPane();
        tab.setContent(gridPane);
        return tab;
    }

    private GridPane getGridPane() {
        ArrayList<GuiField> guiFields = new ArrayList<>();
        for (Object dataField : dataFields) {
            DataField dataField1 = (DataField) dataField;
            guiFields.add(new GuiField(dataField1));
        }
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.BASELINE_CENTER);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(10));
        for (int i = 1, size = guiFields.size(); i < size; i++) {
            gridPane.add(new CheckBox(), 0, i);
            String fieldTitle = String.join(": ", String.valueOf(guiFields.get(i).getFieldNum()), guiFields.get(i).getName());
            gridPane.add(new Label(fieldTitle), 1, i);
            gridPane.add(new Button(String.valueOf(guiFields.get(i).getButtonNum())), 2, i);
            gridPane.add(new TextField(guiFields.get(i).getValue()), 3, i);
        }
        return gridPane;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
