package tn.esprit.Entities;

public class workoutcategory {

    private int id_category;
    private String category_name;
    private  String cat_description;
    private String cat_image;
    private int coachid;

    public workoutcategory(int id_category, String category_name, String cat_description, String cat_image, int coachid) {
        this.id_category = id_category;
        this.category_name = category_name;
        this.cat_description = cat_description;
        this.cat_image = cat_image;
        this.coachid = coachid;
    }

    public workoutcategory(String category_name, String cat_description, String cat_image, int coachid) {
        this.category_name = category_name;
        this.cat_description = cat_description;
        this.cat_image = cat_image;
        this.coachid = coachid;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCat_description() {
        return cat_description;
    }

    public void setCat_description(String cat_description) {
        this.cat_description = cat_description;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }

    public int getCoachid() {
        return coachid;
    }

    public void setCoachid(int coachid) {
        this.coachid = coachid;
    }



    public workoutcategory(){}

}
