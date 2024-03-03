package tn.esprit.Controllers;

import com.github.cage.Cage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import tn.esprit.Entities.admin;
import tn.esprit.Entities.coach;
import tn.esprit.Entities.user;
import tn.esprit.services.LoginService;
import tn.esprit.services.Qrcode;
import tn.esprit.services.userservices;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Login implements Initializable {
    @FXML
    private WebView captcha;
    @FXML
    private Button loginButton;

    @FXML
    private CheckBox remember;
    @FXML
    private PasswordField passwordField;

        @FXML
        private TextField usernameField;
    private final LoginService loginService = new LoginService();
    private final userservices us = new userservices();

    private admin authenticatedadmin;

    private Object authentificatedobject;
    @FXML
    private TextField captchaInputField;
    private String captchaText;
    @FXML
    private Button refreshCaptchaButton;
private final Qrcode qr=new Qrcode();
private user currentuser;
 //   private int authenticatedUserId = -1; // Default value if no user is authenticated

    // Other existing code...



    @FXML
    private ImageView captchaImage;
    ImageView qrCodeImage = null;



    public TextField getUsername() {
        return usernameField;
    }
    private void updateCaptcha() {
        Cage cage = new Cage();
        String captchaText = cage.getTokenGenerator().next();

        // Generate captcha image data
        byte[] imageData = cage.draw(captchaText);

        // Create Image object from byte array
        javafx.scene.image.Image captchaImageSrc = new javafx.scene.image.Image(new ByteArrayInputStream(imageData));

        // Set the Image object to the ImageView
        captchaImage.setImage(captchaImageSrc);

        // Store captcha text somewhere to validate later
        this.captchaText = captchaText;
    }

    public Image generateQR(user authenticatedUser) {
        String fileName = "ahmed_QR.png";

        String absolutePath = getClass().getResource("/").getPath();
        String baseDirectory = System.getProperty("user.dir");

        String directory = baseDirectory + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "qrcode";

        String filePath = directory + File.separator + fileName;
        int userId = authenticatedUser.getUserid();
        String qrCodeData = "User ID - " + Integer.toString(userId).replace(":", "_");

        Path qrCodeFilePath = null;
        try {
            // Set QR code parameters
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            // Generate QR code matrix
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    qrCodeData,
                    BarcodeFormat.QR_CODE,
                    200,
                    200,
                    hints
            );

            if (bitMatrix != null) {
                System.out.println("QR code generation successful.");
            } else {
                System.out.println("QR code generation failed: BitMatrix is null.");
            }

            // Write QR code matrix to file
            qrCodeFilePath = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrCodeFilePath);

            // Show alert for successful generation
            showAlert("QR Code Generated", "QR Code for " + authenticatedUser.getNameuser() + " generated successfully.");
        } catch (Exception e) {
            // Show alert for failure
            showAlert("Error", "Failed to generate QR code: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }

        // Return the Image object
        if (qrCodeFilePath != null) {
            return new Image(qrCodeFilePath.toUri().toString());
        } else {
            return null; // Return null if qrCodeFilePath is not set
        }
    }


    @FXML
    Object authenticate(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Authenticate user
        user authenticatedUser = loginService.authenticate(username, password);
        if (authenticatedUser != null) {
            generateQR(authenticatedUser);
            navigateToHomePage(authenticatedUser);
            SessionManager.getInstance().setAuthenticatedUserId(authenticatedUser.getUserid());

            return authenticatedUser;
        }

        AdminSessionManager sessionManager = AdminSessionManager.getInstance();

        // Authenticate admin
        admin authenticatedAdmin = loginService.authenticateadmin(username, password);
        if (authenticatedAdmin != null) {
            navigateToHomePage1(authenticatedAdmin);
            sessionManager.setAuthenticatedAdminId(authenticatedAdmin.getAdminid());

            return authenticatedAdmin;
        }

        CoachSessionManager CsessionManager = CoachSessionManager.getInstance();

        coach authenticatedCoach = loginService.authenticatecoach(username, password);
        if (authenticatedCoach != null) {
            navigateToHomePage2(authenticatedCoach);
            CsessionManager.setAuthenticatedCoachid(authenticatedCoach.getCoachid());
            return authenticatedCoach;
        }

        // Neither user nor admin authenticated
        showAlert("Authentication Failed", "Invalid username or password.");
        return null;
    }










    public user getAuthentificatedUser(ActionEvent event) {
        return currentuser;
    }

    @FXML
    void reset(ActionEvent event) throws IOException {
        // Get the current stage (the login window)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the Forgot Password FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resetpassword.fxml"));
        Parent root = loader.load();

        // Create a new stage for the forgot password window
        Stage forgotPasswordStage = new Stage();
        forgotPasswordStage.setScene(new Scene(root));

        // Close the login window
        currentStage.close();

        // Show the forgot password window
        forgotPasswordStage.show();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
   /* @FXML
    void onLoginButtonClicked(ActionEvent event) {
        // Call the authenticate method
        user authenticatedUser = authenticate(event);
        // Do something with the authenticated user, if needed
    }
*/
   @FXML
   void navigateToHomePage2(coach authenticatedcoach) {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
           Parent root = loader.load();

           usernameField.getScene().setRoot(root);
           System.out.println("moved");
       } catch (IOException e) {
           e.printStackTrace();
       }}
    @FXML
    void navigateToHomePage(user authenticatedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.initData(authenticatedUser); // Pass the authenticated user object
            usernameField.getScene().setRoot(root);
            System.out.println("moved");
        } catch (IOException e) {
            e.printStackTrace();
        }}
    @FXML
    void navigateToHomePage1(admin authentificatedadmin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin.fxml"));
            Parent root = loader.load();
            admincontroller controller  = loader.getController();
          //  controller.initdata2(authentificatedadmin); // Pass the authenticated user object
            usernameField.getScene().setRoot(root);
            System.out.println("moved");
        } catch (IOException e) {
            e.printStackTrace();
        }}

    /*@FXML
    void nextpage() throws IOException {
        user authenticatedUser = authenticate(new ActionEvent());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.initData(authenticatedUser); // Pass the authenticated user object
        usernameField.getScene().setRoot(root);
        System.out.println("moved");
    }*/
    @FXML
    void refreshCaptcha(ActionEvent event) {
        updateCaptcha();
    }
    @FXML
    void handleremember(ActionEvent event) {
        if(remember.isSelected()) {
            // Let's validate the username field isn't empty (Optional)
            if(!usernameField.getText().isEmpty()){
                Preferences pref = Preferences.userRoot();
                String userName = usernameField.getText();
                pref.put("username", userName);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateCaptcha();
    }
}

    /*@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameField.setText(Preferences.userRoot().get("username", "Username"));
    }*/


    /*@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String urll = "https://recaptchaenterprise.googleapis.com/v1/projects/my-project-7154-1708738655224/assessments?key=API_KEY"; // Replace with your URL

        // Set default CookieHandler
        CookieHandler.setDefault(new CookieManager());

        URI uri = URI.create(urll);
        Map<String, List<String>> headers = new LinkedHashMap<>();
        headers.put("Set-Cookie", Arrays.asList("APISID=E2mcDXOq7ze9A2Vg/Ar9qIklbtuxLfJA1Z", "CONSENT=YES+DE.de+20160612-12-0", "HSID=AZqBaYgd1dLE0PVXI"));
        try {
            java.net.CookieHandler.getDefault().put(uri, headers);
        } catch (Exception e) {
            e.printStackTrace();
        }


        WebEngine webEngine = captcha.getEngine();
        webEngine.setUserAgent("Mozilla/5.0 (Linux) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.116 Safari/537.37");
        webEngine.setJavaScriptEnabled(true);
        Class<?> webConsoleListenerClass = null;
        try {
            webConsoleListenerClass = Class.forName("com.sun.javafx.webkit.WebConsoleListener");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Method setDefaultListenerMethod = null;
        try {
            setDefaultListenerMethod = webConsoleListenerClass.getDeclaredMethod("setDefaultListener", webConsoleListenerClass);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        setDefaultListenerMethod.setAccessible(true);
        try {
            setDefaultListenerMethod.invoke(null, new Object[] {
                    //(webView, message, lineNumber, sourceId) -> System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message)
            });
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        webEngine.load(String.valueOf(url));

    }*/

