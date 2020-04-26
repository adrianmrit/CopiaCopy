package copyTests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import copy.Copiable;
import copy.LinkedFolder;

class TestLinkedFolder {

	@Test
	void test() {
		Path src = Paths.get("Sample/").toAbsolutePath();
		Path dest = Paths.get("TestDest/").toAbsolutePath();
		Copiable copiable = new LinkedFolder(src, src.getParent(), dest, null);
		
		Iterator<Path> it = null;
		try {
			it = Files.walk(src).iterator();
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
