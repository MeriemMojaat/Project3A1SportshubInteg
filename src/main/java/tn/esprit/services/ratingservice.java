package tn.esprit.services;

import javafx.scene.control.Alert;
import tn.esprit.Entities.ratingproduct;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ratingservice implements Irating<ratingproduct> {
    private Irating feedbackDao;

    public ratingservice(){
        con = MyDatabase.getInstance().getCon();
    }
    Connection con;
    public ratingservice(Irating feedbackDao) {
        this.feedbackDao = feedbackDao;
    }
    public void submitFeedback(ratingproduct feedback) {
        if (feedback == null || feedback.getRatingHeart() < 1 || feedback.getRatingHeart() > 5) {
            throw new IllegalArgumentException("Invalid feedback data");
        }
        if (feedback == null || feedback.getRatingBrokenHeart() < 1 || feedback.getRatingBrokenHeart() > 5) {
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
    public void saveFeedback(ratingproduct feedback) {
        String sql = "INSERT INTO rating (id_feedback,ratingHeart,ratingBrokenHeart,ID_PRODUCT, userid ) VALUES (?, ?, ?, ?,?)";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, feedback.getId_feedback());
            statement.setInt(2, feedback.getRatingHeart());
            statement.setInt(3, feedback.getRatingBrokenHeart());
            statement.setInt(4, feedback.getID_PRODUCT());
            statement.setInt(5, feedback.getUserid());


            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while saving feedback: " + e.getMessage());
        }
    }



    public List<ratingproduct> getAllFeedback() {
        List<ratingproduct> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM rating";
        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ratingproduct feedback = new ratingproduct();
                feedback.setId_feedback(resultSet.getInt("id_feedback"));
                feedback.setRatingHeart(resultSet.getInt("ratingHeart"));
                feedback.setRatingBrokenHeart(resultSet.getInt("ratingBrokenHeart"));
                feedback.setID_PRODUCT(resultSet.getInt(" ID_PRODUCT"));
                feedback.setUserid(resultSet.getInt("userid"));
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching feedback: " + e.getMessage());
        }
        return feedbackList;
    }
    public List<ratingproduct> getFeedbackForEvent(int eventId) {
        List<ratingproduct> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM rating WHERE ID_PRODUCT = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, eventId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ratingproduct feedback = new ratingproduct();
                feedback.setId_feedback(resultSet.getInt("id_feedback"));
                feedback.setRatingHeart(resultSet.getInt("ratingHeart"));
                feedback.setRatingBrokenHeart(resultSet.getInt("ratingBrokenHeart"));
                feedback.setID_PRODUCT(resultSet.getInt("ID_PRODUCT"));
                feedback.setUserid(resultSet.getInt("userid"));
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching feedback for products: " + e.getMessage());
        }
        return feedbackList;
    }

    public List<String> getUsernamesForFeedback() throws SQLException {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT nameuser FROM user"; // Assuming 'users' table contains usernames

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("nameuser");
                usernames.add(username);
            }
        }
        return usernames;
    }




}


