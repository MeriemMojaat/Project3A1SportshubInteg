package tn.esprit.services;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class emailservice {
    public static void sendVerificationEmail(String toEmail, String verificationCode) {
        if (toEmail == null) {
            System.err.println("Recipient email is null. Cannot send verification email.");
            return;
        }

        final String username = "jawha20155@gmail.com"; // Your email
        final String password = "rrzi fnvs bulv ppbt"; // Your password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host for Gmail
        props.put("mail.smtp.port", "587"); // SMTP port for Gmail

        Session session = Session.getInstance(props, new tn.esprit.services.Myauthenticator(username, password));



        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("[User] Password Reset Verification Code");
            message.setText("Dear User,\n\nYour verification code is: " + verificationCode + "\n\nRegards,\nSportsHub Team");

            Transport.send(message);

            System.out.println("Verification email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending verification email", e);
        }
    }
}


