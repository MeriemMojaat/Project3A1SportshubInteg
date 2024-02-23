package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.workoutcategory;
import tn.esprit.entities.workouts;
import tn.esprit.services.workoutcategoryService;
import tn.esprit.services.workoutsService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ShowWorkouts {

    @FXML
    private Button event;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private Button tournement;

    @FXML
    private Button trade;

    @FXML
    private Button user;

    @FXML
    private Button workout;

    private int id_category;
    private int WorkoutID;

    private final workoutsService bs = new workoutsService();
    private workoutcategoryService categoryService = new workoutcategoryService();


    @FXML
    void initialize() {
        try {
            // Assuming you have the categoryId stored somewhere, pass it here
            displayWorkouts(id_category);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCategoryId(int id_category) {
        this.id_category = id_category;

    }

    void displayWorkouts(int id_category) throws IOException {
        try {
            List<workouts> workout = bs.displayByCategory(id_category);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(20);
            gridPane.setVgap(20);
            gridPane.setPadding(new Insets(20));

            int row = 0;
            int column = 0;
            for (workouts workouts : workout) {
                HBox hbox = new HBox();
                hbox.getChildren().add(createWorkoutBox(workouts));
                gridPane.add(hbox, column, row);

                column++;
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }

            scrollpane.setContent(gridPane);
        } catch (SQLException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

        private VBox createWorkoutBox (workouts workout){
            VBox eventBox = new VBox();
            eventBox.setStyle("-fx-background-color: #AED6F1; -fx-border-color: #94cdf5; -fx-padding: 30px; -fx-spacing: 10px;");
            eventBox.setSpacing(10);
            String workout_name = workout.getWorkout_name();
            String wk_description = workout.getWk_description();
            String wk_intensity = workout.getWk_intensity();
            String wk_image = workout.getWk_image();
           int coach_id = workout.getCoach_id();
         /*   int id_category = workout.getId_category();*/

            // Create delete button
            eventBox.getProperties().put("id_workout", workout.getId_workout());

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(workouts -> handleDeleteWorkout(workouts));

            Button updateButton = new Button("Update");
            updateButton.setOnAction(event -> handleUpdateWorkout(event, workout.getId_workout()));

            HBox buttonBox = new HBox(deleteButton, updateButton);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);

            // Create labels for the event details
            Label nameLabel = new Label("Name: " + workout_name);
            Label descriptionLabel = new Label("Description: " + wk_description);
            Label intensityLabel = new Label("Intensity: " + wk_intensity);
            Label imageLabel = new Label("Image: " + wk_image);
            Label coachidLabel = new Label("CoachID: " + coach_id);
           /* Label categoryidLabel = new Label("Category: " + id_category);*/

            eventBox.getChildren().addAll(
                    nameLabel, descriptionLabel,intensityLabel ,imageLabel,coachidLabel, buttonBox
            );

            return eventBox;
        }

        private void handleUpdateWorkout (ActionEvent event,int WorkoutID){
            try {
                // Retrieve the Event object associated with the event ID
                workouts workout = bs.getWorkoutsById(WorkoutID);

                if (workout == null) {
                    // Handle the case where the event for the event ID is not found
                    showAlert("Error", "workout not found for ID: " + WorkoutID, Alert.AlertType.ERROR);
                    return;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateWorkout.fxml"));
                Parent root = loader.load();

                UpdateWorkout updateWorkoutController = loader.getController();
                updateWorkoutController.setWorkoutData(workout);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load Update Workout page", Alert.AlertType.ERROR);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        private void handleDeleteWorkout (ActionEvent workouts) {
            if (workouts.getSource() instanceof Button) {
                Button clickedButton = (Button) workouts.getSource();

                // Get the parent of the button (HBox)
                Parent parent = clickedButton.getParent();

                // Traverse the parent hierarchy until finding the VBox containing the event details
                while (parent != null && !(parent instanceof VBox)) {
                    parent = parent.getParent();
                }
                if (parent instanceof VBox) {
                    VBox eventBox = (VBox) parent;
                    // Retrieve the id_event from the properties map of the VBox
                    Integer id_workout = (Integer) eventBox.getProperties().get("id_workout");
                    if (id_workout != null) {
                        try {
                            // Call your EventService delete method with the id_event
                            bs.delete(id_workout);


                            // Remove the VBox from its parent (scrollpane)
                            scrollpane.setContent(null);
                            try {
                                displayWorkouts(id_category); // Refresh the event list
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } catch (SQLException e) {
                            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
                        }
                    }
                }
            }

        }
        private void showAlert (String title, String message, Alert.AlertType alertType){
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    private int retrieveCategoryId() {
        try {
            List<workoutcategory> categories = categoryService.display();
            if (!categories.isEmpty()) {
                // For demonstration, let's assume the first category
                return categories.get(0).getId_category();
            } else {
                // Handle the case when no categories are available
                return -1; // Or any default value you prefer
            }
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return -1; // Return a default value or handle the error accordingly
        }
    }

    @FXML
    void addworkout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddWorkout.fxml"));
            Parent root = loader.load();

            AddWorkout addWorkoutController = loader.getController();
            addWorkoutController.setCategoryId(retrieveCategoryId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
    }
}
