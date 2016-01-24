package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ImageController implements Initializable {

	@FXML
	private VBox vBox;
	
	@FXML
	private HBox hBox;
	
	@FXML
	private AnchorPane root;
	
	private ImageView imageView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Image Scene initialized!");

		root.setOnMouseClicked(new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent arg0) {
				Main.instance.showHome();
			}
			
		});
	}
	
	public void setImage(ImageView image) {
		imageView = image;
		vBox.getChildren().clear();
		
		image.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Main.instance.showHome();
			}
			
		});
		image.setPreserveRatio(true);
		image.setFitHeight(Main.currentHeight);
		
		vBox.getChildren().add(image);
	}
	
	public void rebuildImage() {
		if (imageView != null) {
			imageView.setFitHeight(Main.currentHeight);
		}
	}

}
