package semant;

import intermediate.GetAccel;
import intermediate.GetAccelX;
import intermediate.GetAccelY;
import intermediate.GetAccelZ;
import intermediate.GetBatteryLevel;
import intermediate.GetInclX;
import intermediate.GetInclY;
import intermediate.GetInclZ;
import intermediate.GetSensorID;
import intermediate.GetTemp;
import intermediate.GetTime;
import intermediate.LogDouble;
import intermediate.LogLong;
import intermediate.LogString;
import intermediate.MacAddr;
import intermediate.SetLEDCollor;
import intermediate.SetLEDOn;
import intermediate.TypeBool;
import intermediate.TypeDouble;
import intermediate.TypeLong;
import intermediate.TypeString;
import intermediate.VarDecl;
import intermediate.Variable;
import intermediate.VisitDepthFirst;

import java.util.Collection;
import java.util.List;

import problems.BPMNError;
import problems.InputTypeMismatchError;
import problems.MissingInputVarError;
import problems.MissingOutputVarError;
import problems.OutputTypeMismatchError;

public class ValidateFunctionParameters extends VisitDepthFirst {

	private List<BPMNError> errors;
	private Collection<VarDecl> variables;
	
	public ValidateFunctionParameters (List<BPMNError> l, Collection<VarDecl> vars) {
		errors = l;
		variables = vars;
	}
	
	@Override
	public boolean visit (LogLong data) {
		if (data.inputVar.size() != 1)
			errors.add(new MissingInputVarError ("Expect 1 variable input.",
					data.element));
		else {
			VarDecl var = getVarDecl ((Variable) data.inputVar.toArray()[0]);
			if (! (var.type instanceof TypeLong))
				errors.add(new InputTypeMismatchError("Type expected is Long in " +
						"position parameter.", data.element));		
		}
		return true;
	}
	
	@Override
	public boolean visit (LogDouble data) {
		if (data.inputVar.size() != 1)
			errors.add(new MissingInputVarError ("Expect 1 variable input.", 
					data.element));
		else {
			VarDecl var = getVarDecl ((Variable) data.inputVar.toArray()[0]);
			if (! (var.type instanceof TypeDouble))
				errors.add(new InputTypeMismatchError("Type expected is Double.",
						data.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (LogString data) {
		if (data.inputVar.size() != 1)
			errors.add(new MissingInputVarError ("Expect 1 variable input.",
					data.element));
		else {
			VarDecl var = getVarDecl ((Variable) data.inputVar.toArray()[0]);
			if (! (var.type instanceof TypeString))
				errors.add(new InputTypeMismatchError("Type expected is String.", 
						data.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (SetLEDCollor setLED) {
		if (setLED.inputVar.size() != 4)
			errors.add(new MissingInputVarError ("Expect 4 variables input.", 
					setLED.element));
		else {			
			VarDecl pos = getVarDecl ((Variable) setLED.inputVar.toArray()[0]);
			VarDecl red = getVarDecl ((Variable) setLED.inputVar.toArray()[1]);
			VarDecl green = 
					getVarDecl ((Variable) setLED.inputVar.toArray()[2]);
			VarDecl blue = getVarDecl ((Variable) setLED.inputVar.toArray()[3]);
			if (! (pos.type instanceof TypeLong))
				errors.add(new InputTypeMismatchError("Type expected is Long in " +
						"position parameter.", setLED.element));
			if (! (red.type instanceof TypeLong))
				errors.add(new InputTypeMismatchError("Type expected is Long in " +
						"red parameter.", setLED.element));
			if (! (green.type instanceof TypeLong))
				errors.add(new InputTypeMismatchError("Type expected is Long in " +
						"green parameter.", setLED.element));
			if (! (blue.type instanceof TypeLong))
				errors.add(new InputTypeMismatchError("Type expected is Long in " +
						"blue parameter.", setLED.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (SetLEDOn on) {
		if (on.inputVar.size() != 2)
			errors.add(new MissingInputVarError ("Expect 2 variables input.", 
					on.element));
		else {
			VarDecl pos = getVarDecl ((Variable) on.inputVar.toArray()[0]);
			VarDecl isOn = getVarDecl ((Variable) on.inputVar.toArray()[1]);
			if (! (pos.type instanceof TypeLong))
				errors.add(new InputTypeMismatchError("Type expected is Long in " +
						"position parameter.", on.element));
			if (! (isOn.type instanceof TypeBool))
				errors.add(new InputTypeMismatchError("Type expected is Boolean " +
						"in isOn parameter.", on.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (GetSensorID sensorID) {
		if (sensorID.outputVar.size() != 1)
			errors.add(new MissingInputVarError ("Expect 1 variable output.", 
					sensorID.element));
		else {
			VarDecl id = 
					getVarDecl((Variable) sensorID.outputVar.toArray()[0]);
			if (! (id.type instanceof TypeLong))
				errors.add(new OutputTypeMismatchError("Type expected is Long as" +
						" output.", sensorID.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (GetTemp getTemp) {
		if (getTemp.outputVar.size() != 1)
			errors.add(new MissingOutputVarError ("Expect 1 variable output.",
					getTemp.element));
		else {
			VarDecl temp = 
					getVarDecl((Variable) getTemp.outputVar.toArray()[0]);
			if (! (temp.type instanceof TypeDouble))
				errors.add(new OutputTypeMismatchError("Type expected is Double as" +
						" output.", getTemp.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (GetTime getTime) {
		if (getTime.outputVar.size() != 1)
			errors.add(new MissingOutputVarError ("Expect 1 variable output.",
					getTime.element));
		else {
			VarDecl time = 
					getVarDecl((Variable) getTime.outputVar.toArray()[0]);
			if (! (time.type instanceof TypeLong))
				errors.add(new OutputTypeMismatchError("Type expected is Long as " +
						"output.", getTime.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (GetAccelX accelX) {
		if (accelX.outputVar.size() != 1)
			errors.add(new MissingOutputVarError ("Expect 1 variable output.",
					accelX.element));
		else {
			VarDecl accel = 
					getVarDecl ((Variable) accelX.outputVar.toArray()[0]);
			if (! (accel.type instanceof TypeDouble))
				errors.add(new OutputTypeMismatchError("Type expected is Double as" +
						" output.", accelX.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (GetAccelY accelY) {
		if (accelY.outputVar.size() != 1)
			errors.add(new MissingOutputVarError ("Expect 1 variable output.", 
					accelY.element));
		else {
			VarDecl accel = 
					getVarDecl ((Variable) accelY.outputVar.toArray()[0]);
			if (! (accel.type instanceof TypeDouble))
				errors.add(new OutputTypeMismatchError("Type expected is Double " +
						"as output.", accelY.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (GetAccelZ accelZ) {
		if (accelZ.outputVar.size() != 1)
			errors.add(new MissingOutputVarError ("Expect 1 variable output.", 
					accelZ.element));
		else {
			VarDecl accel = 
					getVarDecl ((Variable) accelZ.outputVar.toArray()[0]);
			if (! (accel.type instanceof TypeDouble))
				errors.add(new OutputTypeMismatchError("Type expected is Double as" +
						" output.", accelZ.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (GetAccel accel) {
		if (accel.outputVar.size() != 1)
			errors.add(new MissingOutputVarError ("Expect 1 variable output.",
					accel.element));
		else {
			VarDecl a = getVarDecl ((Variable) accel.outputVar.toArray()[0]);
			if (! (a.type instanceof TypeDouble))
				errors.add(new OutputTypeMismatchError("Type expected is Double as" +
						" output.", accel.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (GetInclX inclX) {
		if (inclX.outputVar.size() != 1)
			errors.add(new MissingOutputVarError ("Expect 1 variable output.",
					inclX.element));
		else {
			VarDecl incl = getVarDecl ((Variable) inclX.outputVar.toArray()[0]);
			if (! (incl.type instanceof TypeDouble))
				errors.add(new OutputTypeMismatchError("Type expected is Double as" +
						" output.", inclX.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (GetInclY inclY) {
		if (inclY.outputVar.size() != 1)
			errors.add(new MissingOutputVarError ("Expect 1 variable output.",
					inclY.element));
		else {
			VarDecl incl = getVarDecl ((Variable) inclY.outputVar.toArray()[0]);
			if (! (incl.type instanceof TypeDouble))
				errors.add(new OutputTypeMismatchError("Type expected is Double as" +
						" output.", inclY.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (GetInclZ inclZ) {
		if (inclZ.outputVar.size() != 1)
			errors.add(new MissingOutputVarError ("Expect 1 variable output.",
					inclZ.element));
		else {
			VarDecl incl = getVarDecl ((Variable) inclZ.outputVar.toArray()[0]);
			if (! (incl.type instanceof TypeDouble))
				errors.add(new OutputTypeMismatchError("Type expected is Double as" +
						" output.", inclZ.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (GetBatteryLevel battery) {
		if (battery.outputVar.size() != 1)
			errors.add(new MissingOutputVarError ("Expect 1 variable output.",
					battery.element));
		else {
			VarDecl bat = getVarDecl((Variable) battery.outputVar.toArray()[0]);
			if (! (bat.type instanceof TypeLong))
				errors.add(new OutputTypeMismatchError("Type expected is Long as" +
						" output.", battery.element));
		}
		return true;
	}
	
	@Override
	public boolean visit (MacAddr mac) {
		if (mac.outputVar.size() != 1)
			errors.add(new MissingOutputVarError ("Expect 1 variable output.",
					mac.element));
		else {
			VarDecl addr = getVarDecl((Variable) mac.outputVar.toArray()[0]);
			if (! (addr.type instanceof TypeString))
				errors.add(new OutputTypeMismatchError("Type expected is String as" +
						" output.", mac.element));
		}
		return true;
	}
	
	private VarDecl getVarDecl (Variable v) {
		for (VarDecl var : variables)
			if (var.var.equals(v))
				return var;
		return null;
	}
	
}
