package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.entities.workoutcategory;
import tn.esprit.entities.workouts;
import tn.esprit.services.workoutcategoryService;
import tn.esprit.services.workoutsService;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateWorkout {

    @FXML
    private TextField categoryid;

    @FXML
    private TextField coachid;

    @FXML
    private Button event;

    @FXML
    private TextField intensity;

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


    private workouts workoutsToUpdate; // Store the event to be updated
    private final workoutsService es = new workoutsService();



    @FXML
    void gotolistworkout(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/ShowWorkouts.fxml"));
        workoutname.getScene().setRoot(root);
        System.out.println("Previous");
    }


    @FXML
    void updateworkout(ActionEvent event) {
        try{
            // Retrieve updated data from the form fields
            String name = workoutname.getText();
            String description = workoutdescription.getText();
            String Intensity = intensity.getText();
            String Image = workoutimage.getText();
            String CoachID = coachid.getText();
            String CategoryID = categoryid.getText();



            // Update the event object with the new data
            workoutsToUpdate.setWorkout_name(name);
            workoutsToUpdate.setWk_description(description);
            workoutsToUpdate.setWk_intensity(Intensity);
            workoutsToUpdate.setWk_image(Image);
            workoutsToUpdate.setCoach_id(Integer.parseInt(CoachID));
            workoutsToUpdate.setId_category(Integer.parseInt(CategoryID));

            // Update the event in the database
            es.update(workoutsToUpdate);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Event updated successfully");
            alert.showAndWait();}
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void setWorkoutData(workouts workouts) {
        this.workoutsToUpdate = workouts;
        workoutname.setText(workouts.getWorkout_name());
        workoutdescription.setText(workouts.getWk_description());
        intensity.setText(workouts.getWk_intensity());
        workoutimage.setText(workouts.getWk_image());
        coachid.setText(String.valueOf(workouts.getCoach_id()));
        categoryid.setText(String.valueOf(workouts.getId_category()));

    }


}
