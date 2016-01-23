package model;

import java.io.File;

import com.clarifai.api.RecognitionResult;

public interface ResultListener {

	public void OnResult(File file, RecognitionResult result);
}
