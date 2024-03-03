package tn.esprit.services;

import tn.esprit.Entities.Feedback;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackDaoImpl implements FeedbackDao {
    private List<Feedback> feedbackList = new ArrayList<>();
    Connection con;

    public FeedbackDaoImpl() {
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void saveFeedback(Feedback feedback) {
        String sql = "INSERT INTO feedback (id_feedback,rating,id_event, userid ) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, feedback.getId_feedback());
            statement.setInt(2, feedback.getRating());
            statement.setInt(3, feedback.getId_event());
            statement.setInt(4, feedback.getUserid());


            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while saving feedback: " + e.getMessage());
        }
    }



    @Override
    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback";
        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Feedback feedback = new Feedback();
                feedback.setId_feedback(resultSet.getInt("id_feedback"));
                feedback.setRating(resultSet.getInt("rating"));
                feedback.setUserid(resultSet.getInt("userid"));
                feedback.setId_event(resultSet.getInt("id_event"));
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching feedback: " + e.getMessage());
        }
        return feedbackList;
    }
    @Override
    public List<Feedback> getFeedbackForEvent(int eventId) {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback WHERE id_event = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, eventId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Feedback feedback = new Feedback();
                feedback.setId_feedback(resultSet.getInt("id_feedback"));
                feedback.setRating(resultSet.getInt("rating"));
                feedback.setUserid(resultSet.getInt("userid"));
                feedback.setId_event(resultSet.getInt("id_event"));
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
                String username = resultSet.getString("nameuser");
                usernames.add(username);
            }
        }
        return usernames;
    }

}
