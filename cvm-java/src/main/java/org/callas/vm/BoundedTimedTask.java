package org.callas.vm;

import java.util.Timer;  
import java.util.TimerTask;

/**
 * 
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Apr 10, 2012
 * 
**/

public class BoundedTimedTask{
	private Call call;
	private long period;
	private Interpreter emu;
	private Timer timer;
	
	
	public BoundedTimedTask(final Call call, long period, final Interpreter emu) {
		this.call = call;
		this.period = period;
		this.emu = emu;
		this.timer = new Timer();
	}
	
	public void schedule(){
        timer.scheduleAtFixedRate(new TimerTask() {  
            public void run() {
            	synchronized(emu){
            		emu.addRunQueue(call);
            		emu.notifyAll();
            	}
            }
        }, period, period);
	}
	
	public void cancel(){
		timer.cancel();
	}
	
	public Call getCall(){
		return call;
	}
	
	public long getPeriod() {
		return period;
	}
}
