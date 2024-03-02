package tn.esprit.services;

import tn.esprit.entities.coach;

import java.sql.SQLException;

public interface Cservice <T>{
    void addcoach( coach coach) throws SQLException;

    void updatecoach(coach coach) throws SQLException;

    void delete1(String nameuser);

    void removecoach(String nameuser);
    //void deletecoach(int coachid) throws SQLException;
}
