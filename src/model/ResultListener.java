package model;

import java.io.File;
import java.util.List;

public interface ResultListener {

	public void onResult(File file, List<String> tags);
}
