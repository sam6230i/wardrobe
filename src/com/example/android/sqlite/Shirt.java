package com.example.android.sqlite;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 10/11/13
 * Time: 12:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class Shirt {
	private long id;
	private String imagePath;

	public Shirt(){}

	public Shirt(String imagePath) {
		this.imagePath = imagePath;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public String toString() {
		return "";
	}
}
