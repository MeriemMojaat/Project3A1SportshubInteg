package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {
    @Override
    public void start(Stage stage) throws IOException {

         FXMLLoader loader = new FXMLLoader(getClass().getResource("/Visitor.fxml" ));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        //scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());


        stage.setTitle("User Management");
        stage.setScene(scene);
        stage.show();
        // Get controller from FXMLLoader
       /* Controller controller = loader.getController();

        // Add listener to fullscreen property
        stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                controller.adjustToFullScreen();
            } else {
                controller.restoreFromFullScreen();
            }
        });*/

    }
    public static void main(String[] args) {
        launch(args);
    }
}


