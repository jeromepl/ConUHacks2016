package controller;

import java.io.File;
import java.io.IOException;

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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;


public class Main extends Application implements EventHandler<WindowEvent>, ServerListener {

	private APIUtils apiUtils = new APIUtils();
	Server server = new Server();

	public static Main instance;//Singleton

	private Stage stage;
	private Scene loginScene;
	private Scene homeScene;
	
	@Override
	public void start(Stage primaryStage) {
		instance = this;
		stage = primaryStage;

		try {
			server.upload(new File("images/kiwi.jpg"), this);

			
			// Setup login scene
			Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
			loginScene = new Scene(loginRoot, 800, 560);

			// Setup home scene
			Parent homeRoot = FXMLLoader.load(getClass().getResource("/view/Home.fxml"));
			homeScene = new Scene(homeRoot, 800, 560);
			homeScene.setOnDragOver(new EventHandler<DragEvent>() {
	            @Override
	            public void handle(DragEvent event) {
	                Dragboard db = event.getDragboard();
	                if (db.hasFiles()) {
	                    event.acceptTransferModes(TransferMode.COPY);
	                } else {
	                    event.consume();
	                }
	            }
	        });
	        homeScene.setOnDragDropped(new EventHandler<DragEvent>() {
	            @Override
	            public void handle(DragEvent event) {
	                Dragboard db = event.getDragboard();
	                boolean success = false;
	                if (db.hasFiles()) {
	                    success = true;
	                    String filePath = null;
	                    for (File file : db.getFiles()) {
	                        filePath = file.getAbsolutePath();
	                        try {
								server.upload(new File(filePath), Main.instance);
							} catch (IOException e) {
								e.printStackTrace();
							}
	                    }
	                }
	                event.setDropCompleted(success);
	                event.consume();
	            }
	        });
			
			// Setup stage
			primaryStage.setOnCloseRequest(this);

			primaryStage.setScene(loginScene);
			primaryStage.setTitle("Awesome App");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void showHome() {
		stage.setScene(homeScene);
	}
	
	public void showLogin() {
		stage.setScene(loginScene);
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

	public Server getServer() {
		return server;
	}
}
