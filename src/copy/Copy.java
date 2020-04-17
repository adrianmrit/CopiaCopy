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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;

import gui.LongProgressBarModel;

public class Copy extends Thread{
	private static boolean DEBUG = false;
	
	private final File orig;
	private final File dest;
	
//	private final Object[] origPaths;  // TODO: change to Path[]
	private final long totalSize;
	
	private static final int DEFAULT_BUFFER_SIZE = 8192; // 8kb
	
	private HashMap<Path, Path> origDestMap;
	
	// GUI elements
	
	private LongProgressBarModel fileProgressModel;
	private LongProgressBarModel totalProgressModel;
	private JLabel currentLabel;
	private JFrame frame; // used to create windows
	private boolean hasGUI = false;
	
	
	public Copy(String orig, String dest) throws IOException{
		this.orig = new File(orig);
		this.dest = new File(dest);
		
		if (this.orig.isFile()) {
			
			this.origDestMap = new HashMap<>();
			this.origDestMap.put(this.orig.toPath(), resolveDest(this.orig.toPath()));
		} else {
			Iterator<Path> iter = Copy.getPathsIterator(this.orig.toPath());
			setOrigDestMap(iter);
		}
		
		this.totalSize = Copy.getFolderArraySize(this.origDestMap.keySet());
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
		this.totalProgressModel.setLongMaximum(this.totalSize);
	}
	
	public void setCurrentLabel(JLabel currentLabel) {
		this.currentLabel = currentLabel;
	}
	
	public void setHasGUI(boolean b) {
		this.hasGUI = b;
	}
	
	public void setOrigDestMap(Iterator<Path> originPaths) {
		this.origDestMap = new HashMap<>();
		while (originPaths.hasNext()) {
			Path originPath = originPaths.next();
			Path destPath = resolveDest(originPath);
			this.origDestMap.put(originPath, destPath);
		}
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
	
	/** Used to run the copy in a thread
	 *
	 */
	public void run() {
		int exitStatus = 0;
		try {
			long took = System.currentTimeMillis();
//			System.out.println(launchWindowsTest());
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
