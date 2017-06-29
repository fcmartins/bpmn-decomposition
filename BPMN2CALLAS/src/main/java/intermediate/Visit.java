package intermediate;

public interface Visit {
	
	public boolean visit (AbSyn ab);

	public boolean visit (If i);
	public void visitIf (If i);
	public void endVisit (If i);
	
	public boolean visit (Join j);
	public void visitJoin (Join j);
	public void endVisit (Join j);
	
	public boolean visit (GuardedCommand c);
	public void visitGuardedCommand (GuardedCommand c);
	public void endVisit (GuardedCommand c);
	
	public boolean visit (ReturnCommand rc);
	public void visitReturnCommand(ReturnCommand rc);
	public void endVisit (ReturnCommand rc);
		
	public boolean visit (Process p);
	public void visitProcess (Process p);
	public void endVisit (Process p);
	
	public boolean visit (Send s);
	public void visitSend (Send s);
	public void endVisit (Send s);
	
	public boolean visit (Receive r);
	public void visitReceive (Receive r);
	public void endVisit (Receive r);
	
	public boolean visit (GetSensorID g);
	public void visitGetSensorID (GetSensorID g);
	public void endVisit (GetSensorID g);
	
	public boolean visit (GetTemp g);
	public void visitGetTemp (GetTemp g);
	public void endVisit (GetTemp g);
	
	public boolean visit (Value v);
	public void visitValue (Value v);
	public void endVisit (Value v);
	
	public boolean visit (Variable v);
	public void visitVariable (Variable v);
	public void endVisit (Variable v);
	
	public boolean visit (VarDecl v);
	public void visitVarDecl (VarDecl v);
	public void endVisit (VarDecl v);
	
	public boolean visit (Blink bl);
	public void visitBlink (Blink bl);
	public void endVisit (Blink bl);
	
	public boolean visit (GetAccel accel);
	public void visitGetAccel (GetAccel accel);
	public void endVisit (GetAccel accel);
	
	public boolean visit (GetAccelX accelX);
	public void visitGetAccelX (GetAccelX accelX);
	public void endVisit (GetAccelX accelX);
	
	public boolean visit (GetAccelY accelY);
	public void visitGetAccelY (GetAccelY accelY);
	public void endVisit (GetAccelY accelY);
	
	public boolean visit (GetAccelZ accelZ);
	public void visitGetAccelZ (GetAccelZ accelZ);
	public void endVisit (GetAccelZ accelZ);
	
	public boolean visit (GetBatteryLevel battery);
	public void visitGetBatteryLevel (GetBatteryLevel battery);
	public void endVisit (GetBatteryLevel battery);
	
	public boolean visit (GetInclX inclX);
	public void visitGetInclX (GetInclX inclX);
	public void endVisit (GetInclX inclX);
	
	public boolean visit (GetInclY inclY);
	public void visitGetInclY (GetInclY inclY);
	public void endVisit (GetInclY inclY);
	
	public boolean visit (GetInclZ inclZ);
	public void visitGetInclZ (GetInclZ inclZ);
	public void endVisit (GetInclZ inclZ);
	
	public boolean visit (GetLuminosity lumi);
	public void visitGetLuminosity (GetLuminosity lumi);
	public void endVisit (GetLuminosity lumi);
	
	public boolean visit (GetTime time);
	public void visitGetTime (GetTime time);
	public void endVisit (GetTime time);
	
	public boolean visit (LogDouble data);
	public void visitLogDouble (LogDouble data);
	public void endVisit (LogDouble data);
	
	public boolean visit (LogLong data);
	public void visitLogLong (LogLong data);
	public void endVisit (LogLong data);
	
	public boolean visit (LogString data);
	public void visitLogString (LogString data);
	public void endVisit (LogString data);
	
	public boolean visit (MacAddr mac);
	public void visitMacAddr (MacAddr mac);
	public void endVisit (MacAddr mac);
	
	public boolean visit (SetLEDCollor collor);
	public void visitSetLEDCollor (SetLEDCollor collor);
	public void endVisit (SetLEDCollor collor);
	
	public boolean visit (SetLEDOn on);
	public void visitSetLEDOn (SetLEDOn on);
	public void endVisit (SetLEDOn on);
}
