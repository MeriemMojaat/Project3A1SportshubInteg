package tn.esprit.services;

import java.sql.SQLException;
import java.util.List;

public interface Irating<Feedbackproduct>{
    List<Feedbackproduct> getFeedbackForEvent(int eventId);
    void saveFeedback(Feedbackproduct feedback);
    List<Feedbackproduct> getAllFeedback();

    List<String> getUsernamesForFeedback() throws SQLException;
}
