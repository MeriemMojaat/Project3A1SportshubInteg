package tn.esprit.services;

import tn.esprit.entities.admin;

import java.sql.SQLException;
import java.util.List;

public interface Aservice <T>{
    void addadmin(admin admin) throws SQLException;
    void updateadmin(admin admin) throws SQLException;
    List<admin> displayAlladmins() throws SQLException;

    void delete1(String nameuser);

    void deleteamin(String adminname);
}
