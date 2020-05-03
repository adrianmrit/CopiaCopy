package copyTests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import copy.NameFactory;
import testFiles.FileFactory;


class TestNameFactory {
	
	private static final Path TEMP_FILE_1 = Paths.get(FileFactory.FILE_1.toString() + ".tmp");
	
	@BeforeEach
	@AfterEach
	void setupTestDir() throws IOException {
		FileFactory.createTestFiles();
		
		try {
			FileUtils.deleteDirectory(FileFactory.TEST_DEST_FOLDER_PARENT.toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileFactory.TEST_DEST_FOLDER_PARENT.toFile().mkdir();
	}

	/**
	 * If file exists, getUnique must return a different file path
	 */
	@Test
	void testGetUnique() {
		
		String actual = NameFactory.getUnique(FileFactory.FILE_1.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		// If expected exists test will fail
		String fileParentPath = FilenameUtils.getFullPath(FileFactory.FILE_1.toString());
		String fileBaseName = FilenameUtils.getBaseName(FileFactory.FILE_1.toString());
		String fileExt = FilenameUtils.getExtension(FileFactory.FILE_1.toString());
		
		Path expected = Paths.get(fileParentPath, fileBaseName + "_1." + fileExt);
		
		assertEquals(expected.toString(), actual);
	}
	
	@Test
	void testGetUnique_Folder() {
		
		String actual = NameFactory.getUnique(FileFactory.TEST_FOLDER.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		// If expected exists test will fail
		Path expected = Paths.get(FileFactory.TEST_FOLDER.toString() + "_1").toAbsolutePath();
		
		assertEquals(expected.toString(), actual);
	}
	
	/**
	 * If file doesn't exist, getUnique must return the same file path
	 */
	@Test
	void testGetUnique_FileDoesNotExist() {
		Path src = Paths.get("NonExistentFile.sample").toAbsolutePath();
		
		String actual = NameFactory.getUnique(src.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		assertEquals(src.toString(), actual);
	}
	
	
	@Test
	void testTemporal() throws IOException {
		FileFactory.createEmptyFile(TEMP_FILE_1);
		
		String actual = NameFactory.getTemp(FileFactory.FILE_1.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		Path expected = Paths.get(FileFactory.FILE_1.toString() + "_1.tmp").toAbsolutePath();
				
		assertEquals(expected.toString(), actual);
		
		Files.delete(TEMP_FILE_1);
	}
	
	/**
	 * No files here should exist
	 */
	@Test
	void testTemporal_TempDoesntExist() {
		
		String actual = NameFactory.getTemp(FileFactory.FILE_1.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		// If expected exists test will fail
		Path expected = TEMP_FILE_1;
				
		assertEquals(expected.toString(), actual);
	}

}
