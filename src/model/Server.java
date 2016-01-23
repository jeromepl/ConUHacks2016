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

import com.clarifai.api.Tag;

public class Server {

	public static final int MAX_THREADS = 10;

	private ExecutorService threadPool;

	public Server() {
		threadPool = Executors.newFixedThreadPool(MAX_THREADS);
	}

	public void upload(File file, ServerListener listener) throws MalformedURLException, IOException {
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

				listener.onResult();
			}

		});
	}
	
	public static String toGetRequest(List<Tag> tags) {
		StringBuffer string = new StringBuffer();
		for (Tag tag : tags) {
			string.append(tag.getName() + ";");
		}
		string.deleteCharAt(string.length() - 1);
		return string.toString().replaceAll(" ", "_");
	}

	public void close() {
		threadPool.shutdown();
	}
}
