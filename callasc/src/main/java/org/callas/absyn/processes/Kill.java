package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * 
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Apr 10, 2012
 * 
 */

public class Kill extends CallasProcess{
	/**
	 * The id of the timer to terminate.
	 */
  public final CallasValue timerID;
  
  /**
	 * An utility constructor.
	 */
  public Kill(CallasValue value) {
		super();
		this.timerID = value;
  }
  
  /**
	 * The standard constructor.
	 * 
	 * @param sourceLocation
	 *            The source location.
	 */
  public Kill(SourceLocation sourceLocation, CallasValue value) {
     super(sourceLocation);
     this.timerID = value;
  }
  
  
  @Override
  public String toString() {
	  return "kill " + timerID;
  }
}