package tn.esprit.services;

import tn.esprit.Entities.FeedbackWorkout;

import java.sql.SQLException;
import java.util.List;

public interface FeedbackDao{
    List<FeedbackWorkout> getFeedbackForEvent(int eventId);
    void saveFeedback(FeedbackWorkout feedback);
    List<FeedbackWorkout> getAllFeedback();

    List<String> getUsernamesForFeedback() throws SQLException;
}

