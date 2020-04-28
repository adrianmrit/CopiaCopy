package copy;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;

public class IsSymbolicLinkFilter implements FileFilter{

	@Override
	public boolean accept(File pathname) {
		return Files.isSymbolicLink(pathname.toPath());
	}

}
