package copy;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class CopiableAbstract implements Copiable{
	private File origin;
	private Path rootOrigin;
	private Path rootDest;
	private Path coreDestPath;
	private Path absoluteDest;
	public SuperModel SM;
	private boolean copied = false;
	private boolean overwrite = false;
	
	/**
	 * An abstract copiable
	 * @param origin
	 * @param rootOrigin
	 * @param rootDest
	 * @param SM
	 */
	public CopiableAbstract(File origin, Path rootOrigin, Path rootDest, SuperModel SM) {
		this.origin = origin;
		this.rootOrigin = rootOrigin;
		this.rootDest = rootDest;
		this.SM = SM;
		
		setDefaultCoreDestPath();
	}
	
	/**
	 * Path that will be copied to destRoot by default, it could be changed. Example:
	 * if originRoot is <b>"/root/path/folderToCopy/"</b>,
	 * origin is <b>"/root/path/folderToCopy/OriginFolder/Subpath to copy"</b>,
	 * <code>getOriginCorePath()</code> will return <b>"OriginFolder/Subpath"</b>
	 * @return core Path
	 */
	public Path getOriginCorePath() {
		String originRootString = this.rootOrigin.toString();
		
		String coreStruct = this.getOrigin().toString().replaceFirst(originRootString, "");  // removes the root
		return Paths.get(coreStruct);
	}
	
	/** 
	 * Replaces the core destination with a new path.
	 * 
	 * @param oldPath the old path to be replaced
	 * @param newPath the path to replace the old path with
	 */
	public void renameCoreDest(String oldPath, String newPath) { // TODO: recursively rename children folders too
		String newPathString = this.coreDestPath.toString().replaceFirst(oldPath, newPath);
		this.setCoreDestPath(Paths.get(newPathString));
	}
	
	/**
	 * Sets the core destination path (path without root destination)
	 * @param newCoreDestPath
	 */
	public void setCoreDestPath(Path newCoreDestPath) {
		this.coreDestPath = newCoreDestPath;
		this.absoluteDest = Paths.get(this.rootDest.toString(), this.coreDestPath.toString());
	}
	
	/**
	 * Sets a default destination path
	 */
	private void setDefaultCoreDestPath() {
		setCoreDestPath(getOriginCorePath());
	}
	

	public boolean destExists() {
		return this.getDest().exists();
	}
	
	public File getOrigin() {
		return this.origin;
	}
	
	public boolean isFile() {
		return this.origin.isFile();
	}
	
	public boolean isFolder() {
		return this.origin.isDirectory();
	}
	
	public Path getCoreDest() {
		return this.coreDestPath;
	}
	
	public Path getRootOrigin() {
		return this.rootOrigin;
	}
	
	public Path getRootDest() {
		return this.rootDest;
	}
	
	public File getDest() {
		return this.absoluteDest.toFile();
	}
	
	public boolean getOverwriteConfirmation() {
		return this.overwrite;
	}
	
	public void setOverwrite() {
		this.overwrite = true;  // !WARNING, the tree set should not be set to overwrite automatically, ask confirmation for each one
	}
	
	public void setCopied() {
		this.copied = true;
		this.SM.copiableList.movePointer();
	}
	
	public boolean wasCopied() {
		return this.copied;
	}
	
	public void skip() {
		SM.copiableList.movePointer();
	}
	
	public int compareTo(Copiable other) {
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
		Copiable other = (Copiable) obj;
		if (origin == null) {
			if (other.getOrigin() != null)
				return false;
		} else if (!this.getOrigin().equals(other.getOrigin()))
			return false;
		return true;
	}

	public String toString() {
		return this.getOrigin().toString();
	}
}
