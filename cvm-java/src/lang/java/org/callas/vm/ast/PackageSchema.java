package org.callas.vm.ast;

public class PackageSchema {
	public static final Class<?>[] STMTS = new Class[] {
		CVMBinaryOp.class,
		CVMCall.class,
		CVMClose.class,
		CVMDup.class,
		CVMExtern.class,
		CVMGoto.class,
		CVMIfTrue.class,
		CVMLoad.class,
		CVMLoadConstant.class,
		CVMLoadModule.class,
		CVMLoadSensorCode.class,
		CVMKill.class,
		CVMOpen.class,
		CVMPop.class,
		CVMReceive.class,
		CVMReturn.class,
		CVMSend.class,
		CVMStore.class,
		CVMStoreSensorCode.class,
		CVMSwap.class,
		CVMTimer.class,
		CVMUnaryOp.class,
		CVMUpdate.class
	};
}
