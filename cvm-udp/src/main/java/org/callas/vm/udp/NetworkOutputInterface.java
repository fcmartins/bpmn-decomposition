package org.callas.vm.udp;

import java.io.IOException;

import java.net.*;

import org.callas.vm.Call;
import org.callas.vm.Serializer;
import org.callas.vm.INetworkOutputInterface;


/**
 * This class handles outgoing messages to the network
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * 
 */
public class NetworkOutputInterface implements INetworkOutputInterface {
	
	private final byte[] data = new byte[65507];
	
	private final DatagramPacket datagram = new DatagramPacket(data,
			data.length);
	
	private InetAddress target;
	
	private int port;
	
	private DatagramSocket socket;

	public NetworkOutputInterface(InetAddress target, String port){
		this.target = target;
		this.port = Integer.parseInt(port);
		try {
			socket = new DatagramSocket();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void send(Call msg) throws IOException, SocketException{
		byte[] buffer = Serializer.fromCallToBytes(msg);
		socket.send(new DatagramPacket(buffer, buffer.length, target, port));
	}
	
	public void close() throws IOException{
		socket.close();
	}
}