package buffer;

import org.apache.commons.io.FileUtils;

/**
 * A buffer with a fixed size.
 */
public class StaticBuffer extends AbstractBuffer{
	/**
	 * Default buffer size of 30 MB;
	 */
	public static final int DEFAULT_BUFFER_SIZE = (int) FileUtils.ONE_KB*1;
	
	/**
	 * Initializes the buffer with a default size equal to {@link StaticBuffer#DEFAULT_BUFFER_SIZE }.
	 */
	public StaticBuffer() {
		this.setBuffer(DEFAULT_BUFFER_SIZE);
	}
	
	/**
	 * Creates a buffer with size equal to bufferSize.
	 * @param bufferSize
	 */
	public StaticBuffer(int bufferSize) {
		this.setBuffer(bufferSize);
	}
}
