package copy;

import java.io.File;
import java.io.FileFilter;

public class IsFolderFilter implements FileFilter{

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory();
	}

}
