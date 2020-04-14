package gui;

import javax.swing.JProgressBar;

public class LongProgressBar extends JProgressBar{
	private long maxLong;
	private long minLong;
	private static final long serialVersionUID = 1L;
	private long value;
	
	public LongProgressBar() {
		super();
	}
	
	public LongProgressBar(long minLong, long maxLong) {
		super(0, 100);
		this.maxLong = maxLong;
		this.minLong = minLong;
		this.value = minLong;
	}
	
	public int longPercent() {
		int percent = (int) (((double) this.value / (double) this.maxLong) * 100);
		return percent;
	}
	
	public void addValue(long newVal) {
		this.setValue(this.value + newVal);
	}
	
	public void setValue(long newVal) {
		this.value = newVal;
		int percent = this.longPercent();
		if (percent != this.getValue()) {
			this.setValue(this.longPercent()); // avoid unnecesary updates
		}
	}
	
	public void reset() {
		this.setValue(0);
	}
	
	public double getLongValue() {
		return this.value;
	}
	
	public void setMaximum(long n) {
		this.maxLong = n;
		super.setMaximum(100);
	}
	
	public void setMinimum(long n) {
		this.minLong = n;
		super.setMinimum(0);
	}
	
	public double getLongMaximum() {
		return this.maxLong;
	}
	
	public double getLongMinimum() {
		return this.minLong;
	}
	
}
