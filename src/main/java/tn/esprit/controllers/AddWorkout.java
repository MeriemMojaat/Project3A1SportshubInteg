package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.workoutcategory;
import tn.esprit.entities.workouts;
import tn.esprit.services.workoutcategoryService;
import tn.esprit.services.workoutsService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AddWorkout {

    @FXML
    private Button event;

    @FXML
    private TextField intensity;
    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private Button tournement;

    @FXML
    private Button trade;

    @FXML
    private Button user;

    @FXML
    private Button workout;

    @FXML
    private TextArea workoutdescription;

    @FXML
    private TextField workoutimage;
    @FXML
    private TextField workoutname;

    @FXML
    private TextField coachid;

    @FXML
    private int id_category;

    private workoutsService workoutsService = new workoutsService();
    private workoutcategoryService wc = new workoutcategoryService();
    private int currentCategoryId; // This variable holds the ID of the current category

    // Method to set the ID of the current category
    private void populateUserNames() {
        try {
            List<String> userNames = workoutsService.getCategoryNames();
            categoryComboBox.getItems().addAll(userNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCategoryId(int id_category) {
        this.id_category = id_category;
    }

    /*
    private void gotolistworkout(ActionEvent event, int idCategory) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowWorkouts.fxml"));
            Parent root = loader.load();

            ShowWorkouts showWorkoutsController = loader.getController();
            showWorkoutsController.displayWorkouts(idCategory); // Pass the idCategory to displayWorkouts

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load AddBooking page", Alert.AlertType.ERROR);
        }
    }*/
    @FXML
    void gotolistworkout(ActionEvent event) {

    }
    @FXML
    private void initialize() {
        populateUserNames();
    }
    @FXML
    void AddWorkout(ActionEvent event) {
        try {
            String categName = categoryComboBox.getValue();
            int userId = workoutsService.getcatIdByName(categName);

            if (userId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid user selected.", "Please select a valid user.");
                return;
            }

            workouts newBooking = new workouts(workoutname.getText(), workoutdescription.getText(),intensity.getText(),workoutimage.getText(),Integer.parseInt(coachid.getText()),userId);
            workoutsService.add(newBooking);
            showAlert(Alert.AlertType.INFORMATION, "Confirmation", "A new workout is added", null);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add workout.", e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input", "Please enter valid values for participants and price.");
        }

    }
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    }