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
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import enums.ConflictAction;
import enums.CopyMode;
import exceptions.CopyException;
import models.SuperModel;
import utils.NameFactory;
import utils.TimerFormater;

public class CopiableFile extends CopiableAbstract{
	private Path tempName;
	private long copyPosition = 0;
	
	/**
	 * {@link Copiable} file representation.
	 * @param origin Origin file
	 * @param rootOrigin Origin root path
	 * @param rootDest Destination root
	 * @param SM {@link SuperModel} that contains some info
	 * @param parent Copiable parent, supposed to be a folder.
	 * @param mode copy mode
	 */
	public CopiableFile(Path origin, Path rootOrigin, Path rootDest, SuperModel SM, Copiable parent, CopyMode mode) {
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
	    	 JOptionPane.showMessageDialog(SM.frame, String.format(
					 "<HTML>Looks like there was an error in the copy<br> "
					 + "<i>%s</i> <br> and <br> <i>%s</i> <br> lengths differ.</HTML>",
					 tempName, this.getDest()
					 ));
	     }
	}
	
	public void handleCopy() {
		System.out.println("handled");
		FileWriter writter = new FileWriter(this, SM);
		writter.execute();
		boolean copied = false;
		try {
			copied = writter.get();
		}
		catch (InterruptedException | ExecutionException e1) {
			if (e1.getCause().getClass().equals(CopyException.class)) {
				System.out.print("yes");
				this.setError((CopyException) e1.getCause());
			}
			e1.printStackTrace();
		}
		
		if (copied) {
			this.setCopied();
			this.renameTemp(tempName);
			checkSameLenght();
		}
	}
	
	/**
	 * Copies a file.
	 * While the copy is in progress, if there is a GUI, some things will update,
	 * like progress bars and labels.
	 */
	public void copy() {
		if (this.getCopyPosition() == 0) {
			this.resetUICopyFile();
			SM.removeCopyQueue(this);
			SM.setCurrentName(this.getOrigin().getFileName().toString());
			SM.setOrigin(this.getRootOrigin().toString());
			SM.setDestination(this.getRootDest().toString());
		}
		if (!this.wasCopied() && this.getConflictAction() != ConflictAction.SKIP) {
			handleCopy();
		} else if (this.wasCopied()){
			this.checkSameLenght();
			SM.copiableList.movePointer();
		} else {
			SM.copiableList.movePointer();
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
	
	
	@Override
	public void setError(CopyException error) {
		super.setError(error);
		SM.addToErrorList(this);
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
			this.setCopied();
		}
	}
	
	@Override
	public void register() {
		super.register();
		SM.insertCopyQueue(this);
	}

	public Path getTemp() {
		if (tempName == null) {
			tempName = NameFactory.getTemp(getDest());
		}
		return tempName;
	}

	public long getCopyPosition() {
		return this.copyPosition ;
	}
	
	public void setCopyPosition(long position) {
		this.copyPosition = position;
	}

}
