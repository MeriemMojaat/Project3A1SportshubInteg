package tn.esprit.services;

import tn.esprit.Entities.FeedbackGame;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackGameDaoImpl implements FeedbackGameDao {
    private List<FeedbackGame> feedbackList = new ArrayList<>();
    Connection con;

    public FeedbackGameDaoImpl() {
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void saveFeedback(FeedbackGame feedback) {
        String sql = "INSERT INTO feedbackgame (id_feedback,rating,id_game, userid ) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, feedback.getId_feedback());
            statement.setInt(2, feedback.getRating());
            statement.setInt(3, feedback.getid_game());
            statement.setInt(4, feedback.getUserid());


            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while saving feedback: " + e.getMessage());
        }
    }



    @Override
    public List<FeedbackGame> getAllFeedback() {
        List<FeedbackGame> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedbackgame";
        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                FeedbackGame feedback = new FeedbackGame();
                feedback.setId_feedback(resultSet.getInt("id_feedback"));
                feedback.setRating(resultSet.getInt("rating"));
                feedback.setUserid(resultSet.getInt("userid"));
                feedback.setid_game(resultSet.getInt("id_game"));
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching feedback: " + e.getMessage());
        }
        return feedbackList;
    }
    @Override
    public List<FeedbackGame> getFeedbackForEvent(int eventId) {
        List<FeedbackGame> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedbackgame WHERE id_game = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, eventId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FeedbackGame feedback = new FeedbackGame();
                feedback.setId_feedback(resultSet.getInt("id_feedback"));
                feedback.setRating(resultSet.getInt("rating"));
                feedback.setUserid(resultSet.getInt("userid"));
                feedback.setid_game(resultSet.getInt("id_game"));
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
