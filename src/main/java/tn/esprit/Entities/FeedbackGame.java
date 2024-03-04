package tn.esprit.Entities;

public class FeedbackGame {
    private int id_feedback;
    private int rating;
    private int userid;
    private int id_game;

    public FeedbackGame() {
    }

    public FeedbackGame(int id_feedback, int rating, int userid, int id_game) {
        this.id_feedback = id_feedback;
        this.rating = rating;
        this.userid = userid;
        this.id_game = id_game;
    }

    public FeedbackGame(int rating, int userid, int id_game) {
        this.rating = rating;
        this.userid = userid;
        this.id_game = id_game;
    }

    public int getid_game() {
        return id_game;
    }

    public void setid_game(int id_game) {
        this.id_game = id_game;
    }

    public int getId_feedback() {
        return id_feedback;
    }

    public void setId_feedback(int id_feedback) {
        this.id_feedback = id_feedback;
    }



    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }



    @Override
    public String toString() {
        return "Feedback{" +
                "id_feedback=" + id_feedback +
                ", rating=" + rating +
                ", userid=" + userid +
                ", id_game=" + id_game +
                '}';
    }
}

