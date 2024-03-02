package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import tn.esprit.entities.Feedback;
import tn.esprit.entities.workoutcategory;
import tn.esprit.entities.workouts;
import tn.esprit.services.*;
import javafx.scene.media.Media;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ShowWorkouts {

    @FXML
    private Button event;

    @FXML
    private ScrollPane scrollpane;


    @FXML
    private TextField SearchWKid;


    private int id_category;
    private int WorkoutID;

    private final workoutsService bs = new workoutsService();
    private workoutcategoryService categoryService = new workoutcategoryService();
    FeedbackDao feedbackDao = new FeedbackDaoImpl();
    FeedbackService feedbackService = new FeedbackService(feedbackDao);


    @FXML
    void initialize() {

        SearchWKid.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                try {
                    displayWorkouts(id_category); // Revert to original state if search field is cleared
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Trigger the search operation when text changes
                SearchWKid(null); // Pass null or any appropriate event parameter
            }
        });
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
            int id_workout = workout.getId_workout();
            VBox eventBox = new VBox();
            eventBox.setStyle("-fx-background-color: #AED6F1; -fx-border-color: #94cdf5; -fx-padding: 30px; -fx-spacing: 10px;");
            eventBox.setSpacing(10);
            String workout_name = workout.getWorkout_name();
            String wk_description = workout.getWk_description();
            String wk_intensity = workout.getWk_intensity();
            String wk_video_url = workout.getWk_image();
          //  int coach_id = workout.getCoach_id();

            // Create VideoPlayerBox
            VideoPlayerBox videoPlayerBox = new VideoPlayerBox(wk_video_url);

            // Create delete button
            eventBox.getProperties().put("id_workout", workout.getId_workout());
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(workouts -> handleDeleteWorkout(workouts));
            Button updateButton = new Button("Update");
            updateButton.setOnAction(event -> handleUpdateWorkout(event, workout.getId_workout()));
            HBox buttonBox = new HBox(deleteButton, updateButton);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);




            Button likeButton = new Button("👍");
            likeButton.setOnAction(event -> submitFeedback(id_workout));
            Button dislikeButton = new Button("👎");
            dislikeButton.setOnAction(event -> handleDislike(id_workout));
            HBox ratingBox = new HBox(likeButton, dislikeButton);
            ratingBox.setAlignment(Pos.CENTER_RIGHT);



            // Create labels for the event details
            Label nameLabel = new Label("Name: " + workout_name);
            Label descriptionLabel = new Label("Description: " + wk_description);
            Label intensityLabel = new Label("Intensity: " + wk_intensity);
           // Label coachidLabel = new Label("CoachID: " + coach_id);

            // Add components to eventBox
            eventBox.getChildren().addAll(
                    nameLabel, descriptionLabel, intensityLabel,ratingBox, videoPlayerBox, buttonBox
            );

            return eventBox;
        }





    @FXML
    void submitFeedback(int id_workout) {
        try {
            // Perform action to increment like count for the specified workout ID
            Feedback feedback = new Feedback();
            bs.incrementLikeCount(id_workout);
           // feedback.setUserid(getCurrentUserId()); // Set the user ID
            feedback.setId_workout(id_workout); // Set the workout ID
            feedback.setLike_count(1);
            feedbackDao.saveFeedback(feedback);
            showAlert("Liked", "Liked workout with ID: " + id_workout, Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Add event handler for the dislike button
    @FXML
    void handleDislike(int id_workout) {
        try {
            // Perform action to increment dislike count for the specified workout ID
            bs.incrementDislikeCount(id_workout);
            showAlert("Disliked", "Disliked workout with ID: " + id_workout, Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    // Helper method to retrieve workout ID from the event source
    private int getWorkoutIdFromEvent(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();

            // Retrieve the parent VBox containing the workout details
            VBox parentVBox = (VBox) clickedButton.getParent();

            // Retrieve the workout ID associated with the VBox
            int id_workout = (int) parentVBox.getProperties().get("id_workout");
            return id_workout;
        }
        return -1; // Default value indicating no workout ID found
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

    @FXML
    void SearchWKid(ActionEvent event) {
        try {
            String searchCriteria = SearchWKid.getText();
            if (searchCriteria.isEmpty()) {
                displayWorkouts(id_category); // Revert to original state if search field is empty
            } else {

                List<workouts> workout = bs.searchworkout(searchCriteria);
                ObservableList<workouts> observableList = FXCollections.observableList(workout);
                updateVBoxWorkout(workout); // Update the UI with search results
            }
        } catch (SQLException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateVBoxWorkout(List<workouts> workout) {
        // Clear existing content in the scroll pane
        scrollpane.setContent(null);

        // Create a new VBox to hold all event boxes
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));

        // Initialize row and column counters for layout
        int column = 0;

        // Create an HBox to hold events for each row
        HBox currentRowHBox = new HBox();
        currentRowHBox.setSpacing(20);

        // Iterate over the list of events and create event boxes
        for (workouts category : workout) {
            // Create event box for the current event
            VBox catBox = createWorkoutBox(category);

            // Add the event box to the current row HBox
            currentRowHBox.getChildren().add(catBox);

            // Increment column counter
            column++;

            // If the column count reaches 3, reset it and add the current row HBox to the VBox
            if (column == 3) {
                vBox.getChildren().add(currentRowHBox);
                currentRowHBox = new HBox(); // Reset the row HBox for the next row
                currentRowHBox.setSpacing(20);
                column = 0; // Reset column counter
            }
        }

        // If there are remaining events in the last row, add them to the VBox
        if (!currentRowHBox.getChildren().isEmpty()) {
            vBox.getChildren().add(currentRowHBox);
        }

        // Set the VBox as the content of the scroll pane
        scrollpane.setContent(vBox);
    }


    @FXML
    void gotocategories(ActionEvent event) throws IOException {
            Parent root= FXMLLoader.load(getClass().getResource("/ShowCategory.fxml"));
        scrollpane.getScene().setRoot(root);
            System.out.println("List Event Page");

    }

}