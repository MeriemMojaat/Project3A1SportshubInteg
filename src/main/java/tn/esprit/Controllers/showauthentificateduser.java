    package tn.esprit.Controllers;

    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.ScrollPane;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import tn.esprit.Entities.user;

    public class showauthentificateduser {


        @FXML
        private Label dateofbirth;

        @FXML
        private Label gender;

        @FXML
        private Label id;

        @FXML
        private Button homebutton;

        @FXML
        private Label name;

        @FXML
        private Label name1;

        @FXML
        private Label password;

        @FXML
        private Label phone;
        @FXML
        private ImageView qrCodeImage;
        int userid;
        @FXML
        private ScrollPane scrollPane;
    private final Login L =new Login();

        public showauthentificateduser() {
            // Default constructor
        }

      /*  @FXML
        void home1(ActionEvent event) {
            try {
                // Load the homepage FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
                Parent root = loader.load();

                // Get the controller for the homepage
                Controller controller = loader.getController();
                Login login=loader.getController();

                // Pass the authenticated user object to the homepage controller
                user authenticatedUser = login.getAuthentificatedUser(event);
                if (authenticatedUser != null) {
                    controller.initData(authenticatedUser);
                } else {
                    // Handle case when authenticatedUser is null
                    System.err.println("Authenticated user is null. Unable to navigate to homepage.");
                    return; // Exit method if authenticated user is null
                }

                // Set the homepage as the root of the scene
                name.getScene().setRoot(root);
                System.out.println("Moved to homepage");
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception if loading fails
                System.err.println("Error loading homepage: " + e.getMessage());
            }
        }*/

        public void initData1(user authenticatedUser) {
            // Display user information on the profile page

            name.setText(authenticatedUser.getNameuser());
            name1.setText(authenticatedUser.getUseremail());
            password.setText(authenticatedUser.getUserpassword());
            gender.setText(authenticatedUser.getUsergender());
            phone.setText(authenticatedUser.getUsergender());
            dateofbirth.setText(authenticatedUser.getUserdateofbirth().toString());
            qrCodeImage.setImage(displayQRCode(authenticatedUser));
           id.setText(String.valueOf(authenticatedUser.getUserid()));
        }
        private Image displayQRCode(user authenticatedUser) {
            // Assuming you have a method in the Login class to generate QR code for a user
            Image qrCode = L.generateQR(authenticatedUser);

            qrCodeImage.setImage(qrCode);
            return qrCode;
        }


        public void home1(ActionEvent actionEvent) {
        }
    }