package copy;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import enums.ConflictAction;

/**
 * An abstract class that implements common functions for all copiables.
 * @author adrianmrit
 *
 */
public abstract class CopiableAbstract implements Copiable{
	private Path origin;
	private Path rootOrigin;
	private Path rootDest;
	private Path coreDestPath;
	private Path absoluteDest;
	public SuperModel SM;
	private boolean copied = false;
	private boolean skipped = false;
	private boolean overwrite = false;
	private int mode;
	private Copiable parent; // folder that will try to delete after moving
	private ConflictAction onConflict = ConflictAction.DEFAULT;
	
	/**
	 * An abstract copiable
	 * @param origin
	 * @param rootOrigin
	 * @param rootDest
	 * @param SM
	 * @param mode Copiable mode
	 */
	public CopiableAbstract(Path origin, Path rootOrigin,
			Path rootDest, SuperModel SM, Copiable parent, int mode) {
		this.origin = origin;
		this.rootOrigin = rootOrigin;
		this.rootDest = rootDest;
		this.SM = SM;
		this.mode = mode;
		this.parent = parent;
		
		setDefaultCoreDestPath();
		
		if (SM != null && SM.copiableList != null) {
			this.register();
		}
	}
	
	public void register() {
		SM.copiableList.register(this);
		SM.updateLoadingLabel();
	}
	
	public int getMode() {
		return this.mode;
	}
	
	public Copiable getParent() {
		return this.parent;
	}
	
	/**
	 * Either moves or copies this {@link Copiable} according to the mode.
	 */
	public void paste() throws IOException {
		switch (getMode()) {
		case Copiable.COPY_MODE:
			this.copy();
			break;
		case Copiable.CUT_MODE:
			this.move();
			break;
		}
	}
	
	/**
	 *	Deletes this copiable 
	 */
	public void deleteThisAndParent() {
		try {
			Files.deleteIfExists(this.getOrigin());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (this.getParent() != null) {
			this.getParent().deleteThisAndParent();
		}
	}

	/**
	 * Moves a {@link Copiable} from its origin to its destination.
	 * @throws IOException
	 */
	public void move() throws IOException {
		// if not able to move then copy
		try {
			Files.move(this.getOrigin(), this.getDest(),
					StandardCopyOption.ATOMIC_MOVE,
					StandardCopyOption.REPLACE_EXISTING);
					this.setCopied();
					this.setTreeCopied();
		} catch (AtomicMoveNotSupportedException e) {
			copy();
			deleteThisAndParent();
		}
	}
	
	/**
	 * Copies this {@link Copiable} origin into its destination.
	 * @throws IOException
	 */
	public abstract void copy() throws IOException;
	
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
		return Files.exists(this.getDest(), LinkOption.NOFOLLOW_LINKS);
	}
	
	public Path getOrigin() {
		return this.origin;
	}
	
	public boolean isSymbolicLink() {
		return Files.isSymbolicLink(this.getOrigin());
	}
	
	public boolean isFile() {
		return !isSymbolicLink() && Files.isRegularFile(this.getOrigin(), LinkOption.NOFOLLOW_LINKS);
	}
	
	public boolean isFolder() {
		return !isSymbolicLink() && Files.isDirectory(this.getOrigin(), LinkOption.NOFOLLOW_LINKS);
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
	
	public Path getDest() {
		return this.absoluteDest;
	}
	
	public void setCopied() {
		this.copied = true;
		this.SM.copiableList.movePointer();
	}
	
	public void setTreeCopied() {

	}
	
	public void setConflictAction(ConflictAction action) {
		this.onConflict = action;
	}
	
	public void setConflictActionForTree(ConflictAction action) {

	}
	
	public ConflictAction getConflictAction() {
		return this.onConflict;
	}
	
	public boolean wasCopied() {
		return this.copied;
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

	/**
	 * Use default equals. Even if two {@link Copiable} instances have the same values,
	 * they should be treated as different
	 */
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public String toString() {
		return this.getOrigin().toString();
	}
}
