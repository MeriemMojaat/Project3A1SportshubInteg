package tn.esprit.Entities;

import java.time.LocalDate;


public class Event {
    private int id_event;
    private String name_event;
    private String type_event;
    private String Space;
    private  String gender_event;
    private LocalDate startDate_event;
    private LocalDate endDate_event;
    private String localisation_event;
    private String description_event;
    private float price ;
    private int adminid_event;
    public Event(int id_event, String name_event, String type_event, String Space, String gender_event, LocalDate startDate_event, LocalDate endDate_event, String localisation_event, String description_event,float price,int adminid_event) {
        this.id_event = id_event;
        this.name_event = name_event;
        this.type_event = type_event;
        this.Space = Space;
        this.gender_event = gender_event;
        this.startDate_event = startDate_event;
        this.endDate_event = endDate_event;
        this.localisation_event = localisation_event;
        this.description_event = description_event;
        this.price=price;
        this.adminid_event=adminid_event;
    }

    public Event(String name_event, String type_event, String Space, String gender_event, LocalDate startDate_event, LocalDate endDate_event, String localisation_event, String description_event,float price,int adminid_event) {
        this.name_event = name_event;
        this.type_event = type_event;
        this.Space = Space;
        this.gender_event = gender_event;
        this.startDate_event = startDate_event;
        this.endDate_event = endDate_event;
        this.localisation_event = localisation_event;
        this.description_event = description_event;
        this.price=price;
        this.adminid_event=adminid_event;
    }

    public Event() {

    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public String getName_event() {
        return name_event;
    }

    public void setName_event(String name_event) {
        this.name_event = name_event;
    }

    public String getType_event() {
        return type_event;
    }

    public void setType_event(String type_event) {
        this.type_event = type_event;
    }

    public String getSpace() {
        return Space;
    }

    public void setSpace(String Space) {
            this.Space = Space;

    }

    public String getGender_event() {
        return gender_event;
    }

    public void setGender_event(String gender_event) {
        this.gender_event = gender_event;
    }

    public LocalDate getStartDate_event() {
        return startDate_event;
    }

    public void setStartDate_event(LocalDate startDate_event) {
        this.startDate_event = startDate_event;
    }

    public LocalDate getEndDate_event() {
        return endDate_event;
    }

    public void setEndDate_event(LocalDate endDate_event) {
        this.endDate_event = endDate_event;
    }

    public String getLocalisation_event() {
        return localisation_event;
    }

    public void setLocalisation_event(String localisation_event) {
        this.localisation_event = localisation_event;
    }

    public String getDescription_event() {
        return description_event;
    }

    public void setDescription_event(String description_event) {
        this.description_event = description_event;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAdminid_event() {
        return adminid_event;
    }

    public void setAdminid_event(int adminid_event) {
        this.adminid_event = adminid_event;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id_event=" + id_event +
                ", name_event='" + name_event + '\'' +
                ", type_event='" + type_event + '\'' +
                ", Space='" + Space + '\'' +
                ", gender_event='" + gender_event + '\'' +
                ", startDate_event=" + startDate_event +
                ", endDate_event=" + endDate_event +
                ", localisation_event='" + localisation_event + '\'' +
                ", description_event='" + description_event + '\'' +
                ", price=" + price +
                ", adminid_event=" + adminid_event +
                '}';
    }
}
