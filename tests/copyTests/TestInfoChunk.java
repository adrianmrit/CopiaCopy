package copyTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import copy.InfoChunk;
import copy.InfoChunkMap;

class TestInfoChunk {

	@Test
	void test() {
		InfoChunk chunk = new InfoChunk("sample", 10l);
		assertEquals(Long.class, chunk.getValue().getClass());
		long value = chunk.getValue();
		assertEquals(10l, value);

		assertEquals(Long.class, chunk.getType());
		try {
			@SuppressWarnings("unused")
			String value2 = chunk.getValue();
			fail();
		} catch (ClassCastException e) {
			
		}
	}
	
	@Test
	void testInfoChunkMap() {
		InfoChunkMap map = new InfoChunkMap();
		map.createChunk("Sample", 10l);
		long value = map.getChunkValue("Sample");
		assertEquals(10l, value);
	}

}
