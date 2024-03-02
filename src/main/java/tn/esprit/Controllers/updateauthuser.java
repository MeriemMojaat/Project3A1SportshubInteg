package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import tn.esprit.entities.user;
import tn.esprit.services.userservices;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class updateauthuser {

    @FXML
    private TextField date;

    @FXML
    private TextField password;

    @FXML
    private TextField gender;

    @FXML
    private Button homebutton;

    @FXML
    private TextField mail;

    @FXML
    private TextField name;

    @FXML
    private TextField phone;

    @FXML
    private ScrollPane scrollPane;
    private user currentUser;
    private final userservices us = new userservices();
    private final Login login=new Login();
    public void initData(user currentUser) {
        this.currentUser = currentUser;
        // Display current user information in the input fields
        name.setText(currentUser.getNameuser());
        mail.setText(currentUser.getUseremail());
        phone.setText(currentUser.getUphonenumber());
        password.setText(currentUser.getUserpassword());
        date.setText(currentUser.getUserdateofbirth().toString());
        gender.setText(currentUser.getUsergender());

    }
    Controller controller=new Controller();
    public void setController(Controller controller) {
        this.controller = controller;
    }


    @FXML
    void home1(ActionEvent event) {
        // Retrieve the existing instance of the Login controller
        Controller controller1 = new Controller();
        Login loginController=new Login();

        // Get the authenticated user or admin from the existing Login instance
        user authuser = controller1.getAuthenticatedUser();

        // Add debug statements to check if authenticatedObject is obtained correctly
        System.out.println("AuthenticatedObject: " + authuser);

        if (authuser != null) {
            // Load the user homepage FXML

            loginController.navigateToHomePage(authuser);
        } else {
            // Handle case when authenticatedObject is null or invalid
            System.err.println("Authenticated user or admin is null or invalid. Unable to navigate to homepage.");
        }
    }




    @FXML
    void updateauth(ActionEvent event) throws SQLException {
        String newUsername = name.getText();
        String newEmail = mail.getText();
        String newphone = phone.getText();
        String newpass = password.getText();
        LocalDate newdate = Date.valueOf(date.getText()).toLocalDate();
        String newgender = gender.getText();

        // Update the user information via the UserService
        boolean success = us.updatebyname(currentUser);


        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        if (success) {
            // If update is successful, update currentUser object as well
            currentUser.setNameuser(newUsername);
            currentUser.setUseremail(newEmail);
            currentUser.setUphonenumber(newphone);
            currentUser.setUserpassword(newpass);
            currentUser.setUserdateofbirth(newdate);
            currentUser.setUsergender(newgender);

            successAlert.setTitle("Success");
            successAlert.setContentText("User updated successfully.");
            successAlert.showAndWait();
        } else {
            successAlert.setTitle("Failed");
            successAlert.setContentText("Please enter a valid E-mail Address.");
            successAlert.showAndWait();
        }
    }
    }

