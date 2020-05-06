package utils;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

public class FileTools{
	public static final String[] PATH_SEPARATORS = new String[] {"/", "\\"};
	
	/**
	 * Checks if a path is valid or not
	 * @param path path to be checked
	 * @return true if path is valid, false otherwise
	 */
	public static boolean isValidPath(String path) {
		try {
			Paths.get(path);
			return true;
		} catch (InvalidPathException e) {
			return false;
		}
	}
	
	/**
	 * Checks if the given string is a valid file name but not a path.
	 * @param fileName string to be checked
	 * @return true if its valid, false otherwise
	 */
	public static boolean isValidFileName(String fileName) {
		for (String c:PATH_SEPARATORS) {  // checks that is it not a path
			if (fileName.contains(c)){
				return false;
			}
		}
		
		if (isValidPath(fileName)) {  // checks whether the name contains valid characters
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if the given path is a file. Symbolic links are not followed by default.
	 * @param path path to be checked
	 * @return true if it is a file, false otherwise
	 */
	public static boolean isFile(Path path) {
		return Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS);
	}
	
	/**
	 * Checks if the given path is a folder. Symbolic links are not followed by default.
	 * @param path path to be checked
	 * @return true if it is a folder, false otherwise
	 */
	public static boolean isFolder(Path path) {
		return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
	}
	
	/**
	 * Checks if the given path is a symbolic link.
	 * @param path path to be checked
	 * @return true if it is a symbolic link, false otherwise
	 */
	public static boolean isSymbolicLink(Path path) {
		return Files.isSymbolicLink(path);
	}
}
