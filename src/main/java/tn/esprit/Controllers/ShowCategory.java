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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.Entities.workoutcategory;
import tn.esprit.services.workoutcategoryService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ShowCategory {


    @FXML
    private ScrollPane scrollpane;

    @FXML
    private Button tournement;
    @FXML
    private ComboBox<String> combosortCategory;
    @FXML
    private TextField SearchCatId;

    @FXML
    private Button trade;

    @FXML
    private Button user;

    @FXML
    private Button workout;
    @FXML
    void initialize() {
        ObservableList<String> sortingOptions = FXCollections.observableArrayList(
                "Category name", "Category discription"
        );
        combosortCategory.setItems(sortingOptions);
        displayCategory();
        SearchCatId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                displayCategory(); // Revert to original state if search field is cleared
            } else {
                // Trigger the search operation when text changes
                SearchCatId(null); // Pass null or any appropriate event parameter
            }
        });
    }


    private final workoutcategoryService workoutcategoryService = new workoutcategoryService();


    private void displayCategory() {
        try {
            List<workoutcategory> workoutcategories = workoutcategoryService.display();

            GridPane gridPane = new GridPane();
            gridPane.setHgap(20);
            gridPane.setVgap(20);
            gridPane.setPadding(new Insets(20));

            int row = 0;
            int column = 0;
            for (workoutcategory workoutcategory : workoutcategories) {
                HBox hbox = new HBox();
                hbox.getChildren().add(createCategoryBox(workoutcategory));
                gridPane.add(hbox, column, row);

                column++;
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }

            scrollpane.setContent(gridPane);
        } catch (SQLException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


;

// Other imports...

    private VBox createCategoryBox(workoutcategory workoutCat) {
        VBox eventBox = new VBox();
        eventBox.setStyle("-fx-background-color: #AED6F1; -fx-border-color: #94cdf5; -fx-padding: 30px; -fx-spacing: 10px;");
        eventBox.setSpacing(10);

        String categoryName = workoutCat.getCategory_name();
        String catDescription = workoutCat.getCat_description();
        String catImageUrl = workoutCat.getCat_image();

        // Create delete button
        eventBox.getProperties().put("id_category", workoutCat.getId_category());


        Button listWorkoutsButton = new Button("List of Workouts");
        listWorkoutsButton.setOnAction(event -> handlelistworkouts(event,  workoutCat.getId_category()));


        HBox listWorkoutsButtonBox = new HBox(listWorkoutsButton);
        listWorkoutsButtonBox.setAlignment(Pos.CENTER_RIGHT);

        // Create labels for the event details
        Label nameLabel = new Label("Name: " + categoryName);

        Label descriptionLabel = new Label("Description: " + catDescription);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(150); // Set the preferred width of the image
        imageView.setPreserveRatio(true);

        // Load and set the image from its URL
        try {
            if (catImageUrl != null && !catImageUrl.isEmpty()) {
                Image image = new Image(catImageUrl);
                imageView.setImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, e.g., display an error message
            System.err.println("Error loading image for category: " + categoryName);
        }

        eventBox.getChildren().addAll(
                nameLabel, descriptionLabel, imageView,listWorkoutsButtonBox
        );

        return eventBox;
    }


    private void handlelistworkouts(ActionEvent event, int idCategory) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowWorkouts.fxml"));
            Parent root = loader.load();

            ShowWorkouts showWorkoutsController = loader.getController();
            showWorkoutsController.displayWorkouts(idCategory); // Pass the idCategory to displayWorkouts

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load workouts page", Alert.AlertType.ERROR);
        }
    }



    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private int retrieveCategoryId() {
        try {
            List<workoutcategory> categories = workoutcategoryService.display();
            if (!categories.isEmpty()) {
                // For demonstration, let's assume the first category
                return categories.get(0).getId_category();
            } else {
                // Handle the case when no categories are available
                return -1; // Or any default value you prefer
            }
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return -1; // Return a default value or handle the error accordingly
        }
    }


    @FXML
    void sortTableView(ActionEvent event) {
        try {
            String selectedSortOption = combosortCategory.getValue();
            List<workoutcategory> sortedCategory = null;

            switch (selectedSortOption) {
                case "Category name":
                    sortedCategory = workoutcategoryService.displaySorted("category_name");
                    break;
                case "Category discription":
                    sortedCategory = workoutcategoryService.displaySorted("cat_description");
                    break;
                default:
                    break;
            }

            updateVBoxCategory(sortedCategory);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void updateVBoxCategory(List<workoutcategory> categories) {
        // Clear existing content in the scroll pane
        scrollpane.setContent(null);

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
        for (workoutcategory category : categories) {
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
        scrollpane.setContent(vBox);
    }
    @FXML
    void SearchCatId(ActionEvent event) {
        try {
            String searchCriteria = SearchCatId.getText();
            if (searchCriteria.isEmpty()) {
                displayCategory(); // Revert to original state if search field is empty
            } else {

                List<workoutcategory> categories = workoutcategoryService.searchcategory(searchCriteria);
                ObservableList<workoutcategory> observableList = FXCollections.observableList(categories);
                updateVBoxCategory(categories); // Update the UI with search results
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
