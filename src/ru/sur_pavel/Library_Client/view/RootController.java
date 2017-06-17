package ru.sur_pavel.Library_Client.view;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import ru.sur_pavel.Library_Client.MainApp;

/**
 * Controller of root layout
 */
public class RootController {




    @FXML
    private SplitPane VerticalSplit;

    private MainApp mainApp;

    /**
     * Sets MainApp object for controller
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * @return object of root layout SplitPane
     */
    public SplitPane getVerticalSplit() {
        return VerticalSplit;
    }

    /**
     * Calls mainApp showSearch method
     * on select menuItem
     * or on pressing CONTROL+F
     */
    @FXML
    private void handleSearch(){
        mainApp.showSearch();
    }
    @FXML
    private void handleExit() {
        System.exit(0);
    }

}
