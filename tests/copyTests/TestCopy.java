package copyTests;

import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import buffer.Buffer;
import buffer.StaticBuffer;
import copy.Copiable;
import copy.CopiableList;
import copy.Copy;
import copy.SuperModel;

class TestCopy {
	/**
	 * Empties TestDest and creates it before and after tests
	 */
	@BeforeAll
	@AfterAll
	static void setupTestDir() {
		File dest = Paths.get("TestDest/").toAbsolutePath().toFile();
		try {
			FileUtils.deleteDirectory(dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dest.mkdir();
	}
	
	@Test
	void testCopyFile() throws IOException {
		Path src = Paths.get("Sample/SampleFile.sample").toAbsolutePath();
		Path dest = Paths.get("TestDest/").toAbsolutePath();
		
		File expected = Paths.get("TestDest/SampleFile.sample").toAbsolutePath().toFile();
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(SM);
		c.addToCopy(src, dest, Copiable.COPY_MODE);
		c.copyAll();
		
		assertTrue(expected.exists());
	}
	
	@Test
	void testCopyFolder() throws IOException {
		Path src = Paths.get("Sample/").toAbsolutePath();
		Path dest = Paths.get("TestDest/").toAbsolutePath();
		
		File expected = Paths.get("TestDest/Sample/SampleSubFolder").toAbsolutePath().toFile();
		File expected2 = Paths.get("TestDest/Sample/SampleFile.sample").toAbsolutePath().toFile();
		File expected3 = Paths.get("TestDest/Sample/SampleFile2.sample").toAbsolutePath().toFile();
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(SM);
		c.addToCopy(src, dest, Copiable.COPY_MODE);
		c.copyAll();
		
		assertTrue(expected.exists());
		assertTrue(expected2.exists());
		assertTrue(expected3.exists());
	}

}
