package tn.esprit.Entities;

public class FeedbackWorkout {
    private int id_feedback;
    private int userid;
    private int id_workout;
    private int like_count;
    private int dislike_count;


    public FeedbackWorkout() {
    }

    public FeedbackWorkout(int id_feedback, int userid, int id_workout, int like_count, int dislike_count) {
        this.id_feedback = id_feedback;
        this.userid = userid;
        this.id_workout = id_workout;
        this.like_count = like_count;
        this.dislike_count = dislike_count;
    }

    public FeedbackWorkout(int userid, int id_workout, int like_count, int like_diskount) {
        this.userid = userid;
        this.id_workout = id_workout;
        this.like_count = like_count;
        this.dislike_count = dislike_count;
    }

    public int getId_feedback() {
        return id_feedback;
    }

    public void setId_feedback(int id_feedback) {
        this.id_feedback = id_feedback;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getId_workout() {
        return id_workout;
    }

    public void setId_workout(int id_workout) {
        this.id_workout = id_workout;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getDislike_count() {
        return dislike_count;
    }

    public void setDislike_count(int dislike_count) {
        this.dislike_count = dislike_count;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id_feedback=" + id_feedback +
                ", userid=" + userid +
                ", id_workout=" + id_workout +
                ", like_count=" + like_count +
                ", dislike_count=" + dislike_count +
                '}';
    }
}
