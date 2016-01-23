package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;

public class Server implements ServerListener {

	public static final int MAX_THREADS = 10;

	private ExecutorService threadPool;

	public Server() {
		threadPool = Executors.newFixedThreadPool(MAX_THREADS);
	}

	public void upload(File file) throws MalformedURLException, IOException {
		threadPool.submit(new Runnable() {

			@Override
			public void run() {
				try {
					HttpURLConnection httpUrlConnection = (HttpURLConnection)new URL("http://www.nodynotes.com/findme/upload.php?name=" + file.getName()).openConnection();
			        httpUrlConnection.setDoOutput(true);
			        httpUrlConnection.setRequestMethod("POST");
			        OutputStream os = httpUrlConnection.getOutputStream();
			        FileInputStream fis = new FileInputStream(file);

			        while(fis.available() > 0) {
			            os.write(fis.read());
			        }

			        os.close();
			        fis.close();
			        httpUrlConnection.getInputStream(); //the output is not going to send if we don't try to get the answer from the server
				} catch(Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	public void close() {
		threadPool.shutdown();
	}

	@Override
	public void onResult() {
		System.out.println("Uploaded file");
	}
}
