package tn.esprit.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import tn.esprit.entities.product;
import tn.esprit.entities.trade;
import tn.esprit.services.tradeservice;

import java.sql.SQLException;
import java.util.List;

public class updatetrade {
    private trade tradeToUpdate;

    @FXML
    private ComboBox<String> Id_UserName;

    @FXML
    private TextField LOCATION;

    @FXML
    private TextField NAME;

    @FXML
    private TextField TRADESTATUS;


    private tradeservice tradeService = new tradeservice();


    @FXML
    private void initialize() {
        populateUserNames();
    }
    private void populateUserNames()
    {
        try {
            List<String> userNames = tradeService.getUserNames();
            Id_UserName.getItems().addAll(userNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            System.out.println(tradeToUpdate.getID_TRADE());
            String location = LOCATION.getText();
            String name = NAME.getText();
            String tradeStatus = TRADESTATUS.getText();

            // Validate inputs
            if (location.isEmpty() || name.isEmpty() || tradeStatus.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
                return;
            }
            String userName = Id_UserName.getValue(); // Get selected user name

            int userId = tradeService.getUserIdByName(userName);
            trade UpdateTrade = new trade(tradeToUpdate.getID_TRADE(),tradeToUpdate.getID_PRODUCT(), userId, location, tradeStatus, name);

            // Update the product object with the new data

            tradeService.update(UpdateTrade);
            showNotification("Success", "Trade updated successfully");



            showAlert(Alert.AlertType.INFORMATION, "Success", "Trade updated successfully.");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error occurred: " + e.getMessage());
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

