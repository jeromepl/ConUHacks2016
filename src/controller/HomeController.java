package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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
    private FlowPane flowPane;
    
    private ArrayList<ImageView> images = new ArrayList<ImageView>();

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		System.out.println("Home Scene initialized!");
		
		flowPane.prefWidthProperty().bind(ScrollPane.widthProperty());
		flowPane.setVgap(50);
		mainGrid.prefHeightProperty().bind(Root.heightProperty());
		mainGrid.prefWidthProperty().bind(Root.widthProperty());
		//ScrollPane.setBackground(new Background(new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY)));
		
		File folder = new File("images");
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
		    if (listOfFiles[i].isFile()) {
		        
		        String filePath = listOfFiles[i].getAbsolutePath();
		        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);
		        
		        if(ext.equals("jpeg") || ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {
		            addImage(filePath);
		        }
		        else {
		            //Add an icon instead. This is where it would be useful to show the name of the since other wise how do you diffenrentiate 2 files
		        	addImage("file.png");
		        }
		        
		        //HAVE FUN!
		    }
		}

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
				
				flowPane.getChildren().clear();
				images.clear();
				
				for (String image : files) {
					String ext = image.substring(image.lastIndexOf(".") + 1);
			        if(ext.equals("jpeg") || ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {
			        	addImage("images/" + image);
			        }
			        else {
			        	addImage("file.png");
			        }
				}
				
			}
		});
	}
	
	public void addImage(String path) {
		double offset = ScrollPane.getWidth() * 0.01;
		double imageWidth = ScrollPane.getWidth() * 0.235;
		
		flowPane.setHgap(offset);
		flowPane.setPadding(new Insets(0, 0, 0, offset));
		
		try {
			Image img = new Image(new FileInputStream(new File(path)));
			ImageView imageView = new ImageView(img);
			imageView.setPreserveRatio(true);
			imageView.setFitWidth(imageWidth);
			imageView.setEffect(new DropShadow());
			
			flowPane.getChildren().add(imageView);
			images.add(imageView);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void rebuildImages() {
		double offset = ScrollPane.getWidth() * 0.01;
		double imageWidth = ScrollPane.getWidth() * 0.235;
		
		flowPane.setHgap(offset);
		flowPane.setPadding(new Insets(15, 0, 0, offset));
		flowPane.getChildren().clear();
		
		for (ImageView image : images) {
			image.setFitWidth(imageWidth);
			flowPane.getChildren().add(image);
		}
	}

}
