package tn.esprit.services;
import tn.esprit.entities.product;
import tn.esprit.entities.trade;

import java.sql.SQLException;
import java.util.List;
public interface IService <T>{
    void add(T t) throws SQLException;


    void update(T t) throws  SQLException;

    void delete(int I) throws  SQLException;

    List<T> display() throws  SQLException;


}
