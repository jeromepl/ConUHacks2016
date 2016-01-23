package model;

import java.io.File;
import java.util.List;

import com.clarifai.api.Tag;

public interface ResultListener {

	public void onResult(File file, List<Tag> tags);
}
