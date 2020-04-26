package buffer;

import org.apache.commons.io.FileUtils;

public class AbstractBuffer implements Buffer{
	private byte[] buffer;
	private long startTime;
	
	/**
	 * Sets the buffer with a given size.
	 * @param bufferSize size for the buffer
	 */
	public void setBuffer(int bufferSize) {
		buffer = new byte[bufferSize];
	}
	
	public byte[] getBuffer() {
		return buffer;
	}
	
	public void resetTime() {
		this.startTime = System.currentTimeMillis();
	}
	
	public double getSpeed(long written) {
		long lapsedTime = System.currentTimeMillis() - startTime; // lapsed time in milliseconds
		lapsedTime = Math.max(1, lapsedTime);
		return (double) written/lapsedTime;
	}
	
	public String getReadableSpeed(long written) {
		double speed = getSpeed(written);
		double b_per_s = speed*1000; // transform milliseconds to seconds
		String readable = FileUtils.byteCountToDisplaySize((long) b_per_s) + "/s";
		
		return readable.toLowerCase();
	}
}
