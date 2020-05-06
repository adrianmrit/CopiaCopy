package copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;

import utils.TimerFormater;

public class CopiableFile extends CopiableAbstract{
	/**
	 * {@link Copiable} file representation.
	 * @param origin Origin file
	 * @param rootOrigin Origin root path
	 * @param rootDest Destination root
	 * @param SM {@link SuperModel} that contains some info
	 */
	public CopiableFile(Path origin, Path rootOrigin, Path rootDest, SuperModel SM, Copiable parent, int mode) {
		super(origin, rootOrigin, rootDest, SM, parent, mode);
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
		}
	}
	
	private void handleCopy() throws FileNotFoundException, IOException{
		Path tempName = NameFactory.getTemp(this.getDest()); // use a temp name
		
		InputStream is = new FileInputStream(this.getOrigin().toFile());
		OutputStream os = new FileOutputStream(tempName.toFile());
		if (SM.hasGUI()) {
			resetUICopyFile();
		}
		
		try {
			byte[] buf = SM.buffer.getBuffer(); // TODO: Find optimal chunk size
			int bytesRead;
			long totalCopied = 0;
			double speed;
			
			SM.setCurrentTotalSize(this.getSize());
			SM.setFrom(this.getOrigin().toString());
			
			CopyTimer timer = new CopyTimer();
			
			while ((bytesRead = is.read(buf)) > 0) {
				if (this.wasSkipped()) {
					break;
				}
				while (SM.isPaused()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				SM.buffer.resetTime();
				os.write(buf, 0, bytesRead);
				if (SM.hasGUI()) {
					totalCopied += bytesRead;
					SM.addTotalCopiedSize(bytesRead);
					
					SM.setCurrentCopiedSize(bytesRead);
					
					
					speed = SM.buffer.getSpeedSeconds(bytesRead);
					SM.setSpeed(speed);
					timer.calcAvgSpeed(speed);
					if (!timer.isCalculating()) {
						SM.setCalculatingTimeLeft(false); //TODO: fix big buffer sizes showing no time left
						SM.setCurrentTimeLeft(timer.getTime(this.getSize()-totalCopied));
						SM.setTotalTimeLeft(timer.getTime(SM.getTotalSizeLeft()));
					}
				}
			}
		} finally {
			is.close();
			os.close();
			SM.addCopiedFile();
		}
		if (!this.wasSkipped()) {
			Files.deleteIfExists(this.getDest());
			Files.move(tempName, this.getDest(), StandardCopyOption.ATOMIC_MOVE,
					StandardCopyOption.REPLACE_EXISTING);
		} else {
			tempName.toFile().delete();
		}
	}
	
	/**
	 * Copies a file.
	 * While the copy is in progress, if there is a GUI, some things will update,
	 * like progress bars and labels.
	 */
	public void copy() throws IOException {
		if (!this.wasCopied() && !this.wasSkipped()) {
			handleCopy();
			this.setCopied();
		}
	}
	
	/**
	 * Gets the size of this file.
	 */
	public long getSize() {
		try {
			return Files.size(this.getOrigin());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return 0;
		}
	}
	
	
	/**
	 * Same as getSize in this case.
	 */
	public long getSizeRec() {
		return getSize();
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