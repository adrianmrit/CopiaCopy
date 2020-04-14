package copy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
		long size = (long) Math.pow(10, 3);
		assertEquals(1, SizeRep.toKB(size));
	}
	
	@Test
	void testFileSizeMB() {
		long size = (long) Math.pow(10, 6);
		assertEquals(1, SizeRep.toMB(size));
	}
	
	@Test
	void testFileSizeGB() {
		long size = (long) Math.pow(10, 9);
		assertEquals(1, SizeRep.toGB(size));
	}
	
	@Test
	void testFileSizeTB() {
		long size = (long) Math.pow(10, 12);
		assertEquals(1, SizeRep.toTB(size));
	}
	
	@Test
	void testBestIntValFactor() {
		long size = (long) Math.pow(10, 12);
		assertEquals(Math.pow(10, 3), SizeRep.bestIntValFactor(size));
	}

}
