package copy;

import java.awt.EventQueue;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import enums.CopyMode;
import models.SuperModel;

/**
 * A loader for copiables. If the origin is a folder, loads this folder
 * and it's content. It can also load in a separate thread.
 * @author adrianmrit
 */
public class CopiableLoader implements Runnable{
	private Path orig;
	private Path dest;
	private CopyMode mode;
	private SuperModel SM;
	
	/**
	 * Creates a CopiableLoader that can load copiables in the future
	 * @param SM
	 * @param orig Origin copiable
	 * @param dest Destination path
	 * @param mode see {@link CopyMode}
	 */
	public CopiableLoader(SuperModel SM, Path orig, Path dest, CopyMode mode){
		this.SM = SM;
		this.orig = orig;
		this.dest = dest;
		this.mode = mode;
	}
	
	/**
	 * Creates a CopiableLoader that can load copiables in the future
	 * @param SM
	 * @param orig Origin copiable
	 * @param dest Destination path
	 * @param mode {@link Copiable#COPY_MODE} or {@link Copiable#CUT_MODE}
	 */
	public CopiableLoader(SuperModel SM, String orig, String dest, CopyMode mode){
		this(SM, Paths.get(orig), Paths.get(dest), mode);
	}
	
	/**
	 * Loads the copiables, same as {@link #run()}
	 */
	public void load(){
		run();
	}

	/**
	 * Loads the copiables, same as {@link #load()}
	 */
	@Override
	public void run() {
		
		if (Files.isSymbolicLink(orig)) {
			new CopiableSymbolicLink(orig, orig.getParent(), dest, SM, null, mode);
		} else if (Files.isRegularFile(orig, LinkOption.NOFOLLOW_LINKS)) {
			new CopiableFile(orig, orig.getParent(), dest, SM, null, mode);
		} else {
			new CopiableFolder(orig, orig.getParent(), dest, SM, null, mode);
		}
		
	}
}
