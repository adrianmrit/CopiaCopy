package gui;

import java.util.ArrayList;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class LongProgressBarModel implements BoundedRangeModel{
	/**
	 * final serialVersionUID
	 */
	private static final long serialVersionUID = 8797504242884419108L;
	private long maxLong;
	private long minLong;
	private long valueLong;
	private int max;
	private int min;
	private int value;
	private boolean isAdjusting = false;
	private int extent;
	private EventListenerList listenerList;
	private ChangeEvent changeEvent;
	private boolean indeterminate = true;
	private boolean stringPainted = true;
	
	/** An extended <b>javax.swing.JProgressBar</b>.
	 * Allows the use of long type numbers instead of integers 
	 */
	public LongProgressBarModel() {
		this.maxLong = 100;
		this.minLong = 0;
		this.valueLong = 0;
		
		this.max = 100;
		this.min = 0;
		this.value = 0;
		this.extent = 0;
		
		listenerList = new EventListenerList();
	}
	
	
	/** An extended <b>javax.swing.JProgressBar</b>.
	 * Allows the use of long type numbers instead of integers 
	 * @param minLong minimum value
	 * @param maxLong maximum value
	 */
	public LongProgressBarModel(long minLong, long maxLong, int min, int max) {
		this.setLongMaximum(maxLong);
		this.setLongMinimum(minLong);
		this.valueLong = 0;
		
		this.setMaximum(max);
		this.setMinimum(min);
		this.setExtent(extent);
		this.value = 0;
		
		listenerList = new EventListenerList();
	}
	
	public LongProgressBarModel(long minLong, long maxLong) {
		this(minLong, maxLong, 0, 100);
	}
	
	/** Converts to the integer ranged value
	 * @return resulting converted value
	 */
	public int getConvertedValue() {
		int converted = (int) (((double) this.valueLong / (double) this.maxLong) * this.max);
		return converted;
	}
	
	/** Adds to the current value. Calls setValue(long newVal) with the resulting value
	 * @param newVal value to be added to current value
	 */
	public void addLongValue(long newVal) {
		this.setLongValue(this.valueLong + newVal);
	}
	
	/** Sets a new long value, calculates percent,
	 * and calls javax.swing.JProgressBar.setValue(percent) if the percent changed
	 * @param newVal new long type value
	 */
	public void setLongValue(long newVal) {
		this.valueLong = newVal;
		int converted = this.getConvertedValue();
		this.setValue(converted);
	}
	
	/** Sets the maximum
	 * @param n maximum value
	 */
	public void setLongMaximum(long n) {
		this.maxLong = n;
	}
	
	/** Sets the minimum value
	 * @param n minimum value
	 */
	public void setLongMinimum(long n) {
		this.minLong = n;
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
	
	/** Gets the current long value
	 * @return current value
	 */
	public long getLongValue() {
		return this.valueLong;
	}


	@Override
	public int getMinimum() {
		return this.min;
	}


	@Override
	public void setMinimum(int newMinimum) {
		setRangeProperties(this.value, this.extent, newMinimum, this.max, this.isAdjusting);
	}


	@Override
	public int getMaximum() {
		return this.max;
	}


	@Override
	public void setMaximum(int newMaximum) {
		setRangeProperties(this.value, this.extent, this.min, newMaximum, this.isAdjusting);
	}


	@Override
	public int getValue() {
		return this.value;
	}


	@Override
	public void setValue(int newValue) {
		setRangeProperties(newValue, this.extent, this.min, this.max, this.isAdjusting);
		
	}


	@Override
	public void setValueIsAdjusting(boolean b) {
		setRangeProperties(this.value, this.extent, this.min, this.max, b);
		
	}


	@Override
	public boolean getValueIsAdjusting() {
		return this.isAdjusting;
	}


	@Override
	public int getExtent() {
		return this.extent;
	}


	@Override
	public void setExtent(int newExtent) {
		this.extent = newExtent;
	}


	@Override
	public void setRangeProperties(int value, int extent, int min, int max, boolean adjusting) {
		boolean changed =
				(this.value != value) ||
				(this.extent != extent) ||
				(this.min != min) ||
				(this.max != max) ||
				(this.isAdjusting != adjusting);
		if (changed) {
			this.value = value;
			this.extent = extent;
			this.min = min;
			this.max = max;
			this.isAdjusting = adjusting;
			
			fireStateChanged();
		}
	}


	@Override
	public void addChangeListener(ChangeListener x) {
		this.listenerList.add(ChangeListener.class, x);
		
	}


	@Override
	public void removeChangeListener(ChangeListener x) {
		this.listenerList.remove(ChangeListener.class, x);
	}
	
	public ChangeListener[] getChangeListeners() {
		return listenerList.getListeners(ChangeListener.class);
	}
	
	protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -=2 ) {
            if (listeners[i] == ChangeListener.class) {
                if (this.changeEvent == null) {
                	this.changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i+1]).stateChanged(this.changeEvent);
            }
        }
    }

	
	public String toString() {
		String modelStr = 
				"maxLong=" + this.getLongMaximum() + ", " +
				"minLong=" + this.getLongMinimum() + ", " +
				"valueLong=" + this.getLongValue() + ", " +
				"max=" + this.getMaximum() + ", " +
				"min=" + this.getMinimum() + ", " +
				"value=" + this.getValue() + ", " +
				"extent=" + this.getExtent() + ", " +
				"adj=" + this.getValueIsAdjusting() + ", ";
		
		return getClass().getName() + "[" + modelStr + "]";
	}


	public void setIndeterminate(boolean val) {
		this.indeterminate = val;
		fireStateChanged();
	}
	
	public void setStringPainted(boolean val) {
		this.stringPainted  = val;
		fireStateChanged();
	}
	
	public boolean isIndeterminate() {
		return this.indeterminate;
	}
	
	public boolean isStringPainted() {
		return this.stringPainted;
	}
	
}
