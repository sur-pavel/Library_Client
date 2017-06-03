package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

public class TableViewSample{

    private TableView<Person> table = new TableView<Person>();
    private final ObservableList<Person> data =
            FXCollections.observableArrayList(
                    new Person(1, "Smith"),
                    new Person(2, "Johnson"),
                    new Person(3, "Williams"),
                    new Person(4, "Jones"),
                    new Person(5, "Brown")
            );

    public TableView getTableSearchView() {
        Scene scene = new Scene(new Group());

        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("Кол-во");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName"));

        TableColumn lastNameCol = new TableColumn("Значение");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName"));


        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol);

        return table;
    }

    public static class Person {

        private final SimpleIntegerProperty count;
        private final SimpleStringProperty lastName;


        private Person(Integer cnt, String lName) {
            this.count = new SimpleIntegerProperty(cnt);
            this.lastName = new SimpleStringProperty(lName);

        }

        public Integer getFirstName() {
            return count.get();
        }

        public void setCount(Integer cnt) {
            count.set(cnt);
        }

        public String getLastName() {
            return lastName.get();
        }

        public void setLastName(String fName) {
            lastName.set(fName);
        }

    }
}



