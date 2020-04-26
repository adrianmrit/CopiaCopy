package copy;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import buffer.Buffer;
import buffer.StaticBuffer;

class TestCopy {
	@Test
	void testCopyFile() throws IOException {
		Path src = Paths.get("Sample/SampleFile.sample").toAbsolutePath();
		Path dest = Paths.get("TestDest/").toAbsolutePath();
		
		FileUtils.deleteDirectory(dest.toFile()); // delete before tests
		
		File expected = Paths.get("TestDest/SampleFile.sample").toAbsolutePath().toFile();
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(src, dest, SM);
		c.copy();
		
		assertTrue(expected.exists());
		
		FileUtils.deleteDirectory(dest.toFile());
	}
	
	@Test
	void testCopyFolder() throws IOException {
		Path src = Paths.get("Sample/").toAbsolutePath();
		Path dest = Paths.get("TestDest/").toAbsolutePath();
		
		FileUtils.deleteDirectory(dest.toFile()); // delete before tests
		
		File expected = Paths.get("TestDest/Sample/SampleSubFolder").toAbsolutePath().toFile();
		File expected2 = Paths.get("TestDest/Sample/SampleFile.sample").toAbsolutePath().toFile();
		File expected3 = Paths.get("TestDest/Sample/SampleFile2.sample").toAbsolutePath().toFile();
		CopiableList CL = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(CL, buffer);
		Copy c = new Copy(src, dest, SM);
		c.copy();
		
		assertTrue(expected.exists());
		assertTrue(expected2.exists());
		assertTrue(expected3.exists());
		
		FileUtils.deleteDirectory(dest.toFile());
	}

}
