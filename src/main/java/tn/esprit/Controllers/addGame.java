package tn.esprit.Controllers;

import com.modernmt.text.profanity.ProfanityFilter;
import com.modernmt.text.profanity.dictionary.Profanity;
import javafx.application.Platform;
import javafx.scene.control.*;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import tn.esprit.Entities.Game;
import tn.esprit.Entities.GameList;
import tn.esprit.services.GLservice;
import tn.esprit.services.Gservice;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class addGame {

    private ProfanityFilter profanityFilter = new ProfanityFilter();
    @FXML
    private DatePicker DATE_G;

    @FXML
    private Button addButton;

    @FXML
    private DatePicker DATE_EG;

    @FXML
    private TextField GAME_NAME;

    @FXML
    private ComboBox<String> creatorName;

    @FXML
    private ImageView image;

    @FXML
    private TextField NBR_PAR;

    @FXML
    private ComboBox<String> TYPE;

    @FXML
    private ComboBox<String> GPLACE;

    GLservice gls = new GLservice();
    Gservice gs = new Gservice();


    int USER_ID ;

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    // Method to initialize the controller
    public void initialize() {

        GAME_NAME.textProperty().addListener((observable, oldValue, newValue) -> {
            // Call the findAndReplaceProfanity method to detect and replace profanity in TextArea
            String   text= findProfanity(newValue);
            deleteString(newValue,text);
        });
       // NBR_PAR.setTextFormatter(textFormatter);
       // populateUserNames();
        // input control (game name)

        int maxGameNameLength = 15; // Set your desired maximum length here
        GAME_NAME.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > maxGameNameLength) {
                GAME_NAME.setText(oldValue);
            }
        });


       TYPE.getItems().addAll("Football", "Basketball", "Rugby" , "Tennis" , "Vollyball");
        GPLACE.getItems().addAll("Ariana", "Tunis", "Djerba" , "Gabes" , "Sousse");

    }

   /* private void populateUserNames() {
        try {
            List<String> userNames = gs.getUserNames();
            creatorName.getItems().addAll(userNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    */

    @FXML
    void add(ActionEvent event) throws IOException {
        try {
            /*String username = creatorName.getValue();
            int creatorid = gs.getUserIdByName(username);

             */

            int creatorid = USER_ID;

            // input control (date)

            LocalDate currentDate = LocalDate.now();
            LocalDate enteredDate = DATE_G.getValue();
            if (enteredDate != null && enteredDate.isBefore(currentDate)) {
                showErrorAlert("Invalid date. Please enter a valid date.");
                return;
            }

            String gameName = GAME_NAME.getText();
            String type = TYPE.getValue();
            String gplace = GPLACE.getValue();

            int nbrPar = Integer.parseInt(NBR_PAR.getText());
            // Validate input fields
            if ( gameName.isEmpty() || type.isEmpty() || gplace.isEmpty()) {
                showErrorAlert("Please fill in all the fields.");
                return;
            }

            GAME_NAME.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.matches(".*[!@#$%^&*()_+={}\\[\\]:;\"'|\\\\<>,.?/~`].*")) {
                    showAlert("Name Event cannot contain symbols.");
                    GAME_NAME.setText(oldValue);
                } else if (newValue.length() > 10) {
                    showAlert("Name Event cannot exceed 10 characters.");
                    GAME_NAME.setText(oldValue);
                } else {
                    // Check if the event name already exists
                    if (gs.isGameExists(newValue)) {
                        showAlert("An event with the same name already exists.");
                        GAME_NAME.setText(oldValue);
                    }
                }
            });
            DATE_EG.setDayCellFactory(picker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate startDate = DATE_G.getValue();
                    setDisable(startDate != null && date.isBefore(startDate));
                }
            });
            DATE_G.setDayCellFactory(picker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(date.isBefore(LocalDate.now()));
                }
            });

            // Create a new game object
            Game newGame = new Game(creatorid,gameName, nbrPar, type, DATE_G.getValue(),DATE_EG.getValue(), gplace);
            int userid = creatorid;
            System.out.println(userid);

            // Add the game
            gs.add(newGame);
            int gameid = gls.getGameIdByName(gameName);
            System.out.println(gameid);


            GameList gameList = new GameList(gameid , userid);
            gls.add(gameList);


           // showNotification("le pinn piiiiiin"
           // ,"nhebha maaha");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowGameList.fxml"));
            Parent root = loader.load();

            ShowGameList showGameListcontroller = loader.getController();
            showGameListcontroller.setUserId(userid);
            showGameListcontroller.displayGame(userid);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();



            // Set the loaded FXML as the root of the current scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show()
            ;


            // Show success message
            showSuccessAlert("A new game has been added successfully.");
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid input. Please enter valid numeric values.");
        } catch (SQLException e) {
            showErrorAlert("Error while adding the game: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }
    private String findProfanity(String text) {
        String text1="";
        // Test if the string contains profanity
        boolean containsProfanityEn = profanityFilter.test("en", text);
        boolean containsProfanityFr = profanityFilter.test("fr", text);

        // Check if profanity is found in any language
        if (containsProfanityEn || containsProfanityFr) {
            // Show an alert indicating profanity is found
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Profanity Detected");
            alert.setHeaderText("Profanity has been detected in the text.");
            alert.setContentText("Please refrain from using offensive language.");
            alert.showAndWait();
            Profanity profanityen=profanityFilter.find("en",text);
            Profanity profanityfr=profanityFilter.find("fr",text);
            if(containsProfanityEn)
                text1=profanityen.text();
            else {
                text1=profanityfr.text();
            }
        }
        return text1;

    }
    private void deleteString(String text,String stringToDelete) {
        // Check if the new text contains the string to delete
        if (text.contains(stringToDelete)) {
            // Delete the string from the text
            Platform.runLater(() -> {
                // Calculate start and end indices of the string to delete
                int startIndex = text.indexOf(stringToDelete);
                int endIndex = startIndex + stringToDelete.length();
                // Update the text area content with the string deleted
                GAME_NAME.replaceText(startIndex, endIndex, "");
                // Move the caret position to the end of the text area
                GAME_NAME.positionCaret(GAME_NAME.getText().length());
            });
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void addEventFilter(KeyEvent keyEvent) {
    }

    @FXML
    void ShowGameUI(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/GameUI.fxml"));
        GAME_NAME.getScene().setRoot(root);
        System.out.println("Game UI");
    }

    private Stage stage; // Reference to the stage

    // Method to set the stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void showNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .owner(stage) // Set the owner window
                .showInformation();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
}



