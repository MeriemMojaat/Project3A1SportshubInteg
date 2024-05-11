package tn.esprit.Entities;

import javafx.scene.control.ComboBox;

import java.sql.SQLException;
import java.time.LocalDate;;

public class Booking {
    private int id_booking;
    private int id_event;
    private int userid;
    private LocalDate date_booking;
    private int nbParticipants_event;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Booking(int id_booking, int id_event, int userid, int nbParticipants_event) {
        this.id_booking = id_booking;
        this.id_event = id_event;
        this.userid = userid;
        this.nbParticipants_event = nbParticipants_event;
    }

    public Booking(int id_booking, int id_event, int userid, LocalDate date_booking, int nbParticipants_event) {
        this.id_booking = id_booking;
        this.id_event = id_event;
        this.userid = userid;
        this.date_booking = date_booking;
        this.nbParticipants_event = nbParticipants_event;}

    public Booking(int id_event, int userid, LocalDate date_booking, int nbParticipants_event) {
        this.id_event = id_event;
        this.userid = userid;
        this.date_booking = date_booking;
        this.nbParticipants_event = nbParticipants_event;
    }

    public Booking(int id_event, int userid, LocalDate date_booking, int nbParticipants_event, boolean status) {
        this.id_event = id_event;
        this.userid = userid;
        this.date_booking = date_booking;
        this.nbParticipants_event = nbParticipants_event;
        this.status = status;
    }

    public Booking(int userid, LocalDate date_booking, int nbParticipants_event) {
        this.userid = userid;
        this.date_booking = date_booking;
        this.nbParticipants_event = nbParticipants_event;
    }

    public Booking(LocalDate date_booking, int nbParticipants_event) {
        this.date_booking = date_booking;
        this.nbParticipants_event = nbParticipants_event;
    }

    public Booking() {

    }

    public int getId_booking() {
        return id_booking;
    }

    public void setId_booking(int id_booking) {
        this.id_booking = id_booking;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public LocalDate getDate_booking() {
        return date_booking;
    }


    public void setDate_booking(LocalDate date_booking) {
        this.date_booking = date_booking;
    }

    public int getNbParticipants_event() {
        return nbParticipants_event;
    }

    public void setNbParticipants_event(int nbParticipants_event) {
        this.nbParticipants_event = nbParticipants_event;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "date_booking=" + date_booking +
                ", nbParticipants_event=" + nbParticipants_event + '}';
    }


}
