package tn.esprit.services;

import tn.esprit.entities.admin;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class adminservice implements Aservice<admin> {
    Connection con;
    Statement stm;

    public adminservice() {
        con = MyDatabase.getInstance().getCon();
    }
    private final userservices us = new userservices();

    @Override
    public void addadmin(admin admin) throws SQLException {
        {
            String query = "INSERT INTO `admin`( `adminname`, `adminpassword`, `adminadress`, `adminrole`) VALUES ('" + admin.getAdminname() + "','" + admin.getAdminpassword() + "','"+ admin.getAdminadress() +"','" + admin.getAdminrole() + "')";
            if (!us.isEmailValid(admin.getAdminadress())) {
                System.out.println("Email is not valid.");}
            else{
                stm = con.createStatement();
                stm.executeUpdate(query);
                System.out.println("Admin added!");
            }}
    }

    @Override
    public void updateadmin(admin admin) throws SQLException {
        String query = "UPDATE admin SET adminname = ?, adminpassword = ?, adminadress = ?, adminrole = ?  WHERE adminname = ?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, admin.getAdminname());
            pst.setString(2, admin.getAdminpassword());
            pst.setString(3, admin.getAdminadress());

            pst.setString(4, admin.getAdminrole());

                    pst.setString(5, admin.getAdminname());


            pst.executeUpdate();
            System.out.println("admin has ben     successfully updated !");
        } catch (SQLException ex) {
            Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public List<admin> displayAlladmins() throws SQLException {
        {
            List<admin> admins = new ArrayList<>();
            String query = "select * from admin";
            try {
                PreparedStatement pst = con.prepareStatement(query);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    admin admin = new admin();

                    admin.setAdminname(rs.getString("adminname"));
                    admin.setAdminpassword(rs.getString("adminpassword"));
                    admin.setAdminadress(rs.getString("adminadress"));
                    admin.setAdminrole(rs.getString("adminrole"));


                    admins.add(admin);

                }
            } catch (SQLException ex) {
                Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, ex);
            }


            return admins;
        }
    }

    @Override
    public void delete1(String nameuser) {

    }

    @Override
    public void deleteamin(String adminname) {
        try {
            con.setAutoCommit(false);
            String deleteReservationsQuery = "DELETE FROM admin WHERE adminid = (SELECT adminid FROM admin WHERE adminname = ?)";
            PreparedStatement deleteReservationsStatement = con.prepareStatement(deleteReservationsQuery);
            deleteReservationsStatement.setString(1, adminname);
            deleteReservationsStatement.executeUpdate();

            String deleteEventQuery = "DELETE FROM admin WHERE adminname = ?";
            PreparedStatement deleteEventStatement = con.prepareStatement(deleteEventQuery);
            deleteEventStatement.setString(1, adminname);
            deleteEventStatement.executeUpdate();

            con.commit();
            System.out.println("Admin was succefully Deleted!");
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
}
