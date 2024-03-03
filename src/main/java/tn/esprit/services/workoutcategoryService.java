package tn.esprit.services;

import javafx.scene.control.Alert;
import tn.esprit.Entities.workoutcategory;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class workoutcategoryService implements IServicewk<workoutcategory> {
        Connection con;
        Statement stm;

        public workoutcategoryService() {
            con = MyDatabase.getInstance().getCon();
        }

    public static boolean isValidCategoryName(String categoryName) {
        return categoryName.length() <= 10;
    }
    private boolean isAlphaNumeric(String str) {
        return str != null && str.matches("^[a-zA-Z]+$");
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }



    @Override
        public void add(workoutcategory workoutcategory) throws SQLException {
            String query = "INSERT INTO `workoutcategory`( `category_name`, `cat_description`, `cat_image`,`coachid`) VALUES (?,?,?,?)";

        if (!isValidCategoryName(workoutcategory.getCategory_name())) {
            throw new IllegalArgumentException("You shouldn't exceed 10 characters .");
        }
        if (!isAlphaNumeric(workoutcategory.getCategory_name())) {
            throw new IllegalArgumentException("The name must be alphanumeric .");
        }

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, workoutcategory.getCategory_name());
            ps.setString(2, workoutcategory.getCat_description());
            ps.setString(3, workoutcategory.getCat_image());
             ps.setInt(4, workoutcategory.getCoachid());

        ps.executeUpdate();
            System.out.println("workout category added!");
        }

        @Override
        public void update(workoutcategory workoutcategory) throws SQLException {
            String query = "UPDATE `workoutcategory` SET category_name = ?, cat_description = ?, cat_image = ? WHERE id_category = ? AND coachid = ?";
            PreparedStatement ps = con.prepareStatement(query);

            if (!isValidCategoryName(workoutcategory.getCategory_name())) {
                throw new IllegalArgumentException("You shouldn't exceed 10 characters .");
            }
            ps.setString(1, workoutcategory.getCategory_name());
            ps.setString(2, workoutcategory.getCat_description());
            ps.setString(3, workoutcategory.getCat_image());
            ps.setInt(4, workoutcategory.getId_category());
            ps.setInt(5, workoutcategory.getCoachid());


            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("workout category updated successfully!");
            } else {
                System.out.println("Failed to update workout category.");
            }
        }

        @Override
        public void delete(int id_category) throws SQLException {
            String query = "DELETE FROM `workoutcategory` WHERE id_category = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id_category);
            ps.executeUpdate();
            System.out.println("workout category deleted!");
        }

        @Override
        public List<workoutcategory> display() throws SQLException {
            String query = "SELECT id_category, category_name, cat_description, cat_image,coachid  FROM `workoutcategory`";

            stm = con.createStatement();
            ResultSet res = stm.executeQuery(query);
            List<workoutcategory> workoutcategory = new ArrayList<>();
            while (res.next()) {
                workoutcategory e = new workoutcategory (
                        res.getInt("id_category"),
                        res.getString("category_name"),
                        res.getString("cat_description"),
                        res.getString("cat_image"),
                        res.getInt("coachid")

                        );
                workoutcategory.add(e);
            }
            return workoutcategory;

        }
    public int getCategoryIdByName(String categoryName) throws SQLException {
        String query = "SELECT id_category FROM workoutcategory WHERE category_name = ?";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, categoryName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_category");
                } else {
                    // Handle case where category name is not found
                    throw new IllegalArgumentException("Category not found for name: " + categoryName);
                }
            }
        }
    }
        public List<workoutcategory> searchCategory(String category_name) throws SQLException {
            List<workoutcategory> workoutcategory = new ArrayList<>();
            String query = "SELECT * FROM workoutcategory WHERE 1=1";

            if (category_name != null && !category_name.isEmpty()) {
                query += " AND category_name = ?";
            }

            try (PreparedStatement statement = con.prepareStatement(query)) {
                int index = 1;
                if (category_name != null && !category_name.isEmpty()) {
                    statement.setString(index++, category_name);
                }
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    workoutcategory e = new workoutcategory(
                            resultSet.getString("category_name"),
                            resultSet.getString("cat_description"),
                            resultSet.getString("cat_image"),
                            resultSet.getInt("coachid")


                            );
                    workoutcategory.add(e);
                }
            }
            return workoutcategory;
        }
    public List<workoutcategory> searchcategory(String searchCriteria) throws SQLException {
        List<workoutcategory> events = new ArrayList<>();
        String query = "SELECT * FROM workoutcategory WHERE category_name LIKE ? ";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, "%" + searchCriteria + "%");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                workoutcategory cat = new workoutcategory(
                        resultSet.getString("category_name"),
                        resultSet.getString("cat_description"),
                        resultSet.getString("cat_image"),
                        resultSet.getInt("coachid")

                        );
                events.add(cat);
            }
        }

        return events;
    }

    public List<workoutcategory> displaySorted(String sortBy) throws SQLException {
            String query = "SELECT * FROM `workoutcategory` ORDER BY ";

            switch (sortBy) {
                case "category_name":
                    query += "category_name";
                    break;
                case "cat_description":
                    query += "cat_description";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sort criteria.");
            }

            stm = con.createStatement();
            ResultSet res = stm.executeQuery(query);
            List<workoutcategory> workoutcategory = new ArrayList<>();
            while (res.next()) {
                workoutcategory e = new workoutcategory(
                        res.getString("category_name"),
                        res.getString("cat_description"),
                        res.getString("cat_image"),
                        res.getInt("coachid")

                );
                workoutcategory.add(e);
            }
            return workoutcategory;
        }
        public int getNamecatCount(String category_name) throws SQLException {
            String query = "SELECT COUNT(*) FROM workoutcategory WHERE category_name = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, category_name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Failed to get category name count.");
            }

        }
    public boolean exists(int id_category) throws SQLException {
        String query = "SELECT id_category FROM workoutcategory WHERE id_category = ?";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, id_category);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Return true if a category with the given ID exists
            }
        }
    }


    public workoutcategory getCategoryById(int CategoryId) throws SQLException {
        workoutcategory wkcategory = null;
        String query = "SELECT * FROM workoutcategory WHERE id_category = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, CategoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    wkcategory = new workoutcategory();
                    wkcategory.setId_category(resultSet.getInt("id_category"));
                    wkcategory.setCategory_name(resultSet.getString("category_name"));
                    wkcategory.setCat_description(resultSet.getString("cat_description"));
                    wkcategory.setCat_image(resultSet.getString("cat_image"));

                }
            }
        }
        return wkcategory;
    }

    }

