package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.Entities.GameList;
import tn.esprit.services.GLservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Showparticipants {

    @FXML
    private TextField SearchWKid;

    @FXML
    private FlowPane flowpane;

    @FXML
    private ScrollPane scrollPane;

    private final GLservice gls = new GLservice();

    int gameId;

    @FXML
    void initialize() {

        try {
            displayParticipants(gameId); // Revert to original state if search field is cleared
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void displayParticipants(int ID_GAME) throws SQLException {
        System.out.println("Displaying participants for game ID: " + ID_GAME);
        List<GameList> participants = gls.displayByGame(ID_GAME);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));

        int row = 0;
        int column = 0;
        for (GameList gamelist : participants) {
            HBox hbox = new HBox();
            hbox.getChildren().add(createBookingBox(gamelist));
            gridPane.add(hbox, column, row);

            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
        }
        flowpane.getChildren().add(gridPane);
        System.out.println("Display complete.");
    }

    private VBox createBookingBox(GameList gameList) {
        System.out.println("Creating booking box for gameList: " + gameList);

        VBox bookingBox = new VBox();
        bookingBox.setStyle("-fx-background-color:#748CF1; -fx-border-color: #AED6F1; -fx-padding: 20px; -fx-spacing: 10px;");
        bookingBox.setSpacing(10);

        String gamename = null;
        String username = null;
        try {
            int userId = gameList.getID_USER();
            System.out.println("Retrieving user ID: " + userId);

            username = gls.getUserNameById(userId);
            System.out.println("Retrieved username: " + username);

            gamename = gls.getGameNameById(gameList.getID_GAME());
            System.out.println("Retrieved game name: " + gamename);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to get user name for game: " + gameList.getID_GAME(), Alert.AlertType.ERROR);
        }

        Label nameuserLabel = new Label("Name User : " + username);
        Label namgamelabel = new Label("game Name : " + gamename);

      //  int userId = gls.getUserIdByName(username);
        bookingBox.getProperties().put("userId", gameList.getID_USER());
        // Create delete button
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            try {
                handleDeleteParticipant(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        HBox buttonBox = new HBox(deleteButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        bookingBox.getChildren().addAll(
                nameuserLabel, namgamelabel, buttonBox
        );

        System.out.println("Booking box created: " + bookingBox);
        return bookingBox;
    }



    @FXML
    void ViewGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/GameUI.fxml"));
        scrollPane.getScene().setRoot(root);
        System.out.println("show participants");
    }



    private void handleDeleteParticipant(ActionEvent event) throws IOException {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();

            // Get the parent of the button (HBox)
            Parent parent = clickedButton.getParent();

            // Traverse the parent hierarchy until finding the VBox containing the event details
            while (parent != null && !(parent instanceof VBox)) {
                parent = parent.getParent();
            }
            if (parent instanceof VBox) {
                VBox participantBox = (VBox) parent;
                // Retrieve the id of the participant from the properties map of the VBox
                Integer userId = (Integer) participantBox.getProperties().get("userId");
                if (userId != null) {
                    try {
                        // Call your UserService delete method with the userId
                        gls.delete(userId);

                        // Remove the participant VBox from its parent (scrollpane)
                        flowpane.getChildren().remove(participantBox);


                    } catch (SQLException e) {
                        showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            }
        }
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


