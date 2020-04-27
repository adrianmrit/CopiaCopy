package copyTests;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import copy.NameFactory;
import helpers.TestFile;


class TestNameFactory {
	
	@BeforeAll
	static void checkTestFiles() {
		String[] shouldExist = new String[] {
			"Sample/SampleFile.sample",
			"Sample/SampleFile.sample.tmp"
		};
		
		String[] shouldNotExist = new String[] {
				"Sample/SampleFile_1.sample",
				"Sample/SampleFile.sample_1.tmp",
				"Sample/NonExistentFile.sample",
				"Sample/NonExistentFile.sample.tmp"
		};
		
		for(String path: shouldExist) {
			Path f = Paths.get(path).toAbsolutePath();
			if (!Files.exists(f)) {
				TestFile.fileExistsFailed(path);
			}
		}
		
		for(String path: shouldNotExist) {
			Path f = Paths.get(path).toAbsolutePath();
			if (Files.exists(f)) {
				TestFile.fileDoesntExistFailed(path);
			}
		}
	}

	/**
	 * If file exists, getUnique must return a different file path
	 */
	@Test
	void testGetUnique() {
		Path src = Paths.get("Sample/SampleFile.sample").toAbsolutePath();
		
		String actual = NameFactory.getUnique(src.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		// If expected exists test will fail
		Path expected = Paths.get("Sample/SampleFile_1.sample").toAbsolutePath();
		
		assertEquals(expected.toString(), actual);
	}
	
	@Test
	void testGetUnique_Folder() {
		Path src = Paths.get("Sample/").toAbsolutePath();
		
		String actual = NameFactory.getUnique(src.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		// If expected exists test will fail
		Path expected = Paths.get("Sample_1").toAbsolutePath();
		
		assertEquals(expected.toString(), actual);
	}
	
	/**
	 * If file doesn't exist, getUnique must return the same file path
	 */
	@Test
	void testGetUnique_FileDoesNotExist() {
		Path src = Paths.get("Sample/NonExistentFile.sample").toAbsolutePath();
		
		String actual = NameFactory.getUnique(src.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		assertEquals(src.toString(), actual);
	}
	
	
	/**
	 * Take into account that SampleFile.sample.tmp should exist
	 */
	@Test
	void testTemporal() {
		Path src = Paths.get("Sample/SampleFile.sample").toAbsolutePath();
		if (!Files.exists(src)) {
			TestFile.fileExistsFailed(src.toString());
		}
		
		String actual = NameFactory.getTemp(src.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		Path expected = Paths.get("Sample/SampleFile.sample_1.tmp").toAbsolutePath();
				
		assertEquals(expected.toString(), actual);
	}
	
	/**
	 * No files here should exist
	 */
	@Test
	void testTemporal_TempDoesntExist() {
		Path src = Paths.get("Sample/NonExistentFile.sample").toAbsolutePath();
		
		String actual = NameFactory.getTemp(src.toString());
		
		assertFalse(Files.exists(Paths.get(actual)));
		
		// If expected exists test will fail
		Path expected = Paths.get("Sample/NonExistentFile.sample.tmp").toAbsolutePath();
				
		assertEquals(expected.toString(), actual);
	}

}
