package org.callas.translate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.callas.vm.ast.*;

public class SizeOf {
	private Map<Class<? extends CVMStmt>, Integer> sizes = new HashMap<Class<? extends CVMStmt>, Integer>();
	public SizeOf() {
		sizes.put(CVMBinaryOp.class, 1);
		sizes.put(CVMCall.class, 1);
		sizes.put(CVMClose.class, 1);
		sizes.put(CVMDup.class, 1);
		sizes.put(CVMExtern.class, 1);
		sizes.put(CVMGoto.class, 5);
		sizes.put(CVMIfTrue.class, 5);
		sizes.put(CVMKill.class, 1);
		sizes.put(CVMLoad.class, 2);
		sizes.put(CVMLoadConstant.class, 2);
		sizes.put(CVMLoadModule.class, 1);
		sizes.put(CVMLoadSensorCode.class, 1);
		sizes.put(CVMOpen.class, 1);
		sizes.put(CVMPop.class, 1);
		sizes.put(CVMReceive.class, 1);
		sizes.put(CVMReturn.class, 1);
		sizes.put(CVMSend.class, 1);
		sizes.put(CVMStore.class, 2);
		sizes.put(CVMStoreSensorCode.class, 1);
		sizes.put(CVMSwap.class, 1);
		sizes.put(CVMTimer.class, 1);
		sizes.put(CVMUnaryOp.class, 1);
		sizes.put(CVMUpdate.class, 1);
		Set<Class<?>> expected = new HashSet<Class<?>>();
		for (Class<?> cls : PackageSchema.STMTS) {
			expected.add(cls);
		}
		if (! sizes.keySet().equals(expected)) {
			throw new IllegalArgumentException("Expecting classes " + expected + ", but given " + sizes.keySet());
		}
	}
	public int sizeOf(Iterable<? extends CVMStmt> stmts) {
		int count = 0;
		for (CVMStmt stmt : stmts) {
			count += sizeOf(stmt);
		}
		return count;
	}
	public int sizeOf(CVMStmt stmt) {
		return sizes.get(stmt.getClass()).intValue();
	}
}
