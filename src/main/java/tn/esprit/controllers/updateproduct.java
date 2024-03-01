package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import tn.esprit.entities.product;
import tn.esprit.services.productservice;

import java.sql.SQLException;

public class updateproduct {

    @FXML
    private TextField ID_PRODUCT;

    private product productToUpdate;

    @FXML
    private TextField IMAGE;

    @FXML
    private TextArea DESCRIPTION;

    @FXML
    private TextField QUANTITY;

    @FXML
    private TextField STATE;

    @FXML
    private TextField TYPE;

    productservice ps = new productservice();

    public void setProductData(product prod) {
        this.productToUpdate = prod;
        TYPE.setText(prod.getTYPE());
        DESCRIPTION.setText(prod.getDESCRIPTION());
        IMAGE.setText(prod.getIMAGE());
        STATE.setText(prod.getSTATE());
        QUANTITY.setText(String.valueOf(prod.getQUANTITY()));
    }

    @FXML
    void update(ActionEvent event) {
        try {
            // Retrieve updated data from the form fields
            String type = TYPE.getText();
            String description = DESCRIPTION.getText();
            String image = IMAGE.getText();
            String state = STATE.getText();
            int quantity = Integer.parseInt(QUANTITY.getText());


            // Update the product object with the new data
            productToUpdate.setTYPE(type);
            productToUpdate.setDESCRIPTION(description);
            productToUpdate.setIMAGE(image);
            productToUpdate.setSTATE(state);
            productToUpdate.setQUANTITY(quantity);
            ps.update(productToUpdate);
            showNotification("Success", "product updated successfully" );
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Product updated successfully");
            alert.showAndWait();


        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
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
