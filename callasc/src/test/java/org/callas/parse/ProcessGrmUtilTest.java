package org.callas.parse;

import static org.callas.parse.ProcessGrmUtil.fromModuleToFile;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.callas.absyn.processes.BLong;
import org.callas.absyn.processes.Let;
import org.callas.absyn.processes.Variable;
import org.junit.Test;

public class ProcessGrmUtilTest {

	private static void assertPathEquals(String expectedPath, String found) {
		assertEquals(expectedPath.replace("/", File.separator), found);
	}

	@Test
	public void subdir() {
		assertPathEquals("../foo/bar", fromModuleToFile(".foo.bar"));
	}

	@Test
	public void subsibdir() {
		assertPathEquals("../../foo/bar", fromModuleToFile("..foo.bar"));
	}

	@Test
	public void relmod() {
		assertPathEquals("../bar", fromModuleToFile(".bar"));
	}

	@Test
	public void mod() {
		assertPathEquals("bar", fromModuleToFile("bar"));
	}

	@Test
	public void submod() {
		assertPathEquals("foo/bar", fromModuleToFile("foo.bar"));
	}

	@Test
	public void bodyEmpty() {
		assertEquals(new BLong(0), ProcessGrmUtil.composeBody(new BLong(0)));
	}

	@Test
	public void bodyTwoValues() {
		assertEquals(new Let(";", new BLong(0), new BLong(1)), ProcessGrmUtil
				.composeBody(new BLong(0), new BLong(1)));
	}

	@Test
	public void bodySimpleAssign() {
		assertEquals(new Let("x", new BLong(1), new Variable("x")),
				ProcessGrmUtil.composeBody(new Assignment(new Variable("x"),
						new BLong(1)), new Variable("x")));
	}


	@Test
	public void bodySimpleAssign2() {
		Assignment assignment = new Assignment(new Variable("x"),
				new BLong(1));
		assertEquals(new Let("x", new BLong(1), new Variable("x")),
				ProcessGrmUtil.composeBody(assignment));
	}
}
