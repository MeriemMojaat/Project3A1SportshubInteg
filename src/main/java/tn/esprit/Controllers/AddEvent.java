package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import tn.esprit.Entities.Event;
import tn.esprit.Entities.admin;
import tn.esprit.services.EventService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddEvent {


    @FXML
    private RadioButton virtualEventRadioBtn;
    @FXML
    private RadioButton nonVirtualEventRadioBtn;
    @FXML
    private TextField localisation_event_ID;
    @FXML
    private ComboBox<String> GenderEvent;

    @FXML
    private ComboBox<String> TypeEvent;

    @FXML
    private DatePicker endDate_event_id;

    @FXML
    private TextField name_event_id;

    @FXML
    private DatePicker startDate_event_id;
    @FXML
    private TextArea description_eventID;
    @FXML
    private TextField price_id;
    private final EventService es = new EventService();
    @FXML
    private WebView webview;


    @FXML
    void initialize()   {
        // Populate Gender ComboBox
        GenderEvent.getItems().addAll("Female", "Male", "Both");

        // Populate Type ComboBox
        TypeEvent.getItems().addAll("Running", "Cycling", "Hiking", "Dance Oriental", "QuizzSport", "Online Chess");


        TypeEvent.valueProperty().addListener((observable, oldValue, newValue) -> {
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
            } else {
                try {
                    // Check if the event name already exists
                    if (es.isEventExists(newValue)) {
                        showAlert("An event with the same name already exists.");
                        name_event_id.setText(oldValue);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Failed to check event name existence.");
                }
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


        // Add a listener to the localisation_event_ID TextField
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
    void AddEvent(ActionEvent ev) throws SQLException {
        String gender = GenderEvent.getValue();
        LocalDate startDate = startDate_event_id.getValue();
        LocalDate endDate = endDate_event_id.getValue();
        String type = TypeEvent.getValue();
        String localization = localisation_event_ID.getText();
        String description = description_eventID.getText();
        Float price = Float.valueOf(price_id.getText());
        String name= name_event_id.getText();

        String Space = virtualEventRadioBtn.isSelected() ? "Virtual" : (nonVirtualEventRadioBtn.isSelected() ? "Non-Virtual" : null);

        // Validate inputs
        if (gender == null || startDate == null || endDate == null || type == null || localization == null || description.isEmpty() || Space == null) {
            showAlert("Please fill in all fields.");
            return;
        }

        // Check if price is valid
        if (price <= 0) {
            showAlert("Please enter a valid price.");
            return;
        }
        int authenticatedAdminId = AdminSessionManager.getInstance().getAuthenticatedAdminId();
        System.out.println(authenticatedAdminId);
        // Then add event
        es.add(new Event(name,type,Space,gender,startDate,endDate,localization,description,price,authenticatedAdminId));
        showAlert("Event added successfully!");
        clearFields();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void ViewListEvents(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/ShowEventAdmin.fxml"));
        name_event_id.getScene().setRoot(root);
        System.out.println("List Event Page");
    }
    private void clearFields() {
        virtualEventRadioBtn.setSelected(false);
        nonVirtualEventRadioBtn.setSelected(false);
        localisation_event_ID.clear();
        GenderEvent.setValue(null);
        TypeEvent.setValue(null);
        endDate_event_id.setValue(null);
        name_event_id.clear();
        startDate_event_id.setValue(null);
        description_eventID.clear();
        price_id.clear();
    }

}
