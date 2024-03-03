package tn.esprit.Controllers;

public class AdminSessionManager {
    private static AdminSessionManager instance;
    private int authenticatedAdminId = -1; // Default value if no admin is authenticated

    private AdminSessionManager() {
        // Private constructor to prevent external instantiation
    }

    public static synchronized AdminSessionManager getInstance() {
        if (instance == null) {
            instance = new AdminSessionManager();
        }
        return instance;
    }

    public void setAuthenticatedAdminId(int adminId) {
        this.authenticatedAdminId = adminId;
    }

    public int getAuthenticatedAdminId() {
        return authenticatedAdminId;
    }
}
