package controller;

import java.io.File;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.APIUtils;
import model.Server;
import model.ServerListener;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application implements EventHandler<WindowEvent>, ServerListener {

	private APIUtils apiUtils = new APIUtils();
	Server server = new Server();

	@Override
	public void start(Stage primaryStage) {
		try {
			server.upload(new File("images/kiwi.jpg"), this);

			Parent root = FXMLLoader.load(getClass().getResource("/view/Home.fxml"));

			Scene scene = new Scene(root, 800, 560);

			primaryStage.setOnCloseRequest(this);

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

	@Override
	public void handle(WindowEvent e) {
		apiUtils.close();
		server.close();
	}

	@Override
	public void onResult() {
		System.out.println("Uploaded File");
	}
}
