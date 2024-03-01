package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.entities.workoutcategory;
import tn.esprit.services.workoutcategoryService;
import tn.esprit.services.CloudinaryService;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.stage.Stage;
import javafx.scene.image.Image;




import java.io.IOException;
import java.sql.SQLException;

public class AddwkCategory {

    @FXML
    private TextArea categorydescription;


    @FXML
    private TextField categoryname;

    private final workoutcategoryService cs = new workoutcategoryService();



    @FXML
    private ImageView ImageView;


    private String selectedImagePath;

    private final CloudinaryService cloudinaryService = new CloudinaryService();

    @FXML
    void AddPicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            Image image = new Image("file:" + selectedImagePath);
            ImageView.setImage(image);

            String cloudinaryUrl = cloudinaryService.uploadImage(selectedImagePath);

        }
    }


    @FXML
    void AddCategory(ActionEvent event) {
        try{
            workoutcategory item = new workoutcategory();

            String cloudinaryUrl = cloudinaryService.uploadImage(selectedImagePath);
            item.setCat_image(cloudinaryUrl);
            cs.add(new workoutcategory(categoryname.getText(),categorydescription.getText(),cloudinaryUrl));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("A new category is added");
            alert.showAndWait();
        }
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }
    @FXML
    void Gotolist(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/ShowCategory.fxml"));
        categoryname.getScene().setRoot(root);
        System.out.println("List Event Page");

    }

}
