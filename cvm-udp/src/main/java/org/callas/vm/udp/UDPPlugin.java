package org.callas.vm.udp;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.callas.vm.Call;
import org.callas.vm.app.IInterpreter;
import org.callas.vm.app.IInterpreterListener;
import org.callas.vm.app.cli.ICommandLineContext;
import org.callas.vm.app.cli.ICommandLineListener;

public class UDPPlugin implements ICommandLineListener, IInterpreterListener {
	private int port = 8912;
	private InetAddress address;

	private static Collection<InetAddress> getBroadcastAddresses()
			throws SocketException {
		List<InetAddress> result = new LinkedList<InetAddress>();
		Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface
				.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			java.net.NetworkInterface networkInterface = interfaces
					.nextElement();
			if (networkInterface.isLoopback())
				continue; // Don't want to broadcast to the loopback interface
			for (InterfaceAddress interfaceAddress : networkInterface
					.getInterfaceAddresses()) {
				InetAddress broadcast = interfaceAddress.getBroadcast();
				if (broadcast == null)
					continue;
				// Use the address
				result.add(broadcast);
			}
		}
		return result;
	}

	private static class ListBroadcastAddresses implements Runnable {
		public void run() {
			try {
				Collection<InetAddress> addrs = getBroadcastAddresses();
				if (addrs.isEmpty()) {
					System.err.println("Pass -Djava.net.preferIPv4Stack=true "
							+ "to Java if no addresses are being shown.");
				} else {
					PrintStream out = System.out;
					for (InetAddress addr : addrs) {
						out.println(addr.getHostAddress());
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
	}

	public void onParsedCommandLine(CommandLine result) throws ParseException {
		if (result.hasOption('p')) {
			this.port = Integer.valueOf(result.getOptionValue('p')).intValue();
		}
		if (result.hasOption('r') && !result.hasOption('b')) {
			throw new ParseException("Missing required option: -b");
		}
		try {
			this.address = InetAddress.getByName(result.getOptionValue('b'));
		} catch (UnknownHostException e) {
			throw new ParseException(
					"Unknown broadcast IP address (parameter -b)");
		}
	}

	public void handleInterpreterContext(IInterpreter ctx) {
		//nothing to do here
	}

	@SuppressWarnings("static-access")
	public void handleCommandLineContext(ICommandLineContext ctx) {
		ctx.registerAlternativeCommand(new Option("l", "list", false,
				"list broadcast addresses"), new ListBroadcastAddresses());
		ctx.registerOption(OptionBuilder//
				.withDescription("port (default " + port + ")")//
				.withLongOpt("port")//
				.withArgName("PORT")//
				.hasArg()//
				.create("p"));
		ctx.registerOption(OptionBuilder
				.withDescription("broadcast IP address")//
				.withLongOpt("broadcast")//
				.withArgName("IP")//
				.hasArg()//
				.create("b"));
	}

	public void onInterpreterStart() {
		//Nothing to do.
	}

	public void onInterpreterShutdown() {
		// No cleaning up. Nothing to do.
	}

}
