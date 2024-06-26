package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.Entities.FeedbackWorkout;
import tn.esprit.Entities.workouts;
import tn.esprit.services.*;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    FeedbackWorkoutService fs = new FeedbackWorkoutService();


    @FXML
    void initialize() {

        SearchWKid.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {

                    displayWorkouts(id_category); // Revert to original state if search field is cleared
                }
                // Trigger the search operation when text changes
        });
        displayWorkouts(id_category); // Revert to original state if search field is cleared

    }

    public void setCategoryId(int id_category) {
        this.id_category = id_category;

    }




    void displayWorkouts(int id_category) {
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

            scrollpane.setContent(gridPane); // Set the content of the scroll pane after adding all workout boxes
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
            //int coach_id = workout.getCoach_id();

            // Create VideoPlayerBox
            VideoPlayerBox videoPlayerBox = new VideoPlayerBox(wk_video_url);


            // Create delete button
            eventBox.getProperties().put("id_workout", workout.getId_workout());


            Button likeButton = new Button("👍");
            likeButton.setOnAction(event -> handleFeedback(id_workout, true));
            Button dislikeButton = new Button("👎");
            dislikeButton.setOnAction(event -> handleFeedback(id_workout, false));

            int likeCount;
            int dislikeCount;
            try {
                likeCount = bs.getLikeCount(id_workout);
                dislikeCount = bs.getDislikeCount(id_workout);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
                likeCount = 0; // Default value if retrieval fails
                dislikeCount = 0; // Default value if retrieval fails
            }


            HBox ratingBox = new HBox(likeButton, new Label("Likes: " + likeCount), dislikeButton, new Label("Dislikes: " + dislikeCount));
            ratingBox.setAlignment(Pos.CENTER_RIGHT);




            // Create labels for the event details
            Label nameLabel = new Label("Name: " + workout_name);
            Label descriptionLabel = new Label("Description: " + wk_description);
            Label intensityLabel = new Label("Intensity: " + wk_intensity);
           // Label coachidLabel = new Label("CoachID: " + coach_id);

            // Add components to eventBox
            eventBox.getChildren().addAll(
                    nameLabel, descriptionLabel, intensityLabel,ratingBox, videoPlayerBox
            );

            return eventBox;
        }



    void handleFeedback(int id_workout, boolean isLike) {
        try {
            FeedbackWorkout feedback = new FeedbackWorkout();
            if (isLike) {
                bs.incrementLikeCount(id_workout);
                showAlert("Liked", "Liked workout with ID: " + id_workout, Alert.AlertType.INFORMATION);
            } else {
                bs.incrementDislikeCount(id_workout);
                showAlert("Disliked", "Disliked workout with ID: " + id_workout, Alert.AlertType.INFORMATION);
            }
            int authenticatedUserId = SessionManager.getInstance().getAuthenticatedUserId();
            feedback.setUserid(authenticatedUserId); // Set the user ID to 1
            feedback.setId_workout(id_workout); // Set the workout ID
            fs.saveFeedback(feedback);

            // After updating the counts, refresh the UI to reflect the changes

                displayWorkouts(id_category);

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
        } catch (SQLException e) {
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
            Parent root= FXMLLoader.load(getClass().getResource("/ShowCategoryCoach.fxml"));
        scrollpane.getScene().setRoot(root);
            System.out.println("List Event Page");

    }

}
