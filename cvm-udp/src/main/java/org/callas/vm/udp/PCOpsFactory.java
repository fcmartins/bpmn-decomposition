package org.callas.vm.udp;

import org.callas.vm.INativeOperations;
import org.callas.vm.app.INativeOperationsFactory;

public class PCOpsFactory implements INativeOperationsFactory {
	
	public INativeOperations createNativeOperations() {
		return new PCOps(System.out);
	}

}
