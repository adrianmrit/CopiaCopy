package testFiles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.apache.commons.io.FileUtils;

public class FileFactory {
	private static final int CHUNK_SIZE = (int) FileUtils.ONE_MB*30;
	private static final byte FILLING_BYTE = 1;
	
	public static final Path TEST_FOLDER = Paths.get("TestFolder").toAbsolutePath();
	public static final Path TEST_DEST_FOLDER_PARENT = Paths.get("TestDestFolder").toAbsolutePath();
	public static final Path TEST_DEST_FOLDER = Paths.get(TEST_DEST_FOLDER_PARENT.toString(),
			TEST_FOLDER.getFileName().toString());
	
	
	public static final Path SUB_FOLDER = Paths.get(TEST_FOLDER.toString(), "/TestSubFolder");
	public static final Path DEST_SUB_FOLDER = Paths.get(TEST_DEST_FOLDER.toString(), "/TestSubFolder");
	
	public static final Path FILE_1 = Paths.get(TEST_FOLDER.toString(), "/TestFile1.ext");
	public static final Path DEST_FILE_1 = Paths.get(TEST_DEST_FOLDER.toString(), "/TestFile1.ext");
	
	public static final Path FILE_2 = Paths.get(SUB_FOLDER.toString(), "/TestFile2");
	public static final Path DEST_FILE_2 = Paths.get(DEST_SUB_FOLDER.toString(), "/TestFile2");
	
	public static final Path SYMBOLIC_LINK = Paths.get(TEST_FOLDER.toString(), "/SymbolicLink");
	public static final Path DEST_SYMBOLIC_LINK = Paths.get(TEST_DEST_FOLDER.toString(), "/SymbolicLink");
	
	public static final Path SYMBOLIC_LINK_ADDRESS = Paths.get("").toAbsolutePath();
	
	private final static long FILE_SIZE = (int) FileUtils.ONE_MB*30;
	
	public static void createRandomFile(long fileSize, Path file) throws IOException {
		long size = 0;
		byte[] chunk = new byte[CHUNK_SIZE];
		
		Random r = new Random();
		
		r.nextBytes(chunk);
		OutputStream os = new FileOutputStream(file.toFile());
		try {
			while (size <= fileSize){
			size += fileSize;
			
				os.write(chunk, 0, chunk.length);
			}
		}finally {
				os.close();
		}
	}
	
	public static void createEmptyFile(Path file) throws IOException {
		if (!Files.exists(file)) {
			Files.createFile(file);
		}
	}
	
	public static void createOriginEmptyFile(Path file) throws IOException {
		createEmptyFile(Paths.get(TEST_FOLDER.toString(), file.toString()));
	}
	
	public static void createDestEmptyFile(Path file) throws IOException {
		createEmptyFile(Paths.get(TEST_DEST_FOLDER_PARENT.toString(), file.toString()));
	}
	
	public static Path getOrigPath(String filename){
		return Paths.get(TEST_FOLDER.toString(), filename);
	}
	
	public static Path getDestPath(String filename){
		return Paths.get(TEST_DEST_FOLDER_PARENT.toString(), filename);
	}
	
	public static void createTestFiles() throws IOException {

		if (!Files.exists(TEST_FOLDER)) {
			Files.createDirectory(TEST_FOLDER);
		}
		
		if (!Files.exists(SUB_FOLDER)) {
			Files.createDirectory(SUB_FOLDER);
		}
		
		if (!Files.exists(SYMBOLIC_LINK)) {
			Files.createSymbolicLink(SYMBOLIC_LINK, SYMBOLIC_LINK_ADDRESS);
		}
		
		if (!Files.exists(FILE_1)) {
			FileFactory.createRandomFile(FILE_SIZE, FILE_1);
		}
		
		if (!Files.exists(FILE_2)) {
			FileFactory.createRandomFile(FILE_SIZE, FILE_2);
		}
	}
	
	public static void removeTestFiles() throws IOException {
		FileUtils.deleteDirectory(TEST_FOLDER.toFile());
	}
	
	public static void main(String[] args) throws IOException {
		FileFactory.createTestFiles();
	}
}
