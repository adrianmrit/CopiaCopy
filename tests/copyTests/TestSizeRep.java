package copyTests;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import utils.SizeRep;

class TestSizeRep {

	@Test
	void testEmptyFileSize() {
		assertEquals(0, SizeRep.toKB(0));
		assertEquals(0, SizeRep.toMB(0));
		assertEquals(0, SizeRep.toGB(0));
		assertEquals(0, SizeRep.toTB(0));
	}
	
	@Test
	void testFileSizeKB() {
		long size = FileUtils.ONE_KB;
		assertEquals(1, SizeRep.toKB(size));
	}
	
	@Test
	void testFileSizeMB() {
		long size = FileUtils.ONE_MB;
		assertEquals(1, SizeRep.toMB(size));
	}
	
	@Test
	void testFileSizeGB() {
		long size = FileUtils.ONE_GB;
		assertEquals(1, SizeRep.toGB(size));
	}
	
	@Test
	void testFileSizeTB() {
		long size = FileUtils.ONE_TB;
		assertEquals(1, SizeRep.toTB(size));
	}
	
	@Test
	void testBestIntValFactor() {
		long size = (long) Math.pow(10, 12);
		assertEquals(Math.pow(10, 3), SizeRep.bestIntValFactor(size));
	}
	
	@Test
	void testReadable() {
		long size = 25 * 1024 + 500; // 25.49 KB
		assertEquals("25.49 KB", SizeRep.readable(size));
		
		size = 25 * 1024 * 1024 + 500 * 1024; // 25.49 MB
		assertEquals("25.49 MB", SizeRep.readable(size));
	}

}
