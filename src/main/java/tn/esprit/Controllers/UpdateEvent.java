package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import tn.esprit.entities.Event;
import tn.esprit.services.EventService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateEvent {
    private Event eventToUpdate; // Store the event to be updated

    @FXML
    private RadioButton virtualEventRadioBtn;
    @FXML
    private RadioButton nonVirtualEventRadioBtn;
    @FXML
    private TextArea descriptionID;

    @FXML
    private DatePicker endDate_event_id;

    @FXML
    private ComboBox<String> gender_event_id;

    @FXML
    private TextField localisation_event_ID;

    @FXML
    private TextField name_event_id;

    @FXML
    private DatePicker startDate_event_id;

    @FXML
    private ComboBox<String> type_event_id;
    @FXML
    private TextField price_id;
    @FXML
    private WebView webview;

    private final EventService es = new EventService();
    @FXML
    void initialize(){
        gender_event_id.getItems().addAll("Female", "Male", "Both");

        // Populate Type ComboBox
        type_event_id.getItems().addAll("Running", "Cycling", "Hiking", "Dance Oriental", "QuizzSport", "Online Chess");


        type_event_id.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.equals("QuizzSport")) {
                // Set virtualEventRadioBtn and localisation_event_ID to "Virtual"
                virtualEventRadioBtn.setSelected(true);
                nonVirtualEventRadioBtn.setSelected(false);

            } else {
                // Set nonVirtualEventRadioBtn
                virtualEventRadioBtn.setSelected(false);
                nonVirtualEventRadioBtn.setSelected(true);

            }
        });

        // Add listener to name_event_id TextField
        name_event_id.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches(".*[!@#$%^&*()_+={}\\[\\]:;\"'|\\\\<>,.?/~`].*")) {
                showAlert("Name Event cannot contain symbols.");
                name_event_id.setText(oldValue);
            } else if (newValue.length() > 10) {
                showAlert("Name Event cannot exceed 10 characters.");
                name_event_id.setText(oldValue);
            }

        });

        endDate_event_id.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate startDate = startDate_event_id.getValue();
                setDisable(startDate != null && date.isBefore(startDate));
            }
        });
        startDate_event_id.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now()));
            }
        });
        WebEngine webEngine = webview.getEngine();
        webEngine.setJavaScriptEnabled(true);
        localisation_event_ID.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    // Encode the location string to be used in a URL
                    String encodedLocation = URLEncoder.encode(newValue, StandardCharsets.UTF_8.toString());

                    // Append the location to the Google Maps URL
                    String mapUrl = "https://www.google.com/maps?q=" + encodedLocation;

                    // Load the map with the location
                    webEngine.load(mapUrl);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        // Add a listener to the WebView's webEngine to capture location updates
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.startsWith("https://www.google.com/maps?q=")) {
                // Extract the location from the URL

                String searchedLocation = newValue.substring("https://www.google.com/maps?q=".length());

                // Update the localisation_event_ID TextField with the searched location
                localisation_event_ID.setText(searchedLocation);
            }
        });
    }

    @FXML
    void onLocalisationEventChange(ActionEvent event) {
        try {
            String location = localisation_event_ID.getText();
            if (location != null) {
                String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8.toString());
                String mapUrl = "https://www.google.com/maps?q=" + encodedLocation;
                WebEngine webEngine = webview.getEngine();
                webEngine.load(mapUrl);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    @FXML
    void ViewListEventPage(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/ShowEventAdmin.fxml"));
        name_event_id.getScene().setRoot(root);
        System.out.println("Previous");
    }


    @FXML
    void UpdateEvent(ActionEvent event) {
        try{
        // Retrieve updated data from the form fields
        String name = name_event_id.getText();
        String type = type_event_id.getValue();
        String Space = virtualEventRadioBtn.isSelected() ? "Virtual" : (nonVirtualEventRadioBtn.isSelected() ? "Non-Virtual" : null);

            String localization = localisation_event_ID.getText();

        String gender = gender_event_id.getValue();
        LocalDate startDate = startDate_event_id.getValue();
        LocalDate endDate = endDate_event_id.getValue();

        String description = descriptionID.getText();
        Float price = Float.valueOf(price_id.getText());

        // Update the event object with the new data
        eventToUpdate.setName_event(name);
        eventToUpdate.setType_event(type);
        eventToUpdate.setSpace(Space);
        eventToUpdate.setGender_event(gender);
        eventToUpdate.setStartDate_event(startDate);
        eventToUpdate.setEndDate_event(endDate);
        eventToUpdate.setLocalisation_event(localization);
        eventToUpdate.setDescription_event(description);
        eventToUpdate.setPrice(price);



        // Update the event in the database
        es.update(eventToUpdate);
        clearfields();
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

    @FXML
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setEventData(Event event) {
        this.eventToUpdate = event;
        name_event_id.setText(event.getName_event());
        type_event_id.setValue(event.getType_event());
        descriptionID.setText(event.getDescription_event());

        // Set the selected value for the gender ComboBox
        if (event.getGender_event() != null) {
            gender_event_id.setValue(event.getGender_event());
        }

        // Set the selected value for the localisation ComboBox
        if (event.getLocalisation_event() != null) {
            localisation_event_ID.setText(event.getLocalisation_event());
        }

        // Set the selected value for the start and end dates
        if (event.getStartDate_event() != null) {
            startDate_event_id.setValue(event.getStartDate_event());
        }
        if (event.getEndDate_event() != null) {
            endDate_event_id.setValue(event.getEndDate_event());
        }

        // Set the selected value for the Space RadioButtons
        if ("Virtual".equals(event.getSpace())) {
            virtualEventRadioBtn.setSelected(true);
        } else if ("Non-Virtual".equals(event.getSpace())) {
            nonVirtualEventRadioBtn.setSelected(true);
        }
        price_id.setText(String.valueOf(event.getPrice()));
    }
    void clearfields()
    {
        virtualEventRadioBtn.setSelected(false);
        nonVirtualEventRadioBtn.setSelected(false);
        localisation_event_ID.clear();
        gender_event_id.setValue(null);
        type_event_id.setValue(null);
        endDate_event_id.setValue(null);
        name_event_id.clear();
        startDate_event_id.setValue(null);
        descriptionID.clear();
        price_id.clear();
    }
}
