package tn.esprit.services;

import tn.esprit.entities.workoutcategory;
import tn.esprit.entities.workouts;
import tn.esprit.utils.myDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class workoutsService implements IService <workouts> {
    Connection con;
    Statement stm;

    public workoutsService() {
        con = myDataBase.getInstance().getCon();
    }




    @Override
    public void add(workouts workouts) throws SQLException {
        String query = "INSERT INTO `workouts`( `workout_name`, `wk_description`, `wk_intensity`, `wk_image`, `coach_id`, `id_category`) VALUES (?,?,?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, workouts.getWorkout_name());
        ps.setString(2, workouts.getWk_description());
        ps.setString(3, workouts.getWk_intensity());
        ps.setString(4, workouts.getWk_image());
        ps.setInt(5, workouts.getCoach_id());
        ps.setInt(6, workouts.getId_category());
        ps.executeUpdate();
        System.out.println("workout added!");
    }

    @Override
    public void update(workouts workouts) throws SQLException {
        String query = "UPDATE `workouts` SET workout_name = ?, wk_description = ?, wk_intensity = ?, wk_image = ?, coach_id = ?, id_category = ? WHERE id_workout = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, workouts.getWorkout_name());
        ps.setString(2, workouts.getWk_description());
        ps.setString(3, workouts.getWk_intensity());
        ps.setString(4, workouts.getWk_image());
        ps.setInt(5, workouts.getCoach_id());
        ps.setInt(6, workouts.getId_category());
        ps.setInt(7, workouts.getId_workout());

        int rowsUpdated = ps.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("workout category updated successfully!");
        } else {
            System.out.println("Failed to update workout category.");
        }
    }

    @Override
    public void delete(int id_workout) throws SQLException {
        String query = "DELETE FROM `workouts` WHERE id_workout = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id_workout);
        ps.executeUpdate();
        System.out.println("workout deleted!");
    }

    @Override
    public  List<workouts> display() throws SQLException {
        String query = "SELECT id_workout, workout_name, wk_description , wk_intensity, wk_image, coach_id, id_category  FROM `workouts`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<workouts> workouts = new ArrayList<>();
        while (res.next()) {
            workouts e = new workouts (
                    res.getInt("id_workout"),
                    res.getString("workout_name"),
                    res.getString("wk_description"),
                    res.getString("wk_intensity"),
                    res.getString("wk_image"),
                    res.getInt("coach_id"),
                    res.getInt("id_category")
            );
            workouts.add(e);
        }
        return workouts;

    }

    public List<workouts> searchWorkout(String workout_name, String wk_description, String wk_intensity) throws SQLException {
        List<workouts> workouts = new ArrayList<>();
        String query = "SELECT * FROM workouts WHERE 1=1";

        if (workout_name != null && !workout_name.isEmpty()) {
            query += " AND workout_name = ?";
        }
        if (wk_description != null && !wk_description.isEmpty()) {
            query += " AND wk_description = ?";
        }
        if (wk_intensity != null && !wk_intensity.isEmpty()) {
            query += " AND wk_intensity = ?";
        }

        try (PreparedStatement statement = con.prepareStatement(query)) {
            int index = 1;
            if (workout_name != null && !workout_name.isEmpty()) {
                statement.setString(index++, workout_name);
            }
            if (wk_description != null && !wk_description.isEmpty()) {
                statement.setString(index++, wk_description);
            }
            if (wk_intensity != null && !wk_intensity.isEmpty()) {
                statement.setString(index, wk_intensity);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                workouts e = new workouts(
                        resultSet.getInt("id_workout"),
                        resultSet.getString("workout_name"),
                        resultSet.getString("wk_description"),
                        resultSet.getString("wk_intensity"),
                        resultSet.getString("wk_image"),
                        resultSet.getInt("coach_id"),
                        resultSet.getInt("id_category")
                );
                workouts.add(e);
            }
        }

        return workouts;
    }
    public List<workouts> displaySorted(String sortBy) throws SQLException {
        String query = "SELECT workout_name, wk_description,wk_intensity FROM `workouts` ORDER BY ";

        switch (sortBy) {
            case "workout_name":
                query += "workout_name";
                break;
            case "wk_description":
                query += "wk_description";
                break;
            case "wk_intensity":
                query += "wk_intensity";
                break;
            default:
                throw new IllegalArgumentException("Invalid sort criteria.");
        }
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<workouts> workouts = new ArrayList<>();
        while (res.next()) {
            workouts e = new workouts(
                    res.getInt("id_workout"),
                    res.getString("workout_name"),
                    res.getString("wk_description"),
                    res.getString("wk_intensity"),
                    res.getString("wk_image"),
                    res.getInt("coach_id"),
                    res.getInt("id_category")
            );
            workouts.add(e);
        }
        return workouts;
    }
    public int getWKNamecatCount(String workout_name) throws SQLException {
        String query = "SELECT COUNT(*) FROM workouts WHERE workout_name = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, workout_name);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        } else {
            throw new SQLException("Failed to get workout name count.");
        }

    }
    public List<workouts> displayByCategory(int id_category) throws SQLException {
        List<workouts> workoutsList = new ArrayList<>();
        String query = "SELECT * FROM workouts WHERE id_category = ?";

        try (PreparedStatement preparedStatement = myDataBase.getInstance().getCon().prepareStatement(query)) {
            preparedStatement.setInt(1, id_category);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id_workout = resultSet.getInt("id_workout");
                String workout_name = resultSet.getString("workout_name");
                String wk_description = resultSet.getString("wk_description");
                String wk_intensity = resultSet.getString("wk_intensity");
                String wk_image = resultSet.getString("wk_image");
                int coach_id = resultSet.getInt("coach_id");
                // Get other workout attributes as needed

                // Create a new workouts object and add it to the list
                workouts workout = new workouts(id_workout, workout_name, wk_description, wk_intensity, wk_image, coach_id, id_category);
                workoutsList.add(workout);
            }
        }

        return workoutsList;
    }


    public workouts getWorkoutsById(int WorkoutID) throws SQLException {
        workouts workout = null;
        String query = "SELECT * FROM workouts WHERE id_workout = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, WorkoutID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    workout = new workouts();
                    workout.setId_workout(resultSet.getInt("id_workout"));
                    workout.setWorkout_name(resultSet.getString("workout_name"));
                    workout.setWk_description(resultSet.getString("wk_description"));
                    workout.setWk_intensity(resultSet.getString("wk_intensity"));
                    workout.setWk_image(resultSet.getString("wk_image"));
                    workout.setCoach_id(resultSet.getInt("coach_id"));
                    workout.setId_category(resultSet.getInt("id_category"));
                }
            }
        }
        return workout;
    }
    public List<String> getCategoryNames() throws SQLException {
        List<String> userNames = new ArrayList<>();
        String query = "SELECT category_name FROM workoutcategory"; // Corrected column name to 'username'
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                userNames.add(rs.getString("category_name"));
            }
        }
        return userNames;
    }
    public int getcatIdByName(String userName) throws SQLException {
        int userId = -1; // Default value indicating not found
        String query = "SELECT id_category FROM workoutcategory WHERE category_name = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("id_category");
                }
            }
        }
        return userId;
    }
}
