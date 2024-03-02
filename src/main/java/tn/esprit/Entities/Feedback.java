package tn.esprit.entities;

import java.time.LocalDateTime;

public class Feedback {
    private int id_feedback;
    private int rating;
    private int userid;
    private int id_event;

    public Feedback() {
    }

    public Feedback(int id_feedback,int rating,int userid, int id_event) {
        this.id_feedback = id_feedback;
        this.rating = rating;
        this.userid = userid;
        this.id_event = id_event;
    }

    public Feedback( int rating, int userid, int id_event) {
        this.rating = rating;
        this.userid = userid;
        this.id_event = id_event;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
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
                ", id_event=" + id_event +
                '}';
    }
}

