package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.APIUtils;
import model.ResultListener;
import model.Server;
import model.UploadListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;


public class Main extends Application implements EventHandler<WindowEvent>, ResultListener, UploadListener {

	private APIUtils apiUtils = new APIUtils();
	Server server = new Server();

	public static Main instance;//Singleton

	private Stage stage;
	private Scene mainScene;

	private Parent loginRoot;
	private Parent homeRoot;

	private LoginController loginController;
	private HomeController homeController;

	@Override
	public void start(Stage primaryStage) {
		instance = this;
		stage = primaryStage;

		try {
			// Load FXML
			FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
			FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));

			loginRoot = loginLoader.load();
			loginController = loginLoader.getController();
			homeRoot = homeLoader.load();
			homeController = homeLoader.getController();


			mainScene = new Scene(loginRoot, 800, 560);

			// Setup home scene
			homeRoot.setOnDragOver(new EventHandler<DragEvent>() {
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
	        homeRoot.setOnDragDropped(new EventHandler<DragEvent>() {
	            @Override
	            public void handle(DragEvent event) {
	                Dragboard db = event.getDragboard();
	                boolean success = false;
	                if (db.hasFiles()) {
	                    success = true;
	                    String filePath = null;
	                    for (File file : db.getFiles()) {
	                    	filePath = file.getAbsolutePath();

	                    	//Copy locally
	                    	try {
	                    		Path newPath = FileSystems.getDefault().getPath("images", file.getName());
								Files.copy(file.toPath(), newPath, StandardCopyOption.REPLACE_EXISTING);
							} catch (IOException e) {
								e.printStackTrace();
							}

	                    	//Get tags and upload to server
	                        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);
							if(ext.equals("jpeg") || ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {
								apiUtils.analyseImage(file, new ResultListener() {
									@Override
									public void onResult(File file, List<String> tags) {
										server.upload(file, Main.instance, Server.toGetRequest(tags));
									}
								});
							}
							else if(ext.equals("txt")) {
								apiUtils.analyseText(file, new ResultListener() {
									@Override
									public void onResult(File file, List<String> tags) {
										server.upload(file, Main.instance, Server.toGetRequest(tags));
									}
								});
							}
							else
								server.upload(file, Main.instance, "");
	                    }
	                }
	                event.setDropCompleted(success);
	                event.consume();
	            }
	        });

			// Setup stage
			primaryStage.setOnCloseRequest(this);

			primaryStage.setScene(mainScene);
			primaryStage.setTitle("FindMe");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void showHome() {
		mainScene.setRoot(homeRoot);
	}

	public void showLogin() {
		mainScene.setRoot(loginRoot);
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

	@Override
	public void onResult(File file, List<String> tags) {
		// TODO Auto-generated method stub

	}
}
