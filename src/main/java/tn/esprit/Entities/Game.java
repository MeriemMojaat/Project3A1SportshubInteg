package tn.esprit.Entities;

import java.time.LocalDate;
import java.util.List;

public class Game {
    private int GAME_ID; // Primary key
    private int CREATOR_ID; // Foreign key referencing user table
    private String GAME_NAME;
    private Integer NBR_PAR; // Number of participants
    private String TYPE;
    private LocalDate DATE_G;

    private LocalDate DATE_EG;
    private String GPLACE;

    public Game(){
    }


    // Constructors, getters, and setters omitted for brevity

    public Game(String GAME_NAME, Integer NBR_PAR, String TYPE, LocalDate DATE_G, LocalDate DATE_EG, String GPLACE) {
        this.GAME_NAME = GAME_NAME;
        this.NBR_PAR = NBR_PAR;
        this.TYPE = TYPE;
        this.DATE_G = DATE_G;
        this.DATE_EG = DATE_EG;
        this.GPLACE = GPLACE;
    }

    public Game(int GAME_ID, int CREATOR_ID, String GAME_NAME, Integer NBR_PAR, String TYPE, LocalDate DATE_G, LocalDate DATE_EG, String GPLACE) {
        this.GAME_ID = GAME_ID;
        this.CREATOR_ID = CREATOR_ID;
        this.GAME_NAME = GAME_NAME;
        this.NBR_PAR = NBR_PAR;
        this.TYPE = TYPE;
        this.DATE_G = DATE_G;
        this.DATE_EG = DATE_EG;
        this.GPLACE = GPLACE;
    }
    public Game(int CREATOR_ID, String GAME_NAME,  Integer NBR_PAR, String TYPE, LocalDate DATE_G,LocalDate DATE_EG,String GPLACE) {
        this.CREATOR_ID = CREATOR_ID;
        this.GAME_NAME = GAME_NAME;
        this.NBR_PAR = NBR_PAR;
        this.TYPE = TYPE;
        this.DATE_G = DATE_G;
        this.DATE_EG = DATE_EG;
        this.GPLACE = GPLACE;
    }

    public Game(int CREATOR_ID, String GAME_NAME, String TYPE, LocalDate DATE_G,LocalDate DATE_EG, String GPLACE) {
        this.CREATOR_ID = CREATOR_ID;
        this.GAME_NAME = GAME_NAME;
        this.TYPE = TYPE;
        this.DATE_G = DATE_G;
        this.DATE_EG = DATE_EG;
        this.GPLACE = GPLACE;
    }

    public Game(int GAME_ID, int creatorId, String GAME_NAME , String TYPE, LocalDate DATE_G, LocalDate DATE_EG,String GPLACE, List<user> users, Integer NBR_PAR) {
        this.GAME_ID = GAME_ID;
        this.CREATOR_ID = creatorId;
        this.GAME_NAME = GAME_NAME;
        this.NBR_PAR = NBR_PAR;
        this.TYPE = TYPE;
        this.DATE_G = DATE_G;
        this.DATE_EG = DATE_EG;
        this.GPLACE = GPLACE;
    }

    public Game(String GAME_NAME, String TYPE, String GPLACE, LocalDate DATE_G ,LocalDate DATE_EG , Integer NBR_PAR) {
        this.GAME_NAME = GAME_NAME;
        this.NBR_PAR = NBR_PAR;
        this.TYPE = TYPE;
        this.DATE_G = DATE_G;
        this.DATE_EG = DATE_EG;
        this.GPLACE = GPLACE;
    }

    public int getGAME_ID() {
        return GAME_ID;
    }

    public void setGAME_ID(int GAME_ID) {
        this.GAME_ID = GAME_ID;
    }

    public int getCREATOR_ID() {
        return CREATOR_ID;
    }

    public void setCREATOR_ID(int CREATOR_ID) {
        this.CREATOR_ID = CREATOR_ID;
    }

    public String getGAME_NAME() {
        return GAME_NAME;
    }

    public void setGAME_NAME(String GAME_NAME) {
        this.GAME_NAME = GAME_NAME;
    }

    public Integer getNBR_PAR() {
        return NBR_PAR;
    }

    public void setNBR_PAR(Integer NBR_PAR) {
        this.NBR_PAR = NBR_PAR;
    }


    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public LocalDate getDATE_G() {
        return DATE_G;
    }

    public void setDATE_G(LocalDate DATE_G) {
        this.DATE_G = DATE_G;
    }

    public String getGPLACE() {
        return GPLACE;
    }

    public void setGPLACE(String GPLACE) {
        this.GPLACE = GPLACE;
    }

    public LocalDate getDATE_EG() {
        return DATE_EG;
    }

    public void setDATE_EG(LocalDate DATE_EG) {
        this.DATE_EG = DATE_EG;
    }

    @Override
    public String toString() {
        return "Game{" +
                "GAME_ID=" + GAME_ID +
                ", CREATOR_ID=" + CREATOR_ID +
                ", GAME_NAME='" + GAME_NAME + '\'' +
                ", NBR_PAR=" + NBR_PAR +
                ", TYPE='" + TYPE + '\'' +
                ", DATE_G=" + DATE_G +
                ", DATE_EG=" + DATE_EG +
                ", GPLACE='" + GPLACE + '\'' +
                '}';
    }
}
