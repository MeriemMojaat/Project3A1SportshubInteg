package tn.esprit.Entities;

import java.sql.Blob;
import java.time.LocalDate;

public class coach extends user {

    private int coachid;
    private int userid;
    private String coachname, coachavailabilty, coachpassword, coachspeciality, coachschedule;

    public coach(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, String userrole, int coachid, int userid1, String coachname, String coachavailabilty, String coachpassword, String coachspeciality, String coachschedule) {
        super(userid, nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender, userrole);
        this.coachid = coachid;
        this.userid = userid1;
        this.coachname = coachname;
        this.coachavailabilty = coachavailabilty;
        this.coachpassword = coachpassword;
        this.coachspeciality = coachspeciality;
        this.coachschedule = coachschedule;
    }

    public coach(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, int coachid, int userid1, String coachname, String coachavailabilty, String coachpassword, String coachspeciality, String coachschedule) {
        super(userid, nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender);
        this.coachid = coachid;
        this.userid = userid1;
        this.coachname = coachname;
        this.coachavailabilty = coachavailabilty;
        this.coachpassword = coachpassword;
        this.coachspeciality = coachspeciality;
        this.coachschedule = coachschedule;
    }

    public coach(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, Blob ImgUser, int coachid, int userid1, String coachname, String coachavailabilty, String coachpassword, String coachspeciality, String coachschedule) {
        super(userid, nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender, ImgUser);
        this.coachid = coachid;
        this.userid = userid1;
        this.coachname = coachname;
        this.coachavailabilty = coachavailabilty;
        this.coachpassword = coachpassword;
        this.coachspeciality = coachspeciality;
        this.coachschedule = coachschedule;
    }

    public coach(String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, int coachid, int userid, String coachname, String coachavailabilty, String coachpassword, String coachspeciality, String coachschedule) {
        super(nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender);
        this.coachid = coachid;
        this.userid = userid;
        this.coachname = coachname;
        this.coachavailabilty = coachavailabilty;
        this.coachpassword = coachpassword;
        this.coachspeciality = coachspeciality;
        this.coachschedule = coachschedule;
    }

    public coach(String nameuser, String uphonenumber, String useremail, LocalDate userdateofbirth, String usergender, int coachid, int userid, String coachname, String coachavailabilty, String coachpassword, String coachspeciality, String coachschedule) {
        super(nameuser, uphonenumber, useremail, userdateofbirth, usergender);
        this.coachid = coachid;
        this.userid = userid;
        this.coachname = coachname;
        this.coachavailabilty = coachavailabilty;
        this.coachpassword = coachpassword;
        this.coachspeciality = coachspeciality;
        this.coachschedule = coachschedule;
    }

    public coach(int coachid, int userid, String coachname, String coachavailabilty, String coachpassword, String coachspeciality, String coachschedule) {
        this.coachid = coachid;
        this.userid = userid;
        this.coachname = coachname;
        this.coachavailabilty = coachavailabilty;
        this.coachpassword = coachpassword;
        this.coachspeciality = coachspeciality;
        this.coachschedule = coachschedule;
    }

    public coach(int coachid, String cname, String coachavailability, int userid, String cpassword, String coachspeciality, String coachschedule) {
        this.coachid = coachid;
        this.userid = userid;
        this.coachname = cname;
        this.coachavailabilty = coachavailability;
        this.coachpassword = cpassword;
        this.coachspeciality = coachspeciality;
        this.coachschedule = coachschedule;
    }

    public coach(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, String userrole) {
    }

    @Override
    public String toString() {
        return "coach{" +
                "coachid=" + coachid +
                ", userid=" + userid +
                ", coachname='" + coachname + '\'' +
                ", coachavailabilty='" + coachavailabilty + '\'' +
                ", coachpassword='" + coachpassword + '\'' +
                ", coachspeciality='" + coachspeciality + '\'' +
                ", coachschedule='" + coachschedule + '\'' +
                '}';
    }

    public int getCoachid() {
        return coachid;
    }

    public void setCoachid(int coachid) {
        this.coachid = coachid;
    }

    @Override
    public int getUserid() {
        return userid;
    }

    @Override
    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getCoachname() {
        return coachname;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }

    public String getCoachavailabilty() {
        return coachavailabilty;
    }

    public void setCoachavailabilty(String coachavailabilty) {
        this.coachavailabilty = coachavailabilty;
    }

    public String getCoachpassword() {
        return coachpassword;
    }

    public void setCoachpassword(String coachpassword) {
        this.coachpassword = coachpassword;
    }

    public String getCoachspeciality() {
        return coachspeciality;
    }

    public void setCoachspeciality(String coachspeciality) {
        this.coachspeciality = coachspeciality;
    }

    public String getCoachschedule() {
        return coachschedule;
    }

    public void setCoachschedule(String coachschedule) {
        this.coachschedule = coachschedule;
    }

}
