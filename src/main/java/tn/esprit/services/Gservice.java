package tn.esprit.services;

import tn.esprit.Entities.Game;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Gservice implements IServiceGame<Game> {

    Connection con;
    Statement stm;


    public Gservice(){con= MyDatabase.getInstance().getCon();}

    public void add(Game game) throws SQLException {
        System.out.println("Debug: Entering add method");

        // Check if the game object is null
        if (game == null) {
            System.out.println("Debug: Game object is null");
            return;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate enteredDate = game.getDATE_G();

        // Check if the entered date is older than the current date
        if (enteredDate.isBefore(currentDate)) {
            System.out.println("Debug: The entered date is older than the current date. Please enter a valid date.");
            return;
        }

        System.out.println("Debug: Inserting game into the database");

        String insertGameQuery = "INSERT INTO game (CREATOR_ID, GAME_NAME, NBR_PAR, TYPE, DATE_G, DATE_EG, GPLACE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(insertGameQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, game.getCREATOR_ID());
            preparedStatement.setString(2, game.getGAME_NAME());
            preparedStatement.setInt(3, game.getNBR_PAR());
            preparedStatement.setString(4, game.getTYPE());
            preparedStatement.setDate(5, Date.valueOf(game.getDATE_G()));
            preparedStatement.setDate(6, Date.valueOf(game.getDATE_EG()));
            preparedStatement.setString(7, game.getGPLACE());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    game.setGAME_ID(generatedKeys.getInt(1));

                    System.out.println("Game added successfully with GAME_ID: " + game.getGAME_ID());

                    // Debug: Check if userService is properly instantiated
                    //userservice userService = new userservice();
                    System.out.println("Debug: userService instantiated successfully");

                    if (game.getGAME_ID() > 0) {
                       // Session.setCurrentGameId(game.getGAME_ID());
                    }
                } else {
                    System.out.println("Failed to retrieve generated keys after inserting game!");
                }
            } else {
                System.out.println("Failed to add game!");
            }
        }
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


    public List<Game> display() throws SQLException {
        List<Game> games = new ArrayList<>();
        String selectQuery = "SELECT CREATOR_ID, GAME_NAME, NBR_PAR, TYPE, DATE_G, DATE_EG, GPLACE FROM game";

        try (Statement statement = con.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                int creatorId = resultSet.getInt("CREATOR_ID");
                String gameName = resultSet.getString("GAME_NAME");
                int nbrPar = resultSet.getInt("NBR_PAR");
                String type = resultSet.getString("TYPE");
                LocalDate date = resultSet.getDate("DATE_G").toLocalDate();
                LocalDate edate = resultSet.getDate("DATE_EG").toLocalDate();
                String place = resultSet.getString("GPLACE");

                Game game = new Game(creatorId, gameName, nbrPar, type, date, edate,place);
                games.add(game);
            }
        }

        return games;
    }

    public void update(Game updatedGame) throws SQLException {
        String updateQuery = "UPDATE game SET  GAME_NAME=?, TYPE=?, GPLACE=?, DATE_G=?,DATE_EG=?, NBR_PAR=? WHERE GAME_ID=?";

        try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, updatedGame.getGAME_NAME());
            preparedStatement.setString(2, updatedGame.getTYPE());
            preparedStatement.setString(3, updatedGame.getGPLACE());
            preparedStatement.setDate(4, java.sql.Date.valueOf(updatedGame.getDATE_G()));
            preparedStatement.setDate(5, java.sql.Date.valueOf(updatedGame.getDATE_EG()));
            preparedStatement.setInt(6, updatedGame.getNBR_PAR());
            preparedStatement.setInt(7, updatedGame.getGAME_ID());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update game. Game ID " + updatedGame.getGAME_ID() + " not found.");
            }
        }
    }

    public static void Update(Game updatedGame) throws SQLException {
        Gservice gservice = new Gservice();
        gservice.update(updatedGame);
    }


  /*  public void delete(Game game) throws SQLException {
        // Debug: Print game details before deletion
        System.out.println("Deleting game: " + game.toString());

        // Delete the game from the game table
        String deleteGameQuery = "DELETE FROM game WHERE GAME_ID = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(deleteGameQuery)) {
            preparedStatement.setInt(1, game.getGAME_ID());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Game with ID " + game.getGAME_ID() + " deleted successfully from the game table!");
            } else {
                System.out.println("Failed to delete game with ID " + game.getGAME_ID() + " from the game table. Game not found.");
                return; // Exit method if game deletion fails
            }
        } catch (SQLException ex) {
            // Debug: Print exception message
            System.out.println("SQLException occurred: " + ex.getMessage());
            // Rethrow the exception to handle it at a higher level
            throw ex;
        }
    }


   */


    private void deleteRelatedEntries(int gameId) throws SQLException {
        // Example: Deleting entries from a table named 'related_table' where 'GAME_ID' is a foreign key
        String deleteRelatedEntriesQuery = "DELETE FROM gameList WHERE GAME_ID = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(deleteRelatedEntriesQuery)) {
            preparedStatement.setInt(1, gameId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Related entries deleted successfully from the related_table for game with ID " + gameId + "!");
            } else {
                System.out.println("No related entries found in the related_table for game with ID " + gameId + ".");
            }
        }
    }

    public void delete(int gameId) throws SQLException {
        String query = "DELETE FROM `game` WHERE GAME_ID  = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, gameId);
        ps.executeUpdate();
        System.out.println("game deleted!");
    }

    public Game DisplayGame(int gameId) throws SQLException {
        String query = "SELECT * FROM game WHERE GAME_ID = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int creatorId = resultSet.getInt("CREATOR_ID");
                String gameName = resultSet.getString("GAME_NAME");
                int nbrPar = resultSet.getInt("NBR_PAR");
                String type = resultSet.getString("TYPE");
                LocalDate date = resultSet.getDate("DATE_G").toLocalDate();
                LocalDate edate = resultSet.getDate("DATE_EG").toLocalDate();
                String place = resultSet.getString("GPLACE");

                return new Game(gameId,creatorId, gameName,  nbrPar, type, date,edate, place);
            }
        }

        return null; // Game not found
    }


    public List<Game> displayG() throws SQLException {
        List<Game> games = new ArrayList<>();
        String selectQuery = "SELECT GAME_ID, CREATOR_ID, GAME_NAME, TYPE, GPLACE, DATE_G,DATE_EG, NBR_PAR FROM game";

        try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int gameId = resultSet.getInt("GAME_ID");
                int creatorId = resultSet.getInt("CREATOR_ID");
                String gameName = resultSet.getString("GAME_NAME");
                String type = resultSet.getString("TYPE");
                String place = resultSet.getString("GPLACE");
                LocalDate date = resultSet.getDate("DATE_G").toLocalDate();
                LocalDate edate = resultSet.getDate("DATE_EG").toLocalDate();
                int nbrPar = resultSet.getInt("NBR_PAR");

                Game game = new Game(gameId, creatorId, gameName, nbrPar, type, date, edate, place);
                games.add(game);
            }
        }

        return games;
    }




    public List<Game> displaySorted(String sortBy) throws SQLException {
        String query = "SELECT CREATOR_ID, GAME_NAME, NBR_PAR, TYPE, DATE_G, DATE_EG, GPLACE FROM game ORDER BY ";

        switch (sortBy) {
            case "game name":
                query += "GAME_NAME";
                break;
            case "type":
                query += "TYPE";
                break;
            case "start date":
                query += "DATE_G";
                break;
            default:
                throw new IllegalArgumentException("Invalid sort criteria.");
        }

        List<Game> games = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    int creatorId = resultSet.getInt("CREATOR_ID");
                    String gameName = resultSet.getString("GAME_NAME");
                    int nbrPar = resultSet.getInt("NBR_PAR");
                    String type = resultSet.getString("TYPE");
                    LocalDate date = resultSet.getDate("DATE_G").toLocalDate();
                    LocalDate edate = resultSet.getDate("DATE_EG").toLocalDate();
                    String place = resultSet.getString("GPLACE");
                    Game e = new Game(creatorId, gameName, nbrPar, type, date, edate, place);
                    games.add(e);
                }
            }
        }
        return games;
    }


    public List<Game> searchGames(String searchTerm) throws SQLException {
        List<Game> games = new ArrayList<>();
        String searchQuery = "SELECT * FROM game WHERE GAME_NAME LIKE ? OR TYPE LIKE ? OR GPLACE LIKE ? OR DATE_G LIKE ? OR NBR_PAR LIKE ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(searchQuery)) {
            for (int i = 1; i <= 5; i++) {
                preparedStatement.setString(i, "%" + searchTerm + "%");
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int creatorId = resultSet.getInt("CREATOR_ID");
                String gameName = resultSet.getString("GAME_NAME");
                String type = resultSet.getString("TYPE");
                String place = resultSet.getString("GPLACE");
                LocalDate date = resultSet.getDate("DATE_G").toLocalDate();
                LocalDate edate = resultSet.getDate("DATE_EG").toLocalDate();
                int nbrPar = resultSet.getInt("NBR_PAR");

                Game game =new Game( creatorId, gameName, nbrPar, type, date,edate, place);
                games.add(game);
            }
        }

        return games;
    }

    public static Game getGameById(int gameId) throws SQLException {
        Gservice gservice = new Gservice(); // Create an instance of Gservice
        return gservice.DisplayGame(gameId); // Call the non-static method on the instance
    }

    public List<String> getUserNames() throws SQLException {
        List<String> userNames = new ArrayList<>();
        String query = "SELECT nameuser FROM user"; // Corrected column name to 'username'
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                userNames.add(rs.getString("nameuser"));
            }
        }
        return userNames;
    }

    public int getGameIdByName(String gameName) throws SQLException {
        int gameId = -1; // Default value indicating not found
        String query = "SELECT GAME_ID FROM game WHERE GAME_NAME = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, gameName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    gameId = rs.getInt("GAME_ID");
                }
            }
        }
        return gameId;
    }

    public boolean isGameExists(String gameName) {
        String query = "SELECT COUNT(*) FROM game WHERE GAME_NAME = ? ";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, gameName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<Game> getGamesByUserId(int userId) throws SQLException {
        List<Game> games = new ArrayList<>();
        String query = "SELECT * FROM game WHERE CREATOR_ID = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String gameName = resultSet.getString("GAME_NAME");
                int nbrPar = resultSet.getInt("NBR_PAR");
                String type = resultSet.getString("TYPE");
                LocalDate date = resultSet.getDate("DATE_G").toLocalDate();
                LocalDate endDate = resultSet.getDate("DATE_EG").toLocalDate();
                String place = resultSet.getString("GPLACE");

                // Create a new Game object with the retrieved data
                Game game = new Game(userId, gameName, nbrPar, type, date, endDate, place);

                // Add the Game object to the list
                games.add(game);
            }
        }

        return games;
    }
}











