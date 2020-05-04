package copy;

import java.awt.EventQueue;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopiableLoader implements Runnable{
	private String orig;
	private String dest;
	private int mode;
	private SuperModel SM;
	
	public CopiableLoader(SuperModel SM, String orig, String dest, int mode){
		this.SM = SM;
		this.orig = orig;
		this.dest = dest;
		this.mode = mode;
	}
	
	public void load(){
		run();
	}

	@Override
	public void run() {
		Path origF = Paths.get(orig);
		Path destF = Paths.get(dest);
		
		if (Files.isSymbolicLink(origF)) {
			new LinkedSymbolicLink(origF, origF.getParent(), destF, SM, null, mode);
		} else if (Files.isRegularFile(origF, LinkOption.NOFOLLOW_LINKS)) {
			new LinkedFile(origF, origF.getParent(), destF, SM, null, mode);
		} else {
			new LinkedFolder(origF, origF.getParent(), destF, SM, null, mode);
		}
		
	}
}
