package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class myDataBase {
    private final String URL ="jdbc:mysql://localhost:3306/sportshub";
    private final String USERNAME = "root";
    private  final String PWD="";
    private Connection con;

    public static myDataBase instance;

    private myDataBase(){
        try {
            con = DriverManager.getConnection(URL,USERNAME,PWD);
            System.out.println("Connected!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static myDataBase getInstance(){

        if (instance == null){
            instance = new myDataBase();
        }
        return instance;
    }

    public Connection getCon() {
        return con;
    }

}
