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
import javafx.scene.layout.GridPane;

public class HomeController implements Initializable {

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	 @FXML
	    private Label label;

	    @FXML
	    private Button searchB;

	    @FXML
	    private TextField searchBar;

	    @FXML
	    private ImageView image1;

	    @FXML
	    private ImageView iamge2;

	    @FXML
	    private ImageView image3;

	    @FXML
	    private ImageView image4;

	    @FXML
	    private ImageView image5;

	    @FXML
	    private ImageView image6;

	    @FXML
	    private GridPane ImagePane;

    @FXML
    void clickSearch(ActionEvent event) {
    	System.out.println("search clicked");
    }

}
