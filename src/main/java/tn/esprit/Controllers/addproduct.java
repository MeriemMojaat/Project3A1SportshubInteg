package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import tn.esprit.Entities.product;
import tn.esprit.services.productservice;

import java.io.*;
import java.sql.SQLException;

public class addproduct {
    private   final productservice ps = new productservice();

    @FXML
    private Canvas canvas;

    @FXML
    private RadioButton New;

    @FXML
    private RadioButton balls;

    @FXML
    private RadioButton bicycles;

    @FXML
    private RadioButton clothes;

    @FXML
    private RadioButton gym;

    @FXML
    private RadioButton swimming;

    @FXML
    private RadioButton used;



    @FXML
    private TextArea DESCRIPTION;

    @FXML
    private TextField QUANTITY;

    @FXML
    private TextField STATE;

    @FXML
    private TextField TYPE;
    private Image image;
    private double lastX;
    private double startAngle = 0;
    private double angle = 0;
    private String fileName;

    @FXML
    public void imageUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String projectDir = System.getProperty("user.dir");

                // Create a directory named "uploads" within the project folder if it doesn't exist
                File uploadDir = new File(projectDir, "uploads");
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                // Copy the uploaded file to the "uploads" directory
                File destinationFile = new File(uploadDir, selectedFile.getName());
                copyFile(selectedFile, destinationFile);

                fileName = selectedFile.getName();
                image = new Image(selectedFile.toURI().toString());
                redrawImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Upload Image");
            alert.setContentText("Please Select File for upload");
            alert.showAndWait();
        }
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

    @FXML
    void back(ActionEvent event)  throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/showproduct.fxml"));
        DESCRIPTION.getScene().setRoot(root);
        System.out.println("List product Page");
    }

    @FXML
    void addP(ActionEvent event) {
        try {
            String state = New.isSelected() ? "new" : (used.isSelected() ? "used" :null);
            String type = balls.isSelected() ? "balls" : (bicycles.isSelected() ? "bicycles": (clothes.isSelected() ? "clothes" : (gym.isSelected() ? "gym" : (swimming.isSelected() ? "swimming"  :null))));



            int quantity = Integer.parseInt(QUANTITY.getText());
            ps.add(new product(type, DESCRIPTION.getText(),fileName, state, quantity));
            showNotification("Success", "You have added a poduct. Be ready for any Trade requests from  other users.");
        } catch (NumberFormatException e) {
            // Handle the case where the text is not a valid integer
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setContentText("Quantity must be a valid integer.");
            alert.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleMousePressed(MouseEvent event) {
        lastX = event.getX();
        startAngle = angle;
    }

    @FXML
    private void handleMouseDragged(MouseEvent event) {
        double deltaX = event.getX() - lastX;
        double angleDelta = (deltaX / canvas.getWidth()) * 360;
        angle = startAngle + angleDelta;
        redrawImage();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void redrawImage() {
        if (image != null) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            double centerX = canvas.getWidth() / 2.0;
            double centerY = canvas.getHeight() / 2.0;
            gc.save();
            gc.translate(centerX, centerY);
            gc.rotate(angle);
            gc.drawImage(image, -image.getWidth() / 2.0, -image.getHeight() / 2.0);
            gc.restore();
        }
    }
    private Stage stage ;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void showNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .owner(stage)
                .showInformation();// Set the owner window .showInformation();
    }
    // Reference to the stage // Method to set the stage public void setStage(Stage stage) { this.stage = stage; }

}
