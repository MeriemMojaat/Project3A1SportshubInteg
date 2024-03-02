package tn.esprit.services;

import tn.esprit.entities.coach;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class coachservice implements Cservice<coach>{
    Connection con;
    Statement stm;
    userservices us = new userservices();
    public coachservice() {
        con = MyDatabase.getInstance().getCon();
    }
    @Override
    public void addcoach(coach coach) throws SQLException {
        {
            String query = "INSERT INTO `coach`( `coachname`, `coachavailability`, `userid`, `coachpassword`,coachspeciality,coachschedule) VALUES ('" + coach.getCoachname() + "','" + coach.getCoachavailabilty() + "','"+ coach.getUserid() +"','" + coach.getCoachpassword() + "','" + coach.getCoachspeciality() + "','" + coach.getCoachschedule() + "','" + coach.getWorkoutid() + "')";


                stm = con.createStatement();
                stm.executeUpdate(query);
                System.out.println("Coach added!");
            }}


    @Override
    public void updatecoach(coach coach) throws SQLException {
           String query = "UPDATE coach SET coachname = ?, coachavailability = ?, userid = ?, coachpassword = ?,coachspeciality = ?,coachschedule = ?,workoutid = ?  WHERE coachname = ?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, coach.getCoachname());
            pst.setString(2, coach.getCoachavailabilty());
            pst.setInt(3, coach.getUserid());

            pst.setString(4, coach.getCoachpassword());

            pst.setString(5, coach.getCoachspeciality());
            pst.setString(6, coach.getCoachschedule());
            pst.setInt(7, coach.getWorkoutid());
            pst.setString(8, coach.getCoachname());

            pst.executeUpdate();
            System.out.println("coach has ben     successfully updated !");
        } catch (SQLException ex) {
            Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete1(String nameuser) {

    }

    @Override
    public void removecoach(String coachname) {
        try {
            con.setAutoCommit(false);
            String deleteReservationsQuery = "DELETE FROM coach WHERE coachid = (SELECT coachid FROM coach WHERE coachname = ?)";
            PreparedStatement deleteReservationsStatement = con.prepareStatement(deleteReservationsQuery);
            deleteReservationsStatement.setString(1, coachname);
            deleteReservationsStatement.executeUpdate();

            String deleteEventQuery = "DELETE FROM coach WHERE coachname = ?";
            PreparedStatement deleteEventStatement = con.prepareStatement(deleteEventQuery);
            deleteEventStatement.setString(1, coachname);
            deleteEventStatement.executeUpdate();

            con.commit();
            System.out.println("Coach was succefully removed!");
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

