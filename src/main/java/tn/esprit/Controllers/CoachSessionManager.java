package tn.esprit.Controllers;

public class CoachSessionManager {
    private static CoachSessionManager instance;
    private int autheticatedCoachid = -1; // Default value if no admin is authenticated

    private CoachSessionManager() {
        // Private constructor to prevent external instantiation
    }

    public static synchronized CoachSessionManager getInstance() {
        if (instance == null) {
            instance = new CoachSessionManager();
        }
        return instance;
    }

    public void setAuthenticatedCoachid(int coachid) {
        this.autheticatedCoachid = coachid;
    }

    public int getAuthenticatedCoach() {
        return autheticatedCoachid;
    }
}
