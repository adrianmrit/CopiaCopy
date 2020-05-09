package copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import enums.ConflictAction;
import models.SuperModel;
import utils.NameFactory;
import utils.TimerFormater;

public class CopiableFile extends CopiableAbstract{
	private Path tempName;
	
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
	
	private FileChannel loadInputChannel(long position){
		while (true) {
		 try{
//			 FileInputStream ins = new FileInputStream(this.getOrigin().toFile());
			 FileChannel input =  FileChannel.open(this.getOrigin(), StandardOpenOption.READ);
             try {
				input.position(position);
			} catch (IOException e) {
				e.printStackTrace();
			}
			 return input;
		 } catch (SecurityException e) {
			 JOptionPane optionPane = new JOptionPane(
					 String.format("<HTML>Looks like you don't have permision to read <i>%s</i>. Try again?</HTML>",
							 this.getOrigin()
							 ),
						JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
						
				JDialog dialog = optionPane.createDialog(SM.frame, "Copy error");
						dialog.setVisible(true);
						int value = (int) optionPane.getValue();
						if (value == JOptionPane.NO_OPTION) {
							this.setConflictAction(ConflictAction.SKIP);
							break;
						}
		 } catch (IOException e) {
			 JOptionPane optionPane = new JOptionPane(
					 String.format("<HTML>The file <i>%s</i> could not be found. Try again?</HTML>", this.getOrigin()
							 ),
						JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
						
				JDialog dialog = optionPane.createDialog(SM.frame, "Copy error");
						dialog.setVisible(true);
						int value = (int) optionPane.getValue();
						if (value == JOptionPane.NO_OPTION) {
							this.setConflictAction(ConflictAction.SKIP);
							break;
						}
		 }
		}
		return null;
	}
	
	private FileChannel loadOutputChannel(){
		while (true) {
		 try{
//			 FileOutputStream outs = new FileOutputStream(tempName.toFile(), true);
			 FileChannel output = FileChannel.open(tempName, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
			 return output;
		 } catch (SecurityException e) {
			 JOptionPane optionPane = new JOptionPane(
					 String.format(
							 "<HTML>Looks like you don't have permision to read <i>%s</i>. Try again?</HTML>",
							 this.getOrigin()
							 ),
						JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
						
				JDialog dialog = optionPane.createDialog(SM.frame, "Copy error");
						dialog.setVisible(true);
						int value = (int) optionPane.getValue();
						if (value == JOptionPane.NO_OPTION) {
							this.setConflictAction(ConflictAction.SKIP);
							break;
						}
		 } catch (IOException e) {
			 e.printStackTrace();
			 JOptionPane optionPane = new JOptionPane(
					 String.format(
							 "<HTML>The folder <i>%s</i> could not be accessed. Try again?</HTML>",
							 this.getOrigin()
							 ),
						JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
						
				JDialog dialog = optionPane.createDialog(SM.frame, "Copy error");
						dialog.setVisible(true);
						int value = (int) optionPane.getValue();
						if (value == JOptionPane.NO_OPTION) {
							this.setConflictAction(ConflictAction.SKIP);
							break;
						}
		 }
		}
		return null;
	}
	
	private long transfer(FileChannel input, FileChannel output, long pos, long count) {
		while (true){
			try {
				return output.transferFrom(input, pos, count);
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane optionPane = new JOptionPane(
						 String.format(
								 "<HTML>There was an error while trying to write to "
								 + "<i>%s</i>. Try again?</HTML>",
								 this.getDest()
								 ),
							JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
							
				JDialog dialog = optionPane.createDialog(SM.frame, "Copy error");
							dialog.setVisible(true);
							int value = (int) optionPane.getValue();
							if (value == JOptionPane.NO_OPTION || value == JOptionPane.DEFAULT_OPTION) {
								this.setConflictAction(ConflictAction.SKIP);
								break;
							}
							try {
								input.close();
								output.close();
//								input=null;
//								output=null;
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							Path newTemporal = NameFactory.getTemp(tempName);
							try {
								Files.move(tempName, newTemporal, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
								tempName = newTemporal;
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							input = loadInputChannel(pos); // attempts to reload the chanel; TODO: Doesn't work
							output = loadOutputChannel(); // attempts to reload the chanel; TODO: Doesn't work
			}
		}
		return 0;
	}
	
	private void renameTemp(Path tempName) {
		while(true) {
			try {
//				Files.deleteIfExists(this.getDest());
				Files.move(tempName, this.getDest(), StandardCopyOption.ATOMIC_MOVE,
						StandardCopyOption.REPLACE_EXISTING);
				return;
			} catch (IOException e) {
				JOptionPane optionPane = new JOptionPane(
						 String.format(
								 "<HTML>There was an error while trying to rename the temporal file "
								 + "<i>%s</i> to <i>%s</i>. Try again?</HTML>",
								 tempName, this.getDest()
								 ),
							JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
							
				JDialog dialog = optionPane.createDialog(SM.frame, "Copy error");
							dialog.setVisible(true);
							int value = (int) optionPane.getValue();
							if (value == JOptionPane.NO_OPTION) {
								this.setConflictAction(ConflictAction.SKIP);
								break;
							}
			}
		}
	}
	
	public void checkSameLenght() {
		 final long srcLen = this.getOrigin().toFile().length();
	     final long dstLen = this.getDest().toFile().length();
	     if (srcLen != dstLen) {
	    	 JOptionPane optionPane = new JOptionPane(
					 String.format(
							 "<HTML>Looks like there was an error in the copy<br> "
							 + "<i>%s</i> <br> and <br> <i>%s</i> <br> lengths differ.</HTML>",
							 tempName, this.getDest()
							 ),
						JOptionPane.WARNING_MESSAGE);
						
			JDialog dialog = optionPane.createDialog(SM.frame, "Copy error");
						dialog.setVisible(true);
	     }
	}
	
	/**
	 * Handles the actual copy
	 */
	private void handleCopy()
            throws IOException {
		tempName = NameFactory.getTemp(this.getDest()); // use a temp name
		
		if (SM.hasGUI()) {
			resetUICopyFile();
		}
		
		SM.setCurrentTotalSize(this.getSize());
		SM.setCurrentName(this.getOrigin().getFileName().toString());
		
		CopyTimer timer = new CopyTimer();
		FileChannel input = loadInputChannel(0);
		FileChannel output = loadOutputChannel();
        try  {

            final long size = input.size();
            long pos = 0;
            long count = 0;
            double speed;
            while (pos < size) {
            	if (this.getConflictAction() == ConflictAction.SKIP) {
					break;
				}
				while (SM.isPaused()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				SM.buffer.resetTime();
            	
                final long remain = size - pos;
                count = remain > SM.buffer.getBufferSize() ? SM.buffer.getBufferSize() : remain;
                final long bytesCopied = transfer(input, output, pos, count);
                
                if (SM.hasGUI()) {;
					SM.addTotalCopiedSize(bytesCopied);
					
					SM.setCurrentCopiedSize(bytesCopied);
					
					
					speed = SM.buffer.getSpeedSeconds(bytesCopied);
					SM.setSpeed(speed);
					timer.calcAvgSpeed(speed);
					if (!timer.isCalculating()) {
						SM.setCalculatingTimeLeft(false); //TODO: fix big buffer sizes showing no time left
						SM.setCurrentTimeLeft(timer.getTime(this.getSize()-remain));
						SM.setTotalTimeLeft(timer.getTime(SM.getTotalSizeLeft()));
					}
				}
                
                pos += bytesCopied;
            }
        } finally {
        	if (input != null) input.close();
			if (output != null)output.close();
        }
        
        if (this.getConflictAction() != ConflictAction.SKIP) {
			renameTemp(tempName);
			SM.addCopiedFile();

	        checkSameLenght();
	        this.getDest().toFile().setLastModified(this.getOrigin().toFile().lastModified());
		} else {
//			tempName.toFile().delete();
		} 
    }
	
	/**
	 * Copies a file.
	 * While the copy is in progress, if there is a GUI, some things will update,
	 * like progress bars and labels.
	 */
	public void copy() throws IOException {
		SM.removeCopyQueue(this);
		if (!this.wasCopied() && this.getConflictAction() != ConflictAction.SKIP) {
			handleCopy();
			this.setCopied();
		} else {
			this.SM.copiableList.movePointer();
		}
	}
	
	/**
	 * Gets the size of this file.
	 */
	public long getSize() {
		try {
			return Files.size(this.getOrigin());
		} catch (IOException e) {
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
	
	@Override
	public void setConflictAction(ConflictAction action) {
		if (action != ConflictAction.MERGE) {  // files are not supposed to be merged
			super.setConflictAction(action);
		}

		if (action == ConflictAction.SKIP) {
			SM.removeCopyQueue(this);
		}
	}
	
	@Override
	public void register() {
		super.register();
		SM.insertCopyQueue(this);
	}

}
