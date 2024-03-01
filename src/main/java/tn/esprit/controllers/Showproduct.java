package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.product;
import tn.esprit.services.productservice;
import javafx.collections.ObservableList;


import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Showproduct {
     productservice ps = new productservice();

    @FXML
    private TextField searchField; // Text field for search input


    @FXML
    private TableColumn<product, String> IMAGE;

    @FXML
    private TableColumn<product, String> DESCRIPTION;

    @FXML
    private TableColumn<product, Integer> QUANTITY;

    @FXML
    private TableColumn<product, String> STATE;

    @FXML
    private TableColumn<product, String> TYPE;
    @FXML
    private ScrollPane scroll;

    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private TableView<product> tableView;

    private boolean isLiked = false;


    @FXML
    void searchProducts(ActionEvent event) {
        try {
            String searchCriteria = searchField.getText();
            if (searchCriteria.isEmpty()) {
                displayProduct(); // Revert to original state if search field is empty
            } else {
                List<product> products = ps.searchProducts(searchCriteria);
                ObservableList<product> observableList = FXCollections.observableList(products);
                updateScrollPane(products); // Update the UI with search results
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateScrollPane(List<product> products) {

        scroll.setContent(null);

        // Create a new VBox to hold all product boxes
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));

        // Initialize row and column counters for layout
        int column = 0;

        // Create an HBox to hold products for each row
        HBox currentRowHBox = new HBox();
        currentRowHBox.setSpacing(20);

        // Iterate over the list of products and create product boxes
        for (product Product : products) {
            // Create product box for the current product
            VBox productBox = createProductBox(Product);

            // Add the product box to the current row HBox
            currentRowHBox.getChildren().add(productBox);

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

        // If there are remaining products in the last row, add them to the VBox
        if (!currentRowHBox.getChildren().isEmpty()) {
            vBox.getChildren().add(currentRowHBox);
        }

        // Set the VBox as the content of the scroll pane
        scroll.setContent(vBox);
    }



    @FXML
    void search(ActionEvent event) {
        // Your search logic goes here
    }

    @FXML
    void addproduct(ActionEvent event) {  Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/addproduct.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scroll.getScene().setRoot(root);
        System.out.println("Next");

    }

    private void handletrade(ActionEvent event, int eventId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addtrade.fxml"));
            Parent root = loader.load();

            addtrade addBookingController = loader.getController();
            addBookingController.setEventId(eventId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load AddBooking page");

        }
    }
    @FXML
    void initialize() {
        ObservableList<String> sortingOptions = FXCollections.observableArrayList(
                 "Type", "DESCRIPTION", "Quantity", "State"
        );
        sortComboBox.setItems(sortingOptions);
        displayProduct();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                displayProduct(); // Revert to original state if search field is cleared
            } else {
                // Trigger the search operation when text changes
                searchProducts(null); // Pass null or any appropriate event parameter
            }
        });
    }

    private void loadProducts() {
        try {
            List<product> productList = ps.display();
            tableView.getItems().setAll(productList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "SQL Error", "An error occurred while loading products: " + e.getMessage());
        }
    }
    @FXML
    void sortTableView(ActionEvent event) {
        try {
            String selectedSortOption = sortComboBox.getValue();
            List<product> sortedProducts = null;

            switch (selectedSortOption) {
                case "Type":
                    sortedProducts = ps.displaySorted("TYPE");
                    break;
                case "DESCRIPTION":
                    sortedProducts = ps.displaySorted("DESCRIPTION");
                    break;
                case "Quantity":
                    sortedProducts = ps.displaySorted("QUANTITY");
                    break;
                case "State":
                    sortedProducts = ps.displaySorted("STATE");
                    break;
                default:
                    break;
            }

            if (sortedProducts != null) {
                updateScrollPane(sortedProducts);
            } else {
                // Handle the case where sortedProducts is null
                showAlert(Alert.AlertType.ERROR, "Error", "Sorted products list is null.");
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void handleLikeButton(ActionEvent event) {
        // Retrieve the selected product from the table view
        product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                // Increment the likes for the selected product
                ps.likeProduct(selectedProduct.getID_PRODUCT());
                // Update the display
                displayProduct();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to like the product: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a product to like.");
        }
    }

    @FXML
    private void handleDislikeButton(ActionEvent event) {
        // Retrieve the selected product from the table view
        product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                // Increment the dislikes for the selected product
                ps.dislikeProduct(selectedProduct.getID_PRODUCT());
                // Update the display
                displayProduct();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to dislike the product: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a product to dislike.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void displayProduct() {
        try {
            List<product> products = ps.display();

            GridPane gridPane = new GridPane();
            gridPane.setHgap(20);
            gridPane.setVgap(20);
            gridPane.setPadding(new Insets(20));

            int row = 0;
            int column = 0;
            for (product prod : products) {
                HBox hbox = new HBox();
                hbox.getChildren().add(createProductBox(prod));
                gridPane.add(hbox, column, row);

                column++;
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }

            scroll.setContent(gridPane);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private VBox createProductBox(product prod) {
        VBox productBox = new VBox();
        productBox.setStyle("-fx-background-color: #AED6F1; -fx-border-color: #94cdf5; -fx-padding: 30px; -fx-spacing: 10px;");
        productBox.setSpacing(10);

        String TYPE = prod.getTYPE();
        String DESCRIPTION = prod.getDESCRIPTION();
        String IMAGE = prod.getIMAGE();
        String STATE = prod.getSTATE();
        int QUANTITY = prod.getQUANTITY();
        productBox.getProperties().put("ID_PRODUCT", prod.getID_PRODUCT());


        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> handleDeleteProduct(event, prod.getID_PRODUCT()));

        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> handleUpdateProduct(event, prod.getID_PRODUCT()));
        Button tradeButton = new Button("Trade Now");
        tradeButton.setOnAction(event -> handletrade(event, prod.getID_PRODUCT()));
        Button LikeButton = new Button("heartlike.png ");
        LikeButton.setOnAction(event -> handletrade(event, prod.getID_PRODUCT()));
        Button disLikeButton = new Button("heartdislike.png ");
        disLikeButton.setOnAction(event -> handletrade(event, prod.getID_PRODUCT()));


        HBox tradebuttonBox = new HBox(tradeButton);
        tradebuttonBox.setAlignment(Pos.CENTER_RIGHT);

        HBox buttonBox = new HBox(deleteButton, updateButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Label TYPELabel = new Label("TYPE: " + TYPE);
        Label DESCRIPTIONLabel = new Label("DESCRIPTION: " + DESCRIPTION);
        Label IMAGELabel = new Label("IMAGE: " + IMAGE);
        Label STATELabel = new Label("STATE: " + STATE);
        Label QUANTITYLabel = new Label("QUANTITY : " + QUANTITY);

        productBox.getChildren().addAll(TYPELabel, DESCRIPTIONLabel, IMAGELabel, STATELabel, QUANTITYLabel, buttonBox, tradeButton);

        return productBox;
    }


   /* private void toggleLikeAction() {
        try {
            if (isLiked) {
                productservice.unlikePost(userId, postId);
            } else {
                productservice.likePost(userId, postId);
            }
            isLiked = !isLiked;
            setLikeButtonImage(isLiked ? "heartlike.png" : "heartdislike.png");
            loadPostInteractions();
        } catch (SQLException e) {
            showAlert("Error", "Failed to toggle like: " + e.getMessage());
        }
    }


  private void setLikeButtonImage(String imageName) {
      Image image = new Image(getClass().getResourceAsStream(imageName));
      likeImageView.setImage(image);
  }

    */



    private void  handleUpdateProduct(ActionEvent event, int productId) {
        try {
            product prod = ps.getProductById(productId);
            if (prod == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Product not found for ID: " + productId);
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateproduct.fxml"));
            Parent root = loader.load();

            updateproduct updateProductController = loader.getController();
            updateProductController.setProductData(prod);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,  "Error", "Failed to load Updateproduct page");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }
    private void handleDeleteProduct(ActionEvent event, int bookingId) {
        try {
            ps.delete(bookingId);
            displayProduct();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "SQL Error", "An error occurred: " + e.getMessage());

        }
    }}


