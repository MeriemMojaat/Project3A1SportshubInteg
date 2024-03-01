package tn.esprit.entities;

import com.sun.jdi.PrimitiveValue;

public class trade {
    private int ID_TRADE;
    private int ID_PRODUCT;
    private int ID_USER;
    private String LOCATION;
    private String TRADESTATUS;
    private String NAME;

    public trade(int ID_TRADE, int ID_PRODUCT, int ID_USER, String LOCATION, String TRADESTATUS, String NAME) {
        this.ID_TRADE = ID_TRADE;
        this.ID_PRODUCT = ID_PRODUCT;
        this.ID_USER = ID_USER;
        this.LOCATION = LOCATION;
        this.TRADESTATUS = TRADESTATUS;
        this.NAME = NAME;
    }

    public trade() {
        // Initialize attributes if necessary
    }
    public trade(int ID_PRODUCT, int ID_USER, String LOCATION, String TRADESTATUS, String NAME) {
        this.ID_PRODUCT = ID_PRODUCT;
        this.ID_USER = ID_USER;
        this.LOCATION = LOCATION;
        this.TRADESTATUS = TRADESTATUS;
        this.NAME = NAME;
    }


    public int getID_TRADE() {
        return ID_TRADE;
    }

    public void setID_TRADE(int ID_TRADE) {
        this.ID_TRADE = ID_TRADE;
    }

    public int getID_PRODUCT() {
        return ID_PRODUCT;
    }

    public void setID_PRODUCT(int ID_PRODUCT) {
        this.ID_PRODUCT = ID_PRODUCT;
    }

    public int getID_USER() {
        return ID_USER;
    }

    public void setID_USER(int ID_USER) {
        this.ID_USER = ID_USER;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getTRADESTATUS() {
        return TRADESTATUS;
    }

    public void setTRADESTATUS(String TRADESTATUS) {
        this.TRADESTATUS = TRADESTATUS;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    @Override
    public String toString() {
        return "trade{" +
                "ID_TRADE=" + ID_TRADE +
                ", ID_PRODUCT=" + ID_PRODUCT +
                ", ID_USER=" + ID_USER +
                ", LOCATION='" + LOCATION + '\'' +
                ", TRADESTATUS='" + TRADESTATUS + '\'' +
                ", NAME='" + NAME + '\'' +
                '}';
    }
}

