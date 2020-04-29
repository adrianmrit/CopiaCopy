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
import helpers.TestFile;
import testFiles.TestFileFactory;


class TestNameFactory {
	
	private static final Path TEMP_FILE_1 = Paths.get(TestFileFactory.FILE_1.toString() + ".tmp");
	
	@BeforeEach
	@AfterEach
	void setupTestDir() throws IOException {
		TestFileFactory.createTestFiles();
		
		try {
			FileUtils.deleteDirectory(TestFileFactory.TEST_DEST_FOLDER_PARENT.toFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TestFileFactory.TEST_DEST_FOLDER_PARENT.toFile().mkdir();
	}

	/**
	 * If file exists, getUnique must return a different file path
	 */
	@Test
	void testGetUnique() {
		
		String actual = NameFactory.getUnique(TestFileFactory.FILE_1.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		// If expected exists test will fail
		String fileParentPath = FilenameUtils.getFullPath(TestFileFactory.FILE_1.toString());
		String fileBaseName = FilenameUtils.getBaseName(TestFileFactory.FILE_1.toString());
		String fileExt = FilenameUtils.getExtension(TestFileFactory.FILE_1.toString());
		
		Path expected = Paths.get(fileParentPath, fileBaseName + "_1." + fileExt);
		
		assertEquals(expected.toString(), actual);
	}
	
	@Test
	void testGetUnique_Folder() {
		
		String actual = NameFactory.getUnique(TestFileFactory.TEST_FOLDER.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		// If expected exists test will fail
		Path expected = Paths.get(TestFileFactory.TEST_FOLDER.toString() + "_1").toAbsolutePath();
		
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
		TestFileFactory.createEmptyFile(TEMP_FILE_1);
		
		String actual = NameFactory.getTemp(TestFileFactory.FILE_1.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		Path expected = Paths.get(TestFileFactory.FILE_1.toString() + "_1.tmp").toAbsolutePath();
				
		assertEquals(expected.toString(), actual);
		
		Files.delete(TEMP_FILE_1);
	}
	
	/**
	 * No files here should exist
	 */
	@Test
	void testTemporal_TempDoesntExist() {
		
		String actual = NameFactory.getTemp(TestFileFactory.FILE_1.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		// If expected exists test will fail
		Path expected = TEMP_FILE_1;
				
		assertEquals(expected.toString(), actual);
	}

}
