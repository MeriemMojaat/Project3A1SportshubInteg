package tn.esprit.services;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import tn.esprit.entities.product;
import tn.esprit.entities.trade;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class tradeservice implements IService<trade> {
    Connection con;
    Statement stm;


    public tradeservice(){con= MyDataBase.getInstance().getCon();}





    @Override
    public void add(trade Trade) throws SQLException {
        // Validate the length of the name
        if (Trade.getNAME().length() >= 10) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Name must be less than 10 characters.");
            return;
        }

        // Proceed with the insertion if the name length is valid
        String query = "INSERT INTO `trade`( `ID_TRADE`, `ID_PRODUCT`, `ID_USER` ,`LOCATION`, `TRADESTATUS` , `NAME`) VALUES (?,?,?,?,?,?)";

        try (PreparedStatement stm = con.prepareStatement(query)) {
            stm.setInt(1, Trade.getID_TRADE());
            stm.setInt(2, Trade.getID_PRODUCT());
            stm.setInt(3, Trade.getID_USER());
            stm.setString(4, Trade.getLOCATION());
            stm.setString(5, Trade.getTRADESTATUS());
            stm.setString(6, Trade.getNAME());

            // Execute the update
            stm.executeUpdate();
            System.out.println("Trade added successfully!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the trade: " + e.getMessage());
        }

    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void delete(int ID_TRADE) throws SQLException{
        String query = "DELETE from trade where ID_TRADE= '"+ID_TRADE+"' ";
        try{
            Statement st= con.createStatement();
            st.executeUpdate(query);
            System.out.println("trade is deleted !");
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int getUserIdByName(String userName) throws SQLException {
        int userId = -1; // Default value indicating not found
        String query = "SELECT ID_USER FROM user WHERE name = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("ID_USER");
                }
            }
        }
        return userId;
    }

    public List<String> getUserNames() throws SQLException {
        List<String> userNames = new ArrayList<>();
        String query = "SELECT name FROM user"; // Corrected column name to 'username'
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                userNames.add(rs.getString("name"));
            }
        }
        return userNames;
    }
    public List<trade> display() throws SQLException {
        String query = "SELECT ID_TRADE, ID_PRODUCT ,ID_USER ,LOCATION , TRADESTATUS , NAME FROM `trade`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<trade> trades= new ArrayList<>();
        while (res.next()) {
            trade Trade = new trade(
                    res.getInt("ID_TRADE"),
                    res.getInt("ID_PRODUCT"),
                    res.getInt("ID_USER"),
                    res.getString("LOCATION"),
                    res.getString("TRADESTATUS"),
                    res.getString("NAME")

            );
            trades.add(Trade);
        }
        return trades;
    }

    public void update(trade Trade) throws SQLException {
        // Validate the length of the name
        if (Trade.getNAME().length() >= 10) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Name must be less than 10 characters.");
            return;
        }

        // Proceed with the update if the name length is valid
        String query = "UPDATE trade SET ID_PRODUCT = ?, ID_USER = ?, LOCATION = ?, TRADESTATUS = ?, NAME = ? WHERE ID_TRADE = ?";
        try (PreparedStatement stm = con.prepareStatement(query)) {

            stm.setInt(1, Trade.getID_PRODUCT());
            stm.setInt(2, Trade.getID_USER());
            stm.setString(3, Trade.getLOCATION());
            stm.setString(4, Trade.getTRADESTATUS());
            stm.setString(5, Trade.getNAME());
            stm.setInt(6, Trade.getID_TRADE());
            // Execute the update
            stm.executeUpdate();
            System.out.println("Trade updated successfully!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the trade: " + e.getMessage());
        }
    }

 /*   public List<trade> searchTrades(String searchText) throws SQLException {
        String query = "SELECT * FROM trade WHERE ID_TRADE LIKE ? OR ID_PRODUCT LIKE ? OR ID_USER LIKE ? OR LOCATION LIKE ? OR TRADESTATUS LIKE ? OR NAME LIKE ?";
        List<trade> tradeList = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, "%" + searchText + "%");
            pst.setString(2, "%" + searchText + "%");
            pst.setString(3, "%" + searchText + "%");
            pst.setString(4, "%" + searchText + "%");
            pst.setString(5, "%" + searchText + "%");
            pst.setString(6, "%" + searchText + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                trade trd = new trade();
                trd.setID_TRADE(rs.getInt("ID_TRADE"));
                trd.setID_PRODUCT(rs.getInt("ID_PRODUCT"));
                trd.setID_USER(rs.getInt("ID_USER"));
                trd.setLOCATION(rs.getString("LOCATION"));
                trd.setTRADESTATUS(rs.getString("TRADESTATUS"));
                trd.setNAME(rs.getString("NAME"));

                tradeList.add(trd);
            }
        }
        return tradeList;

    }

  */
    public void sortTrades(List<trade> tradeList, String sortBy) {
        switch (sortBy) {
            case "ID_TRADE":
                tradeList.sort(Comparator.comparingInt(trade::getID_TRADE));
                break;
            case "ID_PRODUCT":
                tradeList.sort(Comparator.comparingInt(trade::getID_PRODUCT));
                break;
            case "ID_USER":
                tradeList.sort(Comparator.comparingInt(trade::getID_USER));
                break;
            case "LOCATION":
                tradeList.sort(Comparator.comparing(trade::getLOCATION));
                break;
            case "NAME":
                tradeList.sort(Comparator.comparing(trade::getNAME));
                break;
            case "TRADESTATUS":
                tradeList.sort(Comparator.comparing(trade::getTRADESTATUS));
                break;
            default:
                System.out.println("Invalid attribute for sorting: " + sortBy);
        }
    }

    public trade getTradeById(int tradeId) throws SQLException {
        trade prod = null;
        String query = "SELECT * FROM trade WHERE ID_TRADE = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, tradeId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    prod  = new trade();
                    prod.setID_TRADE(rs.getInt("ID_TRADE"));
                    prod.setID_PRODUCT(rs.getInt("ID_PRODUCT"));
                    prod.setID_USER(rs.getInt("ID_USER"));
                    prod.setLOCATION(rs.getString("LOCATION"));
                    prod.setTRADESTATUS(rs.getString("TRADESTATUS"));
                    prod.setNAME(rs.getString("NAME"));
                }
            }
        }            return prod;

    }

    public List<trade> displaySorted(String sortBy) throws SQLException {
        String query = "SELECT ID_TRADE, ID_PRODUCT, ID_USER, LOCATION, TRADESTATUS, NAME FROM trade ORDER BY ";

        switch (sortBy) {
            case "ID_TRADE":
                query += "ID_TRADE";
                break;
            case "ID_PRODUCT":
                query += "ID_PRODUCT";
                break;
            case "ID_USER":
                query += "ID_USER";
                break;
            case "LOCATION":
                query += "LOCATION";
                break;
            case "TRADESTATUS":
                query += "TRADESTATUS";
                break;
            case "NAME":
                query += "NAME";
                break;
            default:
                throw new IllegalArgumentException("Invalid sort criteria.");
        }

        try (PreparedStatement pst = con.prepareStatement(query)) {
            ResultSet res = pst.executeQuery();
            List<trade> tradeList = new ArrayList<>();
            while (res.next()) {
                trade t = new trade(
                        res.getInt("ID_TRADE"),
                        res.getInt("ID_PRODUCT"),
                        res.getInt("ID_USER"),
                        res.getString("LOCATION"),
                        res.getString("TRADESTATUS"),
                        res.getString("NAME")
                );
                tradeList.add(t);
            }
            return tradeList;
        }
    }
    public List<trade> searchTrades(String searchCriteria) throws SQLException {
        String query = "SELECT * FROM trade WHERE ID_TRADE LIKE ? OR ID_PRODUCT LIKE ? OR ID_USER LIKE ? OR LOCATION LIKE ? OR TRADESTATUS LIKE ? OR NAME LIKE ?";
        List<trade> tradeList = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, "%" + searchCriteria + "%");
            pst.setString(2, "%" + searchCriteria + "%");
            pst.setString(3, "%" + searchCriteria + "%");
            pst.setString(4, "%" + searchCriteria + "%");
            pst.setString(5, "%" + searchCriteria + "%");
            pst.setString(6, "%" + searchCriteria + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                trade trd = new trade();
                trd.setID_TRADE(rs.getInt("ID_TRADE"));
                trd.setID_PRODUCT(rs.getInt("ID_PRODUCT"));
                trd.setID_USER(rs.getInt("ID_USER"));
                trd.setLOCATION(rs.getString("LOCATION"));
                trd.setTRADESTATUS(rs.getString("TRADESTATUS"));
                trd.setNAME(rs.getString("NAME"));

                tradeList.add(trd);
            }
        }
        return tradeList;
    }

}