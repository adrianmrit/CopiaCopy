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
import org.junit.jupiter.api.BeforeAll;

import buffer.Buffer;
import buffer.StaticBuffer;
import copy.Copiable;
import copy.CopiableList;
import copy.CopiableLoader;
import copy.Copy;
import enums.CopyMode;
import models.SuperModel;
import testFiles.FileFactory;
import utils.SystemProps;

class TestCopy {
	/**
	 * Empties TestDest and creates it before and after tests
	 * @throws IOException 
	 */
	@BeforeAll
	static void setUp() {
		SystemProps.setBooleanProp("debug", true);
	}
	
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
	void testCopyFile() throws IOException {
		
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(SM);
		
		FileFactory.DEST_FILE_1.getParent().toFile().mkdir();
		CopiableLoader loader = new CopiableLoader(SM, FileFactory.FILE_1,
				FileFactory.TEST_DEST_FOLDER, CopyMode.COPY);
		
		c.addToCopy(loader);
		c.doTheCopy();
		
		assertTrue(FileFactory.DEST_FILE_1.toFile().exists());
	}
	
	@Test
	void testCopyFolder() throws IOException {
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(SM);
		CopiableLoader loader = new CopiableLoader(SM, FileFactory.TEST_FOLDER,
				FileFactory.TEST_DEST_FOLDER_PARENT, CopyMode.COPY);
		c.addToCopy(loader);
		c.doTheCopy();
		
		assertTrue(FileFactory.DEST_FILE_1.toFile().exists());
		assertTrue(FileFactory.DEST_FILE_2.toFile().exists());
//		assertTrue(FileFactory.DEST_SYMBOLIC_LINK.toFile().exists()); do not check in windows
	}

}
