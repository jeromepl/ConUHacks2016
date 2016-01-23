package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private Hyperlink newAccB;

    @FXML
    private Hyperlink forgotPassB;

    @FXML
    private Button logInB;

    @FXML
    private PasswordField passwordField;

    @FXML
    void forgotPassBPress(ActionEvent event) {
    	//TODO
    }

    @FXML
    void logInPress(ActionEvent event) {
    	Main.instance.showHome();
    }

    @FXML
    void newAccBPress(ActionEvent event) {
    	//TODO
    }

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		System.out.println("Login Scene initialized!");
	}

}
