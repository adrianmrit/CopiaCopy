package copy;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

class TestLinkedFile {

	@Test
	void testEmpty() {
		
		assertEquals(0, new LinkedFileList().count());
	}
	
	@Test // TODO: Figure out how to test
	void test() throws IOException {
		LinkedFileList list = new LinkedFileList();
		Path fPath = Paths.get("Sample").toAbsolutePath();
		Path rootDest = Paths.get("TestDest").toAbsolutePath();
		list.load(fPath.toFile(), rootDest);
		
		while(list.hasNext()) {
			System.out.println(list.getNext());
			list.getNext().setCopied();
		}
		
		list.reset();
	}

}
