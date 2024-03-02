package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import tn.esprit.entities.user;
import tn.esprit.services.userservices;

import javax.swing.text.html.ImageView;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
public class AddUser {

    @FXML
    private TextField nameuserid;

    @FXML
    private TextField uphonenumberid;

    @FXML
    private DatePicker userdateofbirthid;

    @FXML
    private TextField useremailid;

    @FXML
    private ComboBox<String> usergenderid;

    @FXML
    private TextField userpasswordid;
    @FXML
    private ImageView ImgField;

    @FXML
    private Button loginButton;

    private String filePath;



    @FXML
    private final userservices us = new userservices();




    @FXML
    void  initialize() {

        usergenderid.getItems().addAll("Male","Female");

    }
    @FXML
    void ADD(ActionEvent event) {
        try {
            String nameuser = nameuserid.getText();
            String uphonenumber = uphonenumberid.getText();
            String useremail = useremailid.getText();
            String userpassword = userpasswordid.getText();
            LocalDate userdateofbirth = userdateofbirthid.getValue();
            String usergender = usergenderid.getValue();

            if (!us.isValidPassword(userpassword)) {
                showAlert("Password is not strong enough. It must contain at least 8 characters including at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace.");
                return;
            }

            if (!us.isEmailValid(useremail)) {
                showAlert("E-mail is not valid");
                return;
            }

            us.add(new user(nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender));
            showAlert("User added successfully!");
        } catch (SQLException e) {
            showAlert("Error occurred: " + e.getMessage());
        }
    }

        private void showAlert(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText(message);
            alert.showAndWait();
        }
    private void displayPhotoImageView(user U, ImageView imageView) throws SQLException, IOException {
        // Get the photo bytes from the database Blob
        try {
            Blob photoBlob = U.getImgUser();
            byte[] photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            ByteArrayInputStream bis = new ByteArrayInputStream(photoBytes);

          //  Image image = new Image(getClass().getResource(bis));

            // Display image in the ImageView
            imageView.getImage();

        }
        catch (Exception e) {
            // Handle image loading errors
            e.printStackTrace();

    }}
        @FXML
    private void ImporterImg(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selection une image");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.jpg, *.png, *.gif)", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);

        if (file == null) {
            // User didn't select a file
            return;
        }

        // Stockez le chemin d'accès du fichier sélectionné
        filePath = file.getAbsolutePath();

        // Affichez l'image sélectionnée dans l'ImageView
        //Image image = new Image(file.toURI().toString());
        //ImgField.setImage(image);


    }
    @FXML
    void LOGIN(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        loginButton.getScene().setRoot(root);
        System.out.println("moved");

    }

}











