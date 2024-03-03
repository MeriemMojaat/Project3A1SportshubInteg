package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class coach3 implements Initializable {

    @FXML
    private Label AVAILABLE;

    @FXML
    private Label PHONE;

    @FXML
    private Label Speciality;

    @FXML
    private Label name;

    @FXML
    private ImageView qrCodeImage;

    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AVAILABLE.setText("Sorry Mohamed is not available now");
        PHONE.setText("+216 55348390");
        Speciality.setText("Diverse Personal Trainer");
        name.setText("Salah Slimen");
    }
}

