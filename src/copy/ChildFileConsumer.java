package copy;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A consumer that insert files, folder and symbolic link paths
 * to their corresponding list.
 * @author adrianmrit
 */
public class ChildFileConsumer implements Consumer<Path>{
	private List<Path> files;
	private List<Path> folders;
	private List<Path> symbolicLinks;

	/**
	 * Creates a consumer. Takes the lists of paths to be populated
	 * @param files
	 * @param folders
	 * @param symbolicLinks
	 */
	public ChildFileConsumer(List<Path> files, List<Path> folders, List<Path> symbolicLinks) {
		this.files = files;
		this.folders = folders;
		this.symbolicLinks = symbolicLinks;
	}

	/**
	 *	Checks if the given path is a symbolic link, file, or folder
	 *	and adds it to it's corresponding list.
	 */
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
