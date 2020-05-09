package buffer;

public interface Buffer {
	/**
	 * Gets the current buffer
	 * @return buffer
	 */
	public byte[] getBuffer();
	
	/**
	 * Gets the current buffer size
	 * @return
	 */
	public int getBufferSize();
	
	/**
	 * Resets the time
	 */
	public void resetTime();
	
	
	/**
	 * Gets the speed in bytes per millisecond
	 * @param written bytes
	 * @return bytes per millisecond
	 */
	public double getSpeed(long written);
	
	/**
	 * Gets the speed in bytes per second
	 * @param written
	 * @return bytes per second
	 */
	public double getSpeedSeconds(long written);
	
	/**
	 * Gets a readable version of getSpeed
	 * @param written bytes
	 * @return readable speed
	 */
	public String getReadableSpeed(long written);
	
	/** 
	 * Gets the amount of seconds left based on 
	 * @param written
	 * @param total
	 * @return
	 */
	public long getSecondsLeft(long written, long total);
}
