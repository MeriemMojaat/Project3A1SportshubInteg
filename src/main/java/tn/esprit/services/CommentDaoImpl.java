package tn.esprit.services;

import tn.esprit.Entities.Comment;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDaoImpl implements CommentDao {

    Connection con;

    public CommentDaoImpl() {
        con = MyDatabase.getInstance().getCon();
    }
    @Override
    public void SubmitComment(Comment comment) throws SQLException {
        String query = "INSERT INTO comment (user_id, game_id, commentt, date_c) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, comment.getUser_id());
            statement.setInt(2, comment.getgame_id());
            statement.setString(3, comment.getCommentt());
            statement.setString(4, comment.getDate_c());
            statement.executeUpdate();
        }
    }

    public List<String> getUsernamesForGame() throws SQLException {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT nameuser FROM user"; // Assuming 'users' table contains usernames

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("nameuser");
                usernames.add(username);
            }
        }
        return usernames;
    }
    @Override
    public List<Comment> getCommentsForGame(int gameId) throws SQLException {
        List<Comment> commentList = new ArrayList<>();
        String query = "SELECT * FROM comment WHERE game_id = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, gameId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Comment comment = new Comment();
                    comment.setId(resultSet.getInt("id"));
                    comment.setUser_id(resultSet.getInt("user_id"));
                    comment.setgame_id(resultSet.getInt("game_id"));
                    comment.setCommentt(resultSet.getString("commentt"));
                    comment.setDate_c(resultSet.getString("date_c"));
                    commentList.add(comment);
                }
            }
        }
        return commentList;
    }
}
