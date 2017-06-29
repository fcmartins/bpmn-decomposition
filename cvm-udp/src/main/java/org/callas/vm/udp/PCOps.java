package org.callas.vm.udp;

import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.Random;

import org.callas.vm.INativeOperations;

public class PCOps implements INativeOperations {

	private Map<String, Byte> sizes = new TreeMap<String, Byte>();
	private final PrintStream out;
	
	public PCOps(PrintStream out) {
		sizes.put("logLong", (byte) 1);
		sizes.put("logDouble", (byte) 1);
		sizes.put("logString", (byte) 1);
		sizes.put("macAddr", (byte) 0);
		sizes.put("getTime", (byte) 0);
		sizes.put("blink", (byte) 0);
		this.out = out;
	}
	
	public Object execute(String systemCall, Object[] args) {
		if (systemCall.equals("logLong")) {
			long value = ((Long) args[0]).longValue();
			out.print(value);
			return new Boolean(true);
		} else if (systemCall.equals("logDouble")) {
			double value = ((Double) args[0]).doubleValue();
			out.print(value);
			return new Boolean(true);
		} else if (systemCall.equals("logString")) {
			String value = (String) args[0];
			out.print(value);
			return new Boolean(true);
		} else if (systemCall.equals("macAddr")) {
			return Long.toString(this.hashCode(), 16);
			
		} else if (systemCall.equals("getTime")) {
			Long value = new Long(System.currentTimeMillis());
			return value;

		} else if (systemCall.equals("blink")) {
			out.print("blinked!\n");
			return new Boolean(true);
		} else {
			throw new IllegalArgumentException("Unsupported system call: " + systemCall);
		}
	}

	public byte getParameterCount(String systemCall) {
		if (sizes.keySet().contains(systemCall)) {
			return sizes.get(systemCall);
		} else {
			throw new IllegalArgumentException("Unsupported system call: " + systemCall);
		}
	}

	public void init() {
		// OK
	}

}
