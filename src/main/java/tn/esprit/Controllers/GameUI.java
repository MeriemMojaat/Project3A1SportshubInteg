package tn.esprit.Controllers;

import com.modernmt.text.profanity.ProfanityFilter;
import com.modernmt.text.profanity.dictionary.Profanity;
import javafx.application.Platform;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import tn.esprit.Entities.Comment;
import tn.esprit.Entities.FeedbackGame;
import tn.esprit.Entities.GameList;
import tn.esprit.services.*;
import tn.esprit.Entities.Game;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class GameUI {

    private ProfanityFilter profanityFilter = new ProfanityFilter();
    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private Label gamesCountLabel;

    @FXML
    private Button myGamesButton;

    @FXML
    private BorderPane border;
    @FXML
    private ScrollPane gameScrollPane;

    @FXML
    private TextField SearchCatId;

    @FXML
    private AnchorPane anchor;

    @FXML
    private AnchorPane ANCHOR;

    @FXML
    private VBox vbox;

    @FXML
    private FlowPane flowpane;

    @FXML
    private ComboBox<String> combosortCategory;
    private Gservice gs = new Gservice();

    GLservice gls = new GLservice();
    FeedbackGameDao feedbackDao = new FeedbackGameDaoImpl();
    FeedbackGameService feedbackService = new FeedbackGameService(feedbackDao);

    ServiceComment commentService = new ServiceComment();


    CommentDaoImpl cd = new CommentDaoImpl();
    int authenticatedUserId = SessionManager.getInstance().getAuthenticatedUserId();
   // int USERID = gls.getUserIdByName("mohamed");


    private static final String USERNAME = "jawha20155@gmail.com";
    private static final String PASSWORD = "rrzi fnvs bulv ppbt";

    public GameUI() throws SQLException {
    }

    @FXML
    void initialize() {
        flowpane.setPadding(new Insets(30)); // Adjust padding as needed
        flowpane.setHgap(40); // Set horizontal gap between elements
        flowpane.setVgap(40); // Set vertical gap between lines
        flowpane.setStyle("-fx-background-color: #FAFAFA;");
        ObservableList<String> sortingOptions = FXCollections.observableArrayList(
                "game name", "type", "start date"
        );
        combosortCategory.setItems(sortingOptions);
        displayGame();
        SearchCatId.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.isEmpty()) {
                    // If search field is empty, display all games
                    displayGame();
                } else {
                    // If search field has text, perform search operation
                    List<Game> categories = gs.searchGames(newValue);
                    updateVBoxGame(categories); // Update UI with search results
                    flowpane.layout(); // Force layout to be recomputed and displayed
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception appropriately
            }

            // Clear the search field after displaying results
            SearchCatId(null);
        });
    }


    @FXML
    void SearchCatId(ActionEvent event) {
        try {
            String searchCriteria = SearchCatId.getText();
            if (searchCriteria.isEmpty()) {
                displayGame(); // Revert to original state if search field is empty
            } else {

                List<Game> categories = gs.searchGames(searchCriteria);
                ObservableList<Game> observableList = FXCollections.observableList(categories);
                updateVBoxGame(categories); // Update the UI with search results
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }



    private void displayGame() {
        try {
            List<Game> games = gs.display();

            GridPane gridPane = new GridPane();
            gridPane.setHgap(5);
            gridPane.setVgap(5);
            gridPane.setPadding(new Insets(5));

            int row = 0;
            int column = 0;
            int gamesCount = 0;
            for (Game game: games) {
                HBox hbox = new HBox();
                hbox.getChildren().add(createCategoryBox(game));
                gridPane.add(hbox, column,row);

                column++;

                gamesCount++;
                /*if (column == 3) {
                    column = 0;
                    row++;
                }

                 */
            }

            flowpane.getChildren().add(gridPane);
            gamesCountLabel.setText(""+ gamesCount);
            gamesCountLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        } catch (SQLException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace(); // Add this line to print the exception details for debugging
        }
    }

    private VBox createCategoryBox(Game game) throws SQLException {
        VBox eventBox = new VBox();
        eventBox.setStyle("-fx-background-color: #AED6F1; -fx-border-color: #94cdf5; -fx-padding: 10px; -fx-spacing: 15px; -fx-border-radius: 5px;");
        // eventBox.setSpacing(5);
        // Create delete button
        eventBox.getProperties().put("gameId", game.getGAME_ID());
        VBox commentsVBox = new VBox();
        commentsVBox.setSpacing(5);
        int gameid = gs.getGameIdByName(game.getGAME_NAME());
        int userId = gs.getUserIdByName(gs.getUserNameById(game.getCREATOR_ID()));
        //int USERID = gls.getUserIdByName("meriem");
        String email = gls.getemailByName(gs.getUserNameById(game.getCREATOR_ID()));
        Button displayCommentsForGameButton = new Button("Show Comments");
        displayCommentsForGameButton.setOnAction(event -> displayCommentsForGame(gameid , commentsVBox));

        Button listworkoutsButton = new Button("Game participants");
        listworkoutsButton.setOnAction(event -> handlelistparticipants(event, gameid));

        int userid2 = gs.getUserIdByName("azer");

        Button addBookingButton = new Button("join game");
        addBookingButton.setOnAction(event -> {
            try {
                System.out.println("Debug: Game ID: " + gameid); // Print out the gameid value
                GameList gameList = new GameList(gameid, userid2);
                gls.add(gameList);

                showAlert("Success", "You have joined the game successfully!", Alert.AlertType.INFORMATION);
                sendEmail(email);

            } catch (SQLException e) {
                showAlert("Error", "Failed to join the game: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println(userid2);
                System.out.println(email);
                System.out.println(gameid);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });

        Button submitFeedbackButton = new Button("Submit Feedback");
        submitFeedbackButton.setOnAction(event -> handleFeedbackSubmission(event, gameid));

        Button showFeedbackButton = new Button("Show Feedback");
        showFeedbackButton.setOnAction(event -> displayFeedbackForEvent(gameid));


        VBox ButtonBox = new VBox(addBookingButton,listworkoutsButton,submitFeedbackButton,showFeedbackButton);
        ButtonBox.setAlignment(Pos.CENTER);

        ButtonBox.setSpacing(5);

        // Create labels for the game details

        Label GameCreator =new Label("Game Creator: " + gs.getUserNameById(game.getCREATOR_ID()));
        Label nameLabel = new Label("Game Name: " + game.getGAME_NAME());
        Label typeLabel = new Label("Type: " + game.getTYPE());
        Label placeLabel =new Label("Place: " + game.getGPLACE());
        Label dateLabel = new Label("start Date: " + game.getDATE_G());
        Label edateLabel = new Label("end Date: " + game.getDATE_EG());
        Label participantsLabel = new Label("Participants: " + game.getNBR_PAR());

        TextField commentTextField = new TextField();
        commentTextField.setPromptText("Add a comment");

        commentTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Call the findAndReplaceProfanity method to detect and replace profanity in the TextField
            String profanity = findProfanity(newValue);
            deleteString(newValue, profanity,commentTextField);
        });
        // Modify the code for submitting a comment
        commentTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    String commentText = commentTextField.getText().trim();
                    if (!commentText.isEmpty()) {
                        // Check if the user is a participant in the game
                            // User is a participant, submit the comment
                            String profanity = findProfanity(commentText);
                            if (profanity.isEmpty()) {
                                Comment comment = new Comment(authenticatedUserId, gameid, commentText);
                                comment.setDate_c(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                cd.SubmitComment(comment);

                                // Refresh comments display
                                displayCommentsForGame(gameid , commentsVBox);

                                // Clear the text field
                                commentTextField.clear();
                            } else {
                                // Profanity found, delete profanity from text field
                                deleteString(commentText, profanity, commentTextField);
                                // Show an alert indicating profanity is found
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Profanity Detected");
                                alert.setHeaderText("Profanity has been detected in the text.");
                                alert.setContentText("Please refrain from using offensive language.");
                                alert.showAndWait();
                            }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        // Display comments for the game
        displayCommentsForGame(gameid , commentsVBox);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(commentsVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        scrollPane.setPrefHeight(3 * (20 + 20)); // Adjust according to your label sizes

// Set vertical scroll policy
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        eventBox.getChildren().addAll(
                GameCreator, nameLabel, typeLabel, placeLabel,dateLabel,edateLabel,
                participantsLabel ,ButtonBox,commentTextField, scrollPane
        );
        return eventBox;
    }

    @FXML
    void mygames(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowGameList.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ShowGameList controller = loader.getController();
        controller.displayGame(authenticatedUserId);

        Stage stage = (Stage) myGamesButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public boolean isParticipant(int userId, int gameId) {
        try {
            return gls.hasParticipated(userId, gameId);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
            return false; // For simplicity, returning false if an exception occurs
        }
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

    @FXML
    void CreateGame(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addGame.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addGame controller = loader.getController();
        controller.setUSER_ID(authenticatedUserId);

        Stage stage = (Stage) myGamesButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void deleteString(String text, String stringToDelete, TextField textField) {
        // Check if the new text contains the string to delete
        if (text.contains(stringToDelete)) {
            // Delete the string from the text
            Platform.runLater(() -> {
                // Calculate start and end indices of the string to delete
                int startIndex = text.indexOf(stringToDelete);
                int endIndex = startIndex + stringToDelete.length();
                // Update the text area content with the string deleted
                textField.replaceText(startIndex, endIndex, "");
                // Move the caret position to the end of the text area
                textField.positionCaret(textField.getText().length());
            });
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
            message.setSubject(gs.getUserNameById(authenticatedUserId)+  "joined your game ");
            message.setText("This is the content of your email.");

            Transport.send(message);
            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            throw new MessagingException("Failed to send email: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayCommentsForGame(int gameId, VBox commentsVBox) {
        try {
            // Clear previous comments
            commentsVBox.getChildren().clear();

            // Get comments for the specified game ID
            List<Comment> comments = cd.getCommentsForGame(gameId);

            // Iterate through comments and display them with usernames and timestamp
            for (Comment comment : comments) {
                // Get username associated with the comment's user ID
                String username = gs.getUserNameById(comment.getUser_id());

                // Construct the comment string with username
                String commentString = username + " commented: " + comment.getCommentt();

                // Calculate time elapsed since the comment was posted
                LocalDateTime commentTime = LocalDateTime.parse(comment.getDate_c(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime currentTime = LocalDateTime.now();
                Duration duration = Duration.between(commentTime, currentTime);

                // Convert duration to appropriate units (hours, minutes, or seconds)
                long secondsElapsed = duration.getSeconds();
                long minutesElapsed = duration.toMinutes();
                long hoursElapsed = duration.toHours();

                // Construct timestamp string based on the elapsed time
                String timestampString;
                if (hoursElapsed > 0) {
                    timestampString =   hoursElapsed + " hours ago";
                } else if (minutesElapsed > 0) {
                    timestampString =  minutesElapsed + " minutes ago";
                } else {
                    timestampString =  secondsElapsed + " seconds ago";
                }

                // Create labels for the comment and timestamp
                Label commentLabel = new Label(commentString);
                commentLabel.setStyle("-fx-font-weight: bold;");
                Label timestampLabel = new Label(timestampString);

                // You can customize label appearance here if needed
                commentsVBox.getChildren().addAll(commentLabel, timestampLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    private void handleFeedbackSubmission(ActionEvent event, int gameId) {
        try {
            // Retrieve the list of usernames associated with user IDs from the database
            List<String> usernames = feedbackDao.getUsernamesForFeedback();

            // Create a dialog for feedback submission
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Submit Feedback");
            dialog.setHeaderText("Please provide your feedback for this game:");

            // Create emoji rating control
            Node emojiRatingControl = createRatingEmojis(0); // Default rating is 0

            // Create ComboBox for usernames
           // ComboBox<String> usernameComboBox = new ComboBox<>();
          //  usernameComboBox.getItems().addAll(usernames);

            // Create layout for dialog content
            VBox content = new VBox();
            content.getChildren().addAll( emojiRatingControl);
            dialog.getDialogPane().setContent(content);

            // Add OK and Cancel buttons
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Wait for user response
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Retrieve rating from the emoji rating control
                int rating = getEmojiRating(emojiRatingControl);
                // Retrieve selected username from the ComboBox
               // String selectedUsername = usernameComboBox.getValue();
                // Retrieve the user ID associated with the selected username
             //   int userId = gs.getUserIdByName(selectedUsername);

                // Create a Feedback object
                FeedbackGame feedback = new FeedbackGame();
                feedback.setid_game(gameId); // Set the event ID
                feedback.setRating(rating); // Set the rating
                feedback.setUserid(authenticatedUserId); // Set the user ID

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showparticipantsUser.fxml"));
            Parent root = loader.load();

            showparticipantsUser showparticipantsController = loader.getController();
            showparticipantsController.displayParticipants(ID_GAME); // Pass the idCategory to displayWorkouts

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load ", Alert.AlertType.ERROR);
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

            updateVBoxGame(sortedGame);
        } catch (SQLException e) {
            //  throw new RuntimeException(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateVBoxGame(List<Game> categories) throws SQLException {
        // Clear existing content in the scroll pane
        flowpane.getChildren().clear();

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
            VBox catBox = createCategoryBox(category);

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
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
