package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MyFile {

	private Image image;
	private String name = "";
	private String path = "";
	private ImageView imageView;

	public MyFile() {

	}

	public MyFile(Image image, String name, String path) {
		this.image = image;
		this.name = name;
		this.path = path;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public ImageView getImageView() {
		return imageView;
	}
}
