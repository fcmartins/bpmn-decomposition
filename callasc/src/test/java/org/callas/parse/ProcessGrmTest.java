package org.callas.parse;

import static org.callas.absyn.types.CodeType.NIL_TYPE;
import static org.callas.absyn.types.BLongType.LONG_TYPE;
import static org.callas.absyn.types.BStringType.STRING_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.*;

import org.callas.absyn.processes.*;
import org.callas.absyn.processes.Timer;
import org.callas.absyn.types.FunctionType;
import org.callas.core.IFileLoader;
import org.junit.Before;
import org.junit.Test;
import org.tyco.common.errorMsg.*;
import org.tyco.common.symbol.Symbol;

/**
 * Tests the process parser.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class ProcessGrmTest {
	private static final String INDENT = " ";

	private static final String lines(int level, Object... lines) {
		String result = "";
		for (Object line : lines) {
			if (line instanceof String) {
				for (int i = 0; i < level; i++) {
					result += INDENT;
				}
				result += line + "\n";
			} else {
				result += lines(level + 1, (Object[]) line);
			}
		}
		return result;
	}

	public static final String lines(Object... lines) {
		return lines(0, lines);
	}

	/* call */

	@Test
	public void callWithSpaceBefore() {
		assertParses(new Call(new Variable("x"), "f"), "  x.f()", true);
	}

	@Test
	public void extern() {
		Extern proc = new Extern("l");
		assertParses(proc, "extern l()", true);
	}

	@Test
	public void call() {
		Call proc = new Call(new Variable("v"), Symbol.symbol("l"),
				new LinkedList<CallasValue>());
		assertParses(proc, "v.l()");
	}

	@Test
	public void callOneParam() {
		List<CallasValue> params = CallasValue.asList(new Variable("v1"));
		Call proc = new Call(new Variable("v"), Symbol.symbol("l"), params);
		assertParses(proc, "v.l(v1)");
	}

	@Test
	public void callTwoParams() {
		List<CallasValue> params = CallasValue.asList(new Variable("v1"),
				new BLong(2));
		Call proc = new Call(new Variable("v"), Symbol.symbol("l"), params);
		assertParses(proc, "v.l(v1, 2)");
	}

	/* timed-call */

	@Test
	public void timerWithSpaceBefore() {
		assertParses(new Timer(new Variable("v"), "f", new BLong(1)),
				" v.f() every 1");
	}

	@Test
	public void timer() {
		CallasProcess proc = new Timer(new Variable("v"), "l", new BLong(1));
		assertParses(proc, "v.l() every 1");
	}

	@Test
	public void timerOneParam() {
		CallasProcess proc = new Timer(new Variable("v"), "l", new BLong(1), new Variable("v1"));
		assertParses(proc, "v.l(v1) every 1");
	}

	@Test
	public void timerTwoParams() {
		CallasProcess proc = new Timer(new Variable("v"), "l", new BLong(1), 
				new Variable("v1"), new BLong(2));
		assertParses(proc, "v.l(v1, 2) every 1");
	}
	
	@Test
	public void kill() {
		Kill proc = new Kill(new Variable("x"));
		assertParses(proc, "kill x");
	}
	
	@Test
	public void open() {
		Open proc = new Open(new Variable("x"));
		assertParses(proc, "open x");
	}
	
	@Test
	public void close() {
		Close proc = new Close(new Variable("x"));
		assertParses(proc, "close x");
	}

	/* transmission */

	@Test
	public void send() {
		Send proc = new Send(new Variable("ch"),"y");
		assertParses(proc, "select ch send y()", true);
	}

	@Test
	public void sendTwoParams() {
		Send proc = new Send(new Variable("ch"), "y", new Variable("v1"), 
				new Variable("v2"));
		assertParses(proc, "select ch send y(v1, v2)");
	}

	/* install module */

	@Test
	public void update() {
		Update proc = new Update(new Variable("x"), new Variable("y"));
		assertParses(proc, "x || y", true);
	}

	/* new variable */

	@Test
	public void letXinY() {
		Let proc = new Let("x", new Variable("y"), new Variable("x"));
		assertParses(proc, "x = y\n", true);

	}

	@Test
	public void letXinYReturnY() {
		Let proc = new Let("x", new Variable("y"), new Variable("y"));
		assertParses(proc, lines(" x = y", " y"));

	}

	@Test
	public void letXinYReturn1() {
		Let proc = new Let("x", new Variable("y"), new BLong(1));
		assertParses(proc, lines(" x = y", " 1"));

	}
	
	/* branch */
	
	@Test
	public void branch() {
		Branch proc = new Branch(new BLong(1), new BLong(2), new BLong(3));
		assertParses(proc, lines("if 1:", " 2", "else:", " 3"));
	}
	
	/* store sensor code */
	
	@Test
	public void store() {
		StoreSensorCode proc = new StoreSensorCode(new BLong(1));
		assertParses(proc, "store 1");
	}
	
	/* load sensor code */
	
	@Test
	public void load() {
		LoadSensorCode proc = new LoadSensorCode(null);
		assertParses(proc, "load");
	}

	/* module */

	@Test
	public void moduleSimpleProcessInline() {
		Map<Symbol, ProcessAbstraction> map = new HashMap<Symbol, ProcessAbstraction>();
		map.put(Symbol.symbol("v"), new ProcessAbstraction(new BLong(1)));

		Map<Symbol, FunctionType> mapType = new HashMap<Symbol, FunctionType>();
		mapType.put(Symbol.symbol("v"), new FunctionType(LONG_TYPE));

		Code mod = new Code(map, NIL_TYPE);
		Let x = new Let("x", mod, new Variable("x"));
		assertParses(x, lines(" defmodule Nil: pass", " module x of Nil:",
				"  def v(): 1"), true);
	}

	@Test
	public void moduleSimpleProcess() {
		Map<Symbol, ProcessAbstraction> map = new HashMap<Symbol, ProcessAbstraction>();
		map.put(Symbol.symbol("v"), new ProcessAbstraction(new BLong(1)));

		Map<Symbol, FunctionType> mapType = new HashMap<Symbol, FunctionType>();
		mapType.put(Symbol.symbol("v"), new FunctionType(LONG_TYPE));

		Code mod = new Code(map, NIL_TYPE);
		Let x = new Let("x", mod, new Variable("x"));
		assertParses(x, lines(" defmodule Nil: pass", " module x of Nil:",
				"  def v():", "   1"));
	}

	@Test
	public void moduleTwoFuncs() {
		Map<Symbol, ProcessAbstraction> map = new LinkedHashMap<Symbol, ProcessAbstraction>();
		map.put(Symbol.symbol("v"), new ProcessAbstraction(new BLong(1)));
		map.put(Symbol.symbol("l"), new ProcessAbstraction(new BString("foo")));

		Map<Symbol, FunctionType> mapType = new LinkedHashMap<Symbol, FunctionType>();
		mapType.put(Symbol.symbol("v"), new FunctionType(LONG_TYPE));
		mapType.put(Symbol.symbol("l"), new FunctionType(STRING_TYPE));

		Code mod = new Code(map, NIL_TYPE);
		Let x = new Let("x", mod, new Variable("x"));

		assertParses(x, lines(" defmodule Nil: pass", " module x of Nil:",
				"  def v():", "   1", "  def l():", "   \"foo\""));
	}

	@Test
	public void moduleOneProcWithPass() {
		Map<Symbol, ProcessAbstraction> map = new HashMap<Symbol, ProcessAbstraction>();
		map.put(Symbol.symbol("v"), new ProcessAbstraction(Code.NIL));

		Map<Symbol, FunctionType> mapType = new HashMap<Symbol, FunctionType>();
		mapType.put(Symbol.symbol("v"), new FunctionType(NIL_TYPE));

		Code mod = new Code(map, NIL_TYPE);
		Let x = new Let("x", mod, new Variable("x"));
		assertParses(x, lines(" defmodule Nil: pass", " module x of Nil:",
				"  def v():", "   pass\n"));
	}

	@Test
	public void moduleEmpty() {
		Map<Symbol, ProcessAbstraction> map = new HashMap<Symbol, ProcessAbstraction>();

		Code mod = new Code(map, NIL_TYPE);
		Let x = new Let("x", mod, new Variable("x"));
		assertParses(x, lines(" defmodule Nil: pass", " module x of Nil:",
				"  pass"));
	}

	@Test
	public void moduleEmpty2() {
		Map<Symbol, ProcessAbstraction> map = new HashMap<Symbol, ProcessAbstraction>();

		Code mod = new Code(map, NIL_TYPE);
		Let x = new Let("x", mod, new Variable("x"));
		assertParses(x, lines(" defmodule Nil: pass", " module x of Nil: pass"));
	}

	/* built-in values */

	@Test
	public void bInt() {
		BLong proc = new BLong(1);
		assertParses(proc, "1");
	}

	@Test
	public void bFloat() {
		BDouble proc = new BDouble(1);
		assertParses(proc, "1.0");
	}

	@Test
	public void bString() {
		BString proc = new BString("foo");
		assertParses(proc, "\"foo\"");
	}

	@Test
	public void bString2() {
		BString proc = new BString("foo\n\"\" \" bar");
		assertParses(proc, "\"\"\"foo\n\"\" \" bar\"\"\"");
	}

	@Test
	public void bString3() {
		CallasProcess proc = new Let(";", new BString("foo\n "), new BLong(1));
		assertParses(proc, " \"\"\"foo\n \"\"\"\n 1");
	}

	/* variable */

	@Test
	public void variable() {
		Variable proc = new Variable("foo");
		assertParses(proc, "foo");
	}

	/* receive */

	@Test
	public void receive() {
		CallasProcess proc = Receive.RECEIVE;
		assertParses(proc, "receive");
	}

	/* misc */

	@Test
	public void program1() {
		Map<Symbol, ProcessAbstraction> procs = new HashMap<Symbol, ProcessAbstraction>();
		procs.put(Symbol.symbol("l"), new ProcessAbstraction(new Variable("x"),
				"self"));
		Code module = new Code(procs, NIL_TYPE);
		Let letModule = new Let("m", module, new Call(new Variable("m"), "l"));
		Let proc = new Let("x", new BLong(1), letModule);

		assertParses(proc,
				"defmodule Nil: pass\n" +
				"x = 1\n" +
				"\n" +
				"module m of Nil:\n" +
				"  def l(self): x\n" +
				"\n"
				+ "m.l()\n");
	}

	@Test
	public void semiColonBB() {
		Let proc = new Let(";", new BLong(0), new BLong(1));

		assertParses(proc, "0; 1\n");
	}

	@Test
	public void semiColonLet() {
		Let proc = new Let("x", new Call(new Variable("l"), "v"), new BLong(1));

		assertParses(proc, "x = l.v(); 1\n");
	}

	@Test
	public void semiColonLetLetExtern() {
		Call call = new Call(new Variable("l"), "v");
		Let proc = new Let("x", call, new Let("y", new BLong(1), new BLong(0)));
		assertParses(proc, "x = l.v(); y = 1\n0\n");
	}

	/* utility functions */

	@Before
	public void resetFileLoader() {
		fileLoader = IFileLoader.STANDARD_IO;
	}
	
	private IFileLoader fileLoader;
	
	public CallasProcess parse(String source, boolean output,
			Class<?>... messagesCls) {
		IPrinter<Object> printer;
		if (output) {
			printer = new StreamPrinter<Object>(System.err);
		} else {
			printer = IPrinter.SILENT;
		}
		CallasProcess tree = null;
		try {
			CallasProcessParser parser = new CallasProcessParser(fileLoader);
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

	public void assertParses(CallasProcess expected, String sourceCode,
			boolean output, Class<?>... messagesCls) {
		CallasProcess parsed = parse(sourceCode, output, messagesCls);
		assertEquals(expected.toString(), parsed.toString());
	}

	public void assertParses(CallasProcess expected, String sourceCode,
			Class<?>... messagesCls) {
		assertParses(expected, sourceCode, false, messagesCls);
	}

}
