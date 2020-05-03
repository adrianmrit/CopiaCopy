package utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestTimerFormater {

	@Test
	void test() {
		int expectedS = 11;
		int expectedM = 23;
		int expectedH = 25;
		long time = expectedS + expectedM*60 + expectedH*3600;
		TimerFormater t = new TimerFormater(time);
		assertEquals("25:23:11", t.toString());
	}
	
	@Test
	void test2() {
		int expectedS = 0;
		int expectedM = 23;
		int expectedH = 25;
		long time = expectedS + expectedM*60 + expectedH*3600;
		TimerFormater t = new TimerFormater(time);
		assertEquals("25:23:00", t.toString());
	}
	
	@Test
	void test3() {
		int expectedS = 11;
		int expectedM = 23;
		int expectedH = 00;
		long time = expectedS + expectedM*60 + expectedH*3600;
		TimerFormater t = new TimerFormater(time);
		assertEquals("00:23:11", t.toString());
	}

}
