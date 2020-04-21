package copy;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class TestCopy {
	@Test
	void testCopyFile() throws IOException {
		Path src = Paths.get("Sample/SampleFile.sample").toAbsolutePath();
		Path dest = Paths.get("TestDest/").toAbsolutePath();
		File expected = Paths.get("TestDest/SampleFile.sample").toAbsolutePath().toFile();
		Copy c = new Copy(src, dest, new LinkedFileList());
		c.copy();
		
		assertTrue(expected.exists());
		
		expected.delete();
		dest.toFile().delete();
	}
	
	@Test
	void testCopyFolder() throws IOException {
		Path src = Paths.get("Sample/").toAbsolutePath();
		Path dest = Paths.get("TestDest/").toAbsolutePath();
		File expected = Paths.get("TestDest/Sample/SampleSubFolder").toAbsolutePath().toFile();
		File expected2 = Paths.get("TestDest/Sample/SampleFile.sample").toAbsolutePath().toFile();
		File expected3 = Paths.get("TestDest/Sample/SampleFile2.sample").toAbsolutePath().toFile();
		Copy c = new Copy(src, dest, new LinkedFileList());
		c.copy();
		
		assertTrue(expected.exists());
		assertTrue(expected2.exists());
		assertTrue(expected3.exists());
		
		expected.delete();
		expected2.delete();
		expected3.delete();
		dest.toFile().delete();
	}

}
