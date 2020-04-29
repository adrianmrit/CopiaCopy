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
import testFiles.FileFactory;

class TestMove {
	/**
	 * Empties TestDest and creates it before and after tests
	 * @throws IOException 
	 */
	@BeforeEach
	@AfterEach
	void setupTestDir() throws IOException {
		FileFactory.createTestFiles();
		
		try {
			FileUtils.deleteDirectory(FileFactory.TEST_DEST_FOLDER_PARENT.toFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileFactory.TEST_DEST_FOLDER_PARENT.toFile().mkdir();
	}
	
	@Test
	void testMoveFile() throws IOException {
		
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(SM);
		
		c.addToCopy(FileFactory.FILE_1, FileFactory.TEST_DEST_FOLDER_PARENT, Copiable.CUT_MODE);
		c.copyAll();
		
		assertFalse(FileFactory.FILE_1.toFile().exists());
		
		Path expectedMoved = Paths.get(FileFactory.TEST_DEST_FOLDER_PARENT.toString(),
				FileFactory.FILE_1.getFileName().toString());
		
		assertTrue(expectedMoved.toFile().exists());
		
		Files.delete(expectedMoved);
	}
	
	@Test
	void testMoveFolder() throws IOException {
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(SM);
		
		assertTrue(FileFactory.TEST_FOLDER.toFile().exists());
		assertTrue(FileFactory.SUB_FOLDER.toFile().exists());
		assertTrue(FileFactory.FILE_1.toFile().exists());
		assertTrue(FileFactory.FILE_2.toFile().exists());
		
		c.addToCopy(FileFactory.TEST_FOLDER, FileFactory.TEST_DEST_FOLDER_PARENT, Copiable.CUT_MODE);
		c.copyAll();
		
		assertFalse(FileFactory.TEST_FOLDER.toFile().exists());
		assertFalse(FileFactory.SUB_FOLDER.toFile().exists());
		assertFalse(FileFactory.FILE_1.toFile().exists());
		assertFalse(FileFactory.FILE_2.toFile().exists());
		
		assertFalse(FileFactory.SYMBOLIC_LINK.toFile().exists());
		
		System.out.println(FileFactory.TEST_FOLDER);
		System.out.println(FileFactory.FILE_1);
		System.out.println(FileFactory.DEST_FILE_1);
		System.out.println(FileFactory.DEST_SYMBOLIC_LINK);
		
		assertTrue(FileFactory.TEST_DEST_FOLDER_PARENT.toFile().exists());
		assertTrue(FileFactory.DEST_SUB_FOLDER.toFile().exists());
		assertTrue(FileFactory.DEST_FILE_1.toFile().exists());
		assertTrue(FileFactory.DEST_FILE_2.toFile().exists());
		assertTrue(FileFactory.DEST_SYMBOLIC_LINK.toFile().exists());
	}

}