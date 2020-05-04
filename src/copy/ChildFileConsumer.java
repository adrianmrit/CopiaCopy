package copy;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChildFileConsumer implements Consumer<Path>{
	private List<Path> files;
	private List<Path> folders;
	private List<Path> symbolicLinks;

	public ChildFileConsumer(List<Path> files, List<Path> folders, List<Path> symbolicLinks) {
		this.files = files;
		this.folders = folders;
		this.symbolicLinks = symbolicLinks;
	}

	@Override
	public void accept(Path path) {
		if (Files.isSymbolicLink(path)) {
			this.symbolicLinks.add(path);
		} else if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
			this.files.add(path);
		} else if(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
			this.folders.add(path);
		}
	}

}
