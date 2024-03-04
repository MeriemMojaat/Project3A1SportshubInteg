package tn.esprit.services;

import tn.esprit.Entities.Comment;

import java.sql.SQLException;
import java.util.List;

public interface CommentDao {

    List<Comment> getCommentsForGame(int gameId) throws SQLException;

    // Method to add a new comment
    void SubmitComment(Comment comment) throws SQLException;

    // Method to update an existing comment
   // boolean updateComment(Comment comment);

    // Method to delete a comment
   // boolean deleteComment(int commentId);
}
