        package tn.esprit.Controllers;

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
        import javafx.scene.Parent;
        import javafx.scene.control.*;
        import javafx.scene.control.Button;
        import javafx.scene.control.TextField;
        import javafx.scene.layout.VBox;
        import kong.unirest.HttpResponse;
        import kong.unirest.Unirest;
        import kong.unirest.UnirestException;
        import org.json.JSONObject;
        import tn.esprit.Entities.Booking;
        import tn.esprit.Entities.user;
        import tn.esprit.services.BookingService;

        import java.awt.*;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.sql.SQLException;
        import java.time.LocalDate;
        import java.time.LocalDateTime;
        import java.time.format.DateTimeFormatter;
        import java.util.List;

        public class AddBooking {
            private final String PDF_OUTPUT_FOLDER = "src/main/java/tn/esprit/PDF";
            private int eventId; // Event ID passed from ShowEvent
            private boolean status=false;
            @FXML
            private VBox rootContainer;
            private user currentuser;

            @FXML
            private Button addBookingButton;
            private Login loginController=new Login();

            public void setEventId(int eventId) {
                this.eventId = eventId;

            }

            @FXML
            private DatePicker booking_dateId;

            @FXML
            private TextField nbr_participantsId;

            private final BookingService bs = new BookingService();
            private boolean paymentMade = false;
            private user AUTH;


            public void handlePay(Booking booking) throws  UnirestException {
                try {
                    // Calculate the total cost based on the event price and number of participants
                    Double totalCost = Double.valueOf(bs.calculateTotalPrice(booking.getId_event(), booking.getNbParticipants_event()));

                    // Create JSON object with total cost and quantity
                    JSONObject json = new JSONObject();
                    json.put("totalCost", totalCost); // Total cost of the event booking
                    json.put("quantity", booking.getNbParticipants_event()); // Number of participants

                    // Send POST request to create a checkout session
                    HttpResponse<String> response = Unirest.post("http://localhost:8080/create-checkout-session")
                            .header("Content-Type", "application/json")
                            .body(json.toString())
                            .asString();

                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        System.out.println("Response from create checkout session: " + response.getStatus());
                        System.out.println("Response URL: " + response.getBody());
                    }
                } catch (UnirestException e) {
                    throw e; // Re-throw UnirestException
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            private void exportBookingPdf(Booking booking) {
                try {

                    File directory = new File(PDF_OUTPUT_FOLDER);
                    if (!directory.exists()) {
                        directory.mkdirs(); // Create directory and any necessary parent directories
                    }

                    // Generate a unique file name using booking ID or timestamp
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    String fileName = "booking_" + booking.getId_booking() + "_" + timestamp + "_details.pdf";

                    // Get user name using the user ID
                    int userId = booking.getUserid(); // Assuming getUserId() returns the user ID
                    String userName = bs.getUserNameById(userId); // Assuming you have a method to get the user's full name by ID

                    int id_event = booking.getId_event();
                    String eventName =bs.getEventNameById(id_event);
                    // Create a new PDF file for the current booking
                    PdfWriter writer = new PdfWriter(PDF_OUTPUT_FOLDER + "/" + fileName);
                    PdfDocument pdf = new PdfDocument(writer);


                    Document document = new Document(pdf, PageSize.A4);
                    Paragraph title = new Paragraph("Booking Details").setFontSize(20).setBold();
                    title.setTextAlignment(TextAlignment.CENTER);
                    document.add(title);


                    // Set page margins
                    document.setMargins(50, 50, 50, 50);

                    // Add booking details
                    Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
                    table.setWidth(UnitValue.createPercentValue(90));
                    table.setHorizontalAlignment(HorizontalAlignment.CENTER);

                    // Add booking details to the table
                    addRowToTable(table, "Name User:", userName);
                    addRowToTable(table,"Event Name :", eventName);
                    addRowToTable(table, "Date Booking:", String.valueOf(booking.getDate_booking()));
                    addRowToTable(table, "Number of Participants:", String.valueOf(booking.getNbParticipants_event()));

                    document.add(table);

                    document.close();
                    System.out.println("PDF created successfully: " + fileName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            private void addRowToTable(Table table, String key, String value) {
                com.itextpdf.layout.element.Cell keyCell = new com.itextpdf.layout.element.Cell();
                keyCell.add(key).setBackgroundColor(new DeviceRgb(135, 206, 250)); // Light blue
                table.addCell(keyCell);

                com.itextpdf.layout.element.Cell valueCell = new Cell();
                valueCell.add(value);
                table.addCell(valueCell);
            }
            @FXML
            private void initialize() {
                booking_dateId.setDayCellFactory(picker -> new DateCell() {
                    LocalDate startDate;

                    {
                        try {
                            startDate = bs.getEventStartDate(eventId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                                        setDisable(startDate != null && date.isAfter(startDate));
                    }
                });
                addBookingButton.setDisable(true);
    addPaymentButton();
            }
            private void addPaymentButton() {
                Button payButton = new Button("Pay");
                payButton.setOnAction(this::handlePayButtonClick);
                rootContainer.getChildren().add(payButton);
            }

            // Method to handle payment button click
            private void handlePayButtonClick(ActionEvent event) {
                try {


                    // Perform payment handling logic
                    // For demonstration purposes, call handlePay method passing necessary parameters
                    Booking booking = new Booking(eventId,2, booking_dateId.getValue(), Integer.parseInt(nbr_participantsId.getText()));
                    handlePay(booking);
                    paymentMade=true;
                    addBookingButton.setDisable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Payment failed", "An error occurred while processing the payment.");
                }}



            @FXML
            void AddBooking(ActionEvent event) throws IOException {
                try {
                    LocalDate bookingDate = booking_dateId.getValue();
                    if (bookingDate == null) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Booking date not selected.", "Please select a booking date.");
                        return;
                    }
                    if (!paymentMade) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Payment required", "Please make the payment first.");
                        return;
                    }

                    int authenticatedUserId = SessionManager.getInstance().getAuthenticatedUserId();

                    Booking newBooking = new Booking(eventId, authenticatedUserId, booking_dateId.getValue(), Integer.parseInt(nbr_participantsId.getText()));
                    bs.add(newBooking);
                    exportBookingPdf(newBooking);
                    clearfields();
                    paymentMade=true;
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "A new booking is added", null);
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add booking.", e.getMessage());
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid input", "Please enter valid values for participants.");
                }
            }

            @FXML
            void ViewListEvents(ActionEvent event) throws IOException {
                Parent root = FXMLLoader.load(getClass().getResource("/ShowEventUser.fxml"));
                nbr_participantsId.getScene().setRoot(root);
                System.out.println("List Event Page");
            }

            private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
                Alert alert = new Alert(alertType);
                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.setContentText(content);
                alert.showAndWait();
            }
            void clearfields()
            {
                booking_dateId.setValue(null);
                nbr_participantsId.clear();
            }
        }
