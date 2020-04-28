package copy;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;

public class IsFolderFilter implements FileFilter{

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory() && !Files.isSymbolicLink(pathname.toPath());
	}

}
