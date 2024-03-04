package tn.esprit.services;

import javafx.scene.control.Alert;
import tn.esprit.Entities.product;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class productservice implements PService<product> {
    private Connection con;
    private PreparedStatement stm;


public productservice(){
        con= MyDatabase.getInstance().getCon();
    }
    public void add(product Product) throws SQLException {
        // Validate IMAGE and quantity
        if (Product.getQUANTITY() < 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Quantity cannot be less than zero.");
            return;
        }

        // SQL query for insertion
        String query = "INSERT INTO `Product`(`ID_PRODUCT`, `TYPE`, `DESCRIPTION`, `IMAGE`, `STATE`, `QUANTITY`) VALUES (?, ?, ?, ?, ?, ?)";

        // Prepare the statement
        stm = con.prepareStatement(query);
        stm.setInt(1, Product.getID_PRODUCT());
        stm.setString(2, Product.getTYPE());
        stm.setString(3, Product.getDESCRIPTION());
        stm.setString(4, Product.getIMAGE());
        stm.setString(5, Product.getSTATE());
        stm.setInt(6, Product.getQUANTITY());

        // Execute the update
        stm.executeUpdate();
        System.out.println("Product added successfully!");
    }

    // Method to check if a string is alphanumeric


    public int getLikeCount(int productId) throws SQLException {
        int ratingHeart = 0;

        // Define SQL query to retrieve the like count for the specified workout
        String sqlQuery = "SELECT ratingHeart FROM rating WHERE ID_PRODUCT = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, productId);

            // Execute the query and get the result set
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If there is at least one row in the result set, extract the like count
                if (resultSet.next()) {
                    ratingHeart = resultSet.getInt("ratingHeart");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return ratingHeart;
    }

    public int getDislikeCount(int productId) throws SQLException {
        int RatingBrokenHeart = 0;

        // Define SQL query to retrieve the dislike count for the specified workout
        String sqlQuery = "SELECT RatingBrokenHeart FROM rating WHERE ID_PRODUCT = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, productId);

            // Execute the query and get the result set
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If there is at least one row in the result set, extract the dislike count
                if (resultSet.next()) {
                    RatingBrokenHeart = resultSet.getInt("RatingBrokenHeart");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return RatingBrokenHeart;
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void delete(int ID_PRODUCT) throws SQLException {
        String query = "DELETE FROM product WHERE ID_PRODUCT = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, ID_PRODUCT);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product with ID " + ID_PRODUCT + " is deleted!");
            } else {
                System.out.println("Product with ID " + ID_PRODUCT + " not found!");
            }
        } catch (SQLException ex) {
            System.out.println("Error deleting product: " + ex.getMessage());
        }
    }


    // Method to check if a character is alphanumeric
    private boolean isAlphaNumericCharacter(char ch) {
        return Character.isLetterOrDigit(ch);
    }

public void update(product Product) throws SQLException {
        // Validate DESCRIPTION and quantity

        if (Product.getQUANTITY() < 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Quantity cannot be less than zero.");
            return;
        }

        // SQL query for update
        String query = "UPDATE Product SET TYPE = ?, DESCRIPTION = ?, IMAGE = ?, STATE = ?, QUANTITY = ? WHERE ID_PRODUCT = ?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, Product.getTYPE());
            pst.setString(2, Product.getDESCRIPTION());
            pst.setString(3, Product.getIMAGE());
            pst.setString(4, Product.getSTATE());
            pst.setInt(5, Product.getQUANTITY());
            pst.setInt(6, Product.getID_PRODUCT());

            // Execute the update
            pst.executeUpdate();
            System.out.println("Product updated successfully!");
        } catch (SQLException ex) {
            // Handle SQL exception
            System.out.println("Error updating product: " + ex.getMessage());
        }
    }

    // Method to check if a string is alphanumeric
    public boolean isAlphaNumericCharacter(String str) {
        return str != null && str.matches("^[a-zA-Z]+$");
    }



    private void showAlert(Alert.AlertType alertType, String title, String content, String additionalInfo)  {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public List<product> display() throws SQLException {
        List<product> products= new ArrayList<>();
        String query="select * from product";
        try {
            PreparedStatement pst=con.prepareStatement(query);
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                product prt=new product();
                prt.setID_PRODUCT(rs.getInt("ID_PRODUCT"));
                prt.setTYPE(rs.getString("TYPE"));
                prt.setDESCRIPTION(rs.getString("DESCRIPTION"));
                prt.setIMAGE(rs.getString("IMAGE"));
                prt.setSTATE(rs.getString("STATE"));
                prt.setQUANTITY(rs.getInt("QUANTITY"));


                products.add(prt);

            }
        } catch (SQLException ex) {
            Logger.getLogger(productservice.class.getName()).log(Level.SEVERE, null, ex);
        }


        return products;
    }


/*
    public List<product> searchByNumber(String number) throws SQLException {
        List<product> productList = new ArrayList<>();
        String query = "SELECT * FROM product WHERE ID_PRODUCT LIKE ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, number + "%"); // Use number followed by '%' to match IDs starting with the specified number
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                product prt = new product();
                prt.setID_PRODUCT(rs.getInt("ID_PRODUCT"));
                prt.setTYPE(rs.getString("TYPE"));
                prt.setDESCRIPTION(rs.getString("DESCRIPTION"));
                prt.setIMAGE(rs.getString("IMAGE"));
                prt.setSTATE(rs.getString("STATE"));
                prt.setQUANTITY(rs.getInt("QUANTITY"));
                productList.add(prt);
            }
        }
        return productList;
    }

 */
public int countNewProducts() throws SQLException {
    String query = "SELECT COUNT(*) AS count FROM product WHERE STATE = 'New'";
    try (PreparedStatement pst = con.prepareStatement(query)) {
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt("count");
        }
    }
    return 0; // Return 0 if no new products found
}

    // Method to count the number of used products
    public int countUsedProducts() throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM product WHERE STATE = 'Used'";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0; // Return 0 if no used products found
    }


    public List<product> searchProducts(String searchText) throws SQLException {
        String query = "SELECT * FROM product WHERE ID_PRODUCT LIKE ? OR TYPE LIKE ? OR DESCRIPTION LIKE ? OR IMAGE LIKE ? OR STATE LIKE ? OR QUANTITY LIKE ?";
        List<product> productList = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, "%" + searchText + "%");
            pst.setString(2, "%" + searchText + "%");
            pst.setString(3, "%" + searchText + "%");
            pst.setString(4, "%" + searchText + "%");
            pst.setString(5, "%" + searchText + "%");
            pst.setString(6, "%" + searchText + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                product prt = new product();
                prt.setID_PRODUCT(rs.getInt("ID_PRODUCT"));
                prt.setTYPE(rs.getString("TYPE"));
                prt.setDESCRIPTION(rs.getString("DESCRIPTION"));
                prt.setIMAGE(rs.getString("IMAGE"));
                prt.setSTATE(rs.getString("STATE"));
                prt.setQUANTITY(rs.getInt("QUANTITY"));

                productList.add(prt);
            }
        }
        return productList;
    }
    public void sortProducts(List<product> productList, String sortBy) {
        switch (sortBy) {
            case "ID_PRODUCT":
                productList.sort(Comparator.comparingInt(product::getID_PRODUCT));
                break;
            case "TYPE":
                productList.sort(Comparator.comparing(product::getTYPE));
                break;
            case "DESCRIPTION":
                productList.sort(Comparator.comparing(product::getDESCRIPTION));
                break;
            case "QUANTITY":
                productList.sort(Comparator.comparingInt(product::getQUANTITY));
                break;
            case "STATE":
                productList.sort(Comparator.comparing(product::getSTATE));
                break;
            default:
                System.out.println("Invalid attribute for sorting: " + sortBy);
        }
    }

    public List<product> displaySorted(String sortBy) throws SQLException {
        List<product> productList = new ArrayList<>();

        try {
            String query = "SELECT `TYPE`, `DESCRIPTION`, `IMAGE`, `STATE`, `QUANTITY` FROM `product` ORDER BY ";

            switch (sortBy) {
                case "TYPE":
                    query += "TYPE";
                    break;
                case "DESCRIPTION":
                    query += "DESCRIPTION";
                    break;
                case "QUANTITY":
                    query += "QUANTITY";
                    break;
                case "STATE":
                    query += "STATE";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sort criteria.");
            }

            Statement pst = con.createStatement();
            ResultSet res = pst.executeQuery(query);

            while (res.next()) {
                product p = new product(
                        res.getString("TYPE"),
                        res.getString("DESCRIPTION"),
                        res.getString("IMAGE"),
                        res.getString("STATE"),
                        res.getInt("QUANTITY")
                );
                productList.add(p);
            }
        } catch (SQLException e) {
            // Log the exception
            e.printStackTrace();
            throw e; // Rethrow the exception to be handled by the caller
        }

        return productList;
    }




    public product getProductById(int productId) throws SQLException {
        String query = "SELECT * FROM product WHERE ID_PRODUCT = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, productId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                product prod = new product();
                prod.setID_PRODUCT(rs.getInt("ID_PRODUCT"));
                prod.setTYPE(rs.getString("TYPE"));
                prod.setDESCRIPTION(rs.getString("DESCRIPTION"));
                prod.setIMAGE(rs.getString("IMAGE"));
                prod.setSTATE(rs.getString("STATE"));
                prod.setQUANTITY(rs.getInt("QUANTITY"));
                return prod;
            }
        }
        return null; // Return null if no product found with the given ID
    }

    // In your productservice class
    public List<product> displaySortedbyType() throws SQLException {
        String query = "SELECT TYPE, DESCRIPTION, IMAGE, STATE, QUANTITY FROM product ORDER BY TYPE";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            ResultSet res = pst.executeQuery();
            List<product> productList = new ArrayList<>();
            while (res.next()) {
                product p = new product(
                        res.getString("TYPE"),
                        res.getString("DESCRIPTION"),
                        res.getString("IMAGE"),
                        res.getString("STATE"),
                        res.getInt("QUANTITY")
                );
                productList.add(p);
            }
            return productList;
        }
    }


    public void incrementLikeCount(int ID_PRODUCT) throws SQLException {
        String query = "UPDATE rating SET ratingHeart = ratingHeart + 1 WHERE ID_PRODUCT = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, ID_PRODUCT);
        ps.executeUpdate();
        System.out.println("Liked workout with ID: " + ID_PRODUCT);
    }

    public void incrementDislikeCount(int ID_PRODUCT) throws SQLException {
        String query = "UPDATE rating SET ratingBrokenHeart = ratingBrokenHeart + 1 WHERE ID_PRODUCT = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, ID_PRODUCT);
        ps.executeUpdate();
        System.out.println("Disliked workout with ID: " + ID_PRODUCT);
    }
    public int getUserIdByName(String userName) throws SQLException {
        int userId = -1; // Default value indicating not found
        String query = "SELECT userid FROM user WHERE nameuser = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("userid");
                }
            }
        }
        return userId;
    }
    public String getUserNameById(int userId) throws SQLException {
        String userName = null;
        String query = "SELECT nameuser FROM user WHERE userid = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userName = rs.getString("nameuser");
                }
            }
        }
        return userName;
    }

}

