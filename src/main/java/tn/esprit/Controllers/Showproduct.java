package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.Entities.ratingproduct;
import tn.esprit.Entities.product;
import tn.esprit.services.ratingservice;
import tn.esprit.services.productservice;
import javafx.collections.ObservableList;



import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    @FXML
    private PieChart productPieChart;

    private String selectedImagePath;

    ratingservice fs = new ratingservice();

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
    @FXML
    void showtrade(ActionEvent event) {  Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/showtrade.fxml"));
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
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Addtrade page");

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
    @FXML
    void viewCharts(ActionEvent event) {
        try {
            // Retrieve counts of new and used products
            int newProductCount = ps.countNewProducts();
            int usedProductCount = ps.countUsedProducts();

            // Create PieChart data
            PieChart.Data newProductData = new PieChart.Data("New Products", newProductCount);
            PieChart.Data usedProductData = new PieChart.Data("Used Products", usedProductCount);

            // Create PieChart
            PieChart productPieChart = new PieChart();
            productPieChart.getData().addAll(newProductData, usedProductData);

            // Create dialog to display PieChart
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Product Distribution");
            dialog.setHeaderText("New and Used Products Distribution");
            dialog.getDialogPane().setContent(productPieChart);

            // Add OK button to close the dialog
            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(okButtonType);

            // Show the dialog and wait for user response
            dialog.showAndWait();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
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
        int ID_PRODUCT=prod.getID_PRODUCT();
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


        Button likeButton = new Button("❤️"); likeButton.setOnAction(event -> handleFeedback(ID_PRODUCT, true));
        Button dislikeButton = new Button("💔"); dislikeButton.setOnAction(event -> handleFeedback(ID_PRODUCT, false));

        int ratingHeart;
        int ratingBrokenHeart;
        try {
            ratingHeart = ps.getLikeCount(ID_PRODUCT);
            ratingBrokenHeart = ps.getDislikeCount(ID_PRODUCT);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
            ratingHeart = 0; // Default value if retrieval fails
            ratingBrokenHeart = 0; // Default value if retrieval fails
        }

        HBox ratingBox = new HBox(likeButton, new Label("Likes: " + ratingHeart), dislikeButton, new Label("Dislikes: " + ratingBrokenHeart));
        ratingBox.setAlignment(Pos.CENTER_RIGHT);


        HBox tradebuttonBox = new HBox(tradeButton);
        tradebuttonBox.setAlignment(Pos.CENTER_RIGHT);

        HBox buttonBox = new HBox(deleteButton, updateButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Label TYPELabel = new Label("TYPE: " + TYPE);
        Label DESCRIPTIONLabel = new Label("DESCRIPTION: " + DESCRIPTION);
        Label IMAGELabel = new Label("IMAGE: " + IMAGE);
        Label STATELabel = new Label("STATE: " + STATE);
        Label QUANTITYLabel = new Label("QUANTITY : " + QUANTITY);

        productBox.getChildren().addAll(TYPELabel, DESCRIPTIONLabel, IMAGELabel, STATELabel, QUANTITYLabel, buttonBox, ratingBox,tradeButton);

        return productBox;
    }

    void handleFeedback(int ID_PRODUCT, boolean isLike) {
        try {
            ratingproduct feedback = new ratingproduct();
            if (isLike) {
                ps.incrementLikeCount(ID_PRODUCT);
                feedback.setRatingHeart(1);
                showAlert(Alert.AlertType.INFORMATION, "Liked", "Liked workout with ID: " + ID_PRODUCT);
            } else {
                ps.incrementDislikeCount(ID_PRODUCT);
                feedback.setRatingBrokenHeart(1);
                showAlert(Alert.AlertType.INFORMATION, "Dislike", "Liked workout with ID: " + ID_PRODUCT);
            }
            feedback.setUserid(17); // Set the user ID to 1
            feedback.setID_PRODUCT(ID_PRODUCT); // Set the workout ID
            fs.saveFeedback(feedback);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }

    }








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


