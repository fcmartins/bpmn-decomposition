package org.callas.vm.udp;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.lang.Thread;
import java.net.InetAddress;
import org.callas.vm.IConnectionManager;
import org.callas.vm.INetworkOutputInterface;
import org.callas.vm.Call;
import java.util.StringTokenizer;

/**
 * 
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Apr 10, 2012
 * 
**/

public class ConnectionManager implements IConnectionManager {
	private Hashtable inputConnectionMap;
	private Hashtable outputConnectionMap;
	
	public ConnectionManager(){
		inputConnectionMap = new Hashtable();
		outputConnectionMap = new Hashtable();
	}

	public boolean open(String str){
		boolean output = parseURL(str);
		if (output){
			StringTokenizer st = new StringTokenizer(str,":");
			String address = st.nextToken();
			try {
				InetAddress target = InetAddress.getByName(address);
				String port = st.nextToken();
				INetworkOutputInterface iface = new NetworkOutputInterface(target, port);
				outputConnectionMap.put(str,iface);
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
		} else {
			try {
				InputInterface iface = new NetworkInputInterface();
				iface.setConnection(str);
				Thread consumer = new Thread(iface);
				consumer.start();
				inputConnectionMap.put(str,iface);
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public Object lookupInput(String str){ 
		return inputConnectionMap.get(str);
	}
	
	public Object lookupOutput(String str){
		return outputConnectionMap.get(str);
	}
	
	public void close(String str) throws IOException{
		boolean output = parseURL(str);
		if(output){
			NetworkOutputInterface nInterface = (NetworkOutputInterface) outputConnectionMap.get(str);
			nInterface.close();
			outputConnectionMap.remove(str);
		}else{
		    InputInterface nInterface  = (NetworkInputInterface) inputConnectionMap.get(str);
			nInterface.close();
			inputConnectionMap.remove(str);
		}
	}
	
	private boolean parseURL(String str){
		StringTokenizer st = new StringTokenizer(str,":");
		st.nextToken();
		if(st.hasMoreTokens())
			return true;
		return false;
	}
}