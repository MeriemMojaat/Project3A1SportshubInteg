package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class Visitor {


    @FXML
    private Button adminid;
    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    void gotologin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        loginButton.getScene().setRoot(root);
        System.out.println("moved");

    }
    @FXML
    void gotoadmin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/adminlogin.fxml"));
        loginButton.getScene().setRoot(root);
        System.out.println("moved");
    }

    @FXML
    void gotosignup(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AddUser.fxml"));
        loginButton.getScene().setRoot(root);
        System.out.println("moved");
    }

}