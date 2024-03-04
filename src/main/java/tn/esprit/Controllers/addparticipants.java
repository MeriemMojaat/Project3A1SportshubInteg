package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import tn.esprit.Entities.GameList;
import tn.esprit.services.GLservice;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class addparticipants {

    private int gameId; // Event ID passed from ShowEvent


    public void setgameId(int gameId) {
        this.gameId = gameId;
    }

    private int userId;

    public void setuserId(int userId){
        this.userId = userId;
    }
    @FXML
    private Button GameUI;

    @FXML
    private Button ViewGame;

    @FXML
    private Button ViewParticipants;

    @FXML
    private ComboBox<String> userName;

    @FXML
    private ComboBox<String> gameName;

    private final GLservice gls= new GLservice();

    private static final String USERNAME = "ttelacontact@gmail.com";
    private static final String PASSWORD = "umemwindhcrlkjvi"; // Replace with your SMTP server password


    @FXML
    private void initialize() {
        populateUserNames();
    }

    private void populateUserNames() {
        try {
            List<String> userNames = gls.getUserNames();
            userName.getItems().addAll(userNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   /* private void populateGameName() {
        try {
            List<String> gameNames = gls.getGameNames();
            gameName.getItems().addAll(gameNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    */
    @FXML
    void add(ActionEvent event) throws SQLException {
        try {

           // String gamename = gameName.getValue();
            int gameid = gameId;



            if (gameId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid game selected.", "Please select a valid game.");
                return;
            }
            String username = userName.getValue();
            String email = gls.getemailByName(username);
            int userId = gls.getUserIdByName(username);
            GameList newGamelist = new GameList(gameId, userId);
            gls.add(newGamelist);
            clearfields();
            showAlert(Alert.AlertType.INFORMATION, "Confirmation", "A new participant is added", null);
            System.out.println(email);
            System.out.println(email);
            System.out.println(email);

            sendEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add booking.", e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input", "Please enter valid values for participants.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendEmail(String recipientEmail) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);

            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Subject of the Email");
            message.setText("This is the content of your email.");

            Transport.send(message);
            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            throw new MessagingException("Failed to send email: " + e.getMessage());
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    void clearfields()
    {
        userName.setValue(null);
        //gameName.setValue(null);
    }

    @FXML
    void ViewGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ShowGameList.fxml"));
        userName.getScene().setRoot(root);
        System.out.println("add participants");
    }

    @FXML
    void ViewGameUI(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/GameUI.fxml"));
        userName.getScene().setRoot(root);
        System.out.println("show participants");
    }

    @FXML
    void ViewParticipants(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Showparticipants.fxml"));
        userName.getScene().setRoot(root);
        System.out.println("show participants");
    }


}


