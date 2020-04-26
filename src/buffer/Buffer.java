package buffer;

public interface Buffer {
	/**
	 * Gets the current buffer
	 * @return buffer
	 */
	public byte[] getBuffer();
	
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
	 * Gets a readable version of getSpeed
	 * @param written bytes
	 * @return readable speed
	 */
	public String getReadableSpeed(long written);
}
