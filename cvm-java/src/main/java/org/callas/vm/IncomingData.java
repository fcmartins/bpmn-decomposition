package org.callas.vm;

public class IncomingData { 
	
	public final String channel; 
	public final Call call; 
  
	public IncomingData(String channel, Call call) { 
		this.channel = channel; 
		this.call = call; 
	} 
}