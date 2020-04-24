package copy;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class CopiableList {
	private ArrayList<Copiable> allFiles;
	private long totalSize;
	private int lineupPointer;
	
	public CopiableList() {
		this.allFiles = new ArrayList<>();
		this.totalSize = 0;
		this.lineupPointer = 0;
	}
	
	public void register(Copiable c) {
		this.allFiles.add(c);
		this.totalSize += c.getSize();
	}
	
	public void remove(Copiable c) {
		this.allFiles.remove(c);
		this.totalSize -= c.getSize();
	}
	
	public void reset() {
		this.allFiles = new ArrayList<>();
		this.totalSize = 0;
	}
	
	public void movePointer() {
		this.lineupPointer++;
	}
	
//	public void load(File origin, Path rootDest) {
//		LinkedFile rootLf = new LinkedFile(origin, origin.getParentFile().toPath(), rootDest, this);
//		rootLf.register();
//		rootLf.registerTree();
//	}
	
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
	public Copiable getNext() {  // non copied files should go first for this to work;
		if (hasNext()) {
			Copiable c = allFiles.get(lineupPointer);
			return c;
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
