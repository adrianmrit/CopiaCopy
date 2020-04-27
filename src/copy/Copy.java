package copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import org.apache.commons.io.FilenameUtils;

import gui.ExistsDialog;
import gui.ExistsDialogBuilder;
import gui.LongProgressBarModel;


public class Copy extends Thread{
	private static boolean DEBUG = false;
	
	
	private final File origin;
	private final File dest;
	private SuperModel SM;
	
	// TODO: 
	
//	private long totalSize = 0;  // TODO: Update final size dinamically
	// GUI elements
	
	public Copy(String orig, String dest, SuperModel SM) throws IOException{
		this.origin = new File(orig);
		this.dest = new File(dest);
		this.SM = SM;
		
		Copiable f;
		
		if (this.origin.isFile()) {
			f = new LinkedFile(this.origin, origin.getParentFile().toPath(), this.dest.toPath(), SM);
		} else {
			f = new LinkedFolder(this.origin, origin.getParentFile().toPath(), this.dest.toPath(), SM);
		}
		f.register();
	}
	
	public Copy(File orig, File dest, SuperModel SM) throws IOException {
		this(orig.getAbsolutePath(), dest.getAbsolutePath(), SM);
	}
	
	public Copy(Path orig, Path dest, SuperModel SM) throws IOException {
		this(orig.toString(), dest.toString(), SM);
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
			SM.totalProgressModel.setLongMaximum(SM.copiableList.getTotalSize());
			tookGetSize = System.currentTimeMillis() - tookGetSize;
			
			long tookCopy = System.currentTimeMillis();
			this.copyAll();
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
	
	/** 
	 * Copy the files and creates the folders
	 * @throws IOException if fails
	 */
	public void copyAll() throws IOException {
		// TODO: if folder exist, rename, merge (copy content and ask if content exist), or skip (cancel copy)
		// TODO: handle copy in same path, should duplicate file with a "(copy)" at the end,
		// TODO: Avoid copy folder into itself
		// before file extension.
		if (SM.copiableList.hasNext()) {
			Copiable c = SM.copiableList.getNext();
			handleChecks(c);
			c.copy();
			this.completeFileBar();
			copyAll();
			return; // avoid calling the else statement after copy()
		} else {
			this.completeTotalBar();
		}
	}
	
	private void completeFileBar() {
		if (SM.hasGUI()) {
			SM.fileProgressModel.setRangeProperties(100, 0, 0, 100, false); // resets the totalProgressBar
		}
	}
	
	private void completeTotalBar() {
		if (SM.hasGUI()) {
			SM.totalProgressModel.setRangeProperties(100, 0, 0, 100, false); // resets the totalProgressBar
		}
	}
	
	private void handleChecks(Copiable c) {
		handleDestEqualsOrigin(c);
		handleCopyIntoItself(c); // should be handled after handleDestEqualOrigin
		handleExists(c);
	}
	
	/**
	 * Check if there is any file in the copy that exists in the origin. If it does it will ask what to do with it.
	 * Files will be renamed and removed after asking for all of them, to avoid concurrent operation errors.
	 * 
	 * @return true if file exists, false otherwise
	 */
	private void handleExists(Copiable c) {
		if (c.destExists() && !c.getOverwriteConfirmation()) {
			if (c.isFile()) {
				handleFileExistDialog(c);
			} else {
				handleFolderExistDialog(c);
			}
			
//			return true;
		}
		
//		return false;
	}
	
	private void handleDestEqualsOrigin(Copiable c) {
		if (c.getDest().equals(c.getOrigin())) {
			String newName = NameFactory.getUnique(c.getOrigin().toString());
			newName = FilenameUtils.getName(newName);
			
			c.renameCoreDest(newName);
		}
	}
	
	private void handleCopyIntoItself(Copiable c) {
		if (c.isFolder()) {
			Path orig = c.getOrigin().toPath();
			Path dest = c.getDest().toPath();
			
			if (dest.startsWith(orig)) { // checks if dest is sub-folder of orig
				c.setCopied();  // TODO: change setCopied to something like notCopy()
				c.skip(); // skips the tree
				// TODO: Show message dialog saying you can't copy folder into itself
			}
		}
	}
	
	/**
	 * Shows a dialog if the file already exists in the destination.
	 * @param origin Origin path
	 * @param dest Destination path
	 */
	private void handleFileExistDialog(Copiable c) {
		if (SM.hasGUI()) {
			ExistsDialog dialog = ExistsDialogBuilder.getFileExistsDialog(SM.frame, c.getOrigin(), c.getDest());
			dialog.show();
			String action = dialog.getAction();
			switch (action) {
				case ExistsDialogBuilder.CANCEL:
					System.exit(0);  // exit copy// TODO: handle file exists
					break;
				
				case ExistsDialogBuilder.SKIP:
					c.skip();
					SM.totalProgressModel.setLongMaximum(SM.copiableList.getTotalSize());
					break;
				
				case ExistsDialogBuilder.RENAME:
					c.renameCoreDest(dialog.getInputValue());
					break;
				
				case ExistsDialogBuilder.REPLACE:
					c.setOverwrite();
					break;
			}
		}
		else {
			c.setOverwrite();
		}
	}
	
	/**
	 * Shows a dialog if the folder already exists in the destination.
	 * @param origin Origin path
	 * @param dest Destination path
	 */
	private void handleFolderExistDialog(Copiable c) {
		if (SM.hasGUI()) {
			ExistsDialog dialog = ExistsDialogBuilder.getFolderExistsDialog(SM.frame, c.getOrigin(), c.getDest());
			dialog.show();
			String action = dialog.getAction();
			switch (action) {
				case ExistsDialogBuilder.CANCEL:
					System.exit(0);  // exit copy// TODO: handle file exists
					break;
				
				case ExistsDialogBuilder.SKIP:
					c.skip();
					SM.totalProgressModel.setLongMaximum(SM.copiableList.getTotalSize());
					break;
				
				case ExistsDialogBuilder.RENAME:
					c.renameCoreDest(dialog.getInputValue());
					break;
				
				case ExistsDialogBuilder.MERGE:
					c.setOverwrite();
					break;
			}
		} else {
			c.setOverwrite();
		}
	}
}
