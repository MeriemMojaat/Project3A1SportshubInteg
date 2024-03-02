package tn.esprit.entities;

import java.sql.Blob;
import java.time.LocalDate;

public class admin extends user{

    private int adminid;
    private String adminname,adminpassword,adminadress,adminrole;

    public admin(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, String userrole) {
    }

    public Boolean getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(Boolean isadmin) {
        this.isadmin = isadmin;
    }
    public boolean isAdmin() {
        return isadmin;
    }

    public admin(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, int adminid, String adminname, String adminpassword, String adminadress, String adminrole, Boolean isadmin) {
        super(userid, nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender);
        this.adminid = adminid;
        this.adminname = adminname;
        this.adminpassword = adminpassword;
        this.adminadress = adminadress;
        this.adminrole = adminrole;
        this.isadmin = isadmin;
    }

    public admin(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, Blob ImgUser, int adminid, String adminname, String adminpassword, String adminadress, String adminrole, Boolean isadmin) {
        super(userid, nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender, ImgUser);
        this.adminid = adminid;
        this.adminname = adminname;
        this.adminpassword = adminpassword;
        this.adminadress = adminadress;
        this.adminrole = adminrole;
        this.isadmin = isadmin;
    }

    public admin(String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, int adminid, String adminname, String adminpassword, String adminadress, String adminrole, Boolean isadmin) {
        super(nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender);
        this.adminid = adminid;
        this.adminname = adminname;
        this.adminpassword = adminpassword;
        this.adminadress = adminadress;
        this.adminrole = adminrole;
        this.isadmin = isadmin;
    }

    public admin(String nameuser, String uphonenumber, String useremail, LocalDate userdateofbirth, String usergender, int adminid, String adminname, String adminpassword, String adminadress, String adminrole, Boolean isadmin) {
        super(nameuser, uphonenumber, useremail, userdateofbirth, usergender);
        this.adminid = adminid;
        this.adminname = adminname;
        this.adminpassword = adminpassword;
        this.adminadress = adminadress;
        this.adminrole = adminrole;
        this.isadmin = isadmin;
    }

    public admin(int adminid, String adminname, String adminpassword, String adminadress, String adminrole, Boolean isadmin) {
        this.adminid = adminid;
        this.adminname = adminname;
        this.adminpassword = adminpassword;
        this.adminadress = adminadress;
        this.adminrole = adminrole;
        this.isadmin = isadmin;
    }

    private Boolean isadmin;

    public admin(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, int adminid, String adminname, String adminpassword, String adminadress, String adminrole) {
        super(userid, nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender);
        this.adminid = adminid;
        this.adminname = adminname;
        this.adminpassword = adminpassword;
        this.adminadress = adminadress;
        this.adminrole = adminrole;
    }

    public admin() {

    }

    @Override
    public String toString() {
        return "admin{" +
                "adminid=" + adminid +
                ", adminname='" + adminname + '\'' +
                ", adminpassword='" + adminpassword + '\'' +
                ", adminadress='" + adminadress + '\'' +
                ", adminrole='" + adminrole + '\'' +
                '}';
    }

    public admin(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, Blob ImgUser, int adminid, String adminname, String adminpassword, String adminadress, String adminrole) {
        super(userid, nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender, ImgUser);
        this.adminid = adminid;
        this.adminname = adminname;
        this.adminpassword = adminpassword;
        this.adminadress = adminadress;
        this.adminrole = adminrole;
    }

    public admin(String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, int adminid, String adminname, String adminpassword, String adminadress, String adminrole) {
        super(nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender);
        this.adminid = adminid;
        this.adminname = adminname;
        this.adminpassword = adminpassword;
        this.adminadress = adminadress;
        this.adminrole = adminrole;
    }

    public admin(String nameuser, String uphonenumber, String useremail, LocalDate userdateofbirth, String usergender, int adminid, String adminname, String adminpassword, String adminadress, String adminrole) {
        super(nameuser, uphonenumber, useremail, userdateofbirth, usergender);
        this.adminid = adminid;
        this.adminname = adminname;
        this.adminpassword = adminpassword;
        this.adminadress = adminadress;
        this.adminrole = adminrole;
    }

    public admin(int adminid, String adminname, String adminpassword, String adminadress, String adminrole) {
        this.adminid = adminid;
        this.adminname = adminname;
        this.adminpassword = adminpassword;
        this.adminadress = adminadress;
        this.adminrole = adminrole;
    }
    public admin(String adminname,String adminpassword,String adminadress,String adminrole){
        this.adminname = adminname;
        this.adminpassword = adminpassword;
        this.adminadress = adminadress;
        this.adminrole = adminrole;
    }

    public String getAdminrole() {
        return adminrole;
    }

    public void setAdminrole(String adminrole) {
        this.adminrole = adminrole;
    }

    public String getAdminadress() {
        return adminadress;
    }

    public void setAdminadress(String adminadress) {
        this.adminadress = adminadress;
    }

    public int getAdminid() {
        return adminid;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getAdminpassword() {
        return adminpassword;
    }

    public void setAdminpassword(String adminpassword) {
        this.adminpassword = adminpassword;
    }
}