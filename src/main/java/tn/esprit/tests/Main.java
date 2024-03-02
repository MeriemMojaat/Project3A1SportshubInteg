package tn.esprit.tests;

import tn.esprit.entities.*;
import tn.esprit.services.BookingService;
import tn.esprit.services.EventService;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Event e1 = new Event("EventCamp", "Cycling", "Non virtual", "Both", LocalDate.of(2024, 7, 21), LocalDate.of(2024, 4, 21), "Ain Drahem", "alalalalal",3,4);


        EventService ps = new EventService();


 /* try {
             ps.delete(24);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
         try {
            ps.update(e1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
         try {
           ps.delete(e1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
*/
        try {
            System.out.println(ps.display());
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }


       /* Booking b1 = new Booking(7,20,3,startDate,3,2);

        BookingService bs = new BookingService();

        try {
            bs.add(b1);
           System.out.println(bs.display());
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }*/
    }
}