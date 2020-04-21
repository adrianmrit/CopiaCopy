package copy;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class LinkedFileList {
	private ArrayList<LinkedFile> allFiles;  // a common arrayList between all objects, public for testing
	private long totalSize;
	private int lineupPointer;
	
	public LinkedFileList() {
		this.allFiles = new ArrayList<>();
		this.totalSize = 0;
		this.lineupPointer = 0;
	}
	
	public void register(LinkedFile lf) {
		this.allFiles.add(lf);
		this.totalSize += lf.getSize();
	}
	
	public void remove(LinkedFile lf) {
		this.allFiles.remove(lf);
		this.totalSize -= lf.getSize();
	}
	
	public void reset() {
		this.allFiles = new ArrayList<>();
		this.totalSize = 0;
	}
	
	public void movePointer() {
		this.lineupPointer++;
	}
	
	public void load(File origin, Path rootDest) {
		LinkedFile rootLf = new LinkedFile(origin, origin.getParentFile().toPath(), rootDest, this);
		rootLf.register();
		rootLf.registerTree();
	}
	
	/** 
	 * Checks if there are files left to be copied
	 * @return false if all files where copied, true otherwise.
	 */
	public boolean hasNext() {  // non copied files should go first for this to work;
		return lineupPointer < allFiles.size();
	}
	
	/**
	 * Returns the next LinkedFile that is not copied
	 * @return Next LinkedFile that is not copied;
	 */
	public LinkedFile getNext() {  // non copied files should go first for this to work;
		if (hasNext()) {
			LinkedFile f = allFiles.get(lineupPointer);
			return f;
		} else {
			return null; // TODO: throw error
		}
	}
	
	public long getTotalSize() {
		return this.totalSize;
	}
	
	public int count() {
		return this.allFiles.size();
	}
	
	
}
