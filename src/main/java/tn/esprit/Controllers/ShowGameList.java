package tn.esprit.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import tn.esprit.Entities.Comment;
import tn.esprit.Entities.FeedbackGame;
import tn.esprit.Entities.Game;
import tn.esprit.services.*;

public class ShowGameList {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField SearchCatId;

    @FXML
    private FlowPane flowpane;
    @FXML
    private ComboBox<String> combosortCategory;

    @FXML
    private ScrollPane scrollpane;

    private final Gservice gs = new Gservice();

    FeedbackGameDao feedbackDao = new FeedbackGameDaoImpl();
    FeedbackGameService feedbackService = new FeedbackGameService(feedbackDao);

    ServiceComment commentService = new ServiceComment();

    CommentDaoImpl cd = new CommentDaoImpl();

    private int userId; // Member variable to hold the user ID

    public void setUserId(int userId) {
        this.userId = userId;
    }
    @FXML
    void initialize() {
        flowpane.setPadding(new Insets(30)); // Adjust padding as needed
        flowpane.setHgap(40); // Set horizontal gap between elements
        flowpane.setVgap(40); // Set vertical gap between lines
        flowpane.setStyle("-fx-background-color: #FAFAFA;");
        ObservableList<String> sortingOptions = FXCollections.observableArrayList(
                "game name", "type" ,"start date"
        );
        combosortCategory.setItems(sortingOptions);
       displayGame(userId);
        SearchCatId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                displayGame(userId); // Revert to original state if search field is cleared
            } else {
                // Trigger the search operation when text changes
                SearchCatId(null ); // Pass null or any appropriate event parameter
            }
        });
    }
    private void handleAddParticipant(ActionEvent event, int gameId ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addparticipants.fxml"));
            Parent root = loader.load();

            addparticipants addBookingController = loader.getController();
            addBookingController.setgameId(gameId);
            addBookingController.setuserId(userId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load AddBooking page", Alert.AlertType.ERROR);
        }
    }

    public void displayGame(int userId) {
        try {
            List<Game> games = gs.getGamesByUserId(userId);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(5);
            gridPane.setVgap(5);
            gridPane.setPadding(new Insets(5));

            int row = 0;
            int column = 0;
            for (Game game : games) {
                HBox hbox = new HBox();
                hbox.getChildren().add(createCategoryBox(game , userId));
                gridPane.add(hbox, column, row);

                column++;
                // Reset column count and move to the next row if necessary
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }

            flowpane.getChildren().add(gridPane);
        } catch (SQLException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace(); // Add this line to print the exception details for debugging
        }
    }



    private VBox createCategoryBox(Game game , int userdI) throws SQLException {
        VBox eventBox = new VBox();
        eventBox.setStyle("-fx-background-color: #AED6F1; -fx-border-color: #94cdf5; -fx-padding: 10px; -fx-spacing: 15px; -fx-border-radius: 5px;");
       // eventBox.setSpacing(5);



        // Create delete button
        eventBox.getProperties().put("gameId", game.getGAME_ID());
        int gameid = gs.getGameIdByName(game.getGAME_NAME());
        int userid = gs.getUserIdByName(gs.getUserNameById(game.getCREATOR_ID()));
        Button deleteButton = new Button("Delete");

        deleteButton.setOnAction(event -> handleDeleteGame(event , gameid ));

        Button handleCommentsSubmissionButton = new Button("Submit Comment");
        handleCommentsSubmissionButton.setOnAction(event -> {
            try {
                handleCommentsSubmission(event, gameid , userid);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        Button displayCommentsForGameButton = new Button("Show Comments");
        displayCommentsForGameButton.setOnAction(event -> displayCommentsForGame(gameid));

        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> handleUpdateGame(event, gameid));

        Button listworkoutsButton = new Button("Game participants");
        listworkoutsButton.setOnAction(event -> handlelistparticipants(event, gameid));

        Button addBookingButton = new Button("Add Participant");
        addBookingButton.setOnAction(event -> handleAddParticipant(event, gameid));

        Button submitFeedbackButton = new Button("Submit Feedback");
        submitFeedbackButton.setOnAction(event -> handleFeedbackSubmission(event, gameid , userid));

        Button showFeedbackButton = new Button("Show Feedback");
        showFeedbackButton.setOnAction(event -> displayFeedbackForEvent(gameid));

        HBox showFeedbackButtonBox = new HBox(showFeedbackButton,submitFeedbackButton);
        showFeedbackButtonBox.setAlignment(Pos.CENTER);



        HBox buttonBox = new HBox(deleteButton, updateButton);
        buttonBox.setAlignment(Pos.CENTER);
        HBox commentbuttonBox = new HBox(handleCommentsSubmissionButton, displayCommentsForGameButton);
        buttonBox.setAlignment(Pos.CENTER);

        HBox listworkoutsButtonBox = new HBox(listworkoutsButton, addBookingButton);
        listworkoutsButtonBox.setAlignment(Pos.CENTER);


        if (userid == game.getCREATOR_ID()) {
            // Add buttons for the game creator
            eventBox.getChildren().addAll(buttonBox , listworkoutsButtonBox ,showFeedbackButtonBox,commentbuttonBox);
        } else {
            // Add buttons for participants or other users
            eventBox.getChildren().addAll(submitFeedbackButton , commentbuttonBox);
        }
        // Create labels for the game details

        Label GameCreator =new Label("Game Creator: " + gs.getUserNameById(game.getCREATOR_ID()));
        Label nameLabel = new Label("Game Name: " + game.getGAME_NAME());
        Label typeLabel = new Label("Type: " + game.getTYPE());
        Label placeLabel =new Label("Place: " + game.getGPLACE());
        Label dateLabel = new Label("Date: " + game.getDATE_G());
        Label edateLabel = new Label("Date: " + game.getDATE_EG());
        Label participantsLabel = new Label("Participants: " + game.getNBR_PAR());

        eventBox.getChildren().addAll(
                GameCreator, nameLabel, typeLabel, placeLabel,dateLabel,edateLabel,
                participantsLabel
                );
        return eventBox;
    }

    private void handleCommentsSubmission(ActionEvent event, int gameId, int userId) throws SQLException {
        try {
            // Get the comment text
            TextInputDialog commentDialog = new TextInputDialog();
            commentDialog.setTitle("Submit Comment");
            commentDialog.setHeaderText("Please provide your comment for this game:");
            commentDialog.setContentText("Comment:");

            Optional<String> commentResult = commentDialog.showAndWait();
            commentResult.ifPresent(commentText -> {
                // Create a Comment object
                Comment comment = new Comment(userId, gameId, commentText);
                comment.setDate_c(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                // Submit comment using CommentDao
                try {
                    cd.SubmitComment(comment);
                    System.out.println("Comment submitted successfully.");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
            System.out.println("Failed to submit comment.");
        }
    }



    private void displayCommentsForGame(int gameId) {
        try {
            // Retrieve comments for the game
            List<Comment> comments = commentService.getCommentsForGame(gameId);

            // Create a dialog to display the comments
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Comments for Game");
            alert.setHeaderText("Comments for the game:");

            VBox dialogPaneContent = new VBox();

            // Loop through the comments list and add them to the dialog content
            for (Comment comment : comments) {
                Label commentLabel = new Label(comment.getCommentt());
                dialogPaneContent.getChildren().add(commentLabel);
            }

            // Set the content of the dialog to the VBox containing comments
            alert.getDialogPane().setContent(dialogPaneContent);

            // Add an OK button to the dialog if not already added
            if (!alert.getButtonTypes().contains(ButtonType.OK)) {
                alert.getButtonTypes().add(ButtonType.OK);
            }

            // Show the dialog and wait for user response
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    public void displayFeedbackForEvent(int gameId) {
        System.out.println("Displaying feedback for event ID: " + gameId);

        // Retrieve feedback for the event
        List<FeedbackGame> feedbackList = feedbackDao.getFeedbackForEvent(gameId);
        System.out.println("Retrieved feedback entries: " + feedbackList.size());

        // Create a dialog to display the feedback
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Feedback for Event");

        VBox dialogPaneContent = new VBox();

        // Loop through the feedback list and create a HBox for each feedback
        for (FeedbackGame feedback : feedbackList) {
            HBox feedbackEntry = new HBox();
            feedbackEntry.setSpacing(10);

            // Create a Label for the rating displayed in stars
            Label ratingLabel = new Label();
            ratingLabel.setGraphic(createRatingEmojis(feedback.getRating()));




            // Retrieve the username associated with the user ID
            String username = null;
            try {
                username = gs.getUserNameById(feedback.getUserid());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Create a Label for the username
            Label usernameLabel = new Label("User: " + username);

            // Add components to the feedback entry
            feedbackEntry.getChildren().addAll(ratingLabel, usernameLabel);

            // Add the feedback entry to the dialog content
            dialogPaneContent.getChildren().add(feedbackEntry);
        }

        // Set the content of the dialog to the VBox containing feedback entries
        alert.getDialogPane().setContent(dialogPaneContent);

        // Add an OK button to the dialog if not already added
        if (!alert.getButtonTypes().contains(ButtonType.OK)) {
            alert.getButtonTypes().add(ButtonType.OK);
        }

        // Show the dialog and wait for user response
        alert.showAndWait();
    }

    private Rating createRatingStars(int rating) {
        Rating ratingStars = new Rating();
        ratingStars.setRating(rating);
        ratingStars.setDisable(true); // Make it read-only
        return ratingStars;
    }


    private Node createRatingEmojis(int initialRating) {
        HBox emojisBox = new HBox(); // Container for emojis
        emojisBox.setSpacing(5);

        // Add happy and angry face emojis based on the initial rating
        for (int i = 0; i < initialRating; i++) {
            Label happyEmoji = createEmoji("\uD83D\uDE00"); // Happy face emoji
            emojisBox.getChildren().add(happyEmoji);
        }
        for (int i = initialRating; i < 5; i++) {
            Label angryEmoji = createEmoji("\uD83D\uDE20"); // Angry face emoji
            emojisBox.getChildren().add(angryEmoji);
        }

        return emojisBox;
    }

    private Label createEmoji(String emoji) {
        Label emojiLabel = new Label(emoji);
        emojiLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16)); // Set font size
        emojiLabel.setOnMouseClicked(event -> {
            Node source = (Node) event.getSource();
            HBox parentBox = (HBox) source.getParent();
            int currentRating = parentBox.getChildren().indexOf(source) + 1;

            if (emoji.equals("\uD83D\uDE20")) { // If clicked emoji is angry, toggle to happy
                parentBox.getChildren().remove(source);
                Label happyEmoji = createEmoji("\uD83D\uDE00");
                happyEmoji.setOnMouseClicked(event1 -> increaseRating(parentBox));
                parentBox.getChildren().add(currentRating - 1, happyEmoji);
            } else { // If clicked emoji is happy, toggle to angry
                parentBox.getChildren().remove(source);
                Label angryEmoji = createEmoji("\uD83D\uDE20");
                angryEmoji.setOnMouseClicked(event1 -> decreaseRating(parentBox));
                parentBox.getChildren().add(currentRating - 1, angryEmoji);
            }
        });
        return emojiLabel;
    }

    private void increaseRating(HBox parentBox) {
        // Increase the rating if possible
        int currentRating = parentBox.getChildren().size();
        if (currentRating < 5) {
            Label happyEmoji = createEmoji("\uD83D\uDE00");
            happyEmoji.setOnMouseClicked(event -> increaseRating(parentBox));
            parentBox.getChildren().add(happyEmoji);
        }
    }

    private void decreaseRating(HBox parentBox) {
        // Decrease the rating if possible
        int currentRating = parentBox.getChildren().size();
        if (currentRating > 0) {
            parentBox.getChildren().remove(currentRating - 1);
        }
    }

    private void handleFeedbackSubmission(ActionEvent event, int gameId, int userId) {
        try {
            // Create a dialog for feedback submission
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Submit Feedback");
            dialog.setHeaderText("Please provide your feedback for this game:");

            // Create emoji rating control
            Node emojiRatingControl = createRatingEmojis(0); // Default rating is 0

            // Create layout for dialog content
            VBox content = new VBox();
            content.getChildren().addAll(emojiRatingControl);
            dialog.getDialogPane().setContent(content);

            // Add OK and Cancel buttons
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Wait for user response
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Retrieve rating from the emoji rating control
                int rating = getEmojiRating(emojiRatingControl);

                // Create a Feedback object
                FeedbackGame feedback = new FeedbackGame();
                feedback.setid_game(gameId); // Set the event ID
                feedback.setRating(rating); // Set the rating
                feedback.setUserid(userId); // Set the user ID

                // Submit feedback using FeedbackService
                feedbackService.submitFeedback(feedback);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }


    private int getEmojiRating(Node emojiRatingControl) {
        if (emojiRatingControl instanceof HBox) {
            HBox emojisBox = (HBox) emojiRatingControl;
            int happyFacesCount = 0;
            for (Node node : emojisBox.getChildren()) {
                Label emojiLabel = (Label) node;
                if (emojiLabel.getText().equals("\uD83D\uDE00")) { // Check for happy face emoji
                    happyFacesCount++;
                }
            }
            return happyFacesCount;
        }
        return 0; // Default rating if control is not properly initialized
    }
    private void handlelistparticipants(ActionEvent event, int ID_GAME) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Showparticipants.fxml"));
            Parent root = loader.load();

            Showparticipants showparticipantsController = loader.getController();
            showparticipantsController.displayParticipants(ID_GAME); // Pass the idCategory to displayWorkouts

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load ", Alert.AlertType.ERROR);
        }
    }


    private void handleUpdateGame(ActionEvent event, int gameId) {
        try {
            // Retrieve the Event object associated with the event ID
            Game game = Gservice.getGameById(gameId);
            if (game == null) {
                // Handle the case where the event for the event ID is not found
                showAlert("Error", "game not found for ID: " + gameId, Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateGame.fxml"));
            Parent root = loader.load();

            UpdateGame updateCategoryController = loader.getController();
            updateCategoryController.setGameData(game);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load UpdateGame page", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteGame(ActionEvent event , int gameId ) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();

            // Get the parent of the button (HBox)
            Parent parent = clickedButton.getParent();

            // Traverse the parent hierarchy until finding the VBox containing the event details
            while (parent != null && !(parent instanceof VBox)) {
                parent = parent.getParent();
            }
            if (parent instanceof VBox) {
                VBox eventBox = (VBox) parent;
                // Retrieve the id_event from the properties map of the VBox
                Integer id_event = (Integer) eventBox.getProperties().get("gameId");
                if (id_event != null) {
                    try {
                        // Call your EventService delete method with the id_event
                        gs.delete(gameId);

                        // Remove the VBox from its parent (Scrollpane)
                        flowpane.getChildren().remove(eventBox);
                        displayGame(userId); // Refresh the event list
                    } catch (SQLException e) {
                        showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            }
        }
    }

    @FXML
    void sortTableView(ActionEvent event) throws SQLException {
        try {
            String selectedSortOption = combosortCategory.getValue();
            List<Game> sortedGame = null;

            switch (selectedSortOption) {
                case "game name":
                    sortedGame = gs.displaySorted("game name");
                    break;
                case "type":
                    sortedGame = gs.displaySorted("type");
                    break;
                case "start date":
                    sortedGame = gs.displaySorted("start date");
                    break;
                default:
                    break;
            }

            updateVBoxCategory(sortedGame , userId);
        } catch (SQLException e) {
          //  throw new RuntimeException(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateVBoxCategory(List<Game> categories , int userid) throws SQLException {
        // Clear existing content in the scroll pane
        flowpane.getChildren().clear();;

        // Create a new VBox to hold all event boxes
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));

        // Initialize row and column counters for layout
        int column = 0;

        // Create an HBox to hold events for each row
        HBox currentRowHBox = new HBox();
        currentRowHBox.setSpacing(20);

        // Iterate over the list of events and create event boxes
        for (Game category : categories) {
            // Create event box for the current event
            VBox catBox = createCategoryBox(category , userid);

            // Add the event box to the current row HBox
            currentRowHBox.getChildren().add(catBox);

            // Increment column counter
            column++;

            // If the column count reaches 3, reset it and add the current row HBox to the VBox
            if (column == 3) {
                vBox.getChildren().add(currentRowHBox);
                currentRowHBox = new HBox(); // Reset the row HBox for the next row
                currentRowHBox.setSpacing(20);
                column = 0; // Reset column counter
            }
        }

        // If there are remaining events in the last row, add them to the VBox
        if (!currentRowHBox.getChildren().isEmpty()) {
            vBox.getChildren().add(currentRowHBox);
        }

        // Set the VBox as the content of the scroll pane
        flowpane.getChildren().add(vBox);
    }

    @FXML
    void SearchCatId(ActionEvent event) {
        try {
            String searchCriteria = SearchCatId.getText();
            if (searchCriteria.isEmpty()) {
                displayGame(userId); // Revert to original state if search field is empty
            } else {

                List<Game> categories = gs.searchGames(searchCriteria);
                ObservableList<Game> observableList = FXCollections.observableList(categories);
                updateVBoxCategory(categories , userId); // Update the UI with search results
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void GameUI(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/GameUI.fxml"));
        SearchCatId.getScene().setRoot(root);
        System.out.println("Game UI");
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
