package model;

import java.io.File;
import java.util.List;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;

public class APIUtils {

	private static final String id = "MC1Psq3mFwSriro3bn6tcOu2IKyvHCcQ29Rqya4W";
	private static final String secret = "tRAymNmwEuis04Q7m9TccbhgGqvZRUX3VstWbGO7";
	
	ClarifaiClient clarifai;
	
	public APIUtils() {
		clarifai = new ClarifaiClient(id, secret);
		System.out.println("Success");
	}
	
	public List<RecognitionResult> analyseImage(File image) {
		return clarifai.recognize(new RecognitionRequest(image));
	}
}
