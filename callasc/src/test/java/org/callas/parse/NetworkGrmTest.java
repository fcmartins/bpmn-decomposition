package org.callas.parse;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.callas.absyn.NetworkFile;
import org.callas.absyn.SourceValue;
import org.callas.core.IFileLoader;
import org.junit.Test;
import org.tyco.common.errorMsg.ErrorMessage;

/**
 * Tests the parser.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class NetworkGrmTest {
	private String lines(String... lines) {
		String result = "";
		for (String line : lines) {
			result += line + "\n";
		}
		return result;
	}

	private NetworkFile parse(List<ErrorMessage> errors, String parsed) {
		ByteArrayInputStream input = new ByteArrayInputStream(parsed.getBytes());
		NetworkGrm grm = new NetworkGrm(errors, null, input, IFileLoader.STANDARD_IO);
		try {
			return (NetworkFile) grm.parse().value;
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	private void assertParsesSensor(Map<String, String> expected, String parsed) {
		List<ErrorMessage> errors = new LinkedList<ErrorMessage>();
		NetworkFile file = parse(errors, parsed);
		assertEquals("Parsing failed.", Collections.EMPTY_LIST, errors);
		assertEquals("Zero or more than one sensors were parsed.", 1, file
				.getSensorSections().size());
		Map<String, String> map = new TreeMap<String, String>();
		for (Entry<String, SourceValue<String>> entry : file
				.getSensorSections().get(0).getMetadata()) {
			map.put(entry.getKey(), entry.getValue().getValue());
		}
		assertEquals("Sensor properties differ.", expected, map);
	}

	private void assertParsesGlobalProps(Map<String, String> expected,
			String parsed) {
		List<ErrorMessage> errors = new LinkedList<ErrorMessage>();
		NetworkFile file = parse(errors, parsed);
		assertEquals("Parsing failed.", Collections.EMPTY_LIST, errors);
		Map<String, String> map = new TreeMap<String, String>();
		for (Entry<String, SourceValue<String>> entry : file.getMetadata()) {
			map.put(entry.getKey(), entry.getValue().getValue());
		}
		assertEquals("Global properties differ.", expected, map);
	}

	@Test
	public void checkSimple() {
		String parsed = lines("sensor:", "  foo = bar", "  foo2     =   bar2 ");

		Map<String, String> expected = new HashMap<String, String>();
		expected.put("foo", "bar");
		expected.put("foo2", "bar2");
		assertParsesSensor(expected, parsed);
	}

	@Test
	public void checkMultiwordValueInInterface() {
		String parsed = lines("interface = continuous sampling - poll.calnet\n");

		Map<String, String> expected = new HashMap<String, String>();
		expected.put("interface", "continuous sampling - poll.calnet");
		assertParsesGlobalProps(expected, parsed);
	}

	@Test
	public void checkEscapedChars() {
		String parsed = lines("sensor:", "  foo = bar",
				"  foo2     = \\\\\\n\\#\\tbar2\\ # hello!");

		Map<String, String> expected = new HashMap<String, String>();
		expected.put("foo", "bar");
		expected.put("foo2", "\\\n#\tbar2 ");
		assertParsesSensor(expected, parsed);
	}

	@Test
	public void checkGlobal() {
		String parsed = lines("  foo = bar", "  foo2     =   bar2 ");

		Map<String, String> expected = new HashMap<String, String>();
		expected.put("foo", "bar");
		expected.put("foo2", "bar2");
		assertParsesGlobalProps(expected, parsed);
	}

}
