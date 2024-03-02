package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import tn.esprit.entities.user;
import tn.esprit.services.userservices;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowUser  {

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
    private Button addbutt;
    @FXML
    private Button updatebutt;

    @FXML
    private TextField keyword;
    @FXML
    private TextField usertodelete;



    private final userservices us = new userservices();

    @FXML
    void initialize() throws SQLException {
       /* try {
            List<user> users = us.diplayList();
            ObservableList<user> observableList = FXCollections.observableList(users);
            usertableid.setItems(observableList);
            usernameid.setCellValueFactory(new PropertyValueFactory<>("nameuser"));
            phonenumberid.setCellValueFactory(new PropertyValueFactory<>("uphonenumber"));
            emailid.setCellValueFactory(new PropertyValueFactory<>("useremail"));
            dateofbirthid.setCellValueFactory(new PropertyValueFactory<>("userdateofbirth"));
            genderid.setCellValueFactory(new PropertyValueFactory<>("usergender"));


            // Custom cell factory for password column
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

            // Listen for selection changes and update usertodelete TextField
            usertableid.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    usertodelete.setText(String.valueOf(newValue.getUserid()));
                }
            });

        }
        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }*/
      //  String query ="SELECT  `nameuser`, `uphonenumber`, `useremail`, `userdateofbirth`, `usergender` FROM `user` ";
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

        } catch (SQLException e) {
            Logger.getLogger(userservices.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }





    public void deleteuserbyid(javafx.event.ActionEvent actionEvent) {
        try {
            // Retrieve the selected user from the TableView
            user selectedUser = usertableid.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                // Display a confirmation dialog
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText("Delete User");
                confirmationAlert.setContentText("Are you sure you want to delete this user?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                // If user confirms deletion, proceed with deletion
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Call the delete method in UserService to delete the user
                    us.delete(selectedUser.getUserid());

                    // Remove the selected user from the TableView
                    usertableid.getItems().remove(selectedUser);
                }
            } else {
                // If no user is selected, show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please select a user to delete.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }




    public void adduser(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddUser.fxml"));
        Parent root = loader.load();

        // Get the controller of the update page


        // Create a new dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().setStyle("-fx-background-color: #6495ED;");
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

    public void updateuser(javafx.event.ActionEvent actionEvent) {
        try {
            // Load the FXML file for the UpdateUser form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent root = loader.load();

            // Get the controller of the update page


            // Create a new dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(root);
            dialog.getDialogPane().setStyle("-fx-background-color: #6495ED;");
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
    } catch (IOException e) {
            throw new RuntimeException(e);
        }}}

        // public void initData(admin authenticatedadmin) {

   // }
