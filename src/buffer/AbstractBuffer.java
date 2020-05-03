package buffer;

import org.apache.commons.io.FileUtils;

public class AbstractBuffer implements Buffer{
	private byte[] buffer;
	private long startTime;
	
	private boolean calculating;
	private int counter = 0;
	private double totalSpeed = 0;
	private final int COUNT_MAX = 50;

	private double avgSpeed;
	
	private static final int NS = 1000000000;
	private static final int MS = 1000;
	private static final int S = 1;
	
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
		return ((double) written)/lapsedTime;
	}
	
	public double getSpeedSeconds(long written) {
		return getSpeed(written)*NS;
	}
	
	public String getReadableSpeed(long written) {
		double speed = getSpeedSeconds(written);
		String readable = FileUtils.byteCountToDisplaySize((long) speed) + "/s";
		
		return readable.toLowerCase();
	}
	
	public long getSecondsLeft(long written, long sizeLeft) {
		System.out.println(sizeLeft);
		System.out.println(getSpeedSeconds(written));
		System.out.println(getSpeedSeconds((long) ((double) sizeLeft / getSpeedSeconds(written))));
		return (long) ((double) sizeLeft / getSpeedSeconds(written));
	}
}
