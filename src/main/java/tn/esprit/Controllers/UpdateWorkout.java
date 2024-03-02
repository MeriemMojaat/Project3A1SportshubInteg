package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import tn.esprit.Entities.workouts;
import tn.esprit.services.workoutsService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private ComboBox<String> CoachComboBox;
    @FXML
    private RadioButton EasyRadioBtn;
    @FXML
    private RadioButton MediumRadioBtn;
    @FXML
    private RadioButton HardRadioBtn;


    private workouts workoutsToUpdate; // Store the event to be updated
    private final workoutsService es = new workoutsService();


    @FXML
    private void initialize() {

    }
    private void populateCoachNames() throws SQLException {
        List<String> userNames = es.getCoachNames();
        CoachComboBox.setItems(FXCollections.observableArrayList(userNames));
    }
    private void showAlert (String title, String message, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void gotolistworkout(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/ShowCategory.fxml"));
        workoutname.getScene().setRoot(root);
        System.out.println("Previous");
    }


    @FXML
    void updateworkout(ActionEvent event) {
        try{
            // Retrieve updated data from the form fields
            String name = workoutname.getText();
            String description = workoutdescription.getText();
            String intensity = EasyRadioBtn.isSelected() ? "Easy" : (MediumRadioBtn.isSelected() ? "Medium" : HardRadioBtn.isSelected() ? "Easy" :null);
            String Image = workoutimage.getText();
           // String userName = CoachComboBox.getValue(); // Get selected user name
          //  int userId = es.getcatIdByName(userName);

            // Update the event object with the new data
            workoutsToUpdate.setWorkout_name(name);
            workoutsToUpdate.setWk_description(description);
            workoutsToUpdate.setWk_intensity(intensity);
            workoutsToUpdate.setWk_image(Image);

            // Update the event in the database
            es.update(workoutsToUpdate);

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

    public void setWorkoutData(workouts workouts) {
        this.workoutsToUpdate = workouts;
        workoutname.setText(workouts.getWorkout_name());
        workoutdescription.setText(workouts.getWk_description());
        if ("Easy".equals(workouts.getWk_intensity())) {
            EasyRadioBtn.setSelected(true);
        } else if ("Medium".equals(workouts.getWk_intensity())) {
            MediumRadioBtn.setSelected(true);
        } { HardRadioBtn.setSelected(true);  }

        workoutimage.setText(workouts.getWk_image());



    }


}