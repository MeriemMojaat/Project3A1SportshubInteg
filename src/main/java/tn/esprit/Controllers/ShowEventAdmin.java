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
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.Entities.Booking;
import tn.esprit.Entities.Event;
import tn.esprit.services.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


public class ShowEventAdmin {

    @FXML
    private ScrollPane scrollPane;
    private final EventService eventService = new EventService();
    BookingService bookingService=new BookingService();

    @FXML
    void initialize() {displayEvents();}

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
        int adminid= eventt.getadminid();
        // Create delete button
        eventBox.getProperties().put("id_event", eventt.getId_event());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> handleDeleteEvent(event));

        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> handleUpdateEvent(event, eventt.getId_event()));

        HBox buttonBox = new HBox(deleteButton, updateButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button participantsButton = new Button("Show Participants");
        participantsButton.setOnAction(event -> showParticipantsCount(event, eventt.getId_event()));


        HBox participantsButtonBox = new HBox(participantsButton);
        participantsButtonBox.setAlignment(Pos.CENTER_RIGHT);

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
                descriptionLabel,pricelabel,buttonBox,participantsButtonBox
        );

        return eventBox;
    }
    private void showParticipantsCount(ActionEvent event, int eventId) {
        try {
            // Get all bookings for the event
            List<Booking> bookings = bookingService.getBookingsForEvent(eventId);

            // Create a StringBuilder to store the booking information
            StringBuilder bookingInfo = new StringBuilder();

            // Initialize total number of participants
            int totalParticipants = 0;

            // Loop through each booking and append booking information to the StringBuilder
            for (Booking booking : bookings) {
                // Retrieve the username associated with the user ID
                String username = eventService.getUserNameById(booking.getUserid());

                // Append username and number of participants for this booking
                bookingInfo.append("Username: ").append(username)
                        .append(", Number of Participants: ").append(booking.getNbParticipants_event())
                        .append("\n");

                // Add number of participants to total
                totalParticipants += booking.getNbParticipants_event();
            }

            // Display the booking information and total number of participants in a dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Participants Information");
            alert.setHeaderText(null);
            alert.setContentText("Bookings for Event ID " + eventId + ":\n\n" + bookingInfo.toString() +
                    "\nTotal number of participants: " + totalParticipants);
            alert.showAndWait();
        } catch (SQLException e) {
            // Handle any SQL exception
            e.printStackTrace();
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void handleUpdateEvent(ActionEvent event, int eventId) {
        try {
            Event eventt = eventService.getEventById(eventId);
            if (eventt == null) {
                // Handle the case where the event for the event ID is not found
                showAlert("Error", "Event not found for ID: " + eventId, Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvent.fxml"));
            Parent root = loader.load();

            UpdateEvent updateEventController = loader.getController();
            updateEventController.setEventData(eventt);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load UpdateEvent page", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void handleDeleteEvent(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();

            // Get the parent of the button (HBox)
            Parent parent = clickedButton.getParent();

            // Traverse the parent hierarchy until finding the VBox containing the event details
            while (parent != null && !(parent instanceof VBox)) {
                parent = parent.getParent();
            }
            if (parent instanceof VBox) {
                VBox eventBox = (VBox) parent;
                // Retrieve the id_event from the properties map of the VBox
                Integer id_event = (Integer) eventBox.getProperties().get("id_event");
                if (id_event != null) {
                    try {
                        // Call your EventService delete method with the id_event
                        eventService.delete(id_event);

                        // Remove the VBox from its parent (Scrollpane)
                        scrollPane.setContent(null);
                        displayEvents(); // Refresh the event list
                    } catch (SQLException e) {
                        showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            }
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
    void GoToAddEventPage(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/AddEvent.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scrollPane.getScene().setRoot(root);
        System.out.println("Next");
    }
    @FXML
    void GoToAddbookingPage(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ShowBooking.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scrollPane.getScene().setRoot(root);
        System.out.println("Next");
    }

    @FXML
    void StatisticsGenderEvent(ActionEvent event) {
        try {
            // Calculate gender statistics
            EventService eventService = new EventService();
            int maleCount = eventService.getGenderCount("male");
            int femaleCount = eventService.getGenderCount("female");
            int  bothCount = eventService.getGenderCount("both");

            // Create a PieChart with gender statistics
            PieChart pieChart = new PieChart();
            pieChart.getData().add(new PieChart.Data("Male", maleCount));
            pieChart.getData().add(new PieChart.Data("Female", femaleCount));
            pieChart.getData().add(new PieChart.Data("Both", bothCount));
            pieChart.setTitle("Gender Statistics");

            // Create a VBox to contain the PieChart
            VBox vBox = new VBox(pieChart);
            vBox.setPrefWidth(400);
            vBox.setPrefHeight(300);

            // Create a dialog to display the PieChart
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(vBox);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.setTitle("Gender Statistics");
            dialog.setHeaderText(null);

            // Show the dialog and wait for user response
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                dialog.close();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
