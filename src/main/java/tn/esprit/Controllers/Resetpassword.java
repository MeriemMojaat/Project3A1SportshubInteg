package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import tn.esprit.entities.user;
import tn.esprit.services.emailservice;
import tn.esprit.services.userservices;
import tn.esprit.services.verifcode;

import java.sql.SQLException;

public class Resetpassword {

    @FXML
    private Button continueButton;

    @FXML
    private TextField emailFieldId;
    @FXML
    private Text emailAlertId;

    @FXML
    private Text emailTextId;

    @FXML
    private Text resetPwdTextId;

    @FXML
    private TextField txtcodeverif;

    @FXML
    private TextField txtnouveaumdp;

 userservices  us = new userservices();
emailservice em =new emailservice();
verifcode vcg=new verifcode();
    @FXML
    void HoverIn(MouseEvent event) {
        continueButton.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverOut(MouseEvent event) {
        continueButton.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
    }

    @FXML
   public void changerMdp(ActionEvent event) {

        try {
            String email = emailFieldId.getText();
            String verificationCode = txtcodeverif.getText();
            String newPassword = txtnouveaumdp.getText();

            // Check if the email exists
            if (!us.doesEmailExist(email)) {
                showErrorAlert("Error", "No account is associated with this e-mail address.");
                return;
            }

            int userId = us.getUidByEmail(email);
            user user = us.searchByUid(userId);

            // Check if the entered verification code is correct
            if (user.getVerifcode() == null || !verificationCode.trim().equals(user.getVerifcode().trim())) {
                System.out.println("Database Verification Code: " + user.getVerifcode());
                System.out.println("Entered Verification Code: " + verificationCode);
                showErrorAlert("Error", "Invalid verification code. Please check your code and try again.");
                return;
            }

            // Update the user's password
            us.updatePassword(userId, newPassword);

            // Clear the verification code in the database
            us.updateVerificationCode(userId, null);

            // Display success alert
            showInformationAlert("Success", "Password has been successfully updated.");

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error", "An error occurred while updating the password.");
        }
    }

    @FXML
    void redirectToVerif(ActionEvent event) {


        try {
            if (!us.doesEmailExist(emailFieldId.getText())) {
                emailAlertId.setText("No account is associated with this e-mail address.");
                emailAlertId.setStyle("-fx-background-color: #ff4d4d;");
            } else {
                int userId = us.getUidByEmail(emailFieldId.getText());

                // Generate a new verification code
                String verificationCode = verifcode.generateVerificationCode(us.searchByUid(userId));

                // Update the verification code in the database
                us.updateVerificationCode(userId, verificationCode);

                // Envoi de l'e-mail avec le code de vérification
                em.sendVerificationEmail(us.getEmailByUserId(userId), verificationCode);

                showInformationAlert("Confirmation", "A verification code has been sent to the provided e-mail address.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void showInformationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
