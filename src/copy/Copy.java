package copy;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.io.FilenameUtils;

import checkers.Checker;
import checkers.Handlers;
import gui.ExistsDialog;
import gui.ExistsDialogBuilder;
import gui.LongProgressBarModel;


public class Copy extends SwingWorker{
	private static boolean DEBUG = false;

	private SuperModel SM;
	private Handlers checkers;
	
	/**
	 * Intermediate between copiables and the GUI
	 * @param SM Model that holds some global data
	 */
	public Copy(SuperModel SM){
		this.SM = SM;
		this.checkers = new Handlers(SM);
	}
	
	/**
	 * Creates a copiable and add it to the list.
	 * @param orig origin path
	 * @param dest parent destination path
	 * @param mode {@link Copiable#COPY_MODE} or {@link Copiable#CUT_MODE}
	 */
	public void addToCopy(String orig, String dest, int mode){
		File origF = new File(orig);
		File destF = new File(dest);
		
		Copiable f;
		
		if (Files.isSymbolicLink(origF.toPath())) {
			f = new LinkedSymbolicLink(origF, origF.getParentFile().toPath(), destF.toPath(), SM, null, mode);
		} else if (origF.isFile()) {
			f = new LinkedFile(origF, origF.getParentFile().toPath(), destF.toPath(), SM, null, mode);
		} else {
			f = new LinkedFolder(origF, origF.getParentFile().toPath(), destF.toPath(), SM, null, mode);
		}
		
		f.register();
	}
	
	/**
	 * Creates a copiable and add it to the list.
	 * @param orig origin path
	 * @param dest parent destination path
	 * @param mode {@link Copiable#COPY_MODE} or {@link Copiable#CUT_MODE}
	 */
	public void addToCopy(Path orig, Path dest, int mode) throws IOException {
		this.addToCopy(orig.toString(), dest.toString(), mode);
	}
	
	/**
	 * Creates a copiable and add it to the list.
	 * @param orig origin file
	 * @param dest parent destination file
	 * @param mode {@link Copiable#COPY_MODE} or {@link Copiable#CUT_MODE}
	 */
	public void addToCopy(File orig, File dest, int mode) throws IOException {
		this.addToCopy(orig.toString(), dest.toString(), mode);
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
	public Object doInBackground() {
		int exitStatus = 0;
		
		try {
			long tookAll = System.currentTimeMillis();
			
			long tookGetSize = System.currentTimeMillis();
			if (SM.hasGUI()) {
				tookGetSize = System.currentTimeMillis() - tookGetSize;
			}

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
		return 0;
	}
	
	/** 
	 * Copy the files and creates the folders
	 * @throws IOException if fails
	 */
	public void copyAll() throws IOException {
		
		this.initTotalBar();
		
		while (SM.copiableList.hasNext()) {
			Copiable c = SM.copiableList.getNext();
			
			if (checkers.handle(c)) {
			
				c.paste();
			} else {
				continue;
			}
		}
		this.completeTotalBar();
	}
	
	private void initTotalBar() {
		SM.totalProgressModel.setLongMaximum(SM.copiableList.getTotalSize());
	}
	
	/**
	 * Completes the file progress bar.
	 */
	private void completeFileBar() {
		if (SM.hasGUI()) {
			SM.fileProgressModel.setRangeProperties(100, 0, 0, 100, false); // resets the totalProgressBar
		}
	}
	
	/**
	 * Completes the total progress bar.
	 */
	private void completeTotalBar() {
		if (SM.hasGUI()) {
			SM.totalProgressModel.setRangeProperties(100, 0, 0, 100, false); // resets the totalProgressBar
		}
	}
}
