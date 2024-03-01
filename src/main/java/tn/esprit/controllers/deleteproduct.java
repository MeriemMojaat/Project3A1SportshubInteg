package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.esprit.services.productservice;

import java.sql.SQLException;

public class deleteproduct {

    @FXML
    private TextField ID_PRODUCT;

    @FXML
    private TextField IMAGE;

    @FXML
    private TextField DESCRIPTION;

    @FXML
    private TextField QUANTITY;

    @FXML
    private TextField STATE;

    @FXML
    private TextField TYPE;

    // Assuming you have a ProductService instance named productService
    private productservice productService = new productservice(); // Adjust instantiation as per your implementation

    @FXML
    void delete(ActionEvent event) {
        try {
            // Get the product ID from the TextField or another appropriate source
            int productId = Integer.parseInt(ID_PRODUCT.getText());

            // Call the delete method on productService passing the productId
            productService.delete(productId);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product deleted successfully.");

            // Clear existing product boxes or update UI accordingly
            // Assuming you have a method named clearFields to clear the text fields
            clearFields();

            // You may want to refresh your product list or UI here
            // Example: refreshProductList();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid product ID.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    // Method to display an alert dialog
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to clear the text fields
    private void clearFields() {
        ID_PRODUCT.clear();
        IMAGE.clear();
        DESCRIPTION.clear();
        QUANTITY.clear();
        STATE.clear();
        TYPE.clear();
    }
 

}
