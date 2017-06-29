package org.callas.vm.udp;

import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import java.util.Queue;
import java.util.LinkedList;



import org.callas.vm.Deserializer;
import org.callas.vm.Call;
import org.callas.vm.INetworkInputInterface;

import java.net.*;


/**
 * This class handles incomming messages from the network
 * 
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Dec 23, 2012
 * 
 */
public class NetworkInputInterface implements InputInterface, INetworkInputInterface{
	private Queue<Call> inQueue = new LinkedList();
	private boolean isRunning = true;
	private int port;
	private boolean timeoutException = false;
	private DatagramSocket socket;
	private final byte[] data = new byte[65507];
	private final DatagramPacket datagram = new DatagramPacket(data,
			data.length);
	
	public void run() {
		try {
			transmit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void transmit() throws IOException, SocketException {
		try {
			while(isRunning){
				socket.receive(datagram);
				try {
					Call msg = Deserializer.fromBytesToCall(datagram.getData());
					inQueue.offer(msg);
				} catch (IOException e) {
					// malformed message, try again
				}
			}		
		}catch (SocketTimeoutException e){
	    	timeoutException = true;
		}finally{
			if (timeoutException)
				transmit();
			else
				socket.close();
		}
	}
	
	public void setConnection(String port){
		this.port = Integer.parseInt(port);
		try{
			socket = new DatagramSocket(this.port);
			socket.setSoTimeout(1000);
			socket.setBroadcast(true);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public Call popCall(){
		return (Call) inQueue.poll();
	}
	
	public void close(){
		this.isRunning = false;
	}
	
}
