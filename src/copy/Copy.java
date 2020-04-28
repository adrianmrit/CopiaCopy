package copy;

import java.awt.Component;
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

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

import checkers.Checker;
import checkers.Handlers;
import gui.ExistsDialog;
import gui.ExistsDialogBuilder;
import gui.LongProgressBarModel;


public class Copy extends Thread{
	private static boolean DEBUG = false;
	
	
	private final File origin;
	private final File dest;
	private SuperModel SM;
	private Handlers checkers;
	
	// TODO: 
	
//	private long totalSize = 0;  // TODO: Update final size dinamically
	// GUI elements
	
	public Copy(String orig, String dest, SuperModel SM) throws IOException{
		this.origin = new File(orig);
		this.dest = new File(dest);
		this.SM = SM;
		this.checkers = new Handlers(SM);
		
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
		if (SM.copiableList.hasNext()) {
			Copiable c = SM.copiableList.getNext();
			
			if (checkers.handle(c)) {
			
				c.copy();
				this.completeFileBar();
			}
			
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
}
