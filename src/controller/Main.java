package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.APIUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {

	private APIUtils apiUtils = new APIUtils();

	@Override
	public void start(Stage primaryStage) {
		try {

			Parent root = FXMLLoader.load(getClass().getResource("/view/Home.fxml"));

			Scene scene = new Scene(root, 800, 560);

			primaryStage.setScene(scene);
			primaryStage.setTitle("Awesome App");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
