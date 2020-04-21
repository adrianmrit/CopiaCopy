package copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JLabel;

import gui.ExistsDialog;
import gui.ExistsDialogBuilder;
import gui.LongProgressBarModel;


public class Copy extends Thread{
	private static boolean DEBUG = false;
	private static final int DEFAULT_BUFFER_SIZE = 8192; // 8kb
	
	
	private final File origin;
	private final File dest;
	private LinkedFileList linkedFileList;
	
	// TODO: 
	
//	private long totalSize = 0;  // TODO: Update final size dinamically
	// GUI elements
	
	private LongProgressBarModel fileProgressModel;
	private LongProgressBarModel totalProgressModel;
	private JLabel currentLabel;
	private JFrame frame; // used to create windows
	private boolean hasGUI = false;
	
	public Copy(String orig, String dest, LinkedFileList linkedFileList) throws IOException{
		this.origin = new File(orig);
		this.dest = new File(dest);
		this.linkedFileList = linkedFileList;
		
		linkedFileList.load(this.origin, this.dest.toPath());
	}
	
	public Copy(File orig, File dest, LinkedFileList linkedFileList) throws IOException {
		this(orig.getAbsolutePath(), dest.getAbsolutePath(), linkedFileList);
	}
	
	public Copy(Path orig, Path dest, LinkedFileList linkedFileList) throws IOException {
		this(orig.toString(), dest.toString(), linkedFileList);
	}
	
	/** 
	 * Sets the frame. Useful for creating dialogs.
	 * @param frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	/** 
	 * Sets the file progress model
	 * @param fileProgressModel
	 */
	public void setFileProgressModel(LongProgressBarModel fileProgressModel) {
		this.fileProgressModel = fileProgressModel;
	}
	
	/**
	 * Sets the totalProgressModel
	 * @param totalProgressModel
	 */
	public void setTotalProgressModel(LongProgressBarModel totalProgressModel) {
		this.totalProgressModel = totalProgressModel;
	}
	
	/** 
	 * Sets the currentLabel
	 * @param currentLabel
	 */
	public void setCurrentLabel(JLabel currentLabel) {
		this.currentLabel = currentLabel;
	}
	
	/** 
	 * Set hasGUI. If there is GUI it will be updated
	 * @param b
	 */
	public void setHasGUI(boolean b) {
		this.hasGUI = b;
	}
	
	/** Sets the debug status
	 * @param debug status
	 */
	public static void setDebug(boolean debug) {
		DEBUG = debug;
	}
	
	/** 
	 * Used to run the copy in a thread
	 */
	public void run() {
		int exitStatus = 0;
		try {
			long tookAll = System.currentTimeMillis();
			
			long tookGetSize = System.currentTimeMillis();
//			this.totalSize = Copy.getFolderArraySize(this.origDestMap.keySet());
			this.totalProgressModel.setLongMaximum(linkedFileList.getTotalSize());
			tookGetSize = System.currentTimeMillis() - tookGetSize;
			
			long tookCopy = System.currentTimeMillis();
			this.copy();
			tookCopy = System.currentTimeMillis() - tookCopy;
			
			tookAll = System.currentTimeMillis() - tookAll;
			
			if(DEBUG) {
				System.out.println("Took (all): " + tookAll + "ms");
				System.out.println("Took (copy): " + tookCopy + "ms");
				System.out.println("Took (get size): " + tookGetSize + "ms");
			}
		} catch (IOException e) {
			e.printStackTrace();
			exitStatus = 1;
		}
		
		if(!DEBUG) {
			System.exit(exitStatus);
		}
	}
	
	private void doTheCopy(LinkedFile lf) throws IOException {
		if (lf.destExists() && !lf.getOverwriteConfirmation()) {
			if (lf.isFile()) {
				handleFileExistDialog(lf);
			} else {
				handleFolderExistDialog(lf);
			}
		} else {
			if (lf.isFile()) {
				this.copyFile(lf);
			} else {
				lf.getAbsoluteDest().mkdirs();
			}
			
			this.completeFileBar();
			lf.setCopied();
		}
	}
	
	/** 
	 * Copy the files and creates the folders
	 * @throws IOException if fails
	 */
	public void copy() throws IOException {
		// TODO: if folder exist, rename, merge (copy content and ask if content exist), or skip (cancel copy)
		// TODO: handle copy in same path, should duplicate file with a "(copy)" at the end,
		// TODO: Avoid copy folder into itself
		// before file extension.
		while (linkedFileList.hasNext()) {
			LinkedFile lf = linkedFileList.getNext();
			doTheCopy(lf);
		}
		this.completeFileBar();
		this.completeTotalBar();
	}
	
	/** 
	 * Copy a file from origin to destination, and updates the UIElements.
	 * @param orig origin path
	 * @param dest destination path
	 * @throws IOException if fails
	 */
	public void copyFile(LinkedFile lf) throws IOException {
		InputStream is = new FileInputStream(lf.getOrigin());
		OutputStream os = new FileOutputStream(lf.getAbsoluteDest());
		resetUICopyFile(lf);
		
		try {
			copyFileHelper(is, os); // does the copy
		} finally {
			is.close();
			os.close();
		}
	}
	
	/** 
	 * Does the actual copy
	 * @param is InputStream
	 * @param os OutputStream
	 */
	private void copyFileHelper(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[DEFAULT_BUFFER_SIZE]; // TODO: Find optimal chunk size
		int bytesRead;
		
		while ((bytesRead = is.read(buf)) > 0) {
			os.write(buf, 0, bytesRead);
			updateProgressBars(bytesRead);
		}
	}
	
	/** 
	 * Resets the fileProgressBar, and the currentLabel
	 * @param originF
	 * @param destF // not used for now
	 */
	private void resetUICopyFile(LinkedFile lf) {
		if (this.hasGUI) {
			this.fileProgressModel.setLongMaximum(lf.getSize()); // resets the fileProgressBar
			this.fileProgressModel.setLongValue(0); // resets the fileProgressBar
			this.currentLabel.setText("Current: " + lf.getAbsoluteDest());
		}
	}
	
	private void completeFileBar() {
		if (this.hasGUI) {
			this.fileProgressModel.setRangeProperties(100, 0, 0, 100, false); // resets the totalProgressBar
		}
	}
	
	private void completeTotalBar() {
		if (this.hasGUI) {
			this.totalProgressModel.setRangeProperties(100, 0, 0, 100, false); // resets the totalProgressBar
		}
	}
	
	/** 
	 * Updates the progress bars if they exist
	 * @param bytesRead last number of bytes read
	 */
	private void updateProgressBars(long bytesRead) {
		if (this.hasGUI) {
			this.fileProgressModel.addLongValue(bytesRead);
			this.totalProgressModel.addLongValue(bytesRead);
		}
	}
	
	/**
	 * Check if there is any file in the copy that exists in the origin. If it does it will ask what to do with it.
	 * Files will be renamed and removed after asking for all of them, to avoid concurrent operation errors.
	 */
	
	/**
	 * Shows a dialog if the file already exists in the destination.
	 * @param origin Origin path
	 * @param dest Destination path
	 */
	private void handleFileExistDialog(LinkedFile lf) {
		if (this.hasGUI) {
			ExistsDialog dialog = ExistsDialogBuilder.getFileExistsDialog(this.frame, lf.getOrigin(), lf.getAbsoluteDest());
			dialog.show();
			String action = dialog.getAction();
			switch (action) {
				case ExistsDialogBuilder.CANCEL:
					System.exit(0);  // exit copy// TODO: handle file exists
					break;
				
				case ExistsDialogBuilder.SKIP:
					lf.skip();
					this.totalProgressModel.setLongMaximum(linkedFileList.getTotalSize());
					break;
				
				case ExistsDialogBuilder.RENAME:
					lf.renameDest(dialog.getInputValue());
					break;
				
				case ExistsDialogBuilder.REPLACE:
					lf.setOverwrite();
					break;
			}
		}
		else {
			lf.setOverwrite();
		}
	}
	
	/**
	 * Shows a dialog if the folder already exists in the destination.
	 * @param origin Origin path
	 * @param dest Destination path
	 */
	private void handleFolderExistDialog(LinkedFile lf) {
		if (this.hasGUI) {
			ExistsDialog dialog = ExistsDialogBuilder.getFolderExistsDialog(this.frame, lf.getOrigin(), lf.getAbsoluteDest());
			dialog.show();
			String action = dialog.getAction();
			switch (action) {
				case ExistsDialogBuilder.CANCEL:
					System.exit(0);  // exit copy// TODO: handle file exists
					break;
				
				case ExistsDialogBuilder.SKIP:
					lf.skip();
					this.totalProgressModel.setLongMaximum(linkedFileList.getTotalSize());
					break;
				
				case ExistsDialogBuilder.RENAME:
					lf.renameDest(dialog.getInputValue());
					break;
				
				case ExistsDialogBuilder.MERGE:
					lf.setOverwrite();
					break;
			}
		} else {
			lf.setOverwrite();
		}
	}
}
