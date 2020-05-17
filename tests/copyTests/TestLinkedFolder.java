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
import copy.CopiableList;
import enums.CopyMode;
import models.SuperModel;
import copy.CopiableFolder;
import testFiles.FileFactory;

class TestLinkedFolder {

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
	void test() {
		Copiable copiable = new CopiableFolder(FileFactory.TEST_FOLDER,
				FileFactory.TEST_DEST_FOLDER_PARENT, FileFactory.TEST_DEST_FOLDER,
				null, null, CopyMode.COPY);
		
		Iterator<Path> it = null;
		try {
			it = Files.walk(FileFactory.TEST_FOLDER).iterator();
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
		Copiable copiable = new CopiableFolder(FileFactory.TEST_DEST_FOLDER_PARENT,
				FileFactory.TEST_DEST_FOLDER_PARENT.getParent(), FileFactory.TEST_FOLDER,
				null, null, CopyMode.COPY);
		
		Iterator<Path> it = null;
		try {
			it = Files.walk(FileFactory.TEST_DEST_FOLDER_PARENT).iterator();
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
