package copy;

public class CopyTimer {
	private boolean calculating;
	private int counter = 0;
	private double totalSpeed = 0;
	
	/**
	 * Number of speed reads used to calculate average
	 */
	public final int AVG_COUNT_MAX = 100;
	
	private double avgSpeed;
	
	private long lastTimeLeft = Long.MAX_VALUE; // max value so actual is < lastTimeLeft on the first iteration
	private long upJumps=0;
	
	/**
	 * Number of times the time left have to go up before setting it to that value.
	 * This avoids irregular jumps.
	 */
	public final int UP_JUMS_BEFOR_RELOAD = 2;
	
	
	/**
	 * A timer for file copy processes. Uses seconds internally
	 */
	public CopyTimer(){
		calculating = true;
	}
	
	/**
	 * Calculates the average speed internally, in bytes per second
	 * @param speed
	 */
	public void calcAvgSpeed(double speed) {
		if (counter<AVG_COUNT_MAX) {
			counter++;
			totalSpeed += Math.max(speed, 1);
		} else {
			calculating = false;
			avgSpeed = totalSpeed/counter;
			counter=0;
			totalSpeed=0;
		}
	}
	
	/**
	 * Gets if the time left is being calculated or not.
	 * @return
	 */
	public boolean isCalculating() {
		return calculating;
	}
	
	/**
	 * Calculates the time left. If the actual time left is greater than the last time left,
	 * wait and see if it is still higher for next calls. If it is greater for a certain
	 * number of calls then update the time and the last time left, otherwise return the
	 * last time left. This avoid ugly jumps up in time.
	 * @param sizeLeft size left to be copied.
	 * @return expected time to copy that amount of data
	 */
	private long calcTime(long sizeLeft) {
		long actual = (long) ((double) sizeLeft/avgSpeed);  // calculates how long have passed
		if (actual > lastTimeLeft) {
			upJumps++;
			if (upJumps == UP_JUMS_BEFOR_RELOAD) {
				lastTimeLeft = actual;
			}
		} else {
			lastTimeLeft = actual;
		}
		
		return lastTimeLeft;
	}
	
	/**
	 * Gets the expected time to copy a certain amount of data.
	 * @param sizeLeft amount of data
	 * @return expected time in seconds
	 */
	public long getTime(long sizeLeft) {
		return calcTime(sizeLeft);
	}

}
