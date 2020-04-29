package copyTests;

import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

class TestMove {
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
	void testMoveFile() throws IOException {
		
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(SM);
		
		c.addToCopy(TestFileFactory.FILE_1, TestFileFactory.TEST_DEST_FOLDER_PARENT, Copiable.CUT_MODE);
		c.copyAll();
		
		assertFalse(TestFileFactory.FILE_1.toFile().exists());
		
		Path expectedMoved = Paths.get(TestFileFactory.TEST_DEST_FOLDER_PARENT.toString(),
				TestFileFactory.FILE_1.getFileName().toString());
		
		assertTrue(expectedMoved.toFile().exists());
		
		Files.delete(expectedMoved);
	}
	
	@Test
	void testMoveFolder() throws IOException {
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(SM);
		
		assertTrue(TestFileFactory.TEST_FOLDER.toFile().exists());
		assertTrue(TestFileFactory.SUB_FOLDER.toFile().exists());
		assertTrue(TestFileFactory.FILE_1.toFile().exists());
		assertTrue(TestFileFactory.FILE_2.toFile().exists());
		
		c.addToCopy(TestFileFactory.TEST_FOLDER, TestFileFactory.TEST_DEST_FOLDER_PARENT, Copiable.CUT_MODE);
		c.copyAll();
		
		assertFalse(TestFileFactory.TEST_FOLDER.toFile().exists());
		assertFalse(TestFileFactory.SUB_FOLDER.toFile().exists());
		assertFalse(TestFileFactory.FILE_1.toFile().exists());
		assertFalse(TestFileFactory.FILE_2.toFile().exists());
		
		assertFalse(TestFileFactory.SYMBOLIC_LINK.toFile().exists());
		
		System.out.println(TestFileFactory.TEST_FOLDER);
		System.out.println(TestFileFactory.FILE_1);
		System.out.println(TestFileFactory.DEST_FILE_1);
		System.out.println(TestFileFactory.DEST_SYMBOLIC_LINK);
		
		assertTrue(TestFileFactory.TEST_DEST_FOLDER_PARENT.toFile().exists());
		assertTrue(TestFileFactory.DEST_SUB_FOLDER.toFile().exists());
		assertTrue(TestFileFactory.DEST_FILE_1.toFile().exists());
		assertTrue(TestFileFactory.DEST_FILE_2.toFile().exists());
		assertTrue(TestFileFactory.DEST_SYMBOLIC_LINK.toFile().exists());
	}

}