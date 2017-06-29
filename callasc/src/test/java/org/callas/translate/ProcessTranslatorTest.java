package org.callas.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.*;

import org.callas.absyn.processes.*;
import org.callas.absyn.types.CodeType;
import org.callas.vm.ast.*;
import org.junit.Test;
import org.tyco.common.symbol.Symbol;

public class ProcessTranslatorTest {

	@Test
	public void testCaseBBool() {
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(new BBool(true));
		syms.add(new BLong(1));
		syms.add(new BString("foo"));
		syms.add(new BDouble(1.0f));
		
		ProcessTranslator trans = new ProcessTranslator(null, syms);
		List<CVMStmt> stmts = trans.compile(new BBool(true));
		assertEquals(Arrays.asList(new CVMLoadConstant((byte) 0)), stmts);
	}

	@Test
	public void testCaseBFloat() {
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(new BBool(true));
		syms.add(new BLong(1));
		syms.add(new BString("foo"));
		syms.add(new BDouble(1.0f));
		
		ProcessTranslator trans = new ProcessTranslator(null, syms);
		List<CVMStmt> stmts = trans.compile(new BDouble(1.0f));
		assertEquals(Arrays.asList(new CVMLoadConstant((byte) 3)), stmts);
	}

	@Test
	public void testCaseBInt() {
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(new BBool(true));
		syms.add(new BLong(1));
		syms.add(new BString("foo"));
		syms.add(new BDouble(1.0f));
		
		ProcessTranslator trans = new ProcessTranslator(null, syms);
		List<CVMStmt> stmts = trans.compile(new BLong(1));
		assertEquals(Arrays.asList(new CVMLoadConstant((byte) 1)), stmts);
	}

	@Test
	public void testCaseBString() {
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(new BBool(true));
		syms.add(new BLong(1));
		syms.add(new BString("foo"));
		syms.add(new BDouble(1.0f));
		
		ProcessTranslator trans = new ProcessTranslator(null, syms);
		List<CVMStmt> stmts = trans.compile(new BString("foo"));
		assertEquals(Arrays.asList(new CVMLoadConstant((byte) 2)), stmts);
	}

	@Test
	public void testCaseBranch() {
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(new BBool(true));
		syms.add(Code.NIL);
		Branch proc = new Branch(new BBool(true), LoadSensorCode.LOAD, Receive.RECEIVE);
		ProcessTranslator trans = new ProcessTranslator(null, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoadConstant((byte) 0),
				new CVMIfTrue(1 + 2 + 1 + 5), // sizeof(RECEIVE) + sizeof(LDC 1) + sizeof(LDM) + sizeof(GOTO 1) 
				CVMReceive.RECEIVE,
				new CVMLoadConstant((byte) 1),
				CVMLoadModule.LOADM,
				new CVMGoto(1),
				CVMLoadSensorCode.LOADB
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void testCaseCall() {
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(new BString("bar"));
		syms.add(new BLong(1));
		syms.add(new BDouble(1.0f));
		syms.add(Code.NIL);
		CallasProcess proc = new Call(Code.NIL, "bar", new BLong(1), new BDouble(1.0f));
		List<Code> mods = Arrays.asList(Code.NIL);
		ProcessTranslator trans = new ProcessTranslator(null, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoadConstant((byte) 2), // 1.0f
				new CVMLoadConstant((byte) 1), // 1
				new CVMLoadConstant((byte) 3), // nil
				CVMLoadModule.LOADM, // nil
				new CVMLoadConstant((byte) 0), // "bar"
				CVMCall.CALL
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void testCaseCode() {
		LinkedList<Symbol> vars = new LinkedList<Symbol>();
		vars.add(Symbol.symbol("y"));
		vars.add(Symbol.symbol("x"));
		vars.add(Symbol.symbol("foo"));
		
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(new BString("bar"));
		syms.add(new BLong(1));
		syms.add(new BDouble(1.0f));
		syms.add(new BString("foo"));
		syms.add(Code.NIL);
		
		Map<Symbol, ProcessAbstraction> procs = new TreeMap<Symbol, ProcessAbstraction>();
		procs.put(Symbol.symbol("bar"), new ProcessAbstraction(new Let("z", new Variable("x"), new Variable("y")), "self"));
		procs.put(Symbol.symbol("foo"), new ProcessAbstraction(new Variable("x"), "self", "x"));
		CallasProcess proc = new Code(procs , CodeType.NIL_TYPE);
		
		Map<Symbol, ProcessAbstraction> procs2 = new TreeMap<Symbol, ProcessAbstraction>();
		procs2.put(Symbol.symbol("bar"), new ProcessAbstraction(new Let("z", new Variable("x"), new Variable("y")), "self"));
		procs2.put(Symbol.symbol("foo"), new ProcessAbstraction(new Variable("x"), "self", "x"));
		CallasProcess mod2 = new Code(procs , CodeType.NIL_TYPE);
		
		syms.add((Code) mod2);

		ProcessTranslator trans = new ProcessTranslator(vars, syms);
		List<CVMStmt> stmts = Arrays.asList(
				// translate foo
				new CVMLoad((byte) 0), // load y
				new CVMLoad((byte) 1), // load x
				new CVMLoadConstant((byte) 0), // "bar"
				// translate bar
				new CVMLoadConstant((byte) 3), // "foo"
				// load m2
				new CVMLoadConstant((byte) 5),
				CVMLoadModule.LOADM
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void testCaseExtern() {
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(new BString("bar"));
		syms.add(new BLong(1));
		syms.add(new BDouble(1.0f));
		CallasProcess proc = new Extern("bar", new BLong(1), new BDouble(1.0f));
		List<Code> mods = Arrays.asList();
		ProcessTranslator trans = new ProcessTranslator(null, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoadConstant((byte) 2), // 1.0f
				new CVMLoadConstant((byte) 1), // 1
				new CVMLoadConstant((byte) 0), // "bar"
				CVMExtern.EXTERN
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void testCaseUpdate() {
		LinkedList<Symbol> vars = new LinkedList<Symbol>();
		vars.add(Symbol.symbol("y"));
		vars.add(Symbol.symbol("x"));
		vars.add(Symbol.symbol("foo"));
		
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		
		CallasProcess proc = new Update(new Variable("x"), new Variable("y"));
		List<Code> mods = Arrays.asList(Code.NIL);
		ProcessTranslator trans = new ProcessTranslator(vars, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoad((byte) 0), // y
				new CVMLoad((byte) 1), // x
				CVMUpdate.UPDATE
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void testCaseLet() {
		LinkedList<Symbol> vars = new LinkedList<Symbol>();
		vars.add(Symbol.symbol("y"));
		vars.add(Symbol.symbol("x"));
		vars.add(Symbol.symbol("foo"));
		
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		
		CallasProcess proc = new Let("z", new Variable("x"), new Variable("z"));
		
		List<Code> mods = Arrays.asList();
		ProcessTranslator trans = new ProcessTranslator(vars, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoad((byte) 1), // x
				new CVMStore((byte) 3),
				new CVMLoad((byte) 3) // z
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void testCaseLoadSensorCode() {
		LinkedList<Symbol> vars = new LinkedList<Symbol>();
		vars.add(Symbol.symbol("y"));
		vars.add(Symbol.symbol("x"));
		vars.add(Symbol.symbol("foo"));
		
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		
		CallasProcess proc = LoadSensorCode.LOAD;
		
		List<Code> mods = Arrays.asList();
		ProcessTranslator trans = new ProcessTranslator(vars, syms);
		List<CVMStmt> stmts = Arrays.asList((CVMStmt)
				CVMLoadSensorCode.LOADB
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void testCaseReceive() {
		LinkedList<Symbol> vars = new LinkedList<Symbol>();
		vars.add(Symbol.symbol("y"));
		vars.add(Symbol.symbol("x"));
		vars.add(Symbol.symbol("foo"));
		
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(Code.NIL);
		
		CallasProcess proc = Receive.RECEIVE;
		
		ProcessTranslator trans = new ProcessTranslator(vars, syms);
		List<CVMStmt> stmts = Arrays.asList(
				CVMReceive.RECEIVE,
				new CVMLoadConstant((byte) 0),
				CVMLoadModule.LOADM
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void testCaseSend() {
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(new BString("ch"));
		syms.add(new BString("bar"));
		syms.add(new BLong(1));
		syms.add(new BDouble(1.0f));
		syms.add(Code.NIL);
		CallasProcess proc = new Send(new Variable("ch"), "bar", new BLong(1), new BDouble(1.0f));
		ProcessTranslator trans = new ProcessTranslator(null, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoadConstant((byte) 3), // 1.0f
				new CVMLoadConstant((byte) 2), // 1
				new CVMLoadConstant((byte) 1), // "bar"
				new CVMLoadConstant((byte) 0), // "ch"
				CVMSend.SEND,
				new CVMLoadConstant((byte) 3), // "bar"
				CVMLoadModule.LOADM
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void testCaseStoreSensorCode() {
		LinkedList<Symbol> vars = new LinkedList<Symbol>();
		vars.add(Symbol.symbol("y"));
		vars.add(Symbol.symbol("x"));
		vars.add(Symbol.symbol("foo"));
		
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(Code.NIL);
		
		CallasProcess proc = new StoreSensorCode(new Variable("foo"));
		
		List<Code> mods = Arrays.asList(Code.NIL);
		ProcessTranslator trans = new ProcessTranslator(vars, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoad((byte) 2),
				CVMStoreSensorCode.STOREB,
				new CVMLoadConstant((byte) 0),
				CVMLoadModule.LOADM
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void testCaseTimer() {
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(new BDouble(1.0f));
		syms.add(new BLong(1));
		syms.add(new BString("bar"));
		syms.add(new BString("ch"));
		syms.add(Code.NIL);
		CallasProcess proc = new Send(new Variable("ch"), "bar", new BLong(1), 
				new BDouble(1.0f));
		ProcessTranslator trans = new ProcessTranslator(null, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoadConstant((byte) 0), // 1.0f
				new CVMLoadConstant((byte) 1), // 1
				new CVMLoadConstant((byte) 2), // "bar"
				new CVMLoadConstant((byte) 3), // "ch"
				CVMSend.SEND,
				new CVMLoadConstant((byte) 4), // nil
				CVMLoadModule.LOADM
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void testCaseVariableVariable() {
		LinkedList<Symbol> vars = new LinkedList<Symbol>();
		vars.add(Symbol.symbol("a1"));
		vars.add(Symbol.symbol("a2"));
		ProcessTranslator trans = new ProcessTranslator(vars, null);
		List<CVMStmt> stmts = trans.compile(new Variable("a2"));
		assertEquals(Arrays.asList(new CVMLoad((byte) 1)), stmts);
	}

}
