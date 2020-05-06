package copy;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

public class CopiableFolder extends CopiableAbstract{
	private ArrayList<Copiable> childrens= new ArrayList<>();
	private long size = 0;
//	private Logger logger = Logger.getLogger("linkedFolder");
	
	/**
	 * {@link Copiable} folder representation.
	 * @param origin Origin folder
	 * @param rootOrigin Origin root path
	 * @param rootDest Destination root
	 * @param SM {@link SuperModel} that contains some info
	 */
	public CopiableFolder(Path origin, Path rootOrigin, Path rootDest,SuperModel SM, Copiable parent, int mode) {
		super(origin, rootOrigin, rootDest, SM, parent, mode);
		 
		ArrayList<Path> folders = new ArrayList<>();
		ArrayList<Path> files = new ArrayList<>();
		ArrayList<Path> symbolicLinks = new ArrayList<>();
		folders.ensureCapacity(100);
		files.ensureCapacity(100);
		files.ensureCapacity(100);

		Stream<Path> fileStream;
		long time = System.currentTimeMillis();
		try {
			fileStream = Files.list(origin);
			Consumer<Path> fileConsumer = new ChildFileConsumer(files, folders, symbolicLinks);
			fileStream.forEach(fileConsumer);
			fileStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fileStream = null;
		}
		
		Iterator<Path> fIterator = symbolicLinks.iterator();
		while (fIterator.hasNext()) {
			addSymLink(fIterator.next());
		}
		
		fIterator = files.iterator();
		while (fIterator.hasNext()) {
			addFile(fIterator.next());
		}
		
		fIterator = folders.iterator();
		while (fIterator.hasNext()) {
			addFolder(fIterator.next());
		}
		time = (System.currentTimeMillis() - time);
//		logger.log(Level.INFO, ""+time);
	}
	/**
	 * Creates this directory and sub-directories that do not exist
	 */
	public void copy() throws IOException {
		if (!this.wasCopied()) {
			try {
				Files.createDirectory(getDest());
			} catch (FileAlreadyExistsException e){
				// do nothing
			}
			this.setCopied();
		}
	}
	
	/**
	 * Updates the size of the folder
	 * @param size
	 */
	private void updateSize(long size) {
		this.size += size;
	}
	
	/**
	 * Adds a folder to the childrens array
	 * @param ch
	 */
	private void addFolder(Path ch) {
		Copiable children = new CopiableFolder(ch, getRootOrigin(), getRootDest(), SM, this, this.getMode());
		this.childrens.add(children);
		
		updateSize(children.getSizeRec()); // updates the size
	}
	
	/**
	 * Adds a file to the childrens array
	 * @param ch
	 */
	private void addFile(Path ch) {
		Copiable children = new CopiableFile(ch, getRootOrigin(), getRootDest(), SM, this, this.getMode());
		this.childrens.add(children);
		
		updateSize(children.getSize()); // updates the size
	}
	
	/**
	 * Adds a symbolic link to the childrens array
	 * @param ch
	 */
	private void addSymLink(Path ch) {
		Copiable children = new CopiableSymbolicLink(ch, getRootOrigin(), getRootDest(), SM, this, this.getMode());
		this.childrens.add(children);
		
		updateSize(children.getSize()); // updates the size
	}
	
	/**
	 * Gets the total size of this folder content
	 * @return size in bytes
	 */
	public long getSizeRec() {
		return this.size;
	}
	
	public long getSize() {
		return 0;
	}
	/** 
	 * Updates this LinkedFile core destination, and updates the core destination paths in it's children if there is any.
	 * @param newName new name for the folder or file
	 */
	public void renameCoreDest(String newName){
		Path parent = getCoreDest().getParent();
		Path oldPath = getCoreDest();
		Path newPath = Paths.get(parent.toString(), newName);
		
		renameCoreDest(oldPath.toString(), newPath.toString());
		renameTreeCoreDest(oldPath.toString(), newPath.toString());
	}
	
	/**
	 * Replaces this core destination of it's children.
	 * 
	 * <b>oldPath</b> and newPath <b>newPath</b> are passed recursively down the file tree,
	 * thus only the root path is changes, while keeping the 
	 * desired structure
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public void renameTreeCoreDest(String oldPath, String newPath)	{
		
		Iterator<Copiable> childsIterator = childrens.iterator();
		
		while (childsIterator.hasNext()) {
			Copiable c = childsIterator.next();
			c.renameCoreDest(oldPath, newPath);
			c.renameTreeCoreDest(oldPath, newPath);  // pass the newPaths along the branches
		}
	}
	
	/**
	 * Skips this folder and it's children, recursively
	 */
	@Override
	public void skip() {
		super.skip();
		this.skipTree();
	}
	
	/**
	 * Skips this folder's children
	 */
	private void skipTree()	{
		
		Iterator<Copiable> childsIterator = childrens.iterator();
		
		while (childsIterator.hasNext()) {
			childsIterator.next().skip();
		}
	}

}
