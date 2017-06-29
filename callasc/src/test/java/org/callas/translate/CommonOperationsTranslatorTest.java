package org.callas.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.callas.absyn.processes.*;
import org.callas.absyn.processes.BinaryOperator;
import org.callas.vm.ast.*;
import org.callas.vm.ast.UnaryOperator;
import org.junit.Test;
import org.tyco.common.symbol.Symbol;

public class CommonOperationsTranslatorTest {

	private static void runBinOp(BinaryOperator op, org.callas.vm.ast.BinaryOperator transOp) {
		LinkedList<Symbol> vars = new LinkedList<Symbol>();
		vars.add(Symbol.symbol("y"));
		vars.add(Symbol.symbol("x"));
		vars.add(Symbol.symbol("foo"));
		
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(Code.NIL);
		
		CallasProcess proc = new BinaryOperation(new Variable("x"), op, new Variable("y"));
		ProcessTranslator trans = new ProcessTranslator(vars, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoad((byte) 0), // y
				new CVMLoad((byte) 1), // x
				new CVMBinaryOp(transOp)
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}
	
	@Test
	public void boolAnd() {
		runBinOp(BinaryOperator.BOOL_AND, org.callas.vm.ast.BinaryOperator.BOOL_AND);
	}

	@Test
	public void boolExclusiveOr() {
		runBinOp(BinaryOperator.BOOL_EXCLUSIVE_OR, org.callas.vm.ast.BinaryOperator.BOOL_EXCLUSIVE_OR);
	}

	@Test
	public void boolOr() {
		runBinOp(BinaryOperator.BOOL_OR, org.callas.vm.ast.BinaryOperator.BOOL_OR);
	}

	@Test
	public void doubleAdd() {
		runBinOp(BinaryOperator.DOUBLE_ADDITION, org.callas.vm.ast.BinaryOperator.DOUBLE_ADDITION);
	}

	@Test
	public void doubleDiv() {
		runBinOp(BinaryOperator.DOUBLE_DIVISION, org.callas.vm.ast.BinaryOperator.DOUBLE_DIVISION);
	}

	@Test
	public void doubleGt() {
		runBinOp(BinaryOperator.DOUBLE_GREATER_THAN, org.callas.vm.ast.BinaryOperator.DOUBLE_GREATER_THAN);
	}

	@Test
	public void doubleLt() {
		runBinOp(BinaryOperator.DOUBLE_LESS_THAN, org.callas.vm.ast.BinaryOperator.DOUBLE_LESS_THAN);
	}

	@Test
	public void doubleMul() {
		runBinOp(BinaryOperator.DOUBLE_MULTIPLICATION, org.callas.vm.ast.BinaryOperator.DOUBLE_MULTIPLICATION);
	}

	@Test
	public void doubleSub() {
		runBinOp(BinaryOperator.DOUBLE_SUBTRACTION, org.callas.vm.ast.BinaryOperator.DOUBLE_SUBTRACTION);
	}

	@Test
	public void longAdd() {
		runBinOp(BinaryOperator.LONG_ADDITION, org.callas.vm.ast.BinaryOperator.LONG_ADDITION);
	}

	@Test
	public void longAnd() {
		runBinOp(BinaryOperator.LONG_AND, org.callas.vm.ast.BinaryOperator.LONG_AND);
	}

	@Test
	public void longDiff() {
		LinkedList<Symbol> vars = new LinkedList<Symbol>();
		vars.add(Symbol.symbol("y"));
		vars.add(Symbol.symbol("x"));
		vars.add(Symbol.symbol("foo"));
		
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(Code.NIL);
		
		CallasProcess proc = new BinaryOperation(new Variable("x"), BinaryOperator.LONG_DIFFERENT, new Variable("y"));
		ProcessTranslator trans = new ProcessTranslator(vars, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoad((byte) 0), // y
				new CVMLoad((byte) 1), // x
				new CVMBinaryOp(org.callas.vm.ast.BinaryOperator.LONG_EQUALS),
				new CVMUnaryOp(UnaryOperator.BOOL_NOT)
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void longDiv() {
		runBinOp(BinaryOperator.LONG_DIVISION, org.callas.vm.ast.BinaryOperator.LONG_DIVISION);
	}

	@Test
	public void longEq() {
		runBinOp(BinaryOperator.LONG_EQUALS, org.callas.vm.ast.BinaryOperator.LONG_EQUALS);
	}

	@Test
	public void longXor() {
		runBinOp(BinaryOperator.LONG_EXCLUSIVE_OR, org.callas.vm.ast.BinaryOperator.LONG_EXCLUSIVE_OR);
	}

	@Test
	public void longGt() {
		runBinOp(BinaryOperator.LONG_GREATER_THAN, org.callas.vm.ast.BinaryOperator.LONG_GREATER_THAN);
	}

	@Test
	public void longGte() {
		LinkedList<Symbol> vars = new LinkedList<Symbol>();
		vars.add(Symbol.symbol("y"));
		vars.add(Symbol.symbol("x"));
		vars.add(Symbol.symbol("foo"));
		
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(Code.NIL);
		
		CallasProcess proc = new BinaryOperation(new Variable("x"), BinaryOperator.LONG_GREATER_THAN_EQUALS, new Variable("y"));
		ProcessTranslator trans = new ProcessTranslator(vars, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoad((byte) 0), // y
				new CVMLoad((byte) 1), // x
				new CVMBinaryOp(org.callas.vm.ast.BinaryOperator.LONG_LESS_THAN),
				new CVMUnaryOp(UnaryOperator.BOOL_NOT)
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}

	@Test
	public void longLt() {
		runBinOp(BinaryOperator.LONG_LESS_THAN, org.callas.vm.ast.BinaryOperator.LONG_LESS_THAN);
	}

	@Test
	public void longMod() {
		runBinOp(BinaryOperator.LONG_MODULUS, org.callas.vm.ast.BinaryOperator.LONG_REMAINDER);
	}

	@Test
	public void longMul() {
		runBinOp(BinaryOperator.LONG_MULTIPLICATION, org.callas.vm.ast.BinaryOperator.LONG_MULTIPLICATION);
	}

	@Test
	public void longOr() {
		runBinOp(BinaryOperator.LONG_OR, org.callas.vm.ast.BinaryOperator.LONG_OR);
	}

	@Test
	public void longShl() {
		runBinOp(BinaryOperator.LONG_SHIFT_LEFT, org.callas.vm.ast.BinaryOperator.LONG_SHIFT_LEFT);
	}

	@Test
	public void longShr() {
		runBinOp(BinaryOperator.LONG_SHIFT_RIGHT, org.callas.vm.ast.BinaryOperator.LONG_SHIFT_RIGHT);
	}

	@Test
	public void longSub() {
		runBinOp(BinaryOperator.LONG_SUBTRACTION, org.callas.vm.ast.BinaryOperator.LONG_SUBTRACTION);
	}
	
	// unary
	
	private void runUnaryOp(org.callas.absyn.processes.UnaryOperator from, org.callas.vm.ast.UnaryOperator to) {
		LinkedList<Symbol> vars = new LinkedList<Symbol>();
		vars.add(Symbol.symbol("y"));
		vars.add(Symbol.symbol("x"));
		vars.add(Symbol.symbol("foo"));
		
		LinkedList<CallasValue> syms = new LinkedList<CallasValue>();
		syms.add(Code.NIL);
		
		CallasProcess proc = new UnaryOperation(from, new Variable("foo"));
		
		ProcessTranslator trans = new ProcessTranslator(vars, syms);
		List<CVMStmt> stmts = Arrays.asList(
				new CVMLoad((byte) 2),
				new CVMUnaryOp(to)
		);
		assertEquals(stmts.toString(), trans.compile(proc).toString());
	}
	
	@Test
	public void boolNot() {
		runUnaryOp(org.callas.absyn.processes.UnaryOperator.BOOL_NOT, UnaryOperator.BOOL_NOT);
	}
	
	@Test
	public void doubleNeg() {
		runUnaryOp(org.callas.absyn.processes.UnaryOperator.DOUBLE_NEGATION, UnaryOperator.DOUBLE_NEGATION);
	}
	
	@Test
	public void boolNeg() {
		runUnaryOp(org.callas.absyn.processes.UnaryOperator.LONG_NEGATION, UnaryOperator.LONG_NEGATION);
	}
	
	@Test
	public void longNot() {
		runUnaryOp(org.callas.absyn.processes.UnaryOperator.LONG_NOT, UnaryOperator.LONG_NOT);
	}
}
