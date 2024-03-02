package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import tn.esprit.Entities.workouts;
import tn.esprit.services.workoutcategoryService;
import tn.esprit.services.workoutsService;
import tn.esprit.services.videoService;
import javafx.scene.media.MediaView;
import javafx.scene.media.*;



import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class AddWorkout {



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


    @FXML
    private TextArea workoutdescription;

    @FXML
    private TextField workoutimage;
    @FXML
    private TextField workoutname;


    private int eventId;
    public void setEventId(int eventId) {
        this.categoryId = eventId;

    }
    private int categoryId;

    private String videoPath;
    /*@FXML
    private ImageView videoThumbnail;*/
    @FXML
    private MediaView videoThumbnail;
    @FXML
    private Text videoTitle;

    private workoutsService workoutsService = new workoutsService();
    private workoutcategoryService wc = new workoutcategoryService();
    private int currentCategoryId; // This variable holds the ID of the current category

    private final videoService videoService = new videoService();
    @FXML
    void uploadVideo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Video File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mov"));
        File selectedFile = fileChooser.showOpenDialog(videoThumbnail.getScene().getWindow());
        if (selectedFile != null) {
            // Retrieve the selected video file's path and assign it to the class-level variable videoPath
            this.videoPath = selectedFile.getAbsolutePath();
            System.out.println("Selected video file: " + this.videoPath);

            // Create a Media object from the selected file path
            Media media = new Media(selectedFile.toURI().toString());


            // Create a MediaPlayer from the Media object
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            // Bind the MediaPlayer to the MediaView
            videoThumbnail.setMediaPlayer(mediaPlayer);

            // Set the dimensions of the MediaView (optional)
            videoThumbnail.setFitWidth(400); // Set your desired width
            videoThumbnail.setFitHeight(300); // Set your desired height

            // Upload the video to the cloud and get the cloudinary URL
            String cloudinaryUrl = videoService.uploadVideo(videoPath);

            // Now you can use the cloudinaryUrl as needed
            System.out.println("Cloudinary URL: " + cloudinaryUrl);

            // If you need to store the cloudinaryUrl in your workoutsService or perform any other actions, do it here

        }
    }

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
        this.categoryId = id_category;
    }
    private void populateCoachNames() {
        try {
            List<String> userNames = workoutsService.getCoachNames();
            CoachComboBox.getItems().addAll(userNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    private void initialize() {
    }
    @FXML
    void AddWorkout(ActionEvent event) {
        try {
            workoutsService workoutsService = new workoutsService();
            workouts newWorkout = new workouts();


            // Upload video to a cloud service (e.g., Cloudinary) and get the URL
            String cloudinaryUrl = videoService.uploadVideo(videoPath);

            // Set the video path in the workoutsService
            newWorkout.setWk_image(cloudinaryUrl);

            String intensity = EasyRadioBtn.isSelected() ? "Easy" : (MediumRadioBtn.isSelected() ? "Medium" : HardRadioBtn.isSelected() ? "Easy" :null);

            workouts newBooking = new workouts(workoutname.getText(), workoutdescription.getText(),intensity,cloudinaryUrl,categoryId);
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