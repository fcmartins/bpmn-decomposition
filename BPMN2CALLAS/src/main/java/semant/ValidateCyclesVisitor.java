package semant;

import intermediate.Blink;
import intermediate.GetAccel;
import intermediate.GetAccelX;
import intermediate.GetAccelY;
import intermediate.GetAccelZ;
import intermediate.GetBatteryLevel;
import intermediate.GetInclX;
import intermediate.GetInclY;
import intermediate.GetInclZ;
import intermediate.GetLuminosity;
import intermediate.GetSensorID;
import intermediate.GetTemp;
import intermediate.GetTime;
import intermediate.GuardedCommand;
import intermediate.If;
import intermediate.LogDouble;
import intermediate.LogLong;
import intermediate.LogString;
import intermediate.Receive;
import intermediate.Send;
import intermediate.SetLEDCollor;
import intermediate.SetLEDOn;
import intermediate.VisitDepthFirst;

import java.util.List;

import problems.BPMNError;
import problems.CycleFoundError;
import bpmn2.TBaseElement;



/**
 * As Join has various inputs, there is no valid way to determine if process
 * has a cycle with this element.
 * If the process has a cycle it will repeat other elements.
 * So we have a contention mechanism to validate cycles.
 * 
 */

public class ValidateCyclesVisitor extends VisitDepthFirst {
	
	private List<BPMNError> errors;
	
	
	public ValidateCyclesVisitor(List<BPMNError> l) {
		errors = l;
	}
	
	@Override
	public boolean visit (If i) {
		if(visitedNodes.contains(i)) {
			error(i.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit(GuardedCommand c) {
		if(visitedNodes.contains(c)) {
			error(c.element);
			return false;
		}
		return true;
	}
	
//	@Override
//	public boolean visit(ReturnCommand rc) {
//		if(visitedNodes.contains(rc)) {
//			error(rc.element);
//			return false;
//		}
//		return true;
//	}
	
	@Override
	public boolean visit(Send s) {
		if(visitedNodes.contains(s)) {
			error(s.element);
			return false;
		}
		return true;
	}
	
	
	@Override
	public boolean visit(Receive r) {
		if(visitedNodes.contains(r)) {
			error(r.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit(GetSensorID g) {
		if(visitedNodes.contains(g)) {
			error(g.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit(GetTemp g) {
		if(visitedNodes.contains(g)) {
			error(g.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (Blink bl) {
		if (visitedNodes.contains(bl)) {
			error(bl.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (GetAccel accel) {
		if (visitedNodes.contains(accel)) {
			error(accel.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (GetAccelX accelX) {
		if (visitedNodes.contains(accelX)) {
			error(accelX.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (GetAccelY accelY) {
		if (visitedNodes.contains(accelY)) {
			error(accelY.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (GetAccelZ accelZ) {
		if (visitedNodes.contains(accelZ)) {
			error(accelZ.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (GetBatteryLevel battery) {
		if (visitedNodes.contains(battery)) {
			error(battery.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (GetInclX inclX) {
		if (visitedNodes.contains(inclX)) {
			error(inclX.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (GetInclY inclY) {
		if (visitedNodes.contains(inclY)) {
			error(inclY.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (GetInclZ inclZ) {
		if (visitedNodes.contains(inclZ)) {
			error(inclZ.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (GetLuminosity lumi) {
		if (visitedNodes.contains(lumi)) {
			error(lumi.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (GetTime time) {
		if (visitedNodes.contains(time)) {
			error(time.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (LogDouble data) {
		if (visitedNodes.contains(data)) {
			error(data.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (LogLong data) {
		if (visitedNodes.contains(data)) {
			error(data.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (LogString data) {
		if (visitedNodes.contains(data)) {
			error(data.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (SetLEDCollor collor) {
		if (visitedNodes.contains(collor)) {
			error(collor.element);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean visit (SetLEDOn on) {
		if (visitedNodes.contains(on)) {
			error(on.element);
			return false;
		}
		return true;
	}
	
	private void error(TBaseElement e) {
		errors.add(new CycleFoundError("Process has cycle at " + 
				e.toString(), e));
	}

}
