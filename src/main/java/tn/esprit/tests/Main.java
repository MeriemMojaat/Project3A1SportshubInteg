package tn.esprit.test;

import tn.esprit.entities.admin;
import tn.esprit.entities.user;
import tn.esprit.services.adminservice;
import tn.esprit.services.userservices;

import java.sql.SQLException;
import java.time.LocalDate;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws SQLException {


        user u1 = new user(14, "ba", "52973452", "jawha2015@gmail.com", "123456789", LocalDate.of(2024, 7, 8), "male");
        admin a1 = new admin("ba", "bdddda", "ba@gmail.com", "ba");
        adminservice as = new adminservice();
        userservices us = new userservices();
/*
      try {
            as.addadmin(a1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }

        try {
            System.out.println(as.displayAlladmins());
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }/*
        /*try{
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the userid to delete: ");
            int userid = scanner.nextInt();
            us.delete(userid );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        try {
            as.updateadmin(a1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }


    }
}