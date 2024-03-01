package tn.esprit.controllers;

import com.modernmt.text.profanity.ProfanityFilter;
import com.modernmt.text.profanity.dictionary.Profanity;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import tn.esprit.entities.product;
import tn.esprit.services.productservice;

import java.io.IOException;
import java.sql.SQLException;



public class addproduct {
  private   final productservice ps = new productservice();


    @FXML
    private TextField IMAGE;

  @FXML
  private RadioButton balls;

  @FXML
  private RadioButton bicycles;

  @FXML
  private RadioButton clothes;

  @FXML
  private RadioButton gym;

  @FXML
  private RadioButton swimming;

  @FXML
  private RadioButton New;

  @FXML
  private RadioButton used;



  @FXML
  private TextArea DESCRIPTION;

    @FXML
    private TextField QUANTITY;

    @FXML
    private TextField STATE;

  private ProfanityFilter profanityFilter = new ProfanityFilter();


  @FXML
  private void initialize(){
    DESCRIPTION.textProperty().addListener((observable, oldValue, newValue) -> {
      // Call the findAndReplaceProfanity method to detect and replace profanity in TextArea
      String   text= findProfanity(newValue);
      deleteString(newValue,text);
    });

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
        DESCRIPTION.replaceText(startIndex, endIndex, "");
        // Move the caret position to the end of the text area
        DESCRIPTION.positionCaret( DESCRIPTION.getText().length());
      });
    }
  }
  @FXML
  void back(ActionEvent event)  throws IOException {
    Parent root= FXMLLoader.load(getClass().getResource("/showproduct.fxml"));
    DESCRIPTION.getScene().setRoot(root);
    System.out.println("List product Page");

  }


    @FXML
    void add(ActionEvent event) {
      try {
        int quantity = Integer.parseInt(QUANTITY.getText());
        String type = balls.isSelected() ? "balls" : (bicycles.isSelected() ? "bicycles" : clothes.isSelected() ? "clothes"  : swimming.isSelected() ? "swimming" : gym.isSelected() ? "gym":null);
        String state = New.isSelected() ? "new" : (used.isSelected() ? "used" :null);
        ps.add(new product(type,DESCRIPTION.getText(),IMAGE.getText(), state,quantity));
        showNotification("Success", "You have added a product. Be ready for any trade from the other users.");
      } catch (NumberFormatException e) {
        // Handle the case where the text is not a valid integer
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setContentText("ID_USER must be a valid integer.");
        alert.showAndWait();

    } catch (SQLException e) {
          throw new RuntimeException(e);
      }

    }
  private Stage stage ;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void showNotification(String title, String message) {
    Notifications.create()
            .title(title)
            .text(message)
            .owner(stage)
            .showInformation();// Set the owner window .showInformation();
  }
}


