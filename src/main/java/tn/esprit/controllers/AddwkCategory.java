package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.entities.workoutcategory;
import tn.esprit.services.workoutcategoryService;

import java.io.IOException;
import java.sql.SQLException;

public class AddwkCategory {

    @FXML
    private TextArea categorydescription;

    @FXML
    private TextField categoryimage;

    @FXML
    private TextField categoryname;

    private final workoutcategoryService cs = new workoutcategoryService();


    @FXML
    void AddCategory(ActionEvent event) {
        try{
            cs.add(new workoutcategory(categoryname.getText(),categorydescription.getText(),categoryimage.getText()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("A new category is added");
            alert.showAndWait();
        }
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }
    @FXML
    void Gotolist(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/ShowCategory.fxml"));
        categoryname.getScene().setRoot(root);
        System.out.println("List Event Page");

    }


   /* @FXML
    void event(ActionEvent event) {

    }

    @FXML
    void tournement(ActionEvent event) {

    }

    @FXML
    void trade(ActionEvent event) {

    }

    @FXML
    void user(ActionEvent event) {

    }

    @FXML
    void workout(ActionEvent event) {

    }*/

}
