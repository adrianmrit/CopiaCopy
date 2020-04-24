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
	 * Core destination (path after rootDest)
	 */
	private Path coreDestPath;
	
	
	public LinkedFile(File origin, Path rootOrigin, Path rootDest, SuperModel SM) {
		super(origin, rootOrigin, rootDest, SM);
	}
	
	public LinkedFile(Path origin, Path rootOrigin, Path rootDest, SuperModel SM) {
		this(origin.toFile(), rootOrigin, rootDest, SM);
	}
	
	private byte[] getBuffer() {
		return new byte[SM.dinamicBuffer.getBuffer()];
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
			SM.currentLabel.setText("Current: " + this.getAbsoluteDest());
		}
	}
	
	public void copy() throws FileNotFoundException, IOException {
		File dest = this.getAbsoluteDest();
		dest.getParentFile().mkdirs();
		
		InputStream is = new FileInputStream(this.getOrigin());
		OutputStream os = new FileOutputStream(dest);
		if (SM.hasGUI()) {
			resetUICopyFile();
		}
		
		try {
			byte[] buf = getBuffer(); // TODO: Find optimal chunk size
			int bytesRead;
			
			while ((bytesRead = is.read(buf)) > 0) {
				SM.dinamicBuffer.resetTime();
				os.write(buf, 0, bytesRead);
				if (SM.hasGUI()) {
					SM.fileProgressModel.addLongValue(bytesRead);
					SM.totalProgressModel.addLongValue(bytesRead);
				}
				
				SM.dinamicBuffer.recalculate(bytesRead);
				buf = getBuffer();
			}
		} finally {
			is.close();
			os.close();
			
			SM.dinamicBuffer.reset();
		}
	}
	
	public void register() {
		SM.copiableList.register(this);
	}
	
	public long getSize() {
		return this.getOrigin().length();
	}
	
	public void renameCoreDest(String newName){
		Path parent = this.coreDestPath.getParent();
		Path newPath = Paths.get(parent.toString(), newName);

		this.setCoreDestPath(newPath);
	}
	
	public void renameTreeCoreDest(String oldPath, String newPath){
		
	}

}
