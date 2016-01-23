package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
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
		//File image = new File("images/Banana21.jpg");
		//analyseImage(image, this);
		File text = new File("images/text.txt");
		analyseText(text, this);
	}
	
	public void analyseImage(File image, ResultListener listener) {
		threadPool.submit(new Runnable() {

			@Override
			public void run() {
				List<RecognitionResult> results = clarifai.recognize(new RecognitionRequest(image));
				for (RecognitionResult result : results) {
					List<String> names = new ArrayList<String>();
					for (Tag tag : result.getTags()) {
						names.add(tag.getName());
					}
					listener.onResult(image, names);
				}
			}
			
		});
	}
	
	public void analyseText(File textFile, ResultListener listener) {
		// TODO add file checks
		Scanner input;
		try {
			input = new Scanner(textFile);
			ArrayList<String> names = new ArrayList<String>();
			while (input.hasNext()) {
				String name = input.next().replaceAll("[\\W\\d]", "");
				if (!name.isEmpty() && name.length() > 3) {
					names.add(name);
				}
			}
			
			HashMap<String, Integer> count = new HashMap<String, Integer>();
			for (String name : names) {
				Integer countValue = count.get(name);
				if (countValue == null) {
					count.put(name, 1);
				}
				else {
					count.put(name, ++countValue);
				}
			}
			
			ArrayList<String> strings = new ArrayList<String>();
			ArrayList<Integer> counts = new ArrayList<Integer>();
			for (String key : count.keySet()) {
				strings.add(key);
				counts.add(count.get(key));
			}
			
			// Sort by count
			for (int i = 0; i < counts.size(); i++) {
				int maxIndex = i;
				int maxValue = counts.get(i);
				for (int j = i + 1; j < counts.size(); j++) {
					if (counts.get(j) > maxValue) {
						maxIndex = j;
						maxValue = counts.get(j);
					}
				}
				
				// Swap
				int tempCount = counts.get(maxIndex);
				String tempString = strings.get(maxIndex);
				
				counts.set(maxIndex, counts.get(i));
				strings.set(maxIndex, strings.get(i));
				counts.set(i, tempCount);
				strings.set(i, tempString);
			}
			
//			for (String name : names) {
//				System.out.println(name + ": " + count.get(name));
//			}
//			
//			System.out.println("--------------SORTED----------");
//			for (int i = 0; i < strings.size(); i++) {
//				System.out.println(strings.get(i) + ": " + counts.get(i));
//			}
			
			// Take top 20
			ArrayList<String> finalList = new ArrayList<String>();
			for (int i = 0; i < 20 && i < strings.size(); i++) {
				finalList.add(strings.get(i));
			}
			
			listener.onResult(textFile, finalList);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		threadPool.shutdown();
	}

	@Override
	public void onResult(File file, List<String> tags) {
		System.out.println(file.getName());
		System.out.println();
		for (String tag : tags) {
			System.out.println("(" + tag + ")");
		}
		System.out.println();
		System.out.println(Server.toGetRequest(tags));
	}
}
