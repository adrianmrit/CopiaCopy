package copyTests;

import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import copy.Copiable;
import copy.LinkedFolder;
import testFiles.TestFileFactory;

class TestLinkedFolder {

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

	@Test
	void test() {
		Copiable copiable = new LinkedFolder(TestFileFactory.TEST_FOLDER,
				TestFileFactory.TEST_DEST_FOLDER_PARENT, TestFileFactory.TEST_DEST_FOLDER,
				null, null, Copiable.COPY_MODE);
		
		Iterator<Path> it = null;
		try {
			it = Files.walk(TestFileFactory.TEST_FOLDER).iterator();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long expected = 0;
		File f;
		while (it.hasNext()) {
			f = it.next().toFile();
			if (f.isFile()) {
				expected += f.length();
			}
		}
		
		assertEquals(expected, copiable.getSizeRec());
	}
	
	@Test
	void testEmpty() {
		Copiable copiable = new LinkedFolder(TestFileFactory.TEST_DEST_FOLDER_PARENT,
				TestFileFactory.TEST_DEST_FOLDER_PARENT.getParent(), TestFileFactory.TEST_FOLDER,
				null, null, Copiable.COPY_MODE);
		
		Iterator<Path> it = null;
		try {
			it = Files.walk(TestFileFactory.TEST_DEST_FOLDER_PARENT).iterator();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long expected = 0;
		File f;
		while (it.hasNext()) {
			f = it.next().toFile();
			if (f.isFile()) {
				expected += f.length();
			}
		}
		
		assertEquals(expected, copiable.getSize());
	}

}
