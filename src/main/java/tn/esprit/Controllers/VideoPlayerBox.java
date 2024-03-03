package tn.esprit.Controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class VideoPlayerBox extends VBox {
    public VideoPlayerBox(String videoPath) {
        super();

        // Create Media and MediaPlayer
        File file = new File(videoPath);
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        // Create MediaView to display the video
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(400); // Set preferred width
        mediaView.setFitHeight(300); // Set preferred height

        // Create Play/Pause button
        Button playPauseButton = new Button("Play");
        playPauseButton.setOnAction(event -> {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                playPauseButton.setText("Play");
            } else {
                mediaPlayer.play();
                playPauseButton.setText("Pause");
            }
        });

        // Add components to the VBox
        getChildren().addAll(mediaView, playPauseButton);
    }
}
