package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class NewUserController {

    @FXML
    private TextField newUserName;

    @FXML
    private PasswordField passId;

    @FXML
    private PasswordField confirmPassId;

    @FXML
    private Button createAccountB;

    @FXML
    private Label errorField;

    @FXML
    void createAccountBPress(ActionEvent event) {

    }

}
