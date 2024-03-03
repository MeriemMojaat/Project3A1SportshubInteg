package tn.esprit.Entities;

import java.sql.Blob;
import java.time.LocalDate;

public class authentificateduser extends user {
    public authentificateduser(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender) {
        super(userid, nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender);
    }

    public authentificateduser(int userid, String nameuser, String uphonenumber, String useremail, String userpassword, LocalDate userdateofbirth, String usergender, Blob ImgUser) {
        super(userid, nameuser, uphonenumber, useremail, userpassword, userdateofbirth, usergender, ImgUser);
    }

    public authentificateduser(String name, String uphonenumber, String useremail, String password, LocalDate userdateofbirth, String usergender) {
    }

    public authentificateduser(String nameuser, String uphonenumber, String useremail, LocalDate userdateofbirth, String usergender) {
        super(nameuser, uphonenumber, useremail, userdateofbirth, usergender);
    }

    public authentificateduser() {
    }

    @Override
    public String toString() {
        return "authentificateduser{}";
    }
}
