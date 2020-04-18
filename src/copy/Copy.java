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

import gui.FileExistsDialog;
import gui.LongProgressBarModel;


public class Copy extends Thread{
	private static boolean DEBUG = false;
	
	
	private final File origin;
	private final File dest;
	
//	private final Object[] origPaths;  // TODO: change to Path[]
	private long totalSize = 0;
	
	private static final int DEFAULT_BUFFER_SIZE = 8192; // 8kb
	
	private TreeMap<Path, Path> origDestMap;  // ensures order 
	
	// Must be emptied after used. 
	// Useful to avoid ConcurrentModificationException
	private TreeMap<Path, Path> toRename;  // set of files to be renamed.
	private ArrayList<Path> toRemove;
	
	// GUI elements
	
	private LongProgressBarModel fileProgressModel;
	private LongProgressBarModel totalProgressModel;
	private JLabel currentLabel;
	private JFrame frame; // used to create windows
	private boolean hasGUI = false;
	
	
	public Copy(String orig, String dest) throws IOException{
		this.origin = new File(orig);
		this.dest = new File(dest);
		
		if (this.origin.isFile()) {
			
			this.origDestMap = new TreeMap<>();
			this.origDestMap.put(this.origin.toPath(), resolveDest(this.origin.toPath()));
		} else {
			Iterator<Path> iter = Copy.getPathsIterator(this.origin.toPath());
			setOrigDestMap(iter);
		}
	}
	
	public Copy(File orig, File dest) throws IOException {
		this(orig.getAbsolutePath(), dest.getAbsolutePath());
	}
	
	public Copy(Path orig, Path dest) throws IOException {
		this(orig.toString(), dest.toString());
	}
	
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	public void setFileProgressModel(LongProgressBarModel fileProgressModel) {
		this.fileProgressModel = fileProgressModel;
	}
	
	public void setTotalProgressModel(LongProgressBarModel totalProgressModel) {
		this.totalProgressModel = totalProgressModel;
	}
	
	public void setCurrentLabel(JLabel currentLabel) {
		this.currentLabel = currentLabel;
	}
	
	public void setHasGUI(boolean b) {
		this.hasGUI = b;
	}
	
	public void setOrigDestMap(Iterator<Path> originPaths) {
		this.origDestMap = new TreeMap<>();
		while (originPaths.hasNext()) {
			Path originPath = originPaths.next();
			Path destPath = resolveDest(originPath);
			this.origDestMap.put(originPath, destPath);
		}
	}
	
	/** 
	 * Used to run the copy in a thread
	 */
	public void run() {
		int exitStatus = 0;
		try {
			long tookAll = System.currentTimeMillis();
			resolveExistingFiles();
			long tookResolve = System.currentTimeMillis() - tookAll;
			
			long tookGetSize = System.currentTimeMillis();
			this.totalSize = Copy.getFolderArraySize(this.origDestMap.keySet());
			this.totalProgressModel.setLongMaximum(this.totalSize);
			tookGetSize = System.currentTimeMillis() - tookGetSize;
			
			long tookCopy = System.currentTimeMillis();
			this.copy();
			tookCopy = System.currentTimeMillis() - tookCopy;
			
			tookAll = System.currentTimeMillis() - tookAll;
			
			if(DEBUG) {
				System.out.println("Took (all): " + tookAll + "ms");
				System.out.println("Took (copy): " + tookCopy + "ms");
				System.out.println("Took (resolve files): " + tookResolve + "ms");
				System.out.println("Took (get size): " + tookGetSize + "ms");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exitStatus = 1;
		}
		
		if(!DEBUG) {
			System.exit(exitStatus);
		}
	}
	
	/** Copy the files and creates the folders
	 * @throws IOException if fails
	 */
	public void copy() throws IOException {
		// TODO: if folder exist, rename, merge (copy content and ask if content exist), or skip (cancel copy)
		// TODO: handle copy in same path, should duplicate file with a "(copy)" at the end,
		// TODO: Avoid copy folder into itself
		// before file extension.
		Iterator<Path> originIterator = this.origDestMap.keySet().iterator();
		while (originIterator.hasNext()) {
			Path origPath = originIterator.next();
			Path destPath = this.origDestMap.get(origPath);
			
			if (origPath.toFile().isFile()) {
				// TODO: handle file exists
				this.copyFile(origPath, destPath);
			} else {
				destPath.toFile().mkdirs();
			}
		}
	}
	
	/**
	 * Shows a dialog if the file already exists in the destination.
	 * @param origin Origin path
	 * @param dest Destination path
	 */
	private void handleFileExistDialog(File origin, File dest) {
		if (this.hasGUI) {
			FileExistsDialog dialog = new FileExistsDialog(this.frame, origin, dest);
			dialog.run();
			String action = dialog.getAction();
			switch (action) {
				case FileExistsDialog.CANCEL:
					System.exit(0);  // exit copy
				
				case FileExistsDialog.SKIP:
					this.toRemove.add(origin.toPath());
				
				case FileExistsDialog.RENAME:
					Path newDest = Paths.get(dest.getParent().toString(), dialog.getInputValue());
					this.toRename.put(origin.toPath(), newDest);
				
				case FileExistsDialog.REPLACE:
					//do nothing, copy will replace it automatically
			}
		}
	}
	
	/**
	 * Renames the files that where selected to be renamed with FileExistsDialog
	 */
	private void renameFilesInMap() {
		Iterator<Path> toRenameIterator = this.toRename.keySet().iterator();
		while (toRenameIterator.hasNext()) {
			Path origin = toRenameIterator.next();
			this.origDestMap.replace(origin, this.toRename.get(origin));
		}
	}
	
	/**
	 * Removes the files that where selected to be removed with FileExistsDialog
	 */
	private void removeFilesFromMap() {
		Iterator<Path> toRemoveIterator = toRemove.iterator();
		while (toRemoveIterator.hasNext()) {
			Path origin = toRemoveIterator.next();
			this.origDestMap.remove(origin);
		}
	}
	
	/**
	 * Check if there is any file in the copy that exists in the origin. If it does it will ask what to do with it.
	 * Files will be renamed and removed after asking for all of them, to avoid concurrent operation errors.
	 */
	private void resolveExistingFiles() {
		Iterator<Path> originIterator = this.origDestMap.keySet().iterator();
		this.toRename = new TreeMap<>();
		this.toRemove = new ArrayList<>();
		while (originIterator.hasNext()) {
			File origin = originIterator.next().toFile();
			File dest = this.origDestMap.get(origin.toPath()).toFile();
			if (dest.exists() && dest.isFile()) {
				handleFileExistDialog(origin, dest);
			}
		}
		renameFilesInMap();
		removeFilesFromMap();
	}
	
	/** Sets the debug status
	 * @param debug status
	 */
	public static void setDebug(boolean debug) {
		DEBUG = debug;
	}
	
	/** Resolves the <i>path</i> where path will be copied.
	 * @param path origin path
	 * @return destination path
	 */
	public Path resolveDest(Path path) {
		String pathRootParent = this.origin.getParent();
		String subPath = path.toString().replaceFirst(pathRootParent, "");  // path - this.orig parent
		Path resolved = Paths.get(this.dest.toString(), subPath);  // join both paths
		
		return resolved;
	}
	
	/** Returns an Iterator for all the inner files and folder between a path.
	 * @param path Path to be walked
	 * @return Iterator<Path> with all the inner paths
	 * @throws IOException
	 */
	public static Iterator<Path> getPathsIterator(Path path) throws IOException {
		return Files.walk(path).iterator();
	}
	
	/** Get the total size for all the paths between a Set,
	 * if it is a file.
	 * @param paths all the paths
	 * @return long total size
	 */
	public static long getFolderArraySize(Set<Path> paths) {
		long total = 0;
		Iterator<Path> i = paths.iterator();
		while(i.hasNext()) {
			File path = i.next().toFile();
			if (path.isFile()) {
				total += path.length();
			}
		}
		
		return total;
	}

	/** Updates the progress bars if they exist
	 * @param bytesRead last number of bytes read
	 */
	private void updateProgressBars(long bytesRead) {
		if (this.hasGUI) {
			this.fileProgressModel.addLongValue(bytesRead);
			this.totalProgressModel.addLongValue(bytesRead);
		}
	}
	
	/** Copy a file from orig to dest, and updates the UIElements.
	 * @param orig origin path
	 * @param dest destination path
	 * @throws IOException if fails
	 */
	public void copyFile(Path orig, Path dest) throws IOException {
		File origF = orig.toFile();
		File destF = dest.toFile();
		destF.getParentFile().mkdirs();
		
		InputStream is = new FileInputStream(origF);;
		OutputStream os = new FileOutputStream(destF);
		
		if (this.hasGUI) {
			this.fileProgressModel.setLongMaximum(origF.length()); // resets the fileProgressBar
			this.fileProgressModel.setLongValue(0); // resets the fileProgressBar
	
			this.currentLabel.setText("Current: " + orig.toString());
		}
		
		try {
			// do the copy
			byte[] buf = new byte[DEFAULT_BUFFER_SIZE]; // TODO: Find optimal chunk size
			int bytesRead;
			
			while ((bytesRead = is.read(buf)) > 0) {
				os.write(buf, 0, bytesRead);
				updateProgressBars(bytesRead);
			}

		} finally {
			is.close();
			os.close();
		}
	}
}
