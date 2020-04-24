package copy;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.commons.lang3.ArrayUtils;

public class LinkedFolder extends CopiableAbstract{
	private TreeSet<Copiable> childrens= new TreeSet<>();
	
	public LinkedFolder(File origin, Path rootOrigin, Path rootDest, SuperModel SM) {
		super(origin, rootOrigin, rootDest, SM);
		
		FileFilter isFolderFilter = new IsFolderFilter();
		FileFilter isFileFilter = new IsFileFilter();
		
		File[] folders = ArrayUtils.nullToEmpty(origin.listFiles(isFolderFilter), File[].class);
		File[] files = ArrayUtils.nullToEmpty(origin.listFiles(isFileFilter), File[].class);
		
		
		for(int i=0; i<files.length; i++) {
			File f = files[i];
			addChildren(f);
		}
		
		for(int i=0; i<folders.length; i++) {
			File f = folders[i];
			addChildren(f);
		}
	}
	
	public LinkedFolder(Path origin, Path rootOrigin, Path rootDest, SuperModel SM) {
		this(origin.toFile(), rootOrigin, rootDest, SM);
	}
	
	@Override
	public void copy() throws FileNotFoundException, IOException {
		getAbsoluteDest().mkdirs();
	}
	
	private void addChildren(File ch) {
		if (ch.isFile()) {
			addFile(ch);
		} else {
			addFolder(ch);
		}
	}
	
	private void addFolder(File ch) {
		Copiable children = new LinkedFolder(ch, getRootOrigin(), getRootDest(), SM);
		this.childrens.add(children);
	}
	
	private void addFile(File ch) {
		Copiable children = new LinkedFile(ch, getRootOrigin(), getRootDest(), SM);
		this.childrens.add(children);
	}
	
	public void registerTree() {
		if (childrens.size() > 0) {
			Iterator<Copiable> iterator = childrens.iterator();
			while (iterator.hasNext()) {
				Copiable copiable = iterator.next();
				copiable.register();
			}
		}
	}
	
	public void register() {
		SM.copiableList.register((Copiable) this);
		registerTree();
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
	 * Replaces this LinkedFile core destination and it's children.
	 * Must not be accessed outside of LinkedFile
	 * 
	 * <b>oldPath</b> and newPath <b>newPath</b> are passed recursively down the file tree,
	 * starting at this LinkedFile. Thus only the root path is changes, while keeping the 
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
	
	public void skip() {
		super.skip();
		this.skipTree();
	}
	
	private void skipTree()	{
		
		Iterator<Copiable> childsIterator = childrens.iterator();
		
		while (childsIterator.hasNext()) {
			childsIterator.next().skip();
		}
	}

	public String toString() {
		return this.getOrigin().toString();
	}

}
