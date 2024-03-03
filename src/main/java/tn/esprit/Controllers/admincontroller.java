package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import tn.esprit.Entities.admin;
import tn.esprit.Entities.user;
import tn.esprit.services.userservices;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class admincontroller  {
    private final userservices us = new userservices();
    private admin authenticatedadmin;

    @FXML
    private javafx.scene.control.TextField keyword;
    @FXML
    private TableView<user> usertableid;
    @FXML
    private TableColumn<user, String> usernameid;
    @FXML
    private TableColumn<user, String> phonenumberid;
    @FXML
    private TableColumn<user, String> emailid;
    @FXML
    private TableColumn<user, String> passwordid;
    @FXML
    private TableColumn<user, LocalDate> dateofbirthid;
    @FXML
    private TableColumn<user, String> genderid;


    @FXML
    private javafx.scene.control.Label connected;

    @FXML
    private javafx.scene.control.Label name;
    @FXML
    private Button out;
    @FXML
    void EventPage(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ShowEventAdmin.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        usertableid.getScene().setRoot(root);
        System.out.println("List Event Page");
    }

    /*public void initdata2(admin authenticatedadmin) {
        this.authenticatedadmin = authenticatedadmin;

        name.setText("Welcome " + authenticatedadmin.getNameuser());
    }*/

    @FXML
  void initialize() throws SQLException {
        try{
            List<user> users = us.diplayList();
            ObservableList<user> userObservableList = FXCollections.observableArrayList(users);


            usernameid.setCellValueFactory(new PropertyValueFactory<>("nameuser"));
            phonenumberid.setCellValueFactory(new PropertyValueFactory<>("uphonenumber"));
            emailid.setCellValueFactory(new PropertyValueFactory<>("useremail"));
            dateofbirthid.setCellValueFactory(new PropertyValueFactory<>("userdateofbirth"));
            genderid.setCellValueFactory(new PropertyValueFactory<>("usergender"));
            passwordid.setCellFactory(column -> {
                return new TextFieldTableCell<>(new DefaultStringConverter()) {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText("*".repeat(item.length()));
                        }
                    }
                };
            });
            usertableid.setItems(userObservableList);
            FilteredList<user> filtereddata = new FilteredList<>(userObservableList, b -> true);
            keyword.textProperty().addListener(((observable, oldvalue, newvalue) ->{
                filtereddata.setPredicate(user -> {
                    if(newvalue.isEmpty() || newvalue.isBlank() || newvalue ==null){
                        return true;

                    }
                    String searchkeyword = newvalue.toLowerCase();
                    if(user.getNameuser().toLowerCase().contains(searchkeyword)){
                        return  true;
                    }
                    else if (user.getUphonenumber().toLowerCase().contains(searchkeyword)){
                        return true;
                    }
                    else if (user.getUseremail().toLowerCase().contains(searchkeyword)){
                        return true;
                    } else if (user.getUserdateofbirth().toString().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else if (user.getUsergender().toLowerCase().contains(searchkeyword)){
                        return true;
                    }
                    else
                        return false; //no match found
                });
                SortedList<user> sorteddata = new SortedList<>(filtereddata);
                sorteddata.comparatorProperty().bind(usertableid.comparatorProperty());
                usertableid.setItems(sorteddata);

            } ));
            connected.setText(""+ us.calculatenumberUsers());
        } catch (SQLException e) {
            Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }


    public user getauthentificatedadmin() {
        return authenticatedadmin;
    }
  


    public void logout(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        out.getScene().setRoot(root);
        System.out.println("moved");
   
    }

    public void update(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowUser.fxml"));
        Parent root = loader.load();

        // Get the controller of the update page


        // Create a new dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().setStyle("-fx-background-color: #EAEEF4;");
        // Set the dialog size
        dialog.setWidth(600); // Adjust the width as needed
        dialog.setHeight(400); // Adjust the height as needed

        // Allow the dialog to be resizable
        dialog.setResizable(true);

        // Add OK and Cancel buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and wait for user response
        Optional<ButtonType> result = dialog.showAndWait();

        // Handle the result if OK button is clicked
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Handle OK button action
        } else {
            // Handle Cancel button action or dialog closed
        }

        System.out.println("moved");
    }

    public void coach(ActionEvent actionEvent) {
    }
}


