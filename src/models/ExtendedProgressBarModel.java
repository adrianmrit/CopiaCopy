package models;

import components.ExtendedProgressBar;

public class ExtendedProgressBarModel extends LongProgressBarModel{
	private String sizeLeft = "";
	private String timeLeft = "";
	
//	public ExtendedProgressBarModel(ExtendedProgressBar progressBar) {
//		super();
//		this.progressBar = progressBar;
//	}
//	
//	public ExtendedProgressBarModel(ExtendedProgressBar progressBar, long minLong, long maxLong, int min, int max) {
//		super(minLong, maxLong, min, max);
//		this.progressBar = progressBar;
//	}
//	
//	public ExtendedProgressBarModel(ExtendedProgressBar progressBar, long minLong, long maxLong) {
//		super(minLong, maxLong);
//		this.progressBar = progressBar;
//	}
	
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
