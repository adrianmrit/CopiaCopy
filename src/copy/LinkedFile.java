package copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LinkedFile extends CopiableAbstract{
	
	
	/**
	 * {@link Copiable} file representation.
	 * @param origin Origin file
	 * @param rootOrigin Origin root path
	 * @param rootDest Destination root
	 * @param SM {@link SuperModel} that contains some info
	 */
	public LinkedFile(File origin, Path rootOrigin, Path rootDest, SuperModel SM) {
		super(origin, rootOrigin, rootDest, SM);
	}
	
	/**
	 * {@link Copiable} file representation.
	 * @param origin Origin path
	 * @param rootOrigin Origin root path
	 * @param rootDest Destination root
	 * @param SM {@link SuperModel} that contains some info
	 */
	public LinkedFile(Path origin, Path rootOrigin, Path rootDest, SuperModel SM) {
		this(origin.toFile(), rootOrigin, rootDest, SM);
	}
	
	/** 
	 * Resets the fileProgressBar, and the currentLabel
	 * @param originF
	 * @param destF // not used for now
	 */
	private void resetUICopyFile() {
		if (SM.hasGUI()) {
			SM.fileProgressModel.setLongMaximum(this.getSize()); // resets the fileProgressBar
			SM.fileProgressModel.setLongValue(0); // resets the fileProgressBar
			SM.currentLabel.setText("Current: " + this.getDest());
		}
	}
	
	private void handleCopy() throws FileNotFoundException, IOException{
		File dest = this.getDest();
		
		InputStream is = new FileInputStream(this.getOrigin());
		OutputStream os = new FileOutputStream(dest);
		if (SM.hasGUI()) {
			resetUICopyFile();
		}
		
		try {
			byte[] buf = SM.buffer.getBuffer(); // TODO: Find optimal chunk size
			int bytesRead;
			
			while ((bytesRead = is.read(buf)) > 0) {
				SM.buffer.resetTime();
				os.write(buf, 0, bytesRead);
				if (SM.hasGUI()) {
					SM.fileProgressModel.addLongValue(bytesRead);
					SM.totalProgressModel.addLongValue(bytesRead);
				}
			}
		} finally {
			is.close();
			os.close();
		}
	}
	
	/**
	 * Copies a file.
	 * While the copy is in progress, if there is a GUI, some things will update,
	 * like progress bars and labels.
	 */
	public void copy() throws FileNotFoundException, IOException {
		if (!this.wasCopied()) {
			handleCopy();
			this.setCopied();
		}
	}
	
	/**
	 * Registers this file in the copiableList.
	 */
	public void register() {
		SM.copiableList.register(this);
	}
	
	/**
	 * Gets the size of this file.
	 */
	public long getSize() {
		return this.getOrigin().length();
	}
	
	/**
	 * Renames the destination path
	 */
	public void renameCoreDest(String newName){
		Path parent = this.getCoreDest().getParent();
		Path newPath = Paths.get(parent.toString(), newName);

		this.setCoreDestPath(newPath);
	}
	
	/**
	 * Literally does nothing. Needed for CopiableAbstract
	 */
	public void renameTreeCoreDest(String oldPath, String newPath){
		
	}

}
