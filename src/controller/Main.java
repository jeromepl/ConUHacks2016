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

	@Override
	public void start(Stage primaryStage) {
		instance = this;
		stage = primaryStage;

		try {
			server.upload(new File("images/kiwi.jpg"), this);

			Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));

			Scene scene = new Scene(loginRoot, 800, 560);

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

	public void showHome() {

		try {
			Parent homeRoot = FXMLLoader.load(getClass().getResource("/view/Home.fxml"));
			Scene scene = new Scene(homeRoot, 800, 560);

			//DRAG AND DROP
			scene.setOnDragOver(new EventHandler<DragEvent>() {
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

	        // Dropping over surface
	        scene.setOnDragDropped(new EventHandler<DragEvent>() {
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

	        stage.setScene(scene);
		} catch(Exception e) {
			e.printStackTrace();
		}
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
