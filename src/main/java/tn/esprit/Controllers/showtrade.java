package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.Entities.trade;
import tn.esprit.services.tradeservice;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class showtrade {

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private TableColumn<trade, Integer> ID_PRODUCT;

    @FXML
    private TableColumn<trade, Integer> ID_TRADE;

    @FXML
    private TableColumn<trade, Integer> ID_USER;

    @FXML
    private ScrollPane scroll;
    @FXML
    private TableColumn<trade, String> LOCATION;

    @FXML
    private TableColumn<trade, String> NAME;

    @FXML
    private TableColumn<trade, String> TRADESTATUS;

    @FXML
    private TableView<trade> tableView;

    @FXML
    private TextField searchTrades;

    private tradeservice tradeService = new tradeservice();



    @FXML
    void sortTableView(ActionEvent event) {
        try {
            String selectedSortOption = sortComboBox.getValue();
            List<trade> sortedTrades = null;

            switch (selectedSortOption) {
                case "ID_PRODUCT":
                    sortedTrades = tradeService.displaySorted("ID_PRODUCT");
                    break;
                case "ID_TRADE":
                    sortedTrades = tradeService.displaySorted("ID_TRADE");
                    break;
                case "ID_USER":
                    sortedTrades = tradeService.displaySorted("ID_USER");
                    break;
                case "LOCATION":
                    sortedTrades = tradeService.displaySorted("LOCATION");
                    break;
                case "NAME":
                    sortedTrades = tradeService.displaySorted("NAME");
                    break;
                case "TRADESTATUS":
                    sortedTrades = tradeService.displaySorted("TRADESTATUS");
                    break;
                default:
                    break;
            }

            // Update the scroll pane with the sorted trades
            updateScrollPane(sortedTrades);
        } catch (SQLException e) {
            // Handle SQL exception by displaying an error alert
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    void searchTrades(ActionEvent event) {
        try {
            String searchCriteria = searchTrades.getText();
            if (searchCriteria.isEmpty()) {
                displayTrades(); // Revert to original state if search field is empty
            } else {
                List<trade> trades = tradeService.searchTrades(searchCriteria);
                ObservableList<trade> observableList = FXCollections.observableList(trades);
                updateScrollPane(trades); // Update the UI with search results
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "SQL Error", "An error occurred while searching: " + e.getMessage());
        }
    }

    private void updateScrollPane(List<trade> trades) {
        // Clear existing content in the scroll pane
        scroll.setContent(null);

        // Create a new VBox to hold all trade boxes
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));

        // Initialize row and column counters for layout
        int column = 0;

        // Create an HBox to hold trades for each row
        HBox currentRowHBox = new HBox();
        currentRowHBox.setSpacing(20);

        // Iterate over the list of trades and create trade boxes
        for (trade Trade : trades) {
            // Create trade box for the current trade
            VBox tradeBox = createTradeBox(Trade);

            // Add the trade box to the current row HBox
            currentRowHBox.getChildren().add(tradeBox);

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

        // If there are remaining trades in the last row, add them to the VBox
        if (!currentRowHBox.getChildren().isEmpty()) {
            vBox.getChildren().add(currentRowHBox);
        }

        // Set the VBox as the content of the scroll pane
        scroll.setContent(vBox);
    }

    @FXML
    void initialize() {
        ObservableList<String> sortingOptions = FXCollections.observableArrayList(
                "ID_TRADE", "ID_PRODUCT", "ID_USER", "LOCATION", "TRADESTATUS", "NAME"
        );
        sortComboBox.setItems(sortingOptions);
        displayTrades();
        searchTrades.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                displayTrades(); // Revert to original state if search field is cleared
            } else {
                // Trigger the search operation when text changes
                searchTrades(null); // Pass null or any appropriate event parameter
            }
        });
    }

    private void displayTrades() {
        try {
            List<trade> trades = tradeService.display();

            VBox rootVBox = new VBox();
            rootVBox.setSpacing(20);
            rootVBox.setPadding(new Insets(20));

            for (trade trade : trades) {
                rootVBox.getChildren().add(createTradeBox(trade));
            }

            scroll.setContent(rootVBox);
        } catch (SQLException e) {
            // Assuming showAlert expects AlertType as the first parameter
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred");

            // Handle the IOException here
        }
    }


    private VBox createTradeBox(trade trade) {
        VBox tradeBox = new VBox();
        tradeBox.setStyle("-fx-background-color: #AED6F1; -fx-border-color: #94cdf5; -fx-padding: 30px; -fx-spacing: 10px;");
        tradeBox.setSpacing(10);

        // Extract trade attributes
        String NAME = trade.getNAME();
        String TRADESTATUS = trade.getTRADESTATUS();
        String LOCATION = trade.getLOCATION();
        int ID_PRODUCT = trade.getID_PRODUCT();
        int ID_USER = trade.getuserid();
        int ID_TRADE = trade.getID_TRADE();

        // Create delete button
        tradeBox.getProperties().put("ID_TRADE", trade.getID_TRADE());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> handleDeleteTrade(event, trade.getID_TRADE()));

        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> handleUpdateTrade(event, trade.getID_TRADE()));

        HBox buttonBox = new HBox(deleteButton, updateButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // Create labels for the trade details



        Label LOCATIONLabel = new Label("LOCATION: " + LOCATION);
        Label TRADESTATUSLabel = new Label("TRADE STATUS: " + TRADESTATUS);
        Label NAMELabel = new Label("NAME: " + NAME);

        tradeBox.getChildren().addAll(
                LOCATIONLabel, TRADESTATUSLabel,NAMELabel,buttonBox
        );

        return tradeBox;
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void  handleUpdateTrade(ActionEvent event, int userid) {
        try {
            trade prod = tradeService.getTradeById(userid);
            if (prod == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "trade not found for ID: " + userid);
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatetrade.fxml"));
            Parent root = loader.load();

            updatetrade updateTradeController = loader.getController();
            updateTradeController.setTradeData(prod);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,  "Error", "Failed to load Updatetrade page");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }
    @FXML
    void addtrade(ActionEvent event) {  Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/addtrade.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scroll.getScene().setRoot(root);
        System.out.println("Next");

    }
    @FXML
    void showproduct(ActionEvent event) {  Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/showproduct.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scroll.getScene().setRoot(root);
        System.out.println("Next");

    }
    private void handleDeleteTrade(ActionEvent event, int bookingId) {
        try {
            tradeService.delete(bookingId);
            displayTrades();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "SQL Error", "An error occurred: " + e.getMessage());



        }
    }
    private void loadTradeData() {
        // Fetch data from the service and set it to the TableView
        try {
            tableView.getItems().setAll(tradeService.display());
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

}
