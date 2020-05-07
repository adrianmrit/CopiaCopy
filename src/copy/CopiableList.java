package copy;
import java.util.ArrayList;

import enums.ConflictAction;

public class CopiableList {
	private static final int DFAULT_ARRAY_CAPACITY = 1000;
	private ArrayList<Copiable> allFiles;
	private long totalSize = 0;
	private int totalFiles = 0;
	private int lineupPointer = 0;
	
	/**
	 * A list that hold all the copiables that will be copied
	 */
	public CopiableList() {
		this.allFiles = new ArrayList<>(DFAULT_ARRAY_CAPACITY);
		this.allFiles.ensureCapacity(DFAULT_ARRAY_CAPACITY);
	}
	
	/**
	 * Adds a copiable to the list, and updates counters.
	 * @param c
	 */
	public void register(Copiable c) {
		allFiles.add(c);
		totalSize += c.getSize();
	}
	
	/**
	 * Does not actually removes a file from the list, copiables should be set to SKIP
	 * if they shouldn't be copied. Updates counters.
	 * @param c
	 */
	public void remove(Copiable c) {
//		this.allFiles.remove(c); do not remove the files, just skip them
		this.totalSize -= c.getSize();
	}
	
	/**
	 * Moves the pointer to the next copiable object
	 */
	public void movePointer() {
		this.lineupPointer++;
	}
	
	public int getTotalCopied() {
		return this.lineupPointer;
	}
	
	public int getNaturalCurrentCopy() {
		return this.lineupPointer+1;
	}
	
	/** 
	 * Checks if there are copiables left to be copied
	 * @return false if all copiables where copied, true otherwise.
	 */
	public boolean hasNext() {  // non copied files should go first for this to work;
		return lineupPointer < allFiles.size();
	}
	
	/**
	 * Returns the next LinkedFile that is not copied
	 * @return Next LinkedFile that is not copied;
	 */
	public Copiable getNext() {  // non copied files should go first for this to work;
		if (hasNext()) {
			Copiable c = allFiles.get(lineupPointer);
			return c;
		} else {
			return null; // TODO: throw error
		}
	}
	
	/**
	 * Gets the total size in bytes.
	 * @return size in bytes
	 */
	public long getTotalSize() {
		return this.totalSize;
	}
	
	/**
	 * Gets the number of copiable objects
	 * @return number of copiables
	 */
	public int getCount() {
		return this.allFiles.size();
	}

	public void setForAll(ConflictAction action) {
		for (Copiable c:allFiles) {
			c.setConflictAction(action);
		}
		
	}
}
