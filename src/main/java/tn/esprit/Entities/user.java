package tn.esprit.Entities;

import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;

public class user {

    private int userid;
    private String nameuser;
    private String uphonenumber;
    private String useremail;
    private String userpassword;
    private String usergender;

    public user(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, String userrole) {
    }

    public void setVerifcode(String verifcode) {
        this.verifcode = verifcode;
    }

    private String verifcode;

    public Blob getImgUser() {
        return ImgUser;
    }

    public void setImgUser(Blob imgUser) {
        ImgUser = imgUser;
    }

    private Blob ImgUser;

    private LocalDate userdateofbirth;


    public user(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender) {
        this.userid = userid;
        this.nameuser = nameuser;
        this.uphonenumber = uphonenumber;
        this.useremail = useremail;
        this.userpassword = userpassword;
        this.userdateofbirth = userdateofbirth;
        this.usergender = usergender;


    }
    public String getVerifcode() {
        return verifcode;
    }

    public user(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender,Blob ImgUser) {
        this.userid = userid;
        this.nameuser = nameuser;
        this.uphonenumber = uphonenumber;
        this.useremail = useremail;
        this.userpassword = userpassword;
        this.userdateofbirth = userdateofbirth;
        this.usergender = usergender;


    }
    public user( String nameuser, String uphonenumber, String useremail, String userpassword,LocalDate userdateofbirth, String usergender) {

        this.nameuser = nameuser;
        this.uphonenumber = uphonenumber;
        this.useremail = useremail;
        this.userpassword = userpassword;

        this.userdateofbirth = userdateofbirth;
        this.usergender = usergender;
    }
    public user( String nameuser, String uphonenumber, String useremail,LocalDate userdateofbirth, String usergender) {

        this.nameuser = nameuser;
        this.uphonenumber = uphonenumber;
        this.useremail = useremail;


        this.userdateofbirth = userdateofbirth;
        this.usergender = usergender;
    }

    public user() {

    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public String getUphonenumber() {
        return uphonenumber;
    }

    public void setUphonenumber(String uphonenumber) {
        this.uphonenumber = uphonenumber;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUsergender() {
        return usergender;
    }

    public void setUsergender(String usergender) {
        this.usergender = usergender;
    }

    public LocalDate getUserdateofbirth() {
        return userdateofbirth;
    }
    public void setUserdateofbirth(LocalDate userdateofbirth) {
        this.userdateofbirth = userdateofbirth;
    }



    @Override
    public String toString() {
        return "user{" +
                "userid=" + userid +
                ", nameuser='" + nameuser + '\'' +
                ", uphonenumber='" + uphonenumber + '\'' +
                ", useremail='" + useremail + '\'' +
                ", userpassword='" + userpassword + '\'' +
                ", usergender='" + usergender + '\'' +
                ", userdateofbirth='" + userdateofbirth + '\'' +
                '}';
    }
    public void setImgUser(byte[] imageBytes) {
        Blob blob = null;
        try {
            blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
        } catch (SQLException ex) {
            // Handle the exception
        }
        this.ImgUser = blob;
    }


}
