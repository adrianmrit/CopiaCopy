package copy;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class LinkedFile implements Comparable<LinkedFile>{
	private boolean copied = false;
	private boolean overwrite = false;
	private ArrayList<LinkedFile> childs;  // contains a reference to the childs, for easy child mutations
	private LinkedFileList linkedFileList;
	private int status;
	
	private Path rootOrigin;
	private Path rootDest;
	
	private File origin;
	/**
	 * Core destination (path after rootDest)
	 */
	private Path coreDestPath;
	
	
	public LinkedFile(File origin, Path rootOrigin, Path rootDest, LinkedFileList linkedFileList) {
		this.origin = origin;
		this.rootOrigin = rootOrigin;
		this.rootDest = rootDest;
		this.setDefaultCoreDestPath();
		this.linkedFileList = linkedFileList;
		this.childs = new ArrayList<>();
	}
	
	public LinkedFile(Path origin, Path rootOrigin, Path rootDest, LinkedFileList linkedFileList) {
		this(origin.toFile(), rootOrigin, rootDest, linkedFileList);
	}
	
	public void registerTree() {
		File [] files = this.origin.listFiles();
		ArrayList<File> filesWaitingList = new ArrayList<>();
		ArrayList<File> foldersWaitingList = new ArrayList<>();
		
		if (files != null && files.length > 0) {
			for(int i=0; i<files.length; i++) {
				if (files[i].isFile()) {
					filesWaitingList.add(files[i]);
				} else {
					foldersWaitingList.add(files[i]);
				}
			}
			
			// populate folders after files
			this.bulkRegisterTree(filesWaitingList);
			this.bulkRegisterTree(foldersWaitingList);
		}
	}
	
	public void register() {
		this.linkedFileList.register(this);
	}
	
	public void bulkRegisterTree(ArrayList<File> files) {
		for(int i = 0; i < files.size(); i++) {
			LinkedFile lf = new LinkedFile(files.get(i), this.rootOrigin, this.rootDest, this.linkedFileList);
			lf.register();
			this.childs.add(lf); // register the childs
			lf.registerTree();
		}
	}
	
	public void setCopied() {
		this.copied = true;
		this.linkedFileList.movePointer();
	}
	
	public boolean getOverwriteConfirmation() {
		return this.overwrite;
	}
	
	public void setOverwrite() {
		this.overwrite = true;  // !WARNING, the tree set should not be set to overwrite automatically, ask confirmation for each one
	}
	
	public boolean wasCopied() {
		return this.copied;
	}
	
	public boolean isFile() {
		return this.origin.isFile();
	}
	
	public File getOrigin() {
		return this.origin;
	}
	
	public boolean isDirectory() {
		return this.origin.isDirectory();
	}
	
	public boolean destExists() {
		return this.getAbsoluteDest().exists();
	}
	
	public long getSize() {
		if (this.isFile()) {
			return this.origin.length();
		} else {
			return 0;
		}
	}
	
	/**
	 * Path that will be copied to destRoot by default, it could be changed. Example:
	 * if originRoot is <b>"/root/path/folderToCopy/"</b>,
	 * origin is <b>"/root/path/folderToCopy/OriginFolder/Subpath to copy"</b>,
	 * <code>getOriginCorePath()</code> will return <b>"OriginFolder/Subpath"</b>
	 * @return core Path
	 */
	private Path getOriginCorePath() {
		String originRootString = this.rootOrigin.toString();
		String coreStruct = this.origin.toString().replaceFirst(originRootString, "");  // removes the root

		return Paths.get(coreStruct);
	}
	
	private void setDefaultCoreDestPath() {
		setCoreDestPath(getOriginCorePath());
	}
	
	private void setCoreDestPath(Path newCoreDestPath) {
		this.coreDestPath = newCoreDestPath;
	}
	
	public Path getDestCorePath() {
		return this.coreDestPath;
	}
	
	/**
	 * Resolves the destination for the file
	 */
	public File getAbsoluteDest() {
		String rootDestString = this.rootDest.toString();
		Path resolved = Paths.get(rootDestString, this.getDestCorePath().toString());  // join both paths
		return resolved.toFile();
	}
	
	/** 
	 * Updates this LinkedFile core destination, and updates the core destination paths in it's children if there is any.
	 * @param newName new name for the folder or file
	 */
	public void renameDest(String newName){
		Path parent = this.coreDestPath.getParent();
		Path newPath = Paths.get(parent.toString(), newName);
		
		renameTreeCoreDest(this.coreDestPath.toString(), newPath.toString());
	}
	
	/** 
	 * Replaces this LinkedFile core destination with a new path.
	 * Must not be accessed outside of LinkedFile.
	 * 
	 * @param oldPath the old path to be replaced
	 * @param newPath the path to replace the old path with
	 */
	private void renameThisCoreDest(String oldPath, String newPath) { // TODO: recursively rename children folders too
		String newPathString = this.coreDestPath.toString().replaceFirst(oldPath, newPath);
		this.setCoreDestPath(Paths.get(newPathString));
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
	private void renameTreeCoreDest(String oldPath, String newPath)	{
		renameThisCoreDest(oldPath, newPath);  // rename this coreDest first
		
		Iterator<LinkedFile> childsIterator = this.childs.iterator();
		
		while (childsIterator.hasNext()) {
			LinkedFile lf = childsIterator.next();
			lf.renameTreeCoreDest(oldPath, newPath);  // pass the newPaths along the branches
		}
	}
	
	public void skip() {
		this.linkedFileList.remove(this);  // remove from list
		this.skipTree();
	}
	
	private void skipTree()	{
		
		Iterator<LinkedFile> childsIterator = this.childs.iterator();
		
		while (childsIterator.hasNext()) {
			LinkedFile lf = childsIterator.next();
			lf.skip();
		}
	}
	
	public int compareTo(LinkedFile other) {  // TODO: append new copies at end of the list, but before already copied files
		if ((this.wasCopied() && other.wasCopied()) || (!this.wasCopied() && !other.wasCopied())) {
			return this.getOrigin().compareTo(other.getOrigin());
		} else if (this.wasCopied() && !other.wasCopied()) {
			return 1; // copied files must go in the end of the list
		} else {
			return -1;  // non copied files must go first
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkedFile other = (LinkedFile) obj;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		return true;
	}

	public String toString() {
		return this.getOrigin().toString();
	}

}
