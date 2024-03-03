package tn.esprit.Controllers;

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
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import tn.esprit.Entities.Event;
import tn.esprit.Controllers.Login;
import tn.esprit.Entities.Feedback;
import tn.esprit.services.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


public class ShowEventUser {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField SearchEventId;
    @FXML
    private ComboBox<String> combosortEvent;
    private final EventService eventService = new EventService();
    FeedbackDao feedbackDao = new FeedbackDaoImpl();
    FeedbackService feedbackService = new FeedbackService(feedbackDao);
    BookingService bookingService=new BookingService();

    @FXML
    void initialize() {

        ObservableList<String> sortingOptions = FXCollections.observableArrayList(
                "Start Date", "Event Name", "Virtuality"
        );
        combosortEvent.setItems(sortingOptions);
        displayEvents();
        SearchEventId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                displayEvents();
            } else {
                SearchEvent(null);
            }
        });
    }




    private void displayEvents() {
        try {
            List<Event> events = eventService.display();

            GridPane gridPane = new GridPane();
            gridPane.setHgap(20);
            gridPane.setVgap(20);
            gridPane.setPadding(new Insets(20));

            int row = 0;
            int column = 0;
            for (Event event : events) {
                HBox hbox = new HBox();
                hbox.getChildren().add(createEventBox(event));
                gridPane.add(hbox, column, row);

                column++;
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }

            scrollPane.setContent(gridPane);
        } catch (SQLException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private VBox createEventBox(Event eventt) {
        VBox eventBox = new VBox();
        eventBox.setStyle("-fx-background-color: #AED6F1; -fx-border-color: #94cdf5; -fx-padding: 20px; -fx-spacing: 10px;");
        eventBox.setSpacing(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate_event = formatter.format(eventt.getStartDate_event());
        String endDate_event = formatter.format(eventt.getEndDate_event());
        String name_event = eventt.getName_event();
        String type_event = eventt.getType_event();
        String virtuality = eventt.getSpace();
        String gender_event = eventt.getGender_event();
        String localisation_event = eventt.getLocalisation_event();
        String description_event = eventt.getDescription_event();
        Float price = eventt.getPrice();

        // Create delete button
        eventBox.getProperties().put("id_event", eventt.getId_event());

        Button addBookingButton = new Button("Add Booking");
        addBookingButton.setOnAction(event -> handleAddBooking(event, eventt.getId_event()));

        HBox bookingButtonBox = new HBox(addBookingButton);
        bookingButtonBox.setAlignment(Pos.CENTER_RIGHT);

        Button submitFeedbackButton = new Button("Submit Feedback");
        submitFeedbackButton.setOnAction(event -> handleFeedbackSubmission(event, eventt.getId_event()));

        Button showFeedbackButton = new Button("Show Feedback");
        showFeedbackButton.setOnAction(event -> displayFeedbackForEvent(eventt.getId_event()));

        HBox showFeedbackButtonBox = new HBox(showFeedbackButton);
        showFeedbackButtonBox.setAlignment(Pos.CENTER_RIGHT);

        // Create labels for the event details
        Label nameLabel = new Label("Name: " + name_event);
        Label typeLabel = new Label("Type: " + type_event);
        Label virtualityLabel = new Label("Space: " + virtuality);
        Label genderLabel = new Label("Gender: " + gender_event);
        Label startDateLabel = new Label("Start Date: " + startDate_event);
        Label endDateLabel = new Label("End Date: " + endDate_event);
        Label locationLabel = new Label("Location: " + localisation_event);
        Label descriptionLabel = new Label("Description: " + description_event);
        Label pricelabel = new Label("Price: " + price);

        eventBox.getChildren().addAll(
                nameLabel, typeLabel, virtualityLabel, genderLabel,
                startDateLabel, endDateLabel, locationLabel,
                descriptionLabel,pricelabel, bookingButtonBox,submitFeedbackButton,showFeedbackButton
        );

        return eventBox;
    }


    public void displayFeedbackForEvent(int eventId) {
        System.out.println("Displaying feedback for event ID: " + eventId);

        // Retrieve feedback for the event
        List<Feedback> feedbackList = feedbackDao.getFeedbackForEvent(eventId);
        System.out.println("Retrieved feedback entries: " + feedbackList.size());

        // Create a dialog to display the feedback
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Feedback for Event");

        VBox dialogPaneContent = new VBox();

        // Loop through the feedback list and create a HBox for each feedback
        for (Feedback feedback : feedbackList) {
            HBox feedbackEntry = new HBox();
            feedbackEntry.setSpacing(10);

            // Create a Label for the rating displayed in stars
            Label ratingLabel = new Label();
            ratingLabel.setGraphic(createRatingStars(feedback.getRating()));

            // Retrieve the username associated with the user ID
            String username = null;
            try {
                username = eventService.getUserNameById(feedback.getUserid());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Create a Label for the username
            Label usernameLabel = new Label("User: " + username);

            // Add components to the feedback entry
            feedbackEntry.getChildren().addAll(ratingLabel, usernameLabel);

            // Add the feedback entry to the dialog content
            dialogPaneContent.getChildren().add(feedbackEntry);
        }

        // Set the content of the dialog to the VBox containing feedback entries
        alert.getDialogPane().setContent(dialogPaneContent);

        // Add an OK button to the dialog if not already added
        if (!alert.getButtonTypes().contains(ButtonType.OK)) {
            alert.getButtonTypes().add(ButtonType.OK);
        }

        // Show the dialog and wait for user response
        alert.showAndWait();
    }

    // Helper method to create a star rating control
    private Rating createRatingStars(int rating) {
        Rating ratingStars = new Rating();
        ratingStars.setRating(rating);
        ratingStars.setDisable(true); // Make it read-only
        return ratingStars;
    }



    private void handleFeedbackSubmission(ActionEvent event, int eventId) {
        try {
            // Retrieve the list of usernames associated with user IDs from the database
            List<String> usernames = feedbackDao.getUsernamesForFeedback();

            // Create a dialog for feedback submission
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Submit Feedback");
            dialog.setHeaderText("Please provide your feedback for this event:");

            // Create star rating control
            Rating starRatingControl = new Rating();

            // Create ComboBox for usernames


            // Create layout for dialog content
            VBox content = new VBox();
            content.getChildren().addAll(starRatingControl);
            dialog.getDialogPane().setContent(content);

            // Add OK and Cancel buttons
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Wait for user response
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Retrieve rating from the star rating control
                int rating = (int) starRatingControl.getRating();

                // Retrieve selected username from the ComboBox

                int authenticatedUserId = SessionManager.getInstance().getAuthenticatedUserId();



                // Create a Feedback object
                Feedback feedback = new Feedback();
                feedback.setId_event(eventId); // Set the event ID
                feedback.setRating(rating); // Set the rating
                feedback.setUserid(authenticatedUserId); // Set the user ID

                // Submit feedback using FeedbackService
                feedbackService.submitFeedback(feedback);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    private void handleAddBooking(ActionEvent event, int eventId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddBooking.fxml"));

            Parent root = loader.load();

            AddBooking addBookingController = loader.getController();
            addBookingController.setEventId(eventId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load AddBooking page", Alert.AlertType.ERROR);
        }
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void sortTableView(ActionEvent event) {
        try {
            String selectedSortOption = combosortEvent.getValue();
            List<Event> sortedEvents = null;

            switch (selectedSortOption) {
                case "Start Date":
                    sortedEvents = eventService.displaySorted("startDate");
                    break;
                case "Event Name":
                    sortedEvents = eventService.displaySorted("event_name");
                    break;
                case "Virtuality":
                    sortedEvents = eventService.displaySorted("Space");
                    break;
                default:
                    break;
            }

            updateVBoxEvents(sortedEvents);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void updateVBoxEvents(List<Event> events) {
        // Clear existing content in the scroll pane
        scrollPane.setContent(null);

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
        for (Event event : events) {
            // Create event box for the current event
            VBox eventBox = createEventBox(event);

            // Add the event box to the current row HBox
            currentRowHBox.getChildren().add(eventBox);

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
        scrollPane.setContent(vBox);
    }


    @FXML
    void SearchEvent(ActionEvent event) {
        try {
            String searchCriteria = SearchEventId.getText();
            if (searchCriteria.isEmpty()) {
                displayEvents(); // Revert to original state if search field is empty
            } else {
                List<Event> events = eventService.searchEvents(searchCriteria);
                updateVBoxEvents(events); // Update the UI with search results
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


}
