package tn.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;


public class MainFx extends Application {
    @Override
        
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));


        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("SportsHub");
        stage.setScene(scene);
        stage.show();
    }}