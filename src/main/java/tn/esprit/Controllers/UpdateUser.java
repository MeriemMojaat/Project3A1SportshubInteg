package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import tn.esprit.Entities.user;
import tn.esprit.services.userservices;

import java.sql.SQLException;
import java.util.List;

public class UpdateUser {

    @FXML
    private DatePicker date;

    @FXML
    private TextField email;

    @FXML
    private ComboBox<String> gender;

    @FXML
    private TextField password;

    @FXML
    private TextField phonenumber;

    @FXML
    private ComboBox<Integer> userIdComboBox;

    @FXML
    private TextField username;

    private final userservices us = new userservices();


    @FXML
    public void initialize() {
        gender.getItems().addAll("Male", "Female");

        // Populate the ComboBox with user IDs
        List<Integer> userid = null;
        try {
            userid = us.getAllUserId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Integer> userIdsObservable = FXCollections.observableArrayList(userid);
        userIdComboBox.setItems(userIdsObservable);



// Listen for changes in the selected user ID
        userIdComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    // Fetch user details based on selected ID and populate text fields
                    user user = us.getUserById(newValue);
                    populateFields(user);
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle error
                }
            }


        } );
        }
    private void populateFields(user user) {
        username.setText(user.getNameuser());
        phonenumber.setText(user.getUphonenumber());
        email.setText(user.getUseremail());
        password.setText(user.getUserpassword());
        date.setValue(user.getUserdateofbirth());
        gender.setAccessibleText(user.getUsergender());

    }



    @FXML
    void UpdateUser(ActionEvent event) {
        try{
            int userId = userIdComboBox.getValue(); // Get the selected user ID from the ComboBox
            us.update(new user(userId,username.getText(),phonenumber.getText(),email.getText(),password.getText(),date.getValue(),  gender.getAccessibleText()));
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
           if(!us.update(new user(userId,username.getText(),phonenumber.getText(),email.getText(),password.getText(),date.getValue(),  gender.getAccessibleText())))  {
               successAlert.setTitle("Failed");
               successAlert.setContentText("Please enter a valid E-mail Address.");
               successAlert.showAndWait();
           }else{
            successAlert.setTitle("Success");
            successAlert.setContentText("User updated successfully.");
            successAlert.showAndWait();
        }}
        catch (SQLException e)
        {
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }
    }

    public void initData(user authenticatedUser) {
    }
}












