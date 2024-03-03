package tn.esprit.Controllers;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.Entities.user;
import tn.esprit.services.LoginService;
import tn.esprit.services.userservices;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    private Label label1;
    @FXML
    private Label active;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Button btnevents;

    @FXML
    private Button btnhome;

    @FXML
    private Button btnmenu;

    @FXML
    private Button btntournements;

    @FXML
    private Button btntrades;

    @FXML
    private Button btnworkouts;

    @FXML
    private Button menuback;

    @FXML
    private VBox slider;
    @FXML
    private Pane pnlOverview;


    private double anchorPanePrefWidth;
    private double anchorPanePrefHeight;
    @FXML
    private TextField searchid;



    @FXML
    private Button updateuser;
    private final userservices us = new userservices();
    private final LoginService ls= new LoginService();
    private user authenticatedUser;


    @FXML
    void SearchUser(ActionEvent event) {
        try {
            String searchCriteria = searchid.getText();
            if (searchCriteria.isEmpty()) {
                new ShowUser(); // Revert to original state if search field is empty
            } else {
                List<user> users = us.searchUsers(searchCriteria);
                ObservableList<user> observableList = FXCollections.observableList(users);
                // Update the UI with search results
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Visitor.fxml"));
        btnSignout.getScene().setRoot(root);
        System.out.println("moved");

    }



            // Load the FXML file
            @FXML
            void settings(ActionEvent event) throws IOException {
                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateauthuser.fxml"));
                Parent root = loader.load();

                // Get the controller of the update page
                updateauthuser updateController = loader.getController();

                // Pass the information of the authenticated user to the update controller
                updateController.initData(authenticatedUser);

                // Create a new dialog
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.getDialogPane().setContent(root);
                dialog.getDialogPane().setStyle("-fx-background-color: #6495ED;");
                // Set the dialog size
                dialog.setWidth(600); // Adjust the width as needed
                dialog.setHeight(400); // Adjust the height as needed

                // Allow the dialog to be resizable
                dialog.setResizable(true);

                // Add OK and Cancel buttons to the dialog
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                // Show the dialog and wait for user response
                Optional<ButtonType> result = dialog.showAndWait();

                // Handle the result if OK button is clicked
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Handle OK button action
                } else {
                    // Handle Cancel button action or dialog closed
                }

                System.out.println("moved");
            }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        slider.setTranslateX(-176);
        btnmenu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e)-> {
                btnmenu.setVisible(false);
                menuback.setVisible(true);
            });
        });

        menuback.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                btnmenu.setVisible(true);
                menuback.setVisible(false);
            });
        });

        // active.setText(""+ us.calculatenumberUsers());
        //
        // updateuser.setOnAction(e->{active.setText(""+us.calculatenumberUsers());});
        anchorPanePrefWidth = anchorPane.getPrefWidth();
        anchorPanePrefHeight = anchorPane.getPrefHeight();
    }
    public void adjustToFullScreen() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        anchorPane.setPrefWidth(stage.getWidth());
        anchorPane.setPrefHeight(stage.getHeight());
    }
    public void initData(user authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        active.setText(authenticatedUser.getNameuser());
        label1.setText("What are you waiting for " + authenticatedUser.getNameuser() + "?\nCreate your own game now and invite your friends");

    }
    public user getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void restoreFromFullScreen() {
        anchorPane.setPrefWidth(anchorPanePrefWidth);
        anchorPane.setPrefHeight(anchorPanePrefHeight);
    }
    @FXML
    void SHOWCOACH(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Showcoach.fxml"));
        Parent root = loader.load();

        // Get the controller of the update page


        // Create a new dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().setStyle("-fx-background-color: #6495ED;");
        // Set the dialog size
        dialog.setWidth(600); // Adjust the width as needed
        dialog.setHeight(400); // Adjust the height as needed

        // Allow the dialog to be resizable
        dialog.setResizable(true);

        // Add OK and Cancel buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and wait for user response
        Optional<ButtonType> result = dialog.showAndWait();

        // Handle the result if OK button is clicked
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Handle OK button action
        } else {
            // Handle Cancel button action or dialog closed
        }

        System.out.println("moved");
    }

    @FXML
    void SHOWCOACH1(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/showcoach1.fxml"));
        Parent root = loader.load();

        // Get the controller of the update page


        // Create a new dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().setStyle("-fx-background-color: #6495ED;");
        // Set the dialog size
        dialog.setWidth(600); // Adjust the width as needed
        dialog.setHeight(400); // Adjust the height as needed

        // Allow the dialog to be resizable
        dialog.setResizable(true);

        // Add OK and Cancel buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and wait for user response
        Optional<ButtonType> result = dialog.showAndWait();

        // Handle the result if OK button is clicked
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Handle OK button action
        } else {
            // Handle Cancel button action or dialog closed
        }

        System.out.println("moved");
    }
    @FXML
    void Showcoach3(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Showcoach2.fxml"));
        Parent root = loader.load();

        // Get the controller of the update page


        // Create a new dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().setStyle("-fx-background-color: #6495ED;");
        // Set the dialog size
        dialog.setWidth(600); // Adjust the width as needed
        dialog.setHeight(400); // Adjust the height as needed

        // Allow the dialog to be resizable
        dialog.setResizable(true);

        // Add OK and Cancel buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and wait for user response
        Optional<ButtonType> result = dialog.showAndWait();

        // Handle the result if OK button is clicked
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Handle OK button action
        } else {
            // Handle Cancel button action or dialog closed
        }

        System.out.println("moved");
    }
    @FXML
    void home(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Showauthuser.fxml"));
        Parent root = loader.load();

        // Get the controller of the update page
        showauthentificateduser controller = loader.getController();

        // Pass the information of the authenticated user to the update controller
        controller.initData1(authenticatedUser);

        // Create a new dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().setStyle("-fx-background-color: #6495ED;");
        // Set the dialog size
        dialog.setWidth(600); // Adjust the width as needed
        dialog.setHeight(400); // Adjust the height as needed

        // Allow the dialog to be resizable
        dialog.setResizable(true);

        // Add OK and Cancel buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and wait for user response
        Optional<ButtonType> result = dialog.showAndWait();

        // Handle the result if OK button is clicked
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Handle OK button action
        } else {
            // Handle Cancel button action or dialog closed
        }

        System.out.println("moved");
}
    @FXML
    void event(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowEventUser.fxml"));
        Parent root = loader.load();

        // Get the controller of the update page


        // Create a new dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(root);
       // dialog.getDialogPane().setStyle("-fx-background-color: #6495ED;");
        // Set the dialog size
        dialog.setWidth(600); // Adjust the width as needed
        dialog.setHeight(400); // Adjust the height as needed

        // Allow the dialog to be resizable
        dialog.setResizable(true);

        // Add OK and Cancel buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and wait for user response
        Optional<ButtonType> result = dialog.showAndWait();

        // Handle the result if OK button is clicked
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Handle OK button action
        } else {
            // Handle Cancel button action or dialog closed
        }

        System.out.println("moved");
    }

    @FXML
    void category(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowCategory.fxml"));
        Parent root = loader.load();

        // Get the controller of the update page


        // Create a new dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(root);
        // dialog.getDialogPane().setStyle("-fx-background-color: #6495ED;");
        // Set the dialog size
        dialog.setWidth(600); // Adjust the width as needed
        dialog.setHeight(400); // Adjust the height as needed

        // Allow the dialog to be resizable
        dialog.setResizable(true);

        // Add OK and Cancel buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and wait for user response
        Optional<ButtonType> result = dialog.showAndWait();

        // Handle the result if OK button is clicked
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Handle OK button action
        } else {
            // Handle Cancel button action or dialog closed
        }

        System.out.println("moved");
    }





    @FXML
    void tournement(ActionEvent event) {

    }

    @FXML
    void trade(ActionEvent event) {

    }


}