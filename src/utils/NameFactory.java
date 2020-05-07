package utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

public class NameFactory {
	
	private NameFactory() {};

	/**
	 * Returns a doted extension if not empty, otherwise return an empty string; 
	 * @param ext
	 * @return
	 */
	private static String dotExt(String ext) {
		if (ext.isEmpty()) {
			return ext;
		} else {
			return "."+ext;
		}
	}
	
	/** 
	 * Add a prefix and a suffix before the extension to a file name.
	 * <br>
	 * <br>
	 * <b>Example</b><br>
	 * {@code
	 * rename("file.txt", "p_", "_s") --> "p_file_s.txt"
	 * }
	 * @param fName file name only, not a path.
	 * @param prefix
	 * @param suffix
	 * @return filename with prefix and suffix before extension
	 */
	private static String rename(String fName, String prefix, String suffix) {
		String base = FilenameUtils.getBaseName(fName);
		String ext = FilenameUtils.getExtension(fName);
		return prefix + base + suffix + dotExt(ext);
	}
	
	/** 
	 * Similar to {@link NameFactory#rename}, but suffix is added after the extension.
	 * <br>
	 * <br>
	 * <b>Example</b><br>
	 * {@code
	 * rename("file.txt", "p_", "_s") --> "p_file.txt_s"
	 * }
	 * @param fName file name only, not a path.
	 * @param prefix
	 * @param suffix
	 * @return filename with prefix and suffix before extension
	 */
	private static String renameAfterExt(String fName, String prefix, String suffix) {
		return prefix + fName + suffix;
	}
	
	/**
	 * Adds a prefix and a suffix to the file or directory denoted by a path.
	 * <br>
	 * <br>
	 * <b>Example</b><br>
	 * {@code
	 * renamePath("path/to/file.txt", "p_", "_s", false) --> "path/to/p_file_s.txt"
	 * renamePath("path/to/file.txt", "p_", "_s", true)  --> "path/to/p_file.txt_s"
	 * }
	 * 
	 * @param fPath file or directory path
	 * @param prefix
	 * @param suffix
	 * @param afterExt
	 * @return
	 */
	private static String renamePath (String fPath, String prefix, String suffix, boolean afterExt) {
		String fileName = FilenameUtils.getName(fPath);
		String root = FilenameUtils.getFullPath(fPath);
		
		if (afterExt) {
			fileName = renameAfterExt(fileName, prefix, suffix);
		} else {
			fileName = rename(fileName, prefix, suffix);
		}
		
		return FilenameUtils.concat(root, fileName);
	}
	
	/**
	 * Gets a filename that doesn't exist.
	 * 
	 * <br>
	 * 
	 * If <b>fPath</b> doesn't exist the returning value will be fPath,
	 * otherwise _n will be added between the filename and the
	 * extension (if there is), where n starts with 1 and keeps growing
	 * until an unique file name exists;
	 * 
	 * @param fPath
	 * @return
	 */
	public static String getUnique(String fPath) {
		fPath = FilenameUtils.normalizeNoEndSeparator(fPath);
		Path path = Paths.get(fPath);
		int counter = 1;
		
		while (Files.exists(path)) {
			String suffix = String.format("_%d", counter);
			path = Paths.get(renamePath(fPath, "", suffix, false));
			counter++;
		}
		
		return path.toString();
	}
	
	public static Path getUnique(Path fPath) {
		return Paths.get(getUnique(fPath.toString()));
	}
	
	
	/**
	 * Gets a temporal filename denoted by the extension ".tmp".
	 * The resulting temporal file does not exist
	 * 
	 * <br>
	 * 
	 * Note: There is no distinction between files and folders
	 * 
	 * @param fPath
	 * @return
	 */
	public static String getTemp(String fPath) {
		fPath = FilenameUtils.normalizeNoEndSeparator(fPath);
		Path path;
		int counter = 1;
		String tempSuffix = ".tmp";
		
		do {
			path = Paths.get(renamePath(fPath, "", tempSuffix, true));
			tempSuffix = String.format("_%d.tmp", counter);
			counter++;
		} while (Files.exists(path)) ;
		
		return path.toString();
	}
	
	public static Path getTemp(Path fPath) {
		return Paths.get(getTemp(fPath.toString()));
	}
}
