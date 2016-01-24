package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.SearchListener;

public class HomeController implements Initializable, SearchListener {

	@FXML
    private AnchorPane Root;
	
    @FXML
    private GridPane mainGrid;

    @FXML
    private Button searchB;

    @FXML
    private TextField searchBar;

    @FXML
    private ScrollPane ScrollPane;
    
    @FXML
    private FlowPane flowPane;
    
    @FXML
    private HBox bottomBar;
    
    @FXML
    private Label label;
    
    private ArrayList<ImageView> images = new ArrayList<ImageView>();

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		System.out.println("Home Scene initialized!");
		
		flowPane.prefWidthProperty().bind(ScrollPane.widthProperty());
		flowPane.setVgap(50);
		mainGrid.prefHeightProperty().bind(Root.heightProperty());
		mainGrid.prefWidthProperty().bind(Root.widthProperty());
		//ScrollPane.setBackground(new Background(new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY)));
		
		addAllImages();

	}
	
	@FXML
    void search(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER){
			String query = searchBar.getText().trim();
			if(query.length() > 0) {
				query = query.replaceAll(" ", "_");

				Main.instance.getServer().search(query, this);
			}
			else {
				flowPane.getChildren().clear();
	    		images.clear();
	    		addAllImages();
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
    	else {
    		flowPane.getChildren().clear();
    		images.clear();
    		addAllImages();
    	}

    }

	@Override
	public void onResult(String[] files) {
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
			        images.get(images.size() - 1).setOnMouseClicked(new EventHandler<MouseEvent>() {

					     @Override
					     public void handle(MouseEvent event) {
					         Main.instance.showImage((ImageView)event.getSource());
					         event.consume();
					     }
					});
			        
			        // Sorry this is really bad but I'm too tired to restructure everything
			        if (!images.isEmpty()) {
			        	FadeTransition ft = new FadeTransition(new Duration(250),images.get(images.size() - 1));
			        	ft.setFromValue(0);
			        	ft.setToValue(1);
			        	//ft.play();
			        	TranslateTransition tt = new TranslateTransition(new Duration(500), images.get(images.size() - 1));
			        	tt.setFromX(100);
			        	tt.setToX(0);
			        	//tt.play();
			        	ParallelTransition pt = new ParallelTransition();
			        	pt.getChildren().addAll(ft,tt);
			        	pt.play();
			        }
				}
				
			}
		});
	}
	
	public void addImage(String path) {
		
		double offset = 0;
		double imageWidth = 0;
		
		if (ScrollPane.getWidth() != 0) {
		offset = ScrollPane.getWidth() * 0.01;
		imageWidth = ScrollPane.getWidth() * 0.235;
		}
		else {
			offset = Main.WIDTH * 0.01;
			imageWidth = Main.WIDTH * 0.235;
		}
		
		flowPane.setHgap(offset);
		flowPane.setPadding(new Insets(15, 0, 0, offset));
		
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
		double offset = 0;
		double imageWidth = 0;
		
		if (Main.currentWidth != 0) {
		offset = Main.currentWidth * 0.01;
		imageWidth = Main.currentWidth * 0.235;
		}
		else {
			offset = Main.WIDTH * 0.01;
			imageWidth = Main.WIDTH * 0.235;
		}
		
		flowPane.setHgap(offset);
		flowPane.setPadding(new Insets(15, 0, 0, offset));
		flowPane.getChildren().clear();
		
		for (ImageView image : images) {
			image.setFitWidth(imageWidth);
			flowPane.getChildren().add(image);
		}
	}
	
	public void showSync() {
		Platform.runLater(new Runnable() {
		
			@Override
			public void run() {
				bottomBar.getChildren().clear();
				Circle c1 = new Circle(10);
				c1.setFill(Color.valueOf("#b5b5b5ae"));
				Circle c2 = new Circle(10);
				c2.setFill(Color.valueOf("#b5b5b5ae"));
				Circle c3 = new Circle(10);
				c3.setFill(Color.valueOf("#b5b5b5ae"));
				bottomBar.getChildren().add(c1);
				bottomBar.getChildren().add(c2);
				bottomBar.getChildren().add(c3);
				
				ScaleTransition t1 = new ScaleTransition(new Duration(250), c1);
				t1.setCycleCount(2);
				t1.setAutoReverse(true);
				t1.setFromX(1);
				t1.setFromY(1);
				t1.setFromZ(1);
				t1.setToX(1.5);
				t1.setToY(1.5);
				t1.setToZ(1.5);
				//t1.play();
				
				ScaleTransition t2 = new ScaleTransition(new Duration(250), c2);
				t2.setCycleCount(2);
				t2.setAutoReverse(true);
				t2.setFromX(1);
				t2.setFromY(1);
				t2.setFromZ(1);
				t2.setToX(1.5);
				t2.setToY(1.5);
				t2.setToZ(1.5);
				//t2.play();
				
				ScaleTransition t3 = new ScaleTransition(new Duration(250), c3);
				t3.setCycleCount(2);
				t3.setAutoReverse(true);
				t3.setFromX(1);
				t3.setFromY(1);
				t3.setFromZ(1);
				t3.setToX(1.5);
				t3.setToY(1.5);
				t3.setToZ(1.5);
				//t3.play();
				
				SequentialTransition st = new SequentialTransition(t1,t2,t3);
				st.setCycleCount(Transition.INDEFINITE);
				st.play();
			}
		});
	}
	
	public void hideSync() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				bottomBar.getChildren().clear();
				bottomBar.getChildren().add(label);
			}
			
		});

	}
	
	public void addAllImages() {
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
		        images.get(images.size() - 1).setOnMouseClicked(new EventHandler<MouseEvent>() {

				     @Override
				     public void handle(MouseEvent event) {
				         Main.instance.showImage((ImageView)event.getSource());
				         event.consume();
				     }
				});
		        
		        //HAVE FUN!
		    }
		}
	}

}
