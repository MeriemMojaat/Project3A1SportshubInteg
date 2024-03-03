package tn.esprit.Controllers;
public class SessionManager {
    private static SessionManager instance;
    private int authenticatedUserId = -1; // Default value if no user is authenticated

    private SessionManager() {
        // Private constructor to prevent external instantiation
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setAuthenticatedUserId(int userId) {
        this.authenticatedUserId = userId;
    }

    public int getAuthenticatedUserId() {
        return authenticatedUserId;
    }
}
