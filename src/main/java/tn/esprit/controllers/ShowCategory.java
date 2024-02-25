package tn.esprit.controllers;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.workoutcategory;
import tn.esprit.entities.workouts;
import tn.esprit.services.workoutcategoryService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ShowCategory {

    @FXML
    private Button event;

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

    private VBox createCategoryBox(workoutcategory workoutcat) {
        VBox eventBox = new VBox();
        eventBox.setStyle("-fx-background-color: #AED6F1; -fx-border-color: #94cdf5; -fx-padding: 30px; -fx-spacing: 10px;");
        eventBox.setSpacing(10);
        String category_name = workoutcat.getCategory_name();
        String cat_description = workoutcat.getCat_description();
        String cat_image = workoutcat.getCat_image();
        // Create delete button
        eventBox.getProperties().put("id_category", workoutcat.getId_category());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(workoutcategory -> handleDeleteCategory(workoutcategory));

        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> handleUpdateCategory(event, workoutcat.getId_category()));
        
        Button listworkoutsButton = new Button("list of workouts");
        listworkoutsButton.setOnAction(event -> handlelistworkouts(event, workoutcat.getId_category()));


        HBox buttonBox = new HBox(deleteButton, updateButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);


        HBox listworkoutsButtonBox = new HBox(listworkoutsButton);
        listworkoutsButtonBox.setAlignment(Pos.CENTER_RIGHT);

        // Create labels for the event details
        Label nameLabel = new Label("Name: " +  category_name);
        Label descriptionLabel = new Label("Description: " + cat_description);
        Label imageLabel = new Label("Image: " + cat_image);
        eventBox.getChildren().addAll(
                nameLabel, descriptionLabel, imageLabel,buttonBox,listworkoutsButtonBox
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
            showAlert("Error", "Failed to load AddBooking page", Alert.AlertType.ERROR);
        }
    }




    private void handleUpdateCategory(ActionEvent event, int categoryID) {
        try {
            // Retrieve the Event object associated with the event ID
            workoutcategory wkcategory = workoutcategoryService.getCategoryById(categoryID);
            if (wkcategory == null) {
                // Handle the case where the event for the event ID is not found
                showAlert("Error", "Category not found for ID: " + categoryID, Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCategory.fxml"));
            Parent root = loader.load();

            UpdateCategory updateCategoryController = loader.getController();
            updateCategoryController.setCategoryData(wkcategory);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load UpdateCategory page", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleDeleteCategory(ActionEvent workoutcategory) {
        if (workoutcategory.getSource() instanceof Button) {
            Button clickedButton = (Button) workoutcategory.getSource();

            // Get the parent of the button (HBox)
            Parent parent = clickedButton.getParent();

            // Traverse the parent hierarchy until finding the VBox containing the event details
            while (parent != null && !(parent instanceof VBox)) {
                parent = parent.getParent();
            }
            if (parent instanceof VBox) {
                VBox eventBox = (VBox) parent;
                // Retrieve the id_event from the properties map of the VBox
                Integer id_category = (Integer) eventBox.getProperties().get("id_category");
                if (id_category != null) {
                    try {
                        // Call your EventService delete method with the id_event
                        workoutcategoryService.delete(id_category);

                        // Remove the VBox from its parent (scrollpane)
                        scrollpane.setContent(null);
                        displayCategory(); // Refresh the event list
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
    @FXML
    void AddCategory(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/AddWKCategory.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scrollpane.getScene().setRoot(root);
        System.out.println("Next");

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
    void addworkout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddWorkout.fxml"));
            Parent root = loader.load();

            AddWorkout addWorkoutController = loader.getController();
            addWorkoutController.setCategoryId(retrieveCategoryId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
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
