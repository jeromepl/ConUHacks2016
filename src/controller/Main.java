package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.APIUtils;
import model.ResultListener;
import model.Server;
import model.UploadListener;


public class Main extends Application implements EventHandler<WindowEvent>, ResultListener, UploadListener {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 560;
	
	public static double currentWidth = WIDTH;
	public static double currentHeight = HEIGHT;
	
	private APIUtils apiUtils = new APIUtils();
	Server server = new Server();

	public static Main instance;//Singleton

	private Stage stage;
	private Scene mainScene;

	private Parent loginRoot;
	private Parent homeRoot;
	private Parent imageRoot;

	private LoginController loginController;
	private HomeController homeController;
	private ImageController imageController;

	@Override
	public void start(Stage primaryStage) {
		instance = this;
		stage = primaryStage;

		try {
			// Load FXML
			FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
			FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));
			FXMLLoader imageLoader = new FXMLLoader(getClass().getResource("/view/Image.fxml"));

			loginRoot = loginLoader.load();
			loginController = loginLoader.getController();
			homeRoot = homeLoader.load();
			homeController = homeLoader.getController();
			imageRoot = imageLoader.load();
			imageController = imageLoader.getController();


			mainScene = new Scene(loginRoot, WIDTH, HEIGHT);

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
	                	homeController.showSync();
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

	        // Setup resizing
	        mainScene.widthProperty().addListener(new ChangeListener<Number>() {
	            @Override 
	            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
	                currentWidth = (double) newSceneWidth;
	            	homeController.rebuildImages();
	            	imageController.rebuildImage();
	            }
	        });
	        mainScene.heightProperty().addListener(new ChangeListener<Number>() {
	            @Override 
	            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
	            	currentHeight = (double) newSceneHeight;
	            	homeController.rebuildImages();
	            	imageController.rebuildImage();
	            }
	        });
	        
			// Setup stage
			primaryStage.setOnCloseRequest(this);

			primaryStage.setScene(mainScene);
			primaryStage.setTitle("FindMe");
			primaryStage.getIcons().add(new Image("file:icon.png"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void showHome() {
		FadeTransition ft = new FadeTransition(new Duration(1500), homeRoot);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
		mainScene.setRoot(homeRoot);
	}

	public void showLogin() {
		mainScene.setRoot(loginRoot);
	}
	
	public void showImage(ImageView image) {
		mainScene.setRoot(imageRoot);
		imageController.setImage(new ImageView(image.getImage()));
	}

	@Override
	public void handle(WindowEvent e) {
		apiUtils.close();
		server.close();
	}

	@Override
	public void onResult() {
		homeController.hideSync();
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
