package copy;

import java.io.File;
import java.io.FileFilter;

public class IsFileFilter implements FileFilter{

	@Override
	public boolean accept(File pathname) {
		return pathname.isFile();
	}

}
