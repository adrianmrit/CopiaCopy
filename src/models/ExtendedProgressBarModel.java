package models;

import components.ExtendedProgressBar;

public class ExtendedProgressBarModel extends LongProgressBarModel{
	private String sizeLeft = "";
	private String timeLeft = "";
	
	public void setSizeLeft(String sizeLeft) {
		this.sizeLeft = sizeLeft;
		fireStateChanged();
	}
	
	public void setTimeLeft(String timeLeft) {
		this.timeLeft = timeLeft;
		fireStateChanged();
	}
	
	public String getSizeLeft() {
		return this.sizeLeft;
	}
	
	public String getTimeLeft() {
		return this.timeLeft;
	}

}
