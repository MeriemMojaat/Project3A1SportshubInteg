package tn.esprit.Entities;

import javafx.scene.control.TextArea;

public class product {
   private int ID_PRODUCT;
   private String TYPE;
  private   String DESCRIPTION;
   private String IMAGE;
    private String STATE;
   private int QUANTITY;

    private int likes;
    private int dislikes;


    public product(String TYPE, String DESCRIPTION, String IMAGE, String STATE, int QUANTITY) {
        this.TYPE = TYPE;
        this.DESCRIPTION = DESCRIPTION;
        this.IMAGE = IMAGE;
        this.STATE = STATE;
        this.QUANTITY = QUANTITY;
    }

    public product(int ID_PRODUCT, String TYPE, String DESCRIPTION, String IMAGE, String STATE, int QUANTITY) {
        this.ID_PRODUCT = ID_PRODUCT;

        this.TYPE = TYPE;
        this.DESCRIPTION = DESCRIPTION;
        this.IMAGE = IMAGE;
        this.STATE = STATE;
        this.QUANTITY = QUANTITY;
    }

    public product() {

    }

    public product(String text, String state, int quantity, String type, TextArea description) {
    }

    public int getID_PRODUCT() {
        return ID_PRODUCT;
    }

    public void setID_PRODUCT(int ID_PRODUCT) {
        this.ID_PRODUCT = ID_PRODUCT;
    }


    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }
    public int getQUANTITY() {
        return QUANTITY;
    }

    public void setQUANTITY(int QUANTITY) {
        this.QUANTITY = QUANTITY;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }


    @Override
    public String toString() {
        return "product{" +
                "ID_PRODUCT=" + ID_PRODUCT +
                ", TYPE='" + TYPE + '\'' +
                ", DESCRIPTION='" + DESCRIPTION + '\'' +
                ", IMAGE='" + IMAGE + '\'' +
                ", STATE='" + STATE + '\'' +
                ", QUANTITY=" + QUANTITY +
                '}';
    }
}

