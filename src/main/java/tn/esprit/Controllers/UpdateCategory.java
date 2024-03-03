package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.Entities.workoutcategory;
import tn.esprit.services.workoutcategoryService;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateCategory {

    @FXML
    private TextArea categorydescription;

    @FXML
    private TextField categoryimage;

    @FXML
    private TextField categoryname;

    @FXML
    private Button event;

    @FXML
    private Button tournement;

    @FXML
    private Button trade;

    @FXML
    private Button user;

    @FXML
    private Button workout;
    private workoutcategory CategoryToUpdate; // Store the event to be updated
    private final workoutcategoryService es = new workoutcategoryService();



    @FXML
    void gotolistcategory(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/ShowCategoryCoach.fxml"));
        categoryname.getScene().setRoot(root);
        System.out.println("Previous");
    }


    @FXML
    void updatecategory(ActionEvent event) {
        try{
            // Retrieve updated data from the form fields
            String name = categoryname.getText();
            String description = categorydescription.getText();
            String image = categoryimage.getText();

            // Update the event object with the new data
            CategoryToUpdate.setCategory_name(name);
            CategoryToUpdate.setCat_description(description);
            CategoryToUpdate.setCat_image(image);
            // Update the event in the database
            es.update(CategoryToUpdate);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("workout updated successfully");
            alert.showAndWait();}
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void setCategoryData(workoutcategory workoutcategory) {

        this.CategoryToUpdate = workoutcategory;

        categoryname.setText(workoutcategory.getCategory_name());
        categorydescription.setText(workoutcategory.getCat_description());
        categoryimage.setText(workoutcategory.getCat_image());

    }
    private void showAlert (String title, String message, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
