package tn.esprit.services;

import javafx.scene.control.Alert;
import tn.esprit.Entities.FeedbackWorkout;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackWorkoutService implements FeedbackDaoWorkout<FeedbackWorkout> {
    private FeedbackDaoWorkout feedbackDao;

    public FeedbackWorkoutService() {
        con = MyDatabase.getInstance().getCon();
    }
    workoutsService ms = new workoutsService();
    Connection con;
    public FeedbackWorkoutService(FeedbackDaoWorkout feedbackDao) {
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


    public void saveFeedback(FeedbackWorkout feedback) {
        String sql = "INSERT INTO feedbackworkout (id_feedback,userid,id_workout,like_count,dislike_count) VALUES (?, ?, ?, ?,?)";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, feedback.getId_feedback());
            statement.setInt(2, feedback.getUserid());
            statement.setInt(3, feedback.getId_workout());
            statement.setInt(4, feedback.getLike_count());
            statement.setInt(5, feedback.getDislike_count());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while saving feedback: " + e.getMessage());
        }
    }


    public List<FeedbackWorkout> getAllFeedback() {
        List<FeedbackWorkout> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedbackworkout";
        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                FeedbackWorkout feedback = new FeedbackWorkout();
                feedback.setId_feedback(resultSet.getInt("id_feedback"));
                feedback.setUserid(resultSet.getInt("userid"));
                feedback.setId_workout(resultSet.getInt("id_workout"));
                feedback.setLike_count(resultSet.getInt("like_count"));
                feedback.setDislike_count(resultSet.getInt("dislike_count"));


                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching feedback: " + e.getMessage());
        }
        return feedbackList;
    }
    public List<FeedbackWorkout> getFeedbackForEvent(int eventId) {
        List<FeedbackWorkout> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedbackworkout WHERE id_workout = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, eventId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FeedbackWorkout feedback = new FeedbackWorkout();
                feedback.setId_feedback(resultSet.getInt("id_feedback"));
                feedback.setUserid(resultSet.getInt("userid"));
                feedback.setId_workout(resultSet.getInt("id_workout"));
                feedback.setLike_count(resultSet.getInt("like_count"));
                feedback.setDislike_count(resultSet.getInt("dislike_count"));
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching feedback for event: " + e.getMessage());
        }
        return feedbackList;
    }

    public List<String> getUsernamesForFeedback() throws SQLException {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT nameuser FROM userjava"; // Assuming 'users' table contains usernames

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("name_user");
                usernames.add(username);
            }
        }
        return usernames;
    }

    // Helper method to display an alert
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public FeedbackWorkout getMostLikedWorkout() {
        try (Connection con = MyDatabase.getInstance().getCon();
             PreparedStatement statement = con.prepareStatement(
                     "SELECT w.id_workout, COUNT(*) AS like_count " +
                             "FROM feedbackworkout f " +
                             "JOIN workouts w ON f.id_workout = w.id_workout " +
                             "WHERE f.like_count > f.dislike_count " +
                             "GROUP BY w.id_workout " +
                             "ORDER BY COUNT(*) DESC " +
                             "LIMIT 1")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id_workout = resultSet.getInt("id_workout");
                int like_count = resultSet.getInt("like_count");
                int userid = resultSet.getInt("userid");
                return new FeedbackWorkout(id_workout,userid, like_count, 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FeedbackWorkout getMostDislikedWorkout() {
        try (Connection con = MyDatabase.getInstance().getCon();
             PreparedStatement statement = con.prepareStatement(
                     "SELECT w.id_workout,  COUNT(*) AS dislike_count " +
                             "FROM feedbackworkout f " +
                             "JOIN workouts w ON f.id_workout = w.id_workout " +
                             "WHERE f.dislike_count > f.like_count " +
                             "GROUP BY w.id_workout " +
                             "ORDER BY COUNT(*) DESC " +
                             "LIMIT 1")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id_workout = resultSet.getInt("id_workout");
                int userid = resultSet.getInt("userid");
                int dislike_count = resultSet.getInt("dislike_count");
                return new FeedbackWorkout(id_workout,userid , 0, dislike_count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}



