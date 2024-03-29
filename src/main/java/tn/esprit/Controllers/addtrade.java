package tn.esprit.Controllers; // Add the package statement

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import tn.esprit.Entities.trade;
import tn.esprit.services.tradeservice;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class addtrade {

    private int productId; // Event ID passed from ShowEvent


    public void setEventId(int productId) {
        this.productId = productId;

    }

    @FXML
    private TextField ID_PRODUCT;

    @FXML
    private TextField ID_USER;

    @FXML
    private TextField LOCATION;

    @FXML
    private TextField NAME;

    @FXML
    private TextField TRADESTATUS;



    private tradeservice tradeService = new tradeservice();


    private int tradeid;
    public void settradetId(int tradeid){
        this.tradeid = tradeid;
    }

    @FXML
    void back(ActionEvent event)  throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/showproduct.fxml"));
        LOCATION.getScene().setRoot(root);
        System.out.println("List product Page");

    }
    @FXML
    void tradelist(ActionEvent event)  throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/showtrade.fxml"));
        LOCATION.getScene().setRoot(root);
        System.out.println("List product Page");

    }
    @FXML
    private void initialize() {

    }

    @FXML
    public void add(ActionEvent event) {
        try {

            String location = LOCATION.getText();
            String name = NAME.getText();
            String tradeStatus = TRADESTATUS.getText();

            // Validate inputs
            if (location.isEmpty() || name.isEmpty() || tradeStatus.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
                return;
            }
            int authenticatedUserId = SessionManager.getInstance().getAuthenticatedUserId();
            // Then add trade
            trade trade = new trade(productId, authenticatedUserId, location, tradeStatus, name);

            tradeService.add(trade);
            showNotification("Success", "You have added a trade. Please wait for confirmation from the other user.");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Trade.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid numeric values for ID fields.");
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
    // Reference to the stage // Method to set the stage public void setStage(Stage stage) { this.stage = stage; }

}
