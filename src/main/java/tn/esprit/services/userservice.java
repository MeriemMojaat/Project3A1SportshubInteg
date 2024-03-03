package tn.esprit.services;

import tn.esprit.Entities.user;

import java.sql.SQLException;
import java.util.List;

public interface userservice<T> {
    void add(user user) throws SQLException;


    boolean update(T t) throws  SQLException;

    void delete(int userid) throws  SQLException;

    List<user> diplayList() throws  SQLException;

    void delete1(String nameuser);
}

