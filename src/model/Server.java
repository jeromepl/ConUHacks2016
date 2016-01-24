package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	public static final int MAX_THREADS = 10;

	private ExecutorService threadPool;

	public Server() {
		threadPool = Executors.newFixedThreadPool(MAX_THREADS);
	}

	public void upload(File file, UploadListener listener, String tags) {
		threadPool.submit(new Runnable() {

			@Override
			public void run() {
				try {
					String url = "http://www.nodynotes.com/findme/upload.php?name=" + file.getName() + "&tags=" + tags;
					HttpURLConnection httpUrlConnection = (HttpURLConnection)new URL(url).openConnection();
			        httpUrlConnection.setDoOutput(true);
			        httpUrlConnection.setRequestMethod("POST");
			        OutputStream os = httpUrlConnection.getOutputStream();
			        FileInputStream fis = new FileInputStream(file);

			        while(fis.available() > 0) {
			            os.write(fis.read());
			        }

			        os.close();
			        fis.close();

			        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
					String inputLine;
					String response = "";

					while ((inputLine = in.readLine()) != null) {
						response += inputLine;
					}
					in.close();

					System.out.println(response);
				} catch(Exception e) {
					e.printStackTrace();
				}

				listener.onResult();
			}

		});
	}

	public void search(String query, SearchListener listener) {
		threadPool.submit(new Runnable() {

			@Override
			public void run() {

				String[] files = new String[0];
				try {
					URL url = new URL("http://www.nodynotes.com/findme/search.php?query=" + query);
					HttpURLConnection con = (HttpURLConnection)url.openConnection();

					con.setRequestMethod("GET");

					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					String response = "";

					while ((inputLine = in.readLine()) != null) {
						response += inputLine;
					}
					in.close();

					if(!response.isEmpty())
						files = response.split(";");

					System.out.println(response);

				} catch (IOException e) {
					e.printStackTrace();
				}

				listener.onResult(files);
			}
		});
	}

	public static String toGetRequest(List<String> tags) {
		StringBuffer string = new StringBuffer();
		for (String tag : tags) {
			string.append(tag + ";");
		}
		string.deleteCharAt(string.length() - 1);
		return string.toString().replaceAll(" ", "_");
	}

	public void close() {
		threadPool.shutdown();
	}
}
