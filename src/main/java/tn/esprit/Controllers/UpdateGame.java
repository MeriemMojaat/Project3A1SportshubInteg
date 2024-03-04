package tn.esprit.Controllers;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.modernmt.text.profanity.ProfanityFilter;
import com.modernmt.text.profanity.dictionary.Profanity;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import tn.esprit.Entities.Game;
import tn.esprit.services.Gservice;

public class UpdateGame {

    private ProfanityFilter profanityFilter = new ProfanityFilter();
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker DATE_EG;

    @FXML
    private DatePicker DATE_G;

    @FXML
    private TextField GAME_NAME;

    @FXML
    private ComboBox<String> GPLACE;

    @FXML
    private TextField NBR_PAR;

    @FXML
    private ComboBox<String> TYPE;

    @FXML
    private ImageView image;

    @FXML
    private AnchorPane root;

    private Game gameToUpdate; // Store the gameid to be updated

    private final Gservice gs = new Gservice();

    @FXML
    void initialize() {
        GAME_NAME.textProperty().addListener((observable, oldValue, newValue) -> {
            // Call the findAndReplaceProfanity method to detect and replace profanity in TextArea
            String   text= findProfanity(newValue);
            deleteString(newValue,text);
        });
        TYPE.getItems().addAll("Football", "Basketball", "Rugby" , "Tennis" , "Vollyball");
        GPLACE.getItems().addAll("Ariana", "Tunis", "Djerba" , "Gabes");
    }

    @FXML
    void add(KeyEvent event) {

    }

    @FXML
    void addEventFilter(KeyEvent event) {

    }

    @FXML
    void updateGame(ActionEvent event) {
        try {
            // Retrieve updated data from the form fields
            String name = GAME_NAME.getText();
            String gPlace = GPLACE.getValue(); // Assuming GPLACE is a ComboBox
            String type = TYPE.getValue(); // Assuming TYPE is a ComboBox
            String nbrParText = NBR_PAR.getText();

            // Convert NBR_PAR to integer
            int nbrPar = Integer.parseInt(nbrParText);

            // Retrieve dates from DatePickers and convert them to java.sql.Date
            LocalDate localDateEG = DATE_EG.getValue();
            LocalDate localDateG = DATE_G.getValue();

            // Update the game object with the new data
            gameToUpdate.setGAME_NAME(name);
            gameToUpdate.setTYPE(type);
            gameToUpdate.setGPLACE(gPlace);
            gameToUpdate.setDATE_EG(localDateEG);
            gameToUpdate.setDATE_G(localDateG);
            gameToUpdate.setNBR_PAR(nbrPar);

            // Update the game in the database
            gs.update(gameToUpdate);
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Game updated successfully");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void setGameData(Game game) {
        this.gameToUpdate = game;
        GAME_NAME.setText(game.getGAME_NAME());
        TYPE.setValue(game.getTYPE()); // Assuming TYPE is a ComboBox
        GPLACE.setValue(game.getGPLACE()); // Assuming GPLACE is a ComboBox
        NBR_PAR.setText(String.valueOf(game.getNBR_PAR()));
        DATE_G.setValue(game.getDATE_G());
        DATE_EG.setValue(game.getDATE_EG());
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
    @FXML
    void viewGame(ActionEvent event) {

    }

    void clearfields() {
        // Clearing UI components
        GAME_NAME.clear();
        GPLACE.setValue(null); // Assuming GPLACE is a ComboBox
        TYPE.setValue(null); // Assuming TYPE is a ComboBox
        NBR_PAR.clear();
        DATE_G.setValue(null);
        DATE_EG.setValue(null);
    }


}
