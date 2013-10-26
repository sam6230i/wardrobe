package com.example.android.sqlite;

/**
 * User: rakesh
 * Date: 26/10/13
 * Time: 10:29 PM
 */
public class Favourite {
	private long id;
	private long shirtId;
	private long pantId;
	private String shirtPath;
	private String pantPath;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getShirtId() {
		return shirtId;
	}

	public void setShirtId(long shirtId) {
		this.shirtId = shirtId;
	}

	public long getPantId() {
		return pantId;
	}

	public void setPantId(long pantId) {
		this.pantId = pantId;
	}

	public String getShirtPath() {
		return shirtPath;
	}

	public void setShirtPath(String shirtPath) {
		this.shirtPath = shirtPath;
	}

	public String getPantPath() {
		return pantPath;
	}

	public void setPantPath(String pantPath) {
		this.pantPath = pantPath;
	}
}
