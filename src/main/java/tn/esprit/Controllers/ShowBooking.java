package tn.esprit.Controllers;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.google.gson.Gson;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import tn.esprit.entities.Booking;

import tn.esprit.services.BookingService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.*;
import java.util.List;


public class ShowBooking {

    @FXML
    private ScrollPane scrollPane;

    private final BookingService bs = new BookingService();

    private final String PDF_OUTPUT_FOLDER = "src/main/java/tn/esprit/PDF";
    @FXML
    void initialize() {
        displayBookings();
    }




    @FXML
    public void fileuploader(String filePath) {
        String accessToken = "cLvAtooLIBjdeqK1Rfcw6Y3n2kpnMBht"; // Replace with your access token
        BoxAPIConnection api = new BoxAPIConnection(accessToken);

        try {
            // Get the root folder
            BoxFolder rootFolder = BoxFolder.getRootFolder(api);

            // Check if the file already exists
            BoxFile file = getFileByName(rootFolder, new File(filePath).getName());

            if (file != null) {
                // If the file exists, update its content
                FileInputStream stream = new FileInputStream(filePath);
                file.uploadNewVersion(stream);
                stream.close();
                System.out.println("File updated successfully.");
            } else {
                // If the file doesn't exist, upload a new file
                FileInputStream stream = new FileInputStream(filePath);
                BoxFile.Info newFileInfo = rootFolder.uploadFile(stream, new File(filePath).getName());
                stream.close();
                System.out.println("File uploaded successfully.");
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error uploading file: " + e.getMessage());
        }
    }

    // Helper method to get file by name
    private BoxFile getFileByName(BoxFolder folder, String fileName) {
        for (BoxItem.Info itemInfo : folder) {
            if (itemInfo instanceof BoxFile.Info && itemInfo.getName().equals(fileName)) {
                return (BoxFile) itemInfo.getResource();
            }
        }
        return null;
    }

    private void displayBookings() {
        try {
            List<Booking> bookings = bs.display();

            GridPane gridPane = new GridPane();
            gridPane.setHgap(20);
            gridPane.setVgap(20);
            gridPane.setPadding(new Insets(20));

            int row = 0;
            int column = 0;
            for (Booking booking : bookings) {
                HBox hbox = new HBox();
                hbox.getChildren().add(createBookingBox(booking));
                gridPane.add(hbox, column, row);

                column++;
                if (column == 2) {
                    column = 0;
                    row++;
                }
            }

            scrollPane.setContent(gridPane);
        } catch (SQLException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private VBox createBookingBox(Booking booking) {
        VBox bookingBox = new VBox();
        bookingBox.setStyle("-fx-background-color: #AED6F1; -fx-border-color: #AED6F1; -fx-padding: 20px; -fx-spacing: 10px;");
        bookingBox.setSpacing(10);

        String userName = null;
        String eventName= null;
        try {
            userName = bs.getUserNameById(booking.getUserid());
            eventName=bs.getEventNameById(booking.getId_event());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to get user name for booking: " + booking.getId_booking(), Alert.AlertType.ERROR);
        }

        Label nameuserLabel = new Label("Name User : " + userName);
        Label nameventlabel = new Label("Event Name : " +eventName);
        Label dateLabel = new Label("Date Booking: " + booking.getDate_booking());
        Label participantsLabel = new Label("Number of Participants: " + booking.getNbParticipants_event());



        bookingBox.getChildren().addAll(
                nameuserLabel,nameventlabel,dateLabel, participantsLabel
        );

        return bookingBox;
    }


    @FXML
    void exportAllBookingsPdf(ActionEvent event) {
        try {
            List<Booking> bookings = bs.display(); // Retrieve the list of bookings
            exportAllBookingsPdf(bookings); // Call the exportAllBookingsPdf method with the list of bookings
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exportAllBookingsPdf(List<Booking> bookings) {
        try {
            File directory = new File(PDF_OUTPUT_FOLDER);
            if (!directory.exists()) {
                directory.mkdirs(); // Create directory and any necessary parent directories
            }

            // Generate a unique file name for the PDF
            String fileName = "all_bookings_details.pdf";
            String filePath = PDF_OUTPUT_FOLDER + "/" + fileName;
            // Create a new PDF file for all bookings
            PdfWriter writer = new PdfWriter(PDF_OUTPUT_FOLDER + "/" + fileName);
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);
            Paragraph title = new Paragraph("All Bookings Details").setFontSize(20).setBold();
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Set page margins
            document.setMargins(50, 50, 50, 50);

            // Iterate over all bookings and add their details to the PDF
            for (Booking booking : bookings) {
                int userId = booking.getUserid();
                String userName = bs.getUserNameById(userId);

                int eventId = booking.getId_event();
                String eventName = bs.getEventNameById(eventId);

                // Add booking details
                Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
                table.setWidth(UnitValue.createPercentValue(90));
                table.setHorizontalAlignment(HorizontalAlignment.CENTER);

                // Add booking details to the table
                addRowToTable(table, "Name User:", userName);
                addRowToTable(table, "Event Name:", eventName);
                addRowToTable(table, "Date Booking:", String.valueOf(booking.getDate_booking()));
                addRowToTable(table, "Number of Participants:", String.valueOf(booking.getNbParticipants_event()));

                document.add(table);
                document.add(new Paragraph("\n")); // Add some space between each booking
            }

            document.close();
            fileuploader(filePath);
            System.out.println("PDF created successfully: " + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ViewListEvents(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ShowEventUser.fxml"));
        scrollPane.getScene().setRoot(root);
        System.out.println("List Event Page");
    }

    private void addRowToTable(Table table, String key, String value) {
        Cell keyCell = new Cell();
        keyCell.add(key).setBackgroundColor(new DeviceRgb(135, 206, 250)); // Light blue
        table.addCell(keyCell);

        Cell valueCell = new Cell();
        valueCell.add(value);
        table.addCell(valueCell);
    }




    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
