package controller;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.Server;
import javafx.scene.control.ScrollPane;

public class HomeController implements Initializable {

	@FXML
    private AnchorPane Root;

    @FXML
    private Label label;

    @FXML
    private Button searchB;

    @FXML
    private TextField searchBar;

    @FXML
    private ScrollPane ScrollPane;

    @FXML
    private GridPane GridPane;

    @FXML
    private ImageView iamge2;

    @FXML
    private ImageView image1;

    @FXML
    private ImageView image4;

    @FXML
    private ImageView image3;

    @FXML
    private ImageView image5;

    @FXML
    private ImageView image6;

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

	}

    @FXML
    void clickSearch(ActionEvent event) {

    	String query = searchBar.getText().trim();
    	if(query.length() > 0) {
    		query.replaceAll(" ", "_");

    		Main.instance.getServer().search(query);
    	}

    }

}
