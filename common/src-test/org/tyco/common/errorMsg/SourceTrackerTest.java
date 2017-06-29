package org.tyco.common.errorMsg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;


public class SourceTrackerTest {
	SourceTracker t;

	public static void assertLineCol(int line, int col, SourceTracker t, int pos) {
		SourceLocation loc = t.getLocation(pos);
		assertEquals("line", line, loc.line);
		assertEquals("column", col, loc.col);
	}

	@Before()
	public void init() {
		t = new SourceTracker();
	}
	
	@Test
	public void empty() {
		try {
			t.getLocation(0);
			fail();
		} catch (Error e) {}
		
		try {
			t.getLocation(10);
			fail();
		} catch (Error e) {}

		try {
			t.getLocation(-10);
			fail();
		} catch (Error e) {}
	}
	
	@Test
	public void oneSource() {
		t = new SourceTracker();
		t.pushFile("foo", 0);
		
		try {
			t.getLocation(0);
			fail();
		} catch (Error e) {
			// ok
		}

		assertLineCol(1, 1, t, 1);
		assertLineCol(1, 2, t, 2);
		assertLineCol(1, 5, t, 5);
		assertLineCol(1, 10, t, 10);
		assertLineCol(1, 11, t, 11);
		assertLineCol(1, 15, t, 15);
		assertLineCol(1, 20, t, 20);
		assertLineCol(1, 21, t, 21);
		assertLineCol(1, 25, t, 25);

		t.newLine(10);
		
		assertLineCol(1, 1, t, 1);
		assertLineCol(1, 2, t, 2);
		assertLineCol(1, 5, t, 5);
		assertLineCol(1, 10, t, 10);
		assertLineCol(2, 1, t, 11);
		assertLineCol(2, 5, t, 15);
		assertLineCol(2, 10, t, 20);
		assertLineCol(2, 11, t, 21);
		assertLineCol(2, 15, t, 25);
		
		
		t.newLine(20);
		
		assertLineCol(1, 1, t, 1);
		assertLineCol(1, 2, t, 2);
		assertLineCol(1, 5, t, 5);
		assertLineCol(1, 10, t, 10);
		// new line
		assertLineCol(2, 1, t, 11);
		assertLineCol(2, 5, t, 15);
		assertLineCol(2, 10, t, 20);
		// new line
		assertLineCol(3, 1, t, 21);
		assertLineCol(3, 5, t, 25);
	}
	
	@Test
	public void threeSources() {
		t.pushFile("foo", 0);
		
		try {
			t.getLocation(0);
			fail();
		} catch (Error e) {
			// ok
		}

		assertLineCol(1, 1, t, 1);
		assertLineCol(1, 2, t, 2);
		assertLineCol(1, 5, t, 5);
		assertLineCol(1, 10, t, 10);
		assertLineCol(1, 11, t, 11);
		assertLineCol(1, 15, t, 15);
		assertLineCol(1, 20, t, 20);
		assertLineCol(1, 21, t, 21);
		assertLineCol(1, 25, t, 25);

		t.pushFile("foo", 5);
		t.newLine(10);
		
		assertLineCol(1, 1, t, 1);
		assertLineCol(1, 2, t, 2);
		assertLineCol(1, 5, t, 5);
		// new file
		assertLineCol(1, 1, t, 6);
		assertLineCol(1, 5, t, 10);
		// new line
		assertLineCol(2, 1, t, 11);
		assertLineCol(2, 5, t, 15);
		assertLineCol(2, 10, t, 20);
		assertLineCol(2, 11, t, 21);
		assertLineCol(2, 15, t, 25);
		
		
		t.pushFile("foo", 15);
		t.newLine(20);
		
		assertLineCol(1, 1, t, 1);
		assertLineCol(1, 2, t, 2);
		assertLineCol(1, 5, t, 5);
		// new file
		assertLineCol(1, 1, t, 6);
		assertLineCol(1, 5, t, 10);
		// new line
		assertLineCol(2, 1, t, 11);
		assertLineCol(2, 5, t, 15);
		// new file
		assertLineCol(1, 1, t, 16);
		assertLineCol(1, 5, t, 20);
		// new line
		assertLineCol(2, 1, t, 21);
		assertLineCol(2, 5, t, 25);
	}
	
	@Test
	public void mixedIncludes() {
		t.pushFile("foo", 5);
		t.pushFile("bar", 10);
		t.popFile(20);
		assertLineCol(1, 6, t, 21);
		assertLineCol(1, 7, t, 22);
		t.pushFile("bara", 30);
		t.popFile(40);
		// previous maintains
		assertLineCol(1, 6, t, 21);
		assertLineCol(1, 7, t, 22);
		// 10 lines after 
		assertLineCol(1, 16, t, 41);
		assertLineCol(1, 17, t, 42);
	}

	@Test
	public void mixedIncludesWithLines() {
		t.pushFile("foo", 5);
		t.newLine(9);
		t.pushFile("bar", 10);
		t.popFile(20);
		// the push file is the pos 10 (col 1)
		assertLineCol(2, 2, t, 21);
		assertLineCol(2, 3, t, 22);
		t.pushFile("bara", 30);
		t.popFile(40);
		// previous maintains
		assertLineCol(2, 2, t, 21);
		assertLineCol(2, 3, t, 22);
		// 10 lines after 
		assertLineCol(2, 12, t, 41);
		assertLineCol(2, 13, t, 42);
	}
}
