package org.tyco.common.errorMsg;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class SourceDescriptorTest {
	SourceDescription s;

	@Test
	public void empty() {
		s = new SourceDescription("foo", 0);
		assertEquals("foo", s.filename);
		assertEquals(Integer.MAX_VALUE, s.end);
		assertEquals(0, s.start);
		assertEquals(0, s.lines.size());
		assertTrue(s.contains(1));
		assertTrue(s.contains(2));
		assertFalse(s.contains(0));
		assertTrue(s.contains(Integer.MAX_VALUE));
	}
	
	public static void assertLineCol(int line, int col, SourceDescription s, int pos) {
		int[] linecol = s.getLineCol(pos);
		assertEquals("line", line, linecol[0]);
		assertEquals("column", col, linecol[1]);
	}
	
	public static <T> void sortedSetEquals(SortedSet<T> target, T...expected) {
		TreeSet<T> src = new TreeSet<T>();
		for (T t: expected) {
			src.add(t);
		}
		assertEquals("sets", src, target);
	}
	
	@Test
	public void lines() {
		s = new SourceDescription("bar", 0);

		try {
			s.getLineCol(0);
			fail();
		} catch (AssertionError e) {
			// ok
		}

		assertLineCol(1, 1, s, 1);
		assertLineCol(1, 2, s, 2);
		assertLineCol(1, 5, s, 5);
		assertLineCol(1, 10, s, 10);
		assertLineCol(1, 11, s, 11);
		assertLineCol(1, 15, s, 15);
		assertLineCol(1, 20, s, 20);
		assertLineCol(1, 21, s, 21);
		assertLineCol(1, 25, s, 25);

		s.newLine(10);
		sortedSetEquals(s.lines, 10);
		
		assertLineCol(1, 1, s, 1);
		assertLineCol(1, 2, s, 2);
		assertLineCol(1, 5, s, 5);
		assertLineCol(1, 10, s, 10);
		// new line
		assertLineCol(2, 1, s, 11);
		assertLineCol(2, 5, s, 15);
		assertLineCol(2, 10, s, 20);
		assertLineCol(2, 11, s, 21);
		assertLineCol(2, 15, s, 25);
		
		
		s.newLine(20);
		sortedSetEquals(s.lines, 10, 20);
		
		assertLineCol(1, 1, s, 1);
		assertLineCol(1, 2, s, 2);
		assertLineCol(1, 5, s, 5);
		assertLineCol(1, 10, s, 10);
		// new line
		assertLineCol(2, 1, s, 11);
		assertLineCol(2, 5, s, 15);
		assertLineCol(2, 10, s, 20);
		// new line
		assertLineCol(3, 1, s, 21);
		assertLineCol(3, 5, s, 25);
	}
}
