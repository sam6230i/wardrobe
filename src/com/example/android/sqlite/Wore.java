package com.example.android.sqlite;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: rakesh
 * Date: 26/10/13
 * Time: 11:39 PM
 */
public class Wore {
	private long id;
	private long shirtId;
	private long pantId;
	private String shirtPath;
	private String pantPath;
	private Date woreDate;

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

	public Date getWoreDate() {
		return woreDate;
	}

	public String getWoreDateStr() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d");
		return simpleDateFormat.format(woreDate);
	}

	public void setWoreDate(Date woreDate) {
		this.woreDate = woreDate;
	}
}
