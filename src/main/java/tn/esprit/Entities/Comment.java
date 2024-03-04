package tn.esprit.Entities;

public class Comment {
    int id , user_id, game_id;
    String commentt;
    String date_c;

    public Comment(int user_id, int game_id, String commentt, String date_c) {
        this.user_id = user_id;
        this.game_id = game_id;
        this.commentt = commentt;
        this.date_c = date_c;
    }

    public Comment( int user_id, int game_id, String commentt) {
        this.user_id = user_id;
        this.game_id = game_id;
        this.commentt = commentt;
    }

    public Comment(int id, String commentt) {
        this.id = id;
        this.commentt = commentt;
    }

    public Comment(String commentt) {
        this.commentt = commentt;
    }

    public Comment(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getgame_id() {
        return game_id;
    }

    public void setgame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getCommentt() {
        return commentt;
    }

    public void setCommentt(String commentt) {
        this.commentt = commentt;
    }

    public String getDate_c() {
        return date_c;
    }

    public void setDate_c(String date_c) {
        this.date_c = date_c;
    }

    @Override
    public String toString() {
        return "Comment{" +
                ", user_id=" + user_id +
                ", game_id=" + game_id +
                ", commentt='" + commentt + '\'' +
                ", date_c='" + date_c + '\'' +
                '}';
    }
}
