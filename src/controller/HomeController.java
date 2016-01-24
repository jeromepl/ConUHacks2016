package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import model.SearchListener;

public class HomeController implements Initializable, SearchListener {

	@FXML
    private AnchorPane Root;
	
    @FXML
    private GridPane mainGrid;

    @FXML
    private Label label;

    @FXML
    private Button searchB;

    @FXML
    private TextField searchBar;

    @FXML
    private ScrollPane ScrollPane;
    
    @FXML
    private FlowPane FlowPane;

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
		System.out.println("Home Scene initialized!");
		
		// Stupid javafx
		FlowPane.prefHeightProperty().bind(ScrollPane.heightProperty());
		FlowPane.prefWidthProperty().bind(ScrollPane.widthProperty());
		mainGrid.prefHeightProperty().bind(Root.heightProperty());
		mainGrid.prefWidthProperty().bind(Root.widthProperty());
	}

    @FXML
    void clickSearch(ActionEvent event) {

    	String query = searchBar.getText().trim();
    	if(query.length() > 0) {
    		query = query.replaceAll(" ", "_");

    		Main.instance.getServer().search(query, this);
    	}

    }

	@Override
	public void onResult(String[] files) {
		System.out.println(files.length);
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				FlowPane.getChildren().clear();
				for (String image : files) {
					try {
						FlowPane.getChildren().add(new ImageView(new Image(new FileInputStream(new File("images/" + image)))));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

}
