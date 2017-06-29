package handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import model.Execution;

import org.callas.vm.Call;
import org.callas.vm.udp.NetworkOutputInterface;

public class ExecuteHandler {

    public static boolean execute(final Execution execution) {
        new Thread(new Runnable() {
            @Override
            public void run() {
            	try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
            	
                // get function name to execute
                String functionName = execution.getFunction();
                Object[] input = execution.getInput();
                
                // wsn configuration
                System.out.println("Sending execute request to WSN");
                String wsnIpPort = execution.getWsnIpPort();
                InetAddress wsnIp;
				try {
					wsnIp = InetAddress.getAllByName(wsnIpPort.split(":")[0])[0];
				} catch (UnknownHostException e) {
					System.out.println("Failed to initialize connection with WSN");
					e.printStackTrace();
					return;
				}
				
                String wsnPort = wsnIpPort.split(":")[1];
                Object[] args = new Object[input.length + 1];
                args[0] = functionName;
                for (int i = 0 ; i < input.length; i++) {
                	if (input[i] instanceof Number) {
                		if (input[i] instanceof Float || input[i] instanceof Double)
                			args[i + 1] = Double.valueOf(input[i].toString());
                		else
                			args[i + 1] = Long.valueOf(input[i].toString());
                	}
                	else args[i + 1] = input[i];
                }
                Call wsnCall = new Call("execute", args);
                NetworkOutputInterface wsnNetwork = new NetworkOutputInterface(wsnIp, wsnPort);
                try {
					wsnNetwork.send(wsnCall);
				} catch (IOException e) {
					System.out.println("Failed to send execute request to WSN");
					e.printStackTrace();
					return;
				}
            }
        }).start();

        return true;
    }

}
