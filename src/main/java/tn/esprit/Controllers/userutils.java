package tn.esprit.Controllers;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import tn.esprit.entities.user;

public class userutils {
    public static void populateFields(user user, TextField username, TextField phonenumber, TextField email, TextField password, DatePicker date, ComboBox<String> gender) {
        if (user != null) {
            username.setText(user.getNameuser());
            phonenumber.setText(user.getUphonenumber());
            email.setText(user.getUseremail());
            password.setText(user.getUserpassword());
            date.setValue(user.getUserdateofbirth());
            gender.setAccessibleText(user.getUsergender());
        }
    }
}
