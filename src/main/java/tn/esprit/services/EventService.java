package tn.esprit.services;

import tn.esprit.Entities.Event;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IService<Event> {
    Connection con;
    Statement stm;
    public EventService() {
        con = MyDatabase.getInstance().getCon();
    }

    public static boolean isValidEventName(String eventName) {
        return eventName.length() <= 10;
    }
    @Override
    public void add(Event event) throws SQLException {
        String query = "INSERT INTO `event`(`name_event`, `type_event`, `Space`, `gender_event`, `startDate_event`, `endDate_event`, `localisation_event`, `description_event`, `price`,adminid) VALUES (?,?,?,?,?,?,?,?,?,?)";
        if (!isValidEventName(event.getName_event())) {
            throw new IllegalArgumentException("You shouldn't exceed 10 characters .");
        }
        if (isEventExists(event.getName_event())) {
            throw new IllegalArgumentException("An event with the same name already exists.");
        }

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, event.getName_event());
        ps.setString(2, event.getType_event());
        ps.setString(3, event.getSpace());
        ps.setString(4, event.getGender_event());
        ps.setDate(5, Date.valueOf(event.getStartDate_event()));
        ps.setDate(6, Date.valueOf(event.getEndDate_event()));
        ps.setString(7, event.getLocalisation_event());
        ps.setString(8, event.getDescription_event());
        ps.setFloat(9, event.getPrice());
        ps.setInt(10, event.getadminid());
        ps.executeUpdate();
        System.out.println("Event added!");
    }

    @Override
    public void update(Event event) throws SQLException {
        String query = "UPDATE `event` SET name_event = ?, type_event = ?, Space = ?, gender_event = ?, startDate_event = ?, endDate_event = ?, localisation_event = ?, description_event = ? ,price = ? WHERE id_event = ? AND adminid = ? ";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, event.getName_event());
        ps.setString(2, event.getType_event());
        ps.setString(3, event.getSpace());
        ps.setString(4, event.getGender_event());
        ps.setDate(5, Date.valueOf(event.getStartDate_event()));
        ps.setDate(6, Date.valueOf(event.getEndDate_event()));
        ps.setString(7, event.getLocalisation_event());
        ps.setString(8, event.getDescription_event());
        ps.setString(9, String.valueOf(event.getPrice()));
        ps.setInt(10, event.getId_event());
        ps.setInt(11,event.getadminid());

        int rowsUpdated = ps.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Event updated successfully!");
        } else {
            System.out.println("Failed to update event. Event not found.");
        }
    }

    @Override
    public void delete(int eventId) throws SQLException {
        String query = "DELETE FROM `event` WHERE id_event = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, eventId);
        ps.executeUpdate();
        System.out.println("Event deleted!");
    }

    @Override
    public List<Event> display() throws SQLException {
        String query = "SELECT  `id_event`, `name_event`, `type_event`, `Space`, `gender_event`, `startDate_event`, `endDate_event`, `localisation_event`, `description_event`, `price` , `adminid`  FROM `event`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<Event> events = new ArrayList<>();
        while (res.next()) {
            Event e = new Event(
                    res.getInt("id_event"),
                    res.getString("name_event"),
                    res.getString("type_event"),
                    res.getString("Space"),
                    res.getString("gender_event"),
                    res.getDate("startDate_event").toLocalDate(),
                    res.getDate("endDate_event").toLocalDate(),
                    res.getString("localisation_event"),
                    res.getString("description_event"),
                    res.getFloat("price"),
                    res.getInt("adminid")
            );
            events.add(e);
        }
        return events;

    }
    public List<Event> searchEvents(String searchCriteria) throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event WHERE type_event LIKE ? OR gender_event LIKE ? OR localisation_event LIKE ?";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, "%" + searchCriteria + "%");
            statement.setString(2, "%" + searchCriteria + "%");
            statement.setString(3, "%" + searchCriteria + "%");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Event event = new Event(
                        resultSet.getString("name_event"),
                        resultSet.getString("type_event"),
                        resultSet.getString("Space"),
                        resultSet.getString("gender_event"),
                        resultSet.getDate("startDate_event").toLocalDate(),
                        resultSet.getDate("endDate_event").toLocalDate(),
                        resultSet.getString("localisation_event"),
                        resultSet.getString("description_event"),
                        resultSet.getFloat("price"),
                        resultSet.getInt("adminid")

                );
                events.add(event);
            }
        }

        return events;
    }


    public List<Event> displaySorted(String sortBy) throws SQLException {
        String query = "SELECT name_event, type_event, Space, gender_event, startDate_event, endDate_event, localisation_event, description_event , price , adminid  FROM `event` ORDER BY ";

        switch (sortBy) {
            case "startDate":
                query += "startDate_event";
                break;
            case "event_name":
                query += "name_event";
                break;
            case "Space":
                query += "Space";
                break;
            default:
                throw new IllegalArgumentException("Invalid sort criteria.");
        }

        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<Event> events = new ArrayList<>();
        while (res.next()) {
            Event e = new Event(
                    res.getString("name_event"),
                    res.getString("type_event"),
                    res.getString("Space"),
                    res.getString("gender_event"),
                    res.getDate("startDate_event").toLocalDate(),
                    res.getDate("endDate_event").toLocalDate(),
                    res.getString("localisation_event"),
                    res.getString("description_event"),
                    res.getFloat("price"),
                    res.getInt("adminid")
            );
            events.add(e);
        }
        return events;
    }
    public int getGenderCount(String gender) throws SQLException {
        String query = "SELECT COUNT(*) FROM event WHERE gender_event = ?";
        PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, gender);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Failed to get gender count.");
            }

    }
    public Event getEventById(int eventId) throws SQLException {
        Event event = null;
        String query = "SELECT * FROM event WHERE id_event = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, eventId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    event = new Event();
                    event.setId_event(resultSet.getInt("id_event"));
                    event.setName_event(resultSet.getString("name_event"));
                    event.setType_event(resultSet.getString("type_event"));
                    event.setSpace(resultSet.getString("Space"));
                    event.setGender_event(resultSet.getString("gender_event"));
                    event.setStartDate_event(resultSet.getDate("startDate_event").toLocalDate());
                    event.setEndDate_event(resultSet.getDate("endDate_event").toLocalDate());
                    event.setLocalisation_event(resultSet.getString("localisation_event"));
                    event.setDescription_event(resultSet.getString("description_event"));
                    event.setPrice(resultSet.getFloat("price"));
                    event.setadminid(resultSet.getInt("adminid"));
                }
            }
        }
        return event;
    }
    public int calculateTotalParticipantsForEvent(int eventId) throws SQLException {
        int totalParticipants = 0;

        // Query to select bookings for the event
        String query = "SELECT SUM(nbParticipants_event) AS totalParticipants FROM Booking WHERE id_event = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, eventId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("totalParticipants");
                }
            }
        }
        return 0; // Return 0 if no participants found or an error occurs
    }
    public boolean isEventExists(String eventName) throws SQLException {
        String query = "SELECT COUNT(*) FROM event WHERE name_event = ? ";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, eventName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }
    public String getUserNameById(int userId) throws SQLException {
        String userName = null;
        String query = "SELECT nameuser FROM user WHERE userid = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userName = rs.getString("nameuser");
                }
            }
        }
        return userName;
    }
    public int getUserIdByName(String userName) throws SQLException {
        int userId = -1; // Default value indicating not found
        String query = "SELECT userid FROM user WHERE nameuser = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("userid");
                }
            }
        }
        return userId;
    }
    public float getEventPrice(int eventId) throws SQLException {
        String query = "SELECT price FROM event WHERE id_event = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, eventId);
        try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getFloat("price"); // Return the price if found
                } else {
                    throw new SQLException("Event with ID " + eventId + " not found.");
                }
            }
        }
    }