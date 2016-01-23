package model;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;

public class APIUtils implements ResultListener {

	private static final String id = "MC1Psq3mFwSriro3bn6tcOu2IKyvHCcQ29Rqya4W";
	private static final String secret = "tRAymNmwEuis04Q7m9TccbhgGqvZRUX3VstWbGO7";
	
	public static final int MAX_THREADS = 10;
	
	private ClarifaiClient clarifai;
	
	private ExecutorService threadPool;
	
	public APIUtils() {
		clarifai = new ClarifaiClient(id, secret);
		threadPool = Executors.newFixedThreadPool(MAX_THREADS);
		System.out.println("Success");
		
		// Test
		File image = new File("images/Banana21.jpg");
		analyseImage(image, this);
	}
	
	public void analyseImage(File image, ResultListener listener) {
		threadPool.submit(new Runnable() {

			@Override
			public void run() {
				List<RecognitionResult> results = clarifai.recognize(new RecognitionRequest(image));
				for (RecognitionResult result : results) {
					listener.onResult(image, result.getTags());
				}
			}
			
		});
		//return clarifai.recognize(new RecognitionRequest(image));
	}
	
	public void close() {
		threadPool.shutdown();
	}

	@Override
	public void onResult(File file, List<Tag> tags) {
		System.out.println(file.getName());
		System.out.println();
		for (Tag tag : tags) {
			System.out.println("(" + tag.getName() + ", " + (tag.getProbability() * 100) + "%)");
		}
		System.out.println();
	}
}
