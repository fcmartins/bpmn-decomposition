package org.callas.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import org.callas.absyn.types.*;
import org.callas.core.IFileLoader;
import org.junit.Before;
import org.junit.Test;
import org.tyco.common.errorMsg.*;
import org.tyco.common.symbol.Symbol;

public class TypeDefsTest {
	private IFileLoader fileLoader;
	
	@Before
	public void updateFileLoader() {
		fileLoader = IFileLoader.STANDARD_IO;
	}
	
	public Map<Symbol, RecursiveType> parse(String source, boolean output,
			Class<?>... messagesCls) {
		IPrinter<Object> printer;
		if (output) {
			printer = new StreamPrinter<Object>(System.err);
		} else {
			printer = IPrinter.SILENT;
		}
		Map<Symbol, RecursiveType> tree = null;
		try {
			TypeDefsParser parser = new TypeDefsParser(fileLoader);
			tree = parser.parse(source);
		} catch (ErrorMessagesException e1) {
			int index = 0;

			ErrorMessagePrinter errPrinter = new ErrorMessagePrinter(printer);
			for (ErrorMessage msg : e1.getErrors()) {
				errPrinter.print(msg);
			}

			for (ErrorMessage msg : e1.getErrors()) {
				try {
					if (!messagesCls[index].isInstance(msg)) {
						fail(String.format(
								"Expected class: <%s> Given class <%s>",
								messagesCls[index].getSimpleName(), msg
										.getClass().getSimpleName()));
					}
					index++;
				} catch (IndexOutOfBoundsException e) {
					fail(String.format(
							"Not expecting more errors but found <%s>", msg
									.getClass().getSimpleName()));
					break;
				}
			}
			assertEquals("More errors expected", messagesCls.length, e1
					.getErrors().size());
		}
		return tree;
	}

	public void assertParses(String name, RecursiveType expected, String sourceCode, boolean output) {
		Map<Symbol, RecursiveType> types = parse(sourceCode, output);
		RecursiveType recursiveType = types.get(Symbol.symbol(name));
		assertNotNull("Type not defined.", recursiveType);
		assertEquals(expected.toString(), recursiveType.toString());
	}
	public void assertParses(String name, RecursiveType expected, String sourceCode) {
		assertParses(name, expected, sourceCode, false);
	}
	
	public void assertParses(Map<Symbol, RecursiveType> expected, String sourceCode, boolean output) {
		Map<Symbol, RecursiveType> parsed = parse(sourceCode, output);
		assertEquals(new TreeMap<Symbol, RecursiveType>(expected).toString(), new TreeMap<Symbol, RecursiveType>(parsed).toString());
	}
	public void assertParses(Map<Symbol, RecursiveType> expected, String sourceCode) {
		assertParses(expected, sourceCode, false);
	}
	
	// Tests:
	
	@Test
	public void nil() {
		assertParses("Nil", new RecursiveType("Nil", CodeType.NIL_TYPE), "defmodule Nil: pass\n");
	}
	
	@Test
	public void usingNil() {
		Map<Symbol, RecursiveType> types = new TreeMap<Symbol, RecursiveType>();
		
		RecursiveType nil = new RecursiveType("Nil", CodeType.NIL_TYPE);
		types.put(Symbol.symbol("Nil"), nil);
		
		Map<Symbol, FunctionType> funcs = new TreeMap<Symbol, FunctionType>();
		funcs.put(Symbol.symbol("l"), new FunctionType(nil, new TypeVariable("X")));
		CodeType type = new CodeType(funcs);
		
		RecursiveType x = new RecursiveType("X", type);
		types.put(Symbol.symbol("X"), x);
		
		assertParses(types, "defmodule Nil: pass\n" +
				"defmodule X:\n" +
				" Nil l()\n");
	}

	@Test
	public void usingTypesWrongOrder() {
		Map<Symbol, RecursiveType> types = new TreeMap<Symbol, RecursiveType>();
		
		RecursiveType nil = new RecursiveType("Nil", CodeType.NIL_TYPE);
		types.put(Symbol.symbol("Nil"), nil);
		
		Map<Symbol, FunctionType> funcs = new TreeMap<Symbol, FunctionType>();
		funcs.put(Symbol.symbol("l"), new FunctionType(new TypeVariable("Nil"), new TypeVariable("X")));
		CodeType type = new CodeType(funcs);
		
		RecursiveType x = new RecursiveType("X", type);
		types.put(Symbol.symbol("X"), x);
		
		assertParses(types, 
				"defmodule X:\n" +
				" Nil l()\n" +
				"defmodule Nil: pass\n");
	}
		
	@Test
	public void extendingModules() {
		Map<Symbol, RecursiveType> types = new TreeMap<Symbol, RecursiveType>();
		
		RecursiveType nil = new RecursiveType("Nil", CodeType.NIL_TYPE);
		types.put(Symbol.symbol("Nil"), nil);
		
		Map<Symbol, FunctionType> funcs = new TreeMap<Symbol, FunctionType>();
		funcs.put(Symbol.symbol("l"), new FunctionType(nil, new TypeVariable("X"), new TypeVariable("X")));
		CallasType type = new CodeType(funcs);
		RecursiveType x = new RecursiveType("X", type);
		types.put(Symbol.symbol("X"), x);
		
		funcs = new TreeMap<Symbol, FunctionType>();
		funcs.put(Symbol.symbol("l"), new FunctionType(nil, new TypeVariable("Y"), new TypeVariable("Y")));
		type = new CodeType(funcs);
		RecursiveType y = new RecursiveType("Y", type);
		types.put(Symbol.symbol("Y"), y);
		
		assertParses(types, "defmodule Nil: pass\n" +
				"defmodule X:\n" +
				" Nil l(X other)\n" + 
				"defmodule Y(Nil, X): pass\n");
		
	}
	
	@Test
	public void closure() {
		Map<Symbol, RecursiveType> types = new TreeMap<Symbol, RecursiveType>();
		Map<Symbol, FunctionType> funcs = new TreeMap<Symbol, FunctionType>();
		funcs.put(Symbol.symbol("v"), new FunctionType(BLongType.LONG_TYPE, new TypeVariable("Sensor")));
		CodeType sensor = new CodeType(funcs);
		types.put(Symbol.symbol("Sensor"), new RecursiveType("Sensor", sensor));
		assertParses(types,
				"defmodule Sensor:\n" + 
				"  long v()\n" + 
				"\n" + 
				"\n");
	}
	
	@Test
	public void importTypeAsAnotherType() {
		fileLoader = new IFileLoader(){
			public InputStream loadFile(String format) {
				String data = "defmodule X:\n" +
						" long l()\n";
				return new ByteArrayInputStream(data.getBytes());
			}
		};
		
		Map<Symbol, RecursiveType> types = new TreeMap<Symbol, RecursiveType>();
		Map<Symbol, FunctionType> funcs = new TreeMap<Symbol, FunctionType>();
		funcs.put(Symbol.symbol("l"), new FunctionType(BLongType.LONG_TYPE, new TypeVariable("Y")));
		CodeType mod = new CodeType(funcs);
		types.put(Symbol.symbol("Y"), new RecursiveType("Y", mod));
		
		assertParses(types,
				"from foo import X as Y\n"
				);
	}
}
