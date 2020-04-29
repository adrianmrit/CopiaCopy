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
		this.startTime = System.nanoTime();
	}
	
	public double getSpeed(long written) {
		long lapsedTime = System.nanoTime() - startTime; // lapsed time in nanoseconds
		lapsedTime = Math.max(1, lapsedTime);
		return (double) written/lapsedTime;
	}
	
	public String getReadableSpeed(long written) {
		double speed = getSpeed(written);
		double b_per_s = speed*1000000000; // transform nanoseconds to seconds (1s = 1 000 000 000)
		String readable = FileUtils.byteCountToDisplaySize((long) b_per_s) + "/s";
		
		return readable.toLowerCase();
	}
}
