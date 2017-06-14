package ru.sur_pavel.Library_Client.view;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import ru.sur_pavel.Library_Client.MainApp;

public class RootController {




    @FXML
    private SplitPane VerticalSplit;
    @FXML
    private SplitPane HorizontalSplit;

    private MainApp mainApp;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public SplitPane getVerticalSplit() {
        return VerticalSplit;
    }

    public SplitPane getHorizontalSplit() {
        return HorizontalSplit;
    }

    @FXML
    private void handleSearch(){
        mainApp.showSearch();
    }
    @FXML
    private void handleExit() {
        System.exit(0);
    }

}
