package tn.esprit.Controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import tn.esprit.Entities.trade;
import tn.esprit.services.tradeservice;

import java.sql.SQLException;
import java.util.List;

public class updatetrade {
    private trade tradeToUpdate;


    @FXML
    private TextField LOCATION;

    @FXML
    private TextField NAME;

    @FXML
    private TextField TRADESTATUS;


    private tradeservice tradeService = new tradeservice();


    @FXML
    private void initialize() {
    }


    @FXML
    void update(ActionEvent event) {
        try {
            String name = NAME.getText();
            String loc = LOCATION.getText();
            String status = TRADESTATUS.getText();

            tradeToUpdate.setNAME(name);
            tradeToUpdate.setLOCATION(loc);
            tradeToUpdate.setTRADESTATUS(status);

            tradeService.update(tradeToUpdate);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Trade updated successfully");
            alert.showAndWait();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setTradeData(trade prod) {
          this.tradeToUpdate = prod;


        LOCATION.setText(prod.getLOCATION());
        TRADESTATUS.setText(prod.getTRADESTATUS());
        NAME.setText(String.valueOf(prod.getNAME()));
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private Stage stage ;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void showNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .owner(stage)
                .showInformation();// Set the owner window .showInformation();
    }

}

