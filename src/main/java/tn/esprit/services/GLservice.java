package tn.esprit.services;

import tn.esprit.Entities.GameList;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GLservice implements IServiceGame<GameList> {

    private Connection con;

    public GLservice() {
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(GameList gameList) throws SQLException {
        String insertQuery = "INSERT INTO gamelist (ID_GAME,ID_USER) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, gameList.getID_GAME());
            preparedStatement.setInt(2, gameList.getID_USER());
            preparedStatement.executeUpdate();
        }
    }

    public void update(GameList gameList) throws SQLException {
    }

    /*public void addUserToGame(int gameId, int userId) throws SQLException {
        String insertQuery = "INSERT INTO gameList (ID_USER, ID_GAME) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, gameId);
            preparedStatement.executeUpdate();
        }
    }

     */


    public void update(int gameid, int userid) throws SQLException {
        String updateQuery = "UPDATE gameList SET ID_USER = ? WHERE ID_GAME = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, gameid);
            preparedStatement.setInt(2, userid);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(int userId) throws SQLException {
        String query = "DELETE FROM `gamelist` WHERE ID_USER = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, userId);
        ps.executeUpdate();
        System.out.println("participant deleted!");
    }

    @Override
    public List<GameList> display() throws SQLException {
        List<GameList> gameLists = new ArrayList<>();
        String selectQuery = "SELECT * FROM gamelist";
        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            while (resultSet.next()) {
                int userId = resultSet.getInt("ID_USER");
                int gameId = resultSet.getInt("ID_GAME");
                gameLists.add(new GameList(userId, gameId));
            }
        }
        return gameLists;
    }

    public List<GameList> getGameListEntries(int gameId) throws SQLException {
        List<GameList> gameListEntries = new ArrayList<>();
        String query = "SELECT * FROM gameList WHERE ID_GAME = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, gameId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("ID_GAME");
                    gameListEntries.add(new GameList(userId, gameId));
                }
            }
        }
        return gameListEntries;
    }


  /*  public void addUserToGame(int userId, int gameId) throws SQLException {
        // Retrieve the game from the database based on gameId
        Game game = Gservice.getGameById(gameId);

        // Retrieve the user from the database based on userId
        user user = userservice.getUserById(userId);

        // Add the user to the game's list of users
        game.getUsers().add(user);

        // Update the game in the database to save the association
        Gservice.Update(game);

        // Create a new GameList entry
        GameList gameListEntry = new GameList(userId, user.getName(), user.getSurname(), gameId);

        // Add the entry to the GameList table
        GLservice.addGameList(gameListEntry);
    }

   */

    public static void addGameList(GameList gameList) throws SQLException {
        GLservice gameListService = new GLservice(); // Create an instance of GameListService
        gameListService.add(gameList); // Call the non-static method on the instance
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

    public List<String> getGameNames() throws SQLException {
        List<String> gameNames = new ArrayList<>();
        String query = "SELECT GAME_NAME FROM game"; // Corrected column name to 'username'
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                gameNames.add(rs.getString("GAME_NAME"));
            }
        }
        return gameNames;
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

    public String getGameNameById(int gameId) throws SQLException {
        String userName = null;
        String query = "SELECT GAME_NAME FROM game WHERE GAME_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, gameId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userName = rs.getString("GAME_NAME");
                }
            }
            return userName;
        }
    }

  /*  public List<GameList> searchworkout(String searchCriteria) throws SQLException {
        List<GameList> gl = new ArrayList<>();
        String query = "SELECT * FROM gamelist WHERE Name LIKE ? ";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, "%" + searchCriteria + "%");
            statement.setString(2, "%" + searchCriteria + "%");


            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                GameList cat = new GameList(
                        resultSet.getString("name"),
                        resultSet.getString("surname")
                );
                gl.add(cat);
            }
        }
        return gl;
    }

   */


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

    public String getemailByName(String gameName) throws SQLException {
        String email = ""; // Default value indicating not found
        String query = "SELECT useremail FROM user WHERE nameuser = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, gameName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    email = rs.getString("useremail");
                }
            }
        }
        return email;
    }


    public List<GameList> displayByGame(int ID_GAME) {
        List<GameList> participants = new ArrayList<>();
        String query = "SELECT * FROM gamelist WHERE ID_GAME = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, ID_GAME);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("ID_USER");
                int gameId = resultSet.getInt("ID_GAME");
                // Get other necessary attributes if needed

                // Create a new GameList object and add it to the list
                GameList gl = new GameList(gameId, userId);
                participants.add(gl);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print out the exception message for debugging
            throw new RuntimeException(e);
        }

        return participants;
    }

    public boolean hasParticipated(int userId, int gameId) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean participated = false;

        try {
            String query = "SELECT * FROM GameList WHERE ID_USER = ? AND ID_GAME = ?";
            stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, gameId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // The user has participated in the game
                participated = true;
            }
        } finally {
            // Close resources
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return participated;
    }


    public List<Integer> getParticipatedGames(int userId) throws SQLException {
        List<Integer> participatedGames = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT ID_USER FROM GameList WHERE ID_GAME = ?";
            stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            // Retrieve the game IDs of all games the user has participated in
            while (rs.next()) {
                int gameId = rs.getInt("ID_GAME");
                participatedGames.add(gameId);
            }
        } finally {
            // Close resources
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return participatedGames;
    }

}
