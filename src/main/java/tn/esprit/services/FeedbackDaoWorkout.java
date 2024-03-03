package tn.esprit.services;

import java.sql.SQLException;
import java.util.List;

public interface FeedbackDaoWorkout<FeedbackWorkout>{
    List<FeedbackWorkout> getFeedbackForEvent(int eventId);
    void saveFeedback(FeedbackWorkout feedback);
    List<FeedbackWorkout> getAllFeedback();

    List<String> getUsernamesForFeedback() throws SQLException;
}

