package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class Showcoach implements Initializable {

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
        AVAILABLE.setText("Available");
        PHONE.setText("+216 29502719");
        Speciality.setText("Body Building");
        name.setText("Ahmed Mohamed");
    }
}
