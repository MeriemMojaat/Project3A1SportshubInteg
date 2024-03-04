package tn.esprit.Entities;

import java.util.Objects;

public class GameList {
    private int ID_GAME;
    private int ID_USER;

    public GameList(int ID_GAME, int ID_USER) {
        this.ID_GAME = ID_GAME;
        this.ID_USER = ID_USER;
    }

    public int getID_USER() {
        return ID_USER;
    }

    public void setID_USER(int ID_USER) {
        this.ID_USER = ID_USER;
    }

    public int getID_GAME() {
        return ID_GAME;
    }

    public void setID_GAME(int ID_GAME) {
        this.ID_GAME = ID_GAME;
    }


    @Override
    public String toString() {
        return "GameList{" +
                "ID_GAME=" + ID_GAME +
                ", ID_USER=" + ID_USER +
                '}';
    }
}
