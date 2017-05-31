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
import org.marc4j.marc.Record;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        InputStream in = new FileInputStream("summerland.mrc");
        MarcReader reader = new MarcStreamReader(in);
        while (reader.hasNext()) {
            Record record = reader.next();
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
        GridPane gridPane = getGridPane(false, "910", 1, "$a281301");
        tab.setContent(gridPane);
        return tab;
    }

    private GridPane getGridPane(boolean check, String fieldTitle, int btnNum, String value ) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.BASELINE_CENTER);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.add(new CheckBox(),0,1);
        gridPane.add(new Label(fieldTitle),1,1);
        gridPane.add(new Button(String.valueOf(btnNum)),2,1);
        gridPane.add(new TextField(value), 3, 1);
        gridPane.setPadding(new Insets(10));
        return gridPane;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
