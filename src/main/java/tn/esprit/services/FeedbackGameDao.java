package tn.esprit.services;

import tn.esprit.Entities.FeedbackGame;

import java.sql.SQLException;
import java.util.List;

public interface FeedbackGameDao {
    List<FeedbackGame> getFeedbackForEvent(int eventId);
    void saveFeedback(FeedbackGame feedback);
    List<FeedbackGame> getAllFeedback();

    List<String> getUsernamesForFeedback() throws SQLException;
}
