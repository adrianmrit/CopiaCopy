package copy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Abstract interface that represents a file or folder,
 * with an origin and a destination where it will be copied
 */
public interface Copiable extends Comparable<Copiable>{
	public static int COPY_MODE = 1;
	public static int CUT_MODE = 2;
	/** 
	 * Copies the origin file to the destination
	 * @throws FileNotFoundException if the origin file doesn't exist
	 * @throws IOException
	 */
	void paste() throws FileNotFoundException, IOException;
	
	void deleteThisAndParent();
	
	int getMode();
	
	/**
	 * Adds this file to the {@link CopiableList}
	 */
	void register();
	
	/**
	 * Sets this file to copied and move the pointer in the {@link CopiableList}
	 * to the next {@link Copiable}
	 */
	void setCopied();

	/**
	 * Checks if it's a symbolic link
	 * @return true if it's a symbolic link, false otherwise
	 */
	boolean isSymbolicLink();
	
	/**
	 * Checks if it's a file
	 * @return true if it's a file, false otherwise
	 */
	boolean isFile();
	
	/**
	 * Checks if it's a folder
	 * @return true if it's a folder, false otherwise
	 */
	boolean isFolder();
	
	/**
	 * Checks if the destination exists
	 * @return true if the destination exits, false otherwise
	 */
	boolean destExists();
	
	/**
	 * Gets the origin {@link File}
	 * @return the File that represents the origin
	 */
	Path getOrigin();
	
	/**
	 * Gets the core origin {@link Path}
	 * @return the Path that represents the origin - origin root
	 */
	Path getOriginCorePath();
	
	/**
	 * Gets the size in bytes for this folder or file. If it's a folder it will return 0
	 * @return the size in bytes
	 */
	long getSize();
	
	/**
	 * Same as {@link #getSize()}, except that the size of the folders will
	 * be the total size of it's files instead of 0;
	 * @return the size in bytes
	 */
	long getSizeRec();
	
	/**
	 * Returns true if the file was copied.
	 * @return true if file was copied, false otherwise
	 */
	boolean wasCopied();
	
	/**
	 * Set this copiable to be overwritten if it exists
	 */
	void setOverwrite();
	
	/**
	 * Gets if this copiable is set to be overwritten
	 * @return
	 */
	boolean getOverwriteConfirmation();
	
	/**
	 * Skips this copiable
	 */
	void skip();
	
	/**
	 * Renames the destination file
	 * @param newName
	 */
	void renameCoreDest(String newName);
	
	/**
	 * Renames the core destination path
	 * @param newName
	 */
	void renameCoreDest(String oldPath, String newPath);
	
	/**
	 * Renames the core destination path of the childrens of this copiable
	 * @param newName
	 */
	void renameTreeCoreDest(String oldPath, String newPath);  // usded only if it's file
	
	/**
	 * Gets the absolute destination
	 * @return
	 */
	Path getDest();
}
