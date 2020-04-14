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

import gui.UIElementsHolder;

public class Copy extends Thread{
	private static boolean DEBUG = false;
	
	private final File orig;
	private final File dest;
	
	private final Object[] origPaths;  // TODO: change to Path[]
	private final long totalSize;
	
	private final UIElementsHolder UIELEMENTS;
	
	private static final int DEFAULT_BUFFER_SIZE = 1024; // 8kb
	private static final long MAX_BUFFER_SIZE = 1048576; // 1mb
	
	private int bufferSize; // current choosed buffer size
	private short lastAvgTime; // average time between x number of writes to a file, used to calculate bufferSize
	private static final short NUMBER_STEPS = 10;
	private short operationCounter;  // counter, should reset when equals NUMBER_STEPS
	
	public Copy(String orig, String dest, UIElementsHolder UIELEMENTS) throws IOException{
		this.orig = new File(orig);
		this.dest = new File(dest);
		this.bufferSize = DEFAULT_BUFFER_SIZE;
		
		this.UIELEMENTS = UIELEMENTS;
		
		if (this.orig.isFile()) {
			// if file there is only one path to be copied
			this.origPaths = new Path[] {this.orig.toPath()};
		} else {
			// get all sub-paths to be copied
			this.origPaths = Copy.getPaths(this.orig.toPath());
		}
		
		this.totalSize = Copy.getFolderArraySize(this.origPaths);
		
		// Initialize the total progress bar
		if (this.UIELEMENTS != null) {
			// avoid overriding the folder progress bar when passed to new Copy objects;
			this.UIELEMENTS.totalProgressBar.setMaximum(this.totalSize);
			this.UIELEMENTS.totalProgressBar.setMinimum(0);
			this.UIELEMENTS.totalProgressBar.setValue(0);
		}
	}
	
	public Copy(File orig, File dest, UIElementsHolder UIELEMENTS) throws IOException {
		this(orig.getAbsolutePath(), dest.getAbsolutePath(), UIELEMENTS);
	}
	
	public Copy(Path orig, Path dest, UIElementsHolder UIELEMENTS) throws IOException {
		this(orig.toString(), dest.toString(), UIELEMENTS);
	}
	
	public Copy(String orig, String dest) throws IOException {
		this(orig, dest, null);
	}
	
	public Copy(File orig, File dest) throws IOException {
		this(orig, dest, null);
	}
	
	public Copy(Path orig, Path dest) throws IOException {
		this(orig, dest, null);
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
		String pathRootParent = this.orig.getParent();
		String subPath = path.toString().replaceFirst(pathRootParent, "");  // path - this.orig parent
		Path resolved = Paths.get(this.dest.toString(), subPath);  // join both paths
		
		return resolved;
	}
	
	/** Get an Object array that contains all the paths between this folder,
	 * including paths between sub-folders.
	 * @param path
	 * @return Object[] with all sub-paths
	 * @throws IOException if fails
	 */
	public static Object[] getPaths(Path path) throws IOException {
		Object [] paths = Files.walk(path).toArray();
		return paths;
	}
	
	/** Get the total size for all the paths between an Object array,
	 * if it is a file.
	 * @param paths Object array with all the paths
	 * @return long total size
	 */
	public static long getFolderArraySize(Object[] paths) {
		long total = 0;
		for (int i=0;i<paths.length;i++) {
			File path = ((Path) paths[i]).toFile();
			if (path.isFile()) {
				total += path.length();
			}
		}
		
		return total;
	}
	
	/** Used to run the copy in a thread
	 *
	 */
	public void run() {
		int exitStatus = 0;
		try {
			long took = System.currentTimeMillis();
			
			this.copy();
			
			took = System.currentTimeMillis() - took;
			
			if(DEBUG) {
				System.out.println("Took: " + took + "ms");
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
		// TODO: if file exists, skip, or rename
		// TODO: handle copy in same path, should duplicate file with a "(copy)" at the end,
		// before file extension.
		for (int i=0; i<this.origPaths.length; i++) {
			Path origPath = (Path) this.origPaths[i];
			Path destPath = this.resolveDest(origPath);
			if (origPath.toFile().isFile()) {
				// TODO: handle file exists
				this.copyFile(origPath, destPath);
			} else {
				destPath.toFile().mkdirs();
			}
		}
	}

	/** Updates the progress bars if they exist
	 * @param bytesRead last number of bytes read
	 */
	public void updateProgressBars(long bytesRead) {
		if (this.UIELEMENTS != null) {
			this.UIELEMENTS.fileProgressBar.addValue(bytesRead);
			this.UIELEMENTS.totalProgressBar.addValue(bytesRead);
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
		
		if (this.UIELEMENTS != null) {
			this.UIELEMENTS.fileProgressBar.setMaximum(origF.length()); // resets the fileProgressBar
			this.UIELEMENTS.fileProgressBar.setMinimum(0);
			this.UIELEMENTS.fileProgressBar.setValue(0L);
			
			this.UIELEMENTS.currentLabel.setText("Current: " + orig.toString());
		}
		
		try {
			// do the copy
			byte[] buf = new byte[this.bufferSize]; // TODO: Find optimal chunk size
			int bytesRead;
			
			while ((bytesRead = is.read(buf)) > 0) {
				os.write(buf, 0, bytesRead);
				this.updateProgressBars(bytesRead);
			}

		} finally {
			is.close();
			os.close();
		}
	}
}
