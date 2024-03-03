package tn.esprit.Controllers;

import tn.esprit.Entities.user;

public class userinfo {
    static user currentUser;

    public static void setCurrentUser(user user) {
        currentUser = user;
    }

    public static user getCurrentUser() {
        return currentUser;
    }

    public static void populateUserInfo(user user) {
        if (user != null) {
            // Populate user information
        }
    }
}
