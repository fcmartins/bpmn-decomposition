package org.callas.translate;

import static org.junit.Assert.*;

import java.util.*;

import org.callas.absyn.processes.Code;
import org.callas.vm.ast.*;
import org.junit.Test;
import org.tyco.common.symbol.Symbol;

public class TranslatorTest {

	@Test
	public void testCompile() {
		Translator trans = new Translator();
		
		CVMModule nil = new CVMModule(new HashMap<Symbol, CVMFunction>());
		
		Map<Symbol, CVMFunction> funcs = new HashMap<Symbol, CVMFunction>();
		List<CVMStmt> stmts = Arrays.asList(new CVMLoadConstant((byte) 0), CVMLoadModule.LOADM, CVMReturn.RETURN);
		LinkedList<CVMValue> constants = new LinkedList<CVMValue>();
		constants.add(nil);
		CVMFunction func = new CVMFunction((byte)1, (byte)0, (byte)1, stmts, constants);
		funcs.put(Symbol.symbol("run"), func);
		CVMModule mod = new CVMModule(funcs );
		assertEquals(mod.toString(), trans.compile(Code.NIL).toString());
	}

}
