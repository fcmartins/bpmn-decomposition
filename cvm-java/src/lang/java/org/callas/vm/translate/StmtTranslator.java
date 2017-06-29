package org.callas.vm.translate;

import java.util.*;

import org.callas.vm.Opcodes;
import org.callas.vm.ast.*;
import org.tyco.common.api.ICompiler;

public class StmtTranslator implements ICompiler<CVMStmt, List<Byte>> {

	private static final Map<CVMStmt, Byte> CACHE;
	static {
		HashMap<CVMStmt, Byte> cache = new HashMap<CVMStmt, Byte>();
		cache.put(CVMTimer.TIMER, Opcodes.TMR);
		cache.put(CVMSwap.SWAP, Opcodes.SWAP);
		cache.put(CVMDup.DUP, Opcodes.DUP);
		cache.put(CVMCall.CALL, Opcodes.CALL);
		cache.put(CVMClose.CLOSE, Opcodes.CLS);
		cache.put(CVMExtern.EXTERN, Opcodes.SYS);
		cache.put(CVMKill.KILL, Opcodes.KILL);
		cache.put(CVMOpen.OPEN, Opcodes.OPEN);
		cache.put(CVMPop.POP, Opcodes.POP);
		cache.put(CVMReceive.RECEIVE, Opcodes.RCV);
		cache.put(CVMUpdate.UPDATE, Opcodes.UPD);
		cache.put(CVMReturn.RETURN, Opcodes.RET);
		cache.put(CVMSend.SEND, Opcodes.SND);
		cache.put(CVMLoadSensorCode.LOADB, Opcodes.LDB);
		cache.put(CVMStoreSensorCode.STOREB, Opcodes.STB);
		CACHE = Collections.unmodifiableMap(cache);
	}

	private static List<Byte> one(Byte value) {
		List<Byte> result = new LinkedList<Byte>();
		result.add(value);
		return result;
	}

	public List<Byte> compile(CVMStmt from) {
		if (CACHE.containsKey(from)) {
			return one(CACHE.get(from));
		}
		if (from instanceof CVMBinaryOp) {
			BinaryOperator oper = ((CVMBinaryOp) from).operator;
			switch (oper) {
			case BOOL_AND:
				return one(Opcodes.BAND);
			case BOOL_EXCLUSIVE_OR:
				return one(Opcodes.BXOR);
			case BOOL_OR:
				return one(Opcodes.BOR);
			case DOUBLE_ADDITION:
				return one(Opcodes.DADD);
			case DOUBLE_DIVISION:
				return one(Opcodes.DDIV);
			case DOUBLE_EQUALS:
				return one(Opcodes.DE);
			case DOUBLE_GREATER_THAN:
				return one(Opcodes.DGT);
			case DOUBLE_LESS_THAN:
				return one(Opcodes.DLT);
			case DOUBLE_MULTIPLICATION:
				return one(Opcodes.DMUL);
			case DOUBLE_SUBTRACTION:
				return one(Opcodes.DSUB);
			case LONG_ADDITION:
				return one(Opcodes.LADD);
			case LONG_AND:
				return one(Opcodes.LAND);
			case LONG_DIVISION:
				return one(Opcodes.LDIV);
			case LONG_EQUALS:
				return one(Opcodes.LE);
			case LONG_EXCLUSIVE_OR:
				return one(Opcodes.LXOR);
			case LONG_GREATER_THAN:
				return one(Opcodes.LGT);
			case LONG_LESS_THAN:
				return one(Opcodes.LLT);
			case LONG_MULTIPLICATION:
				return one(Opcodes.LMUL);
			case LONG_OR:
				return one(Opcodes.LOR);
			case LONG_REMAINDER:
				return one(Opcodes.LREM);
			case LONG_SHIFT_LEFT:
				return one(Opcodes.LSHL);
			case LONG_SHIFT_RIGHT:
				return one(Opcodes.LSHR);
			case LONG_SUBTRACTION:
				return one(Opcodes.LSUB);
			default:
				throw new IllegalArgumentException("Binary op not supported: " + oper);
			}
		}
		if (from instanceof CVMUnaryOp) {
			UnaryOperator oper = ((CVMUnaryOp) from).operator;
			switch(oper) {
			case BOOL_NOT:
				return one(Opcodes.BNOT);
			case DOUBLE_NEGATION:
				return one(Opcodes.DNEG);
			case LONG_NEGATION:
				return one(Opcodes.LNEG);
			case LONG_NOT:
				return one(Opcodes.LNOT);
			default:
				throw new IllegalArgumentException("Unary op not supported: " + oper);
			}
		}
		if (from instanceof CVMGoto) {
			CVMGoto stmt = (CVMGoto) from;
			return two(Opcodes.GOTO, stmt.offset);
		} else if (from instanceof CVMIfTrue) {
			CVMIfTrue stmt = (CVMIfTrue) from;
			return two(Opcodes.IFT, stmt.offset);
		} else if (from instanceof CVMLoadConstant) {
			CVMLoadConstant stmt = (CVMLoadConstant) from;
			return Arrays.asList(Opcodes.LDC, stmt.index);
		} else if (from instanceof CVMLoadModule) {
			CVMLoadModule stmt = (CVMLoadModule) from;
			return Arrays.asList(Opcodes.LDM);
		} else if (from instanceof CVMLoad) {
			CVMLoad stmt = (CVMLoad) from;
			return Arrays.asList(Opcodes.LD, stmt.index);
		} else if (from instanceof CVMStore) {
			CVMStore stmt = (CVMStore) from;
			return Arrays.asList(Opcodes.ST, stmt.index);
		} else {
			throw new IllegalArgumentException("Stmt unsupported " + from);
		}
	}

	private List<Byte> two(byte opcode, int v) {
		return Arrays.asList(
		        new Byte(opcode),
				new Byte((byte) ((v >>> 24) & 0xFF)),
		        new Byte((byte) ((v >>> 16) & 0xFF)),
		        new Byte((byte) ((v >>>  8) & 0xFF)),
		        new Byte((byte) ((v >>>  0) & 0xFF))
		);
	}
}
