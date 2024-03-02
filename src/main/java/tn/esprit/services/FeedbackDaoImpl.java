package tn.esprit.services;

import tn.esprit.Entities.FeedbackWorkout;
import tn.esprit.utils.myDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDaoImpl implements FeedbackDao {
    private List<FeedbackWorkout> feedbackList = new ArrayList<>();
    Connection con;

    public FeedbackDaoImpl() {
        con = myDataBase.getInstance().getCon();
    }

    @Override
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



    @Override
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
    @Override
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

    @Override
    public List<String> getUsernamesForFeedback() throws SQLException {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT nameuser FROM user"; // Assuming 'users' table contains usernames

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("name_user");
                usernames.add(username);
            }
        }
        return usernames;
    }

}
