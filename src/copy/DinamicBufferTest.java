package copy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DinamicBufferTest {

	@Test
	void testDefaultBuffer() {
		DinamicBuffer dinamicBuffer = new DinamicBuffer();
		assertEquals(DinamicBuffer.DEFAULT_BUFFER, dinamicBuffer.getBuffer());
	}
	
	@Test
	void test() {
		DinamicBuffer dinamicBuffer = new DinamicBuffer();
		dinamicBuffer.recalculate(DinamicBuffer.DEFAULT_BUFFER, 5);
		
		int expected = DinamicBuffer.DEFAULT_BUFFER + DinamicBuffer.INCREMENT_SIZE;  // should increase the first time anyways
		assertEquals(expected, dinamicBuffer.getBuffer());
		
		dinamicBuffer.recalculate(DinamicBuffer.DEFAULT_BUFFER + 1024, 5);  // ratio greater than last time
		expected = DinamicBuffer.DEFAULT_BUFFER + (DinamicBuffer.INCREMENT_SIZE*2); // should increase again
		assertEquals(expected, dinamicBuffer.getBuffer());
		
		dinamicBuffer.recalculate(DinamicBuffer.DEFAULT_BUFFER + 2048, 10); // ratio smaller than last time
		expected = DinamicBuffer.DEFAULT_BUFFER + (DinamicBuffer.INCREMENT_SIZE); //decreases buffer;
		assertEquals(expected, dinamicBuffer.getBuffer());
	}

}
