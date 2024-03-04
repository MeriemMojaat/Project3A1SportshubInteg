package tn.esprit.services;

import javafx.scene.control.Alert;
import tn.esprit.Entities.FeedbackGame;

import java.sql.Connection;

public class FeedbackGameService {

    private FeedbackGameDao feedbackDao;
    FeedbackGameDaoImpl f = new FeedbackGameDaoImpl();
    Connection con;
    public FeedbackGameService(FeedbackGameDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }
    public void submitFeedback(FeedbackGame feedback) {
        if (feedback == null || feedback.getRating() < 1 || feedback.getRating() > 5) {
            throw new IllegalArgumentException("Invalid feedback data");
        }

        feedbackDao.saveFeedback(feedback);

        // Display an alert for successful feedback submission
        showAlert("Feedback Submitted", "Thank you for your feedback!");
    }

    // Helper method to display an alert
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
