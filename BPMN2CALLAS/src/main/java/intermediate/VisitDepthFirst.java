package intermediate;

import java.util.HashSet;
import java.util.Set;

public class VisitDepthFirst implements Visit {
	
	protected Set<AbSyn> visitedNodes;
	
	public VisitDepthFirst(){
		visitedNodes = new HashSet<AbSyn>();
	}

	@Override
	public boolean visit(AbSyn ab) {
		return false;
	}

	@Override
	public boolean visit(If i) {
		return !alreadyVisited(i);
	}

	@Override
	public void visitIf(If i) {
		if (visit(i)){
			visitedNodes.add(i);			
			for (GuardedCommand gc : i.getGuardedCommands())
				gc.accept(this);
			
			if (i.getContinuation() != null)
				i.getContinuation().accept(this);
			endVisit (i);
		}
	}

	@Override
	public void endVisit(If i) {
		//does nothing
	}

	@Override
	public boolean visit(Join j) {
		return !alreadyVisited(j);
	}

	@Override
	public void visitJoin(Join j) {
		if (visit(j)){
			visitedNodes.add(j);			
			if (j.getContinuation() != null)
				j.getContinuation().accept(this);
			endVisit (j);
		}
	}

	@Override
	public void endVisit(Join j) {
		//does nothing
	}

	@Override
	public boolean visit(GuardedCommand c) {
		return !alreadyVisited(c);
	}

	@Override
	public void visitGuardedCommand(GuardedCommand c) {
		if(visit(c)){
			visitedNodes.add(c);
			c.cmd.accept(this);
			c.guard.accept(this);
			endVisit(c);
		}
	}

	@Override
	public void endVisit(GuardedCommand c) {
		//does nothing
	}

	@Override
	public boolean visit(ReturnCommand rc) {
		return !alreadyVisited(rc);
	}

	@Override
	public void visitReturnCommand(ReturnCommand rc) {
		if(visit(rc)){
			visitedNodes.add(rc);
			endVisit(rc);
		}
	}

	@Override
	public void endVisit(ReturnCommand rc) {
		//does nothing
	}

	@Override
	public boolean visit(Process p) {
		return !alreadyVisited(p);
	}

	@Override
	public void visitProcess(Process p) {
		if(visit (p)){
			visitedNodes.add(p);
			p.getInitialCommand().accept(this);
			endVisit(p);
		}
	}

	@Override
	public void endVisit(Process p) {
		//does nothing
	}

	@Override
	public boolean visit(Send s) {
		return !alreadyVisited(s);
	}

	@Override
	public void visitSend(Send s) {
		if (visit (s)){
			visitedNodes.add(s);
			s.getContinuation().accept(this);
			endVisit(s);
		}
	}

	@Override
	public void endVisit(Send s) {
		//does nothing
	}

	@Override
	public boolean visit(Receive r) {
		return !alreadyVisited(r);
	}

	@Override
	public void visitReceive(Receive r) {
		if (visit (r)){
			visitedNodes.add(r);
			r.getContinuation().accept(this);
			endVisit(r);
		}
	}

	@Override
	public void endVisit(Receive r) {
		//does nothing
	}

	@Override
	public boolean visit(Value v) {
		return true;
	}

	@Override
	public void visitValue(Value v) {
		if (visit (v)){
			endVisit(v);
		}
	}

	@Override
	public void endVisit(Value v) {
		//does nothing
	}

	@Override
	public boolean visit(Variable v) {
		return true;
	}

	@Override
	public void visitVariable(Variable v) {
		if (visit (v)){
			endVisit(v);
		}
	}

	@Override
	public void endVisit(Variable v) {
		//does nothing
	}

	@Override
	public boolean visit(VarDecl v) {
		return true;
	}

	@Override
	public void visitVarDecl(VarDecl v) {
		if (visit (v)){
			v.var.accept(this);
			endVisit(v);
		}
	}

	@Override
	public void endVisit(VarDecl v) {
		//does nothing
	}


	private boolean alreadyVisited(AbSyn ab) {
		return visitedNodes.contains(ab);
	}

	@Override
	public boolean visit(Blink bl) {
		return !alreadyVisited(bl);
	}

	@Override
	public void visitBlink(Blink bl) {
		if (visit (bl)){
			visitedNodes.add(bl);
			if (bl.getContinuation() != null)
				bl.getContinuation().accept(this);
			endVisit(bl);
		}
		
	}

	@Override
	public void endVisit(Blink bl) { }

	@Override
	public boolean visit(GetTemp g) {
		return !alreadyVisited(g);
	}

	@Override
	public void visitGetTemp(GetTemp g) {
		if (visit (g)){
			visitedNodes.add(g);
			if (g.getContinuation() != null)
				g.getContinuation().accept(this);
			endVisit(g);
		}
	}

	@Override
	public void endVisit(GetTemp g) {
		//does nothing
	}
	
	@Override
	public boolean visit(GetAccel accel) {
		return !alreadyVisited(accel);
	}

	@Override
	public void visitGetAccel(GetAccel accel) {
		if (visit (accel)){
			visitedNodes.add(accel);
			if (accel.getContinuation() != null)
				accel.getContinuation().accept(this);
			endVisit(accel);
		}
	}

	@Override
	public void endVisit(GetAccel accel) { }

	@Override
	public boolean visit(GetAccelX accelX) {
		return !alreadyVisited(accelX);
	}

	@Override
	public void visitGetAccelX(GetAccelX accelX) {
		if (visit (accelX)){
			visitedNodes.add(accelX);
			if (accelX.getContinuation() != null)
				accelX.getContinuation().accept(this);
			endVisit(accelX);
		}		
	}

	@Override
	public void endVisit(GetAccelX accelX) { }

	@Override
	public boolean visit(GetAccelY accelY) {
		return !alreadyVisited(accelY);
	}

	@Override
	public void visitGetAccelY(GetAccelY accelY) {
		if (visit (accelY)){
			visitedNodes.add(accelY);
			if (accelY.getContinuation() != null)
				accelY.getContinuation().accept(this);
			endVisit(accelY);
		}	
	}

	@Override
	public void endVisit(GetAccelY accelY) { }

	@Override
	public boolean visit(GetAccelZ accelZ) {
		return !alreadyVisited(accelZ);
	}

	@Override
	public void visitGetAccelZ(GetAccelZ accelZ) {
		if (visit (accelZ)){
			visitedNodes.add(accelZ);
			if (accelZ.getContinuation() != null)
				accelZ.getContinuation().accept(this);
			endVisit(accelZ);
		}	
	}

	@Override
	public void endVisit(GetAccelZ accelZ) { }

	@Override
	public boolean visit(GetBatteryLevel battery) {
		return !alreadyVisited(battery);
	}

	@Override
	public void visitGetBatteryLevel(GetBatteryLevel battery) {
		if (visit (battery)){
			visitedNodes.add(battery);
			if (battery.getContinuation() != null)
				battery.getContinuation().accept(this);
			endVisit(battery);
		}		
	}

	@Override
	public void endVisit(GetBatteryLevel baterry) {	}

	@Override
	public boolean visit(GetInclX inclX) {
		return !alreadyVisited(inclX);
	}

	@Override
	public void visitGetInclX(GetInclX inclX) {
		if (visit (inclX)){
			visitedNodes.add(inclX);
			if (inclX.getContinuation() != null)
				inclX.getContinuation().accept(this);
			endVisit(inclX);
		}
	}

	@Override
	public void endVisit(GetInclX inclX) { }

	@Override
	public boolean visit(GetInclY inclY) {
		return !alreadyVisited(inclY);
	}

	@Override
	public void visitGetInclY(GetInclY inclY) {
		if (visit (inclY)){
			visitedNodes.add(inclY);
			if (inclY.getContinuation() != null)
				inclY.getContinuation().accept(this);
			endVisit(inclY);
		}
	}

	@Override
	public void endVisit(GetInclY inclY) { }

	@Override
	public boolean visit(GetInclZ inclZ) {
		return !alreadyVisited(inclZ);
	}

	@Override
	public void visitGetInclZ(GetInclZ inclZ) {
		if (visit (inclZ)){
			visitedNodes.add(inclZ);
			if (inclZ.getContinuation() != null)
				inclZ.getContinuation().accept(this);
			endVisit(inclZ);
		}	
	}

	@Override
	public void endVisit(GetInclZ inclZ) { }

	@Override
	public boolean visit(GetLuminosity lumi) {
		return !alreadyVisited(lumi);
	}

	@Override
	public void visitGetLuminosity(GetLuminosity lumi) {
		if (visit (lumi)){
			visitedNodes.add(lumi);
			if (lumi.getContinuation() != null)
				lumi.getContinuation().accept(this);
			endVisit(lumi);
		}
	}

	@Override
	public void endVisit(GetLuminosity lumi) { }

	@Override
	public boolean visit(GetSensorID g) {
		return !alreadyVisited(g);
	}

	@Override
	public void visitGetSensorID(GetSensorID g) {
		if (visit (g)){
			visitedNodes.add(g);
			if (g.getContinuation() != null)
				g.getContinuation().accept(this);
			endVisit(g);
		}
	}

	@Override
	public void endVisit(GetSensorID g) {
		//does nothing
	}

	@Override
	public boolean visit(GetTime time) {
		return !alreadyVisited(time);
	}

	@Override
	public void visitGetTime(GetTime time) {
		if (visit (time)){
			visitedNodes.add(time);
			if (time.getContinuation() != null)
				time.getContinuation().accept(this);
			endVisit(time);
		}
	}

	@Override
	public void endVisit(GetTime time) { }
	
	@Override
	public boolean visit(LogDouble data) {
		return !alreadyVisited(data);
	}

	@Override
	public void visitLogDouble(LogDouble data) {
		if (visit (data)){
			visitedNodes.add(data);
			if (data.getContinuation() != null)
				data.getContinuation().accept(this);
			endVisit(data);
		}
	}

	@Override
	public void endVisit(LogDouble data) { }

	@Override
	public boolean visit(LogLong data) {
		return !alreadyVisited(data);
	}

	@Override
	public void visitLogLong(LogLong data) {
		if (visit (data)){
			visitedNodes.add(data);
			if (data.getContinuation() != null)
				data.getContinuation().accept(this);
			endVisit(data);
		}
	}

	@Override
	public void endVisit(LogLong data) { }

	@Override
	public boolean visit(LogString data) {
		return !alreadyVisited(data);
	}

	@Override
	public void visitLogString(LogString data) {
		if (visit (data)){
			visitedNodes.add(data);
			if (data.getContinuation() != null)
				data.getContinuation().accept(this);
			endVisit(data);
		}
	}

	@Override
	public void endVisit(LogString data) { }
	
	@Override
	public boolean visit(MacAddr mac) {
		return !alreadyVisited(mac);
	}

	@Override
	public void visitMacAddr(MacAddr mac) {
		if (visit (mac)){
			visitedNodes.add(mac);
			if (mac.getContinuation() != null)
				mac.getContinuation().accept(this);
			endVisit(mac);
		}
	}

	@Override
	public void endVisit(MacAddr mac) { }

	@Override
	public boolean visit(SetLEDCollor collor) {
		return !alreadyVisited(collor);
	}

	@Override
	public void visitSetLEDCollor(SetLEDCollor collor) {
		if (visit (collor)){
			visitedNodes.add(collor);
			if (collor.getContinuation() != null)
				collor.getContinuation().accept(this);
			endVisit(collor);
		}
	}

	@Override
	public void endVisit(SetLEDCollor collor) { }

	@Override
	public boolean visit(SetLEDOn on) {
		return !alreadyVisited(on);
	}

	@Override
	public void visitSetLEDOn(SetLEDOn on) {
		if (visit (on)){
			visitedNodes.add(on);
			if (on.getContinuation() != null)
				on.getContinuation().accept(this);
			endVisit(on);
		}
	}

	@Override
	public void endVisit(SetLEDOn on) { }
}
