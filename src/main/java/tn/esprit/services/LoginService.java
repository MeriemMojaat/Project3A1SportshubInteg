package tn.esprit.services;

import javafx.scene.control.Alert;
import tn.esprit.entities.admin;
import tn.esprit.entities.coach;
import tn.esprit.entities.user;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LoginService {
    private final Connection con;

    public LoginService() {
        con = MyDatabase.getInstance().getCon();
    }


    public user authenticate(String nameuser, String userpassword ) {
        String query = "SELECT * FROM user WHERE nameuser = ? AND userpassword = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nameuser);
            ps.setString(2, userpassword);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("nameuser");
                    String uphonenumber = resultSet.getString("uphonenumber");
                    String useremail = resultSet.getString("useremail");
                    String password = resultSet.getString("userpassword");
                    LocalDate userdateofbirth = resultSet.getDate("userdateofbirth").toLocalDate();
                    String usergender = resultSet.getString("usergender");
                    displayAlert("Success", "User found!");
                    return new user(name,uphonenumber,useremail,password,userdateofbirth,usergender);

                } else {
                    // Display popup for incorrect credentials

                    return null ;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Display popup for database error
            displayAlert("Error", "An error occurred. Please try again later.");
            return null ;

        }
    }
    public String getAuthenticatedUserName(user authenticatedUser) {
        if (authenticatedUser != null) {
            return authenticatedUser.getNameuser();
        } else {
            return null;
        }
    }





    private void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public admin authenticateadmin(String adminname , String adminpassword ) {
        String query = "SELECT * FROM admin WHERE adminname = ? AND adminpassword = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, adminname);
            ps.setString(2, adminpassword);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    String aname = resultSet.getString("adminname");
                    String apassword = resultSet.getString("adminpassword");
                    String adminadress = resultSet.getString("adminadress");
                    String adminrole = resultSet.getString("adminrole");

                    displayAlert("Success", "Admin found!");
                    return new admin(aname,apassword,adminadress,adminrole);

                } else {
                    // Display popup for incorrect credentials

                    return null ;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Display popup for database error
            displayAlert("Error", "An error occurred. Please try again later.");
            return null ;

        }
    }

    public coach authenticatecoach(String coachname, String coachpassword ) {
        String query = "SELECT * FROM coach WHERE coachname = ? AND coachpassword = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, coachname);
            ps.setString(2, coachpassword);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    String cname = resultSet.getString("coachname");
                    String coachavailability = resultSet.getString("coachavailability");
                    int userid = resultSet.getInt("userid");
                    String cpassword = resultSet.getString("coachpassword");

                    String coachspeciality = resultSet.getString("coachspeciality");
                    String coachschedule = resultSet.getString("coachschedule");
                    int workoutid = resultSet.getInt("workoutid");
                    displayAlert("Success", "coach found!");
                    return new coach(cname,coachavailability,userid,cpassword,coachspeciality,coachschedule,workoutid);

                } else {
                    // Display popup for incorrect credentials

                    return null ;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Display popup for database error
            displayAlert("Error", "An error occurred. Please try again later.");
            return null ;

        }
    }
}

