package gui;

import javax.swing.JProgressBar;

public class LongProgressBar extends JProgressBar{
	private long maxLong;
	private long minLong;
	private static final long serialVersionUID = 1L;
	private long value;
	
	/** An extended <b>javax.swing.JProgressBar</b>.
	 * Allows the use of long type numbers instead of integers 
	 */
	public LongProgressBar() {
		super();
	}
	
	
	/** An extended <b>javax.swing.JProgressBar</b>.
	 * Allows the use of long type numbers instead of integers 
	 * @param minLong minimum value
	 * @param maxLong maximum value
	 */
	public LongProgressBar(long minLong, long maxLong) {
		super(0, 100);
		this.maxLong = maxLong;
		this.minLong = minLong;
		this.value = minLong;
	}
	
	/** Calculates percent
	 * @return resulting percent
	 */
	public int longPercent() {
		int percent = (int) (((double) this.value / (double) this.maxLong) * 100);
		return percent;
	}
	
	/** Adds to the current value. Calls setValue(long newVal) with the resulting value
	 * @param newVal value to be added to current value
	 */
	public void addValue(long newVal) {
		this.setValue(this.value + newVal);
	}
	
	/** Sets a new long value, calculates percent,
	 * and calls javax.swing.JProgressBar.setValue(percent) if the percent changed
	 * @param newVal new long type value
	 */
	public void setValue(long newVal) {
		this.value = newVal;
		int percent = this.longPercent();
		if (percent != this.getValue()) {
			this.setValue(this.longPercent()); // avoid unnecessary updates
		}
	}
	
	/** Resets the value to the minimum value
	 */
	public void reset() {
		this.setValue(this.minLong);
	}
	
	/** Gets the current value
	 * @return current value
	 */
	public long getLongValue() {
		return this.value;
	}
	
	/** Sets the maximum
	 * @param n maximum value
	 */
	public void setMaximum(long n) {
		this.maxLong = n;
		super.setMaximum(100);
	}
	
	/** Sets the minimum value
	 * @param n minimum value
	 */
	public void setMinimum(long n) {
		this.minLong = n;
		super.setMinimum(0);
	}
	
	/** Gets the maximum value
	 * @return maximum value
	 */
	public long getLongMaximum() {
		return this.maxLong;
	}
	
	/** Gets the minimum value
	 * @return minimum value
	 */
	public long getLongMinimum() {
		return this.minLong;
	}
	
}
