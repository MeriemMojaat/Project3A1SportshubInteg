package tn.esprit.Entities;

public class ratingproduct {
    private int id_feedback;
    private int ratingHeart;
    private int ratingBrokenHeart;
    private int ID_PRODUCT;
    private int userid;

    public int getId_feedback() {
        return id_feedback;
    }

    public void setId_feedback(int id_feedback) {
        this.id_feedback = id_feedback;
    }

    public int getRatingHeart() {
        return ratingHeart;
    }

    public void setRatingHeart(int ratingHeart) {
        this.ratingHeart = ratingHeart;
    }

    public int getRatingBrokenHeart() {
        return ratingBrokenHeart;
    }

    public void setRatingBrokenHeart(int ratingBrokenHeart) {
        this.ratingBrokenHeart = ratingBrokenHeart;
    }

    public int getID_PRODUCT() {
        return ID_PRODUCT;
    }

    public void setID_PRODUCT(int ID_PRODUCT) {
        this.ID_PRODUCT = ID_PRODUCT;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public ratingproduct(int id_feedback, int ratingHeart, int ratingBrokenHeart, int ID_PRODUCT, int userid) {
        this.id_feedback = id_feedback;
        this.ratingHeart = ratingHeart;
        this.ratingBrokenHeart = ratingBrokenHeart;
        this.ID_PRODUCT = ID_PRODUCT;
        this.userid = userid;
    }

    public ratingproduct(int ratingHeart, int ratingBrokenHeart, int ID_PRODUCT, int userid) {
        this.ratingHeart = ratingHeart;
        this.ratingBrokenHeart = ratingBrokenHeart;
        this.ID_PRODUCT = ID_PRODUCT;
        this.userid = userid;
    }

    public ratingproduct() {

    }

    @Override
    public String toString() {
        return "Feedbackproduct{" +
                "id_feedback=" + id_feedback +
                ", ratingHeart=" + ratingHeart +
                ", ratingBrokenHeart=" + ratingBrokenHeart +
                ", ID_PRODUCT=" + ID_PRODUCT +
                ", userid=" + userid +
                '}';
    }
}
