package tn.esprit.Entities;

public class trade {
    private int ID_TRADE;
    private int ID_PRODUCT;
    private int userid;
    private String LOCATION;
    private String TRADESTATUS;
    private String NAME;

    public trade(int ID_TRADE, int ID_PRODUCT, int userid, String LOCATION, String TRADESTATUS, String NAME) {
        this.ID_TRADE = ID_TRADE;
        this.ID_PRODUCT = ID_PRODUCT;
        this.userid = userid;
        this.LOCATION = LOCATION;
        this.TRADESTATUS = TRADESTATUS;
        this.NAME = NAME;
    }

    public trade() {
        // Initialize attributes if necessary
    }
    public trade(int ID_PRODUCT, int userid, String LOCATION, String TRADESTATUS, String NAME) {
        this.ID_PRODUCT = ID_PRODUCT;
        this.userid = userid;
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

    public int getuserid() {
        return userid;
    }

    public void setuserid(int userid) {
        this.userid = userid;
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
                ", userid=" + userid +
                ", LOCATION='" + LOCATION + '\'' +
                ", TRADESTATUS='" + TRADESTATUS + '\'' +
                ", NAME='" + NAME + '\'' +
                '}';
    }
}

