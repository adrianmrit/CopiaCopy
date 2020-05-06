package copy;

import java.awt.EventQueue;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopiableLoader implements Runnable{
	private Path orig;
	private Path dest;
	private int mode;
	private SuperModel SM;
	
	public CopiableLoader(SuperModel SM, Path orig, Path dest, int mode){
		this.SM = SM;
		this.orig = orig;
		this.dest = dest;
		this.mode = mode;
	}
	
	public CopiableLoader(SuperModel SM, String orig, String dest, int mode){
		this(SM, Paths.get(orig), Paths.get(dest), mode);
	}
	
	public void load(){
		run();
	}

	@Override
	public void run() {
		
		if (Files.isSymbolicLink(orig)) {
			new LinkedSymbolicLink(orig, orig.getParent(), dest, SM, null, mode);
		} else if (Files.isRegularFile(orig, LinkOption.NOFOLLOW_LINKS)) {
			new LinkedFile(orig, orig.getParent(), dest, SM, null, mode);
		} else {
			new LinkedFolder(orig, orig.getParent(), dest, SM, null, mode);
		}
		
	}
}
