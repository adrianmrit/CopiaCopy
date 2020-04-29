package copyTests;

import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import buffer.Buffer;
import buffer.StaticBuffer;
import copy.Copiable;
import copy.CopiableList;
import copy.Copy;
import copy.SuperModel;
import testFiles.TestFileFactory;

class TestCopy {
	/**
	 * Empties TestDest and creates it before and after tests
	 * @throws IOException 
	 */
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
	void testCopyFile() throws IOException {
		
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(SM);
		
		TestFileFactory.DEST_FILE_1.getParent().toFile().mkdir();
		
		c.addToCopy(TestFileFactory.FILE_1, TestFileFactory.DEST_FILE_1.getParent(), Copiable.COPY_MODE);
		c.copyAll();
		
		assertTrue(TestFileFactory.DEST_FILE_1.toFile().exists());
	}
	
	@Test
	void testCopyFolder() throws IOException {
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(SM);
		
		c.addToCopy(TestFileFactory.TEST_FOLDER, TestFileFactory.TEST_DEST_FOLDER_PARENT, Copiable.COPY_MODE);
		c.copyAll();
		
		assertTrue(TestFileFactory.DEST_FILE_1.toFile().exists());
		assertTrue(TestFileFactory.DEST_FILE_2.toFile().exists());
		assertTrue(TestFileFactory.DEST_SYMBOLIC_LINK.toFile().exists());
	}

}
