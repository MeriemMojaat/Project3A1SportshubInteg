package tn.esprit.services;

import tn.esprit.Entities.admin;
import tn.esprit.Entities.coach;
import tn.esprit.Entities.user;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class userservices implements userservice <user> {

    Connection con;
    Statement stm;

    public userservices() {
        con = MyDatabase.getInstance().getCon();
    }

    public void add(user user) throws SQLException {
        // Check if the email is valid
        if (!isEmailValid(user.getUseremail())) {
            System.out.println("Email is not valid.");
            return; // Exit the method if the email is not valid
        }

        // Check if the password is strong
        if (!isValidPassword(user.getUserpassword())) {
            System.out.println("Password is not strong enough.");
            return; // Exit the method if the password is not strong enough
        }
        if (user.getNameuser() == null || user.getNameuser().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return; // Exit the method if the name is empty
        }

        // Check if the phone number is valid
        if (!isValidPhoneNumber(user.getUphonenumber())) {
            System.out.println("Phone number is not valid.");
            return; // Exit the method if the phone number is not valid
        }

        String query = "INSERT INTO `user`( `nameuser`, `uphonenumber`, `useremail`, `userpassword`, `userdateofbirth`, `usergender`) VALUES ('" + user.getNameuser() + "','" + user.getUphonenumber() + "','" + user.getUseremail() + "','" + user.getUserpassword() + "','" + user.getUserdateofbirth() + "','" + user.getUsergender() + "')";
        stm = con.createStatement();
        stm.executeUpdate(query);
        System.out.println("User added!");
    }

    private boolean isValidPhoneNumber(String uphonenumber) {
        // Regular expression pattern for a valid phone number
        String regex = "^[0-9][-0-9 ]{5,}$"; // Adjust pattern as needed

        // Check if the phone number matches the pattern
        return uphonenumber != null && uphonenumber.matches(regex);
    }

    public boolean update(user user) throws SQLException {
        boolean valid=false;
        String query = "UPDATE user SET nameuser = ?, uphonenumber = ?, useremail = ?, userpassword = ?, userdateofbirth = ?, usergender = ?  WHERE userid = ?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, user.getNameuser());
            pst.setString(2, user.getUphonenumber());
            pst.setString(3, user.getUseremail());
            pst.setString(4, user.getUserpassword());
            pst.setDate(5,Date.valueOf(user.getUserdateofbirth()));
            pst.setString(6, user.getUsergender());

            pst.setInt(7, user.getUserid());
            if (!isEmailValid(user.getUseremail())) {
                System.out.println("Email is not valid.");

            }
            else{
                pst.executeUpdate();
                System.out.println("user has ben     successfully updated !");
                valid=true;

            }}
        catch (SQLException ex) {
            Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }
    public boolean updatebyname(user user) throws SQLException {
        boolean valid=false;
        String query = "UPDATE user SET nameuser = ?, uphonenumber = ?, useremail = ?, userpassword = ?, userdateofbirth = ?, usergender = ?  WHERE nameuser = ?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, user.getNameuser());
            pst.setString(2, user.getUphonenumber());
            pst.setString(3, user.getUseremail());
            pst.setString(4, user.getUserpassword());
            pst.setDate(5,Date.valueOf(user.getUserdateofbirth()));
            pst.setString(6, user.getUsergender());

            pst.setString(7, user.getNameuser());
            if (!isEmailValid(user.getUseremail())) {
                System.out.println("Email is not valid.");

            }
            else{
                pst.executeUpdate();
                System.out.println("user has ben     successfully updated !");
                valid=true;

            }}
        catch (SQLException ex) {
            Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }
    public user searchByUid(int userid) throws SQLException {
        String query = "SELECT * FROM user WHERE userid = ?";
        user ur = null;

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, userid);

            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    String nameuser = res.getString(2);
                    String uphonenumber = res.getString(3);
                    String useremail = res.getString(4);
                    String userpassword = res.getString(5);
                    LocalDate userdateofbirth = LocalDate.parse(res.getString(6));
                    String usergender = res.getString(7);
                    String userrole = res.getString(8);
                    String verifcode = res.getString(9);



                    if ("user".equals(userrole)) {
                        ur = new user(userid,nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender, userrole);
                    } else if ("admin".equals(userrole)) {
                        ur = new admin(userid, nameuser, uphonenumber, useremail,userpassword,userdateofbirth,usergender,userrole);
                    } else if ("coach".equals(userrole)) {
                        ur = new coach(userid, nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender,userrole);
                    } else {
                        throw new IllegalArgumentException("Invalid user type: " + userrole);
                    }

                    // Check for null and assign an empty string if needed
                    if (verifcode != null) {
                        ur.setVerifcode(verifcode);
                    }
                }
            }
        }

        return ur;
    }
    public void updatePassword(int userId, String newPassword) throws SQLException {
        String query = "UPDATE user SET userpassword = ? WHERE userid = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
    public boolean doesEmailExist(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE useremail = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Return true if count is greater than 0, indicating the email exists
                }
            }
        }
        return false; // Return false if an exception occurs or no rows are found
    }
    public int getUidByEmail(String email) throws SQLException {
        String query = "SELECT userid FROM user WHERE useremail = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("userid");
                } else {
                    // Email not found
                    return -1;
                }
            }
        }
    }
    public void updateVerificationCode(int userId, String verificationCode) throws SQLException {
        String query = "UPDATE user SET verifcode = ? WHERE userid = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, verificationCode);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
    public String getEmailByUserId(int userId) throws SQLException {
        String query = "SELECT useremail FROM User WHERE userid = ?";
        String email = null;

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    email = resultSet.getString("useremail");
                }
            }
        }

        return email;
    }
    public void update2(user user) throws SQLException {
        String query = "UPDATE user SET nameuser = ?, uphonenumber = ?, useremail = ?, userdateofbirth = ?, usergender = ?  WHERE userid = ?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, user.getNameuser());
            pst.setString(2, user.getUphonenumber());
            pst.setString(3, user.getUseremail());

            pst.setDate(4,Date.valueOf(user.getUserdateofbirth()));
            pst.setString(5, user.getUsergender());

            pst.setInt(6, user.getUserid());

            pst.executeUpdate();
            System.out.println("user has ben     successfully updated !");
        } catch (SQLException ex) {
            Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void delete(int userid) throws SQLException {
        String query = "DELETE from user where userid= '" + userid + "' ";
        try {
            Statement st = con.createStatement();
            st.executeUpdate(query);
            System.out.println("user is deleted !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    @Override
    public List<user> diplayList() throws SQLException {
        List<user> users = new ArrayList<>();
        String query = "select * from user";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                user usr = new user();
                usr.setUserid(rs.getInt("userid"));
                usr.setNameuser(rs.getString("nameuser"));
                usr.setUphonenumber(rs.getString("uphonenumber"));
                usr.setUseremail(rs.getString("useremail"));
                usr.setUserpassword(rs.getString("userpassword"));
                usr.setUserdateofbirth(rs.getDate("userdateofbirth").toLocalDate());
                usr.setUsergender(rs.getString("usergender"));

                users.add(usr);

            }
        } catch (SQLException ex) {
            Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, ex);
        }


        return users;
    }

    public boolean isEmailValid(String useremail) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = useremail;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;


    }
    public boolean isValidPassword(String userpassword) {
        // Define criteria for a strong password
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return userpassword.matches(regex);
    }

    public void insertPst(user user) {
        String query = "insert into user(nameuser,uphonenumber,useremail,userpassword,userdateofbirth,usergender,ImgUser) values(?,?,?,?,?,?,?)";

        if (!isEmailValid(user.getUseremail())) {
            System.out.println("L'email n'est pas valide.");
        } else {
            try {
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, user.getNameuser());
                pst.setString(2, user.getUphonenumber());
                pst.setString(3, user.getUseremail());
                pst.setString(4, user.getUserpassword());
                pst.setDate(5, Date.valueOf(user.getUserdateofbirth()));
                pst.setString(6, user.getUsergender());
                pst.setBlob(7, user.getImgUser());

                pst.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, ex);
            }}



    }
    public List<user> searchUsers(String searchCriteria) throws SQLException {
        List<user> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE nameuser LIKE ? OR userdateofbirth LIKE ? OR useremail LIKE ?";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, "%" + searchCriteria + "%");
            statement.setString(2, "%" + searchCriteria + "%");
            statement.setString(3, "%" + searchCriteria + "%");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user usr = new user (

                        resultSet.getString("nameuser"),
                        resultSet.getString("uphonenumber"),
                        resultSet.getString("useremail"),
                        resultSet.getString("userpassword"),
                        resultSet.getDate("userdateofbirth").toLocalDate(),
                        resultSet.getString("usergender")
                );
                users.add(usr);
            }
        }

        return users;
    }
    public List<Integer> getAllUserId() throws SQLException {
        List<Integer> userIds = new ArrayList<>();
        String query = "SELECT userid FROM User";
        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int userId = resultSet.getInt("userid");
                userIds.add(userId);
            }
        }
        return userIds;
    }
    public user getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM User WHERE userid = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    user user = new user();
                    user.setUserid(resultSet.getInt("userid"));
                    user.setNameuser(resultSet.getString("nameuser"));
                    user.setUphonenumber(resultSet.getString("uphonenumber"));
                    user.setUseremail(resultSet.getString("useremail"));
                    user.setUserpassword(resultSet.getString("userpassword"));
                    user.setUserdateofbirth(resultSet.getDate("userdateofbirth").toLocalDate());
                    user.setUsergender(resultSet.getString("usergender"));

                    return user;
                } else {
                    return null; // User not found
                }
            }
        }
    }
    public user getuserbyname(String nameuser) throws SQLException {
        String query = "SELECT * FROM user WHERE nameuser = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nameuser);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    user user = new user();

                    user.setNameuser(resultSet.getString("nameuser"));
                    user.setUphonenumber(resultSet.getString("uphonenumber"));
                    user.setUseremail(resultSet.getString("useremail"));
                    user.setUserpassword(resultSet.getString("userpassword"));
                    user.setUserdateofbirth(resultSet.getDate("userdateofbirth").toLocalDate());
                    user.setUsergender(resultSet.getString("usergender"));

                    return user;
                } else {
                    return null; // User not found
                }
            }
        }
    }
    @Override
    public void delete1(String nameuser) {
        try {
            con.setAutoCommit(false);
            String deleteReservationsQuery = "DELETE FROM user WHERE userid = (SELECT userid FROM user WHERE nameuser = ?)";
            PreparedStatement deleteReservationsStatement = con.prepareStatement(deleteReservationsQuery);
            deleteReservationsStatement.setString(1, nameuser);
            deleteReservationsStatement.executeUpdate();

            String deleteEventQuery = "DELETE FROM user WHERE nameuser = ?";
            PreparedStatement deleteEventStatement = con.prepareStatement(deleteEventQuery);
            deleteEventStatement.setString(1, nameuser);
            deleteEventStatement.executeUpdate();

            con.commit();
            System.out.println("User was succefully Deleted!");
        } catch (SQLException ex) {
            Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.rollback();
            } catch (SQLException e) {
                Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, e);
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    public int calculatenumberUsers() {
        int nbuser= 0;
        String query = "SELECT COUNT(*) FROM user";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                String chaine = String.valueOf(rs.getString(1));
                nbuser = Integer.parseInt(chaine);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("The Total number of users is " + nbuser);
        return nbuser;
    }

}
