package copy;

public class DinamicBuffer {
	public static final int DEFAULT_BUFFER = 8192; // 8kb
	public static final int MAX_BUFFER = 1048576; // 1mb;
	public static final int MIN_BUFFER = 1024; // 1kb;
	public static final double ERROR_RANGE = 0.25;
	public static final int INCREMENT_SIZE = 1024;  // increments of 1KB
	
	private int currentBuffer;
	private double lastRatio;
	private long startTime;
	
	public DinamicBuffer() {
		currentBuffer = DEFAULT_BUFFER;
	}
	
	public int getBuffer() {
		return currentBuffer;
	}
	
	public void resetTime() {
		this.startTime = System.currentTimeMillis();
	}
	
	public void reset() {
		this.lastRatio = 0;
	}
	
	public void increaseBuffer() {
		currentBuffer = Math.min(currentBuffer+INCREMENT_SIZE, MAX_BUFFER);
	}
	
	public void decreaseBuffer() {
		currentBuffer = Math.max(currentBuffer-INCREMENT_SIZE, MIN_BUFFER);
	}
	
	public void recalculate(long written) {
		long lapsedTime = System.currentTimeMillis() - startTime; // lapsed time in milliseconds
		recalculate(written, lapsedTime);
	}
	
	public void recalculate(long written, long lapsedTime) {  // can be used for testing purposes
		double ratio = (double) written / lapsedTime; // bytes per millisecond;
		
		if (lastRatio == 0) {
			lastRatio = ratio;
		}
		
		calcNewBuffer(ratio);
	}
	
	private void calcNewBuffer(double ratio) {
		double diff = ratio - lastRatio;
		if (diff < -ERROR_RANGE) {  // new ratio is less than lastRatio, buffer was too big, max speed reached
			decreaseBuffer();
		} else {
			increaseBuffer();
		}
	}
	
	

}
