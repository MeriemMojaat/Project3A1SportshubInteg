package tn.esprit.Controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.esprit.services.tradeservice;

import java.sql.SQLException;

public class deletetrade {

    @FXML
    private TextField ID_PRODUCT;

    @FXML
    private TextField ID_TRADE;

    @FXML
    private TextField userid;

    @FXML
    private TextField LOCATION;

    @FXML
    private TextField NAME;

    @FXML
    private TextField TRADESTATUS;


    private tradeservice tradeService = new tradeservice();

    @FXML
    void delete(ActionEvent event) {
        try {
            int tradeId = Integer.parseInt(ID_TRADE.getText());

            // Perform the delete operation
            tradeService.delete(tradeId);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Trade deleted successfully.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid trade ID.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error occurred: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
