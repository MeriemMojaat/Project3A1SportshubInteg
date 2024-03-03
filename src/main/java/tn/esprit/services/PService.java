package tn.esprit.services;

import java.sql.SQLException;
import java.util.List;
public interface PService<T>{
    void add(T t) throws SQLException;


    void update(T t) throws  SQLException;

    void delete(int I) throws  SQLException;

    List<T> display() throws  SQLException;


}
