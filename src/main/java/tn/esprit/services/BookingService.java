package tn.esprit.services;


import java.sql.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import tn.esprit.utils.MyDatabase;
import tn.esprit.Entities.Booking;

public class BookingService implements IService<Booking>{
    Connection con;
    Statement stm;
    public BookingService() { con = MyDatabase.getInstance().getCon(); }

    @Override
    public void add(Booking booking) throws SQLException {
        String query = "INSERT INTO `bookings`(`id_event`, `userid`, `date_booking`, `nbParticipants_event`) VALUES (?,?,?,?)";

        // Check if the user exists before inserting the booking
        if (!userExists(booking.getUserid())) {
            throw new SQLException("User with id " + booking.getUserid() + " does not exist.");
        }

        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, booking.getId_event());
        ps.setInt(2, booking.getUserid());
        ps.setDate(3, Date.valueOf(booking.getDate_booking()));
        ps.setInt(4, booking.getNbParticipants_event());

        ps.executeUpdate();
        System.out.println("Booking added!");
    }

    // Helper method to check if the user exists
    private boolean userExists(int userId) throws SQLException {
        String query = "SELECT * FROM `userjava` WHERE `userid` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        return rs.next(); // Returns true if the user exists, false otherwise
    }


    public void update(Booking booking) throws SQLException {
        String query = "UPDATE `bookings` SET `id_event`=?,`userid`=?, `date_booking`=?, `nbParticipants_event`=? WHERE `id_booking`=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, booking.getId_event());
            ps.setInt(2, booking.getUserid());
            ps.setDate(3, Date.valueOf(booking.getDate_booking()));
            ps.setInt(4, booking.getNbParticipants_event());
            ps.setInt(5, booking.getId_booking());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Booking updated successfully!");
            } else {
                throw new SQLException("Failed to update booking. Booking not found.");
            }
        }
    }


    @Override
    public void delete(int id_booking) throws SQLException {
        String query = "DELETE FROM `bookings` WHERE id_booking = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id_booking);
        ps.executeUpdate();
        System.out.println("Booking deleted!");
    }

    @Override
    public List<Booking> display() throws SQLException {
        String query = "SELECT id_booking,id_event, userid, date_booking, nbParticipants_event FROM `bookings`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<Booking> bookings = new ArrayList<>();
        while (res.next()) {
            Booking b = new Booking(
                    res.getInt("id_booking"),
                    res.getInt("id_event"),
                    res.getInt("userid"),
                    res.getDate("date_booking").toLocalDate(),
                    res.getInt("nbParticipants_event")
            );
            b.setId_booking(res.getInt("id_booking"));
            bookings.add(b);
        }
        return bookings;
    }
    public List<String> getUserNames() throws SQLException {
        List<String> userNames = new ArrayList<>();
        String query = "SELECT nameuser FROM userjava"; // Corrected column name to 'username'
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                userNames.add(rs.getString("nameuser"));
            }
        }
        return userNames;
    }

    public String getUserNameById(int userId) throws SQLException {
        String userName = null;
        String query = "SELECT nameuser FROM userjava WHERE userid = ?";
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
    public String getEventNameById(int id_event) throws SQLException {
        String userName = null;
        String query = "SELECT name_event FROM event WHERE id_event = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id_event);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userName = rs.getString("name_event");
                }
            }
        }
        return userName;
    }

    public int getUserIdByName(String userName) throws SQLException {
        int userId = -1; // Default value indicating not found
        String query = "SELECT userid FROM userjava WHERE nameuser = ?";
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
    public Booking getBookingById(int bookingId) throws SQLException {
        Booking booking = null;
        String query = "SELECT * FROM bookings WHERE id_booking = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, bookingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    booking = new Booking();
                    booking.setId_booking(resultSet.getInt("id_booking")); // Fetch id_booking column
                    booking.setId_event(resultSet.getInt("id_event")); // Fetch id_booking column
                    booking.setUserid(resultSet.getInt("userid"));
                    booking.setDate_booking(LocalDate.parse(resultSet.getString("date_booking")));
                    booking.setNbParticipants_event(resultSet.getInt("nbParticipants_event"));}
            }
        }
        return booking;
    }
    public List<Booking> getBookingsForEvent(int eventId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        // SQL query to retrieve bookings for the specified event ID
        String sql = "SELECT * FROM bookings WHERE id_event = ?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, eventId);
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and create Booking objects
            while (resultSet.next()) {
                int bookingId = resultSet.getInt("id_booking");
                int userId = resultSet.getInt("userid");
                int numParticipants = resultSet.getInt("nbParticipants_event");

                // Create a new Booking object and add it to the list
                Booking booking = new Booking(bookingId, eventId, userId, numParticipants);
                bookings.add(booking);
            }
        }

        return bookings;
    }
    public LocalDate getEventStartDate(int eventId) throws SQLException {
        String query = "SELECT startDate_event FROM event WHERE id_event = ?";
        try (
                PreparedStatement preparedStatement = con.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDate("startDate_event").toLocalDate();
                }
            }
        }
        return null;
    }
    EventService eventService= new EventService();
    public float calculateTotalPrice(int eventId, int numberOfParticipants) throws SQLException {
        float eventPrice = eventService.getEventPrice(eventId); // Retrieve the event price
        if (eventPrice < 0) {
            throw new IllegalArgumentException("Event price cannot be negative.");
        }
        if (numberOfParticipants <= 0) {
            throw new IllegalArgumentException("Number of participants must be positive.");
        }
        return eventPrice * numberOfParticipants; // Calculate the total price
    }
}

