package tn.esprit.services;

import javafx.scene.control.Alert;
import tn.esprit.Entities.FeedbackWorkout;

import java.sql.Connection;

public class FeedbackService  {
    private FeedbackDao feedbackDao;
    FeedbackDaoImpl f = new FeedbackDaoImpl();
    Connection con;
    public FeedbackService(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }
    public void submitFeedback(FeedbackWorkout feedback) {
        if (feedback == null || feedback.getLike_count() < 1 || feedback.getDislike_count() < 5) {
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


