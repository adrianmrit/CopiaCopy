package utils;

public class TimerFormater {
	private long timeSeconds;
	
	/**
	 * A class used to format time in HH:MM:SS, where hours can be grater than 24.
	 * @param time in seconds
	 */
	public TimerFormater(long time) {
		this.timeSeconds = time;
	}
	
	/**
	 * Gets the number of hours between a number of seconds.
	 * @param seconds
	 * @return number of hours
	 */
	private static int getHours(long seconds) {
		return (int) (seconds/3600);
	}
	
	/**
	 * Gets the number of minutes between a number of seconds.
	 * @param seconds
	 * @return number of minutes
	 */
	private static int getMinutes(long seconds) {
		return (int) (seconds/60);
	}
	
	/**
	 * Returns the time in HH:MM:SS format.
	 */
	public String toString() {
		int h = getHours(timeSeconds);
		int hSeconds = h*3600;
		
		int m = getMinutes(timeSeconds-h*3600);
		int mSeconds = m*60;
		
		int s = (int) (timeSeconds - hSeconds - mSeconds);
		
		return String.format("%02d:%02d:%02d", h, m, s);
	}

}
