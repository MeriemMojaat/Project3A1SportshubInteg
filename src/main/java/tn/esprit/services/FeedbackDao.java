package tn.esprit.services;

import tn.esprit.Entities.Feedback;

import java.sql.SQLException;
import java.util.List;

public interface FeedbackDao{
    List<Feedback> getFeedbackForEvent(int eventId);
    void saveFeedback(Feedback feedback);
    List<Feedback> getAllFeedback();

    List<String> getUsernamesForFeedback() throws SQLException;
}
