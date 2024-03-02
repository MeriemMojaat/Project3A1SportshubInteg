package tn.esprit.services;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Myauthenticator extends Authenticator {
    private String username;
    private String password;

    public Myauthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
