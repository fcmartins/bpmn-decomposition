package translate;

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
import intermediate.Join;
import intermediate.LogDouble;
import intermediate.LogLong;
import intermediate.LogString;
import intermediate.MacAddr;
import intermediate.Process;
import intermediate.Send;
import intermediate.SetLEDCollor;
import intermediate.SetLEDOn;
import intermediate.TimedProcess;
import intermediate.Type;
import intermediate.TypeBool;
import intermediate.TypeDouble;
import intermediate.TypeLong;
import intermediate.TypeString;
import intermediate.VarDecl;
import intermediate.Variable;
import intermediate.VisitDepthFirst;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import structured.AssignCommand;
import structured.BlockCommand;
import structured.Command;
import structured.Expression;
import structured.ExternalGetFunctionCall;
import structured.ExternalSetFunctionCall;
import structured.Function;
import structured.FunctionSignature;
import structured.IfStructured;
import structured.SendFunctionCall;
import structured.TimedFunction;
import structured.TypeInteger;
import structured.Value;



public class ToStructured extends VisitDepthFirst{
	
	protected Stack<Command> translatedCommand; 
	protected List<Command> block;
	protected Map <If, Join> map;
	protected Map <If, IfStructured> ifs;
	protected Set<VarDecl> varsD;
	
	public ToStructured(Stack<Command> s, Map <If, Join> map, Map<If, IfStructured> ifs, 
			Set<intermediate.VarDecl> vd) {
		translatedCommand = s;
		block =  new LinkedList<Command>();
		this.map = map;
		this.ifs = ifs;
		varsD = vd;
	}
	
	@Override
	public void visitGuardedCommand(GuardedCommand gc) {
		if(visit(gc)){		
			translatedCommand.push(new Value(gc.guard, gc.guard.name));
			gc.cmd.accept(this);			
			endVisit(gc);
		}
	}
	
	@Override
	public void endVisit (GuardedCommand gc) {
		translatedCommand.push(new BlockCommand(block));
		block.clear();
	}
	
	@Override
	public void visitIf(If i) {
		
		if (visit(i)) {
			for (GuardedCommand gc : i.getGuardedCommands()){
				ToStructured translator = new ToStructured(translatedCommand, 
						map, ifs, varsD);
				gc.accept(translator);				
			}
			
			//else branch of lastIf
			ToStructured translator = new ToStructured(translatedCommand,
					map, ifs, varsD);
			i.getContinuation().accept(translator);			
						
			translatedCommand.push(new BlockCommand(translator.block));
			
			endVisit (i);
		}
	}	
	
	@Override
	public void endVisit (If i) {
		//do size + 1 times for continuation command of intermediate.If
//		for (int a = 0; a < i.getGuardedCommands().size(); a++) {
//			Command elseCmd = translatedCommand.pop();
//			Command thenCmd = translatedCommand.pop();
//			Expression guard = (Expression) translatedCommand.pop();
//			IfStructured iTemp = new IfStructured(i, guard, thenCmd, elseCmd);
//			ifs.put(i, iTemp);
//			block.add(iTemp);
//		}
		
		Command elseCmd = translatedCommand.pop();
		IfStructured iTemp = null;
		for (int a = 0; a < i.getGuardedCommands().size(); a++) {
			Command thenCmd = translatedCommand.pop();
			Expression guard = (Expression) translatedCommand.pop();
			iTemp = new IfStructured(i, guard, thenCmd, elseCmd);
			elseCmd = iTemp;
		}

		ifs.put(i, iTemp);
		block.add(iTemp);

		// continue to commands after this if
		map.get(i).getContinuation().accept(this);
	}
	
	
	@Override
	public void endVisit (Process p) {	
		BlockCommand blockCommand = new BlockCommand(block);
		
		List<structured.VarDecl> arguments = new LinkedList<structured.VarDecl>();
		for (intermediate.VarDecl arg : p.getArguments()) {
			arguments.add(new structured.VarDecl(
					new structured.Variable(arg.var, arg.var.name),
					getType(arg.type)));
			
		}
		
		Function result = null;
		FunctionSignature signature = new FunctionSignature(p.getName(), arguments);
		if (p instanceof TimedProcess) {
			TimedProcess tp = (TimedProcess)p;
			result = new TimedFunction(signature, blockCommand, 
					new structured.Variable(tp.getPeriodicity(), tp.getPeriodicity().name));
		}
		else
			result = new Function(signature, blockCommand);
		
		translatedCommand.push(result);
	}

	
	@Override
	public boolean visit(Join j) {
		return false;
	}

	@Override
	public void visitSend (Send s) {
		if (visit (s)) {			
				
//			List<Expression> vars = new LinkedList<Expression>();
//			for (Variable v : s.inputVar)
//				vars.add(new structured.Variable (v, v.name));
			
			List<structured.VarDecl> vars = new LinkedList<structured.VarDecl>();
			for (Variable v : s.inputVar) {
				vars.add(new structured.VarDecl(
						new structured.Variable(v, v.name),
						getType(getVarDeclByName(v.name).type)));
			}
			
			block.add(new SendFunctionCall(s, "send", vars));
			
			//TODO: Define function name
			
			s.getContinuation().accept(this);
			endVisit(s);
		}		
	}
	
	@Override
	public void visitGetTemp(GetTemp g) { 
		if (visit(g)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : g.outputVar)
				vars.add(v.name);
			
			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(g, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)), 
					getType(temp.type)),
					new ExternalGetFunctionCall(g, "getTemperature", vars)));

			g.getContinuation().accept(this);
			endVisit (g);
		}
	}
	
	@Override
	public void visitGetSensorID (GetSensorID gsid) {
		if (visit (gsid)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : gsid.outputVar)
				vars.add(v.name);

			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(gsid, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)), 
					getType(temp.type)),
					new ExternalGetFunctionCall(gsid, "macAddr", vars)));

			gsid.getContinuation().accept(this);
			endVisit (gsid);
		}
	}
	
	@Override
	public void visitBlink (Blink blink) {
		if (visit (blink)) {			
//			block.add(new ExternalSetFunctionCall(blink, "blink", 
//					new LinkedList<String>()));
//			blink.getContinuation().accept(this);
//			endVisit (blink);
			
			
			List<String> vars = new LinkedList<String>();
			for(Variable v : blink.outputVar)
				vars.add(v.name);
			
			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(blink, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)), 
					getType(temp.type)),
					new ExternalGetFunctionCall(blink, "blink", vars)));

			blink.getContinuation().accept(this);
			endVisit (blink);
		}
	}
	
	@Override
	public void visitGetAccel (GetAccel acc) {
		if (visit (acc)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : acc.outputVar)
				vars.add(v.name);

			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(acc, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)), 
					getType(temp.type)),
					new ExternalGetFunctionCall(acc, "getAccel", vars)));

			acc.getContinuation().accept(this);
			endVisit (acc);
		}
	}
	
	@Override
	public void visitGetAccelX (GetAccelX acc) {
		if (visit (acc)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : acc.outputVar)
				vars.add(v.name);

			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(acc, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)), 
					getType(temp.type)),
					new ExternalGetFunctionCall(acc, "getAccelX", vars)));

			acc.getContinuation().accept(this);
			endVisit (acc);
		}
	}
	
	@Override
	public void visitGetAccelY (GetAccelY acc) {
		if (visit (acc)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : acc.outputVar)
				vars.add(v.name);

			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(acc, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)),
					getType(temp.type)),
					new ExternalGetFunctionCall(acc, "getAccelY", vars)));

			acc.getContinuation().accept(this);
			endVisit (acc);
		}
	}
	
	@Override
	public void visitGetAccelZ (GetAccelZ acc) {
		if (visit (acc)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : acc.outputVar)
				vars.add(v.name);

			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(acc, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)), 
					getType(temp.type)),
					new ExternalGetFunctionCall(acc, "getAccelZ", vars)));

			acc.getContinuation().accept(this);
			endVisit (acc);
		}
	}
	
	@Override
	public void visitGetBatteryLevel (GetBatteryLevel batt) {
		if (visit (batt)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : batt.outputVar)
				vars.add(v.name);

			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(batt, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)),
					getType(temp.type)),
					new ExternalGetFunctionCall(batt, "getBatteryLevel", vars)));

			batt.getContinuation().accept(this);
			endVisit (batt);
		}
	}
	
	@Override
	public void visitGetInclX (GetInclX incl) {
		if (visit (incl)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : incl.outputVar)
				vars.add(v.name);

			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(incl, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)), 
					getType(temp.type)),
					new ExternalGetFunctionCall(incl, "getInclX", vars)));

			incl.getContinuation().accept(this);
			endVisit (incl);
		}
	}
	
	@Override
	public void visitGetInclY (GetInclY incl) {
		if (visit (incl)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : incl.outputVar)
				vars.add(v.name);

			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(incl, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)), 
					getType(temp.type)),
					new ExternalGetFunctionCall(incl, "getInclY", vars)));

			incl.getContinuation().accept(this);
			endVisit (incl);
		}
	}
	
	@Override
	public void visitGetInclZ (GetInclZ incl) {
		if (visit (incl)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : incl.outputVar)
				vars.add(v.name);

			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(incl, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)),
					getType(temp.type)),
					new ExternalGetFunctionCall(incl, "getInclZ", vars)));

			incl.getContinuation().accept(this);
			endVisit (incl);
		}
	}
	
	
	
	@Override
	public void visitGetLuminosity (GetLuminosity lumi) {
		if (visit (lumi)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : lumi.outputVar)
				vars.add(v.name);

			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(lumi, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)), 
					getType(temp.type)),
					new ExternalGetFunctionCall(lumi, "getLuminosity", vars)));

			lumi.getContinuation().accept(this);
			endVisit (lumi);
		}
	}
	
	@Override
	public void visitGetTime (GetTime time) {
		if (visit (time)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : time.outputVar)
				vars.add(v.name);

			VarDecl temp = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					temp = v;
			
			block.add(new AssignCommand(time, new structured.VarDecl(
					new structured.Variable(temp.var, vars.get(0)), 
					getType(temp.type)),
					new ExternalGetFunctionCall(time, "getTime", vars)));

			time.getContinuation().accept(this);
			endVisit (time);
		}
	}
	
	@Override
	public void visitLogDouble (LogDouble data) {
		if (visit (data)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : data.inputVar)
				vars.add(v.name);
			
			block.add(new ExternalSetFunctionCall(data, "logDouble", vars));

			data.getContinuation().accept(this);
			endVisit (data);
		}
	}
	
	@Override
	public void visitLogLong (LogLong data) {
		if (visit (data)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : data.inputVar)
				vars.add(v.name);
			
			block.add(new ExternalSetFunctionCall(data, "logLong", vars));

			data.getContinuation().accept(this);
			endVisit (data);
		}
	}
	
	@Override
	public void visitLogString (LogString data) {
		if (visit (data)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : data.inputVar)
				vars.add(v.name);
			
			block.add(new ExternalSetFunctionCall(data, "logString", vars));

			data.getContinuation().accept(this);
			endVisit (data);
		}
	}
	
	@Override
	public void visitMacAddr (MacAddr mac) {
		if (visit (mac)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : mac.outputVar)
				vars.add(v.name);

			VarDecl addr = null;
			for (VarDecl v : varsD)
				if (v.var.name.equals(vars.get(0)))
					addr = v;
			
			block.add(new AssignCommand(mac, new structured.VarDecl(
					new structured.Variable(addr.var, vars.get(0)), 
					getType(addr.type)),
					new ExternalGetFunctionCall(mac, "macAddr", vars)));

			mac.getContinuation().accept(this);
			endVisit (mac);
		}
	}
	
	@Override
	public void visitSetLEDCollor (SetLEDCollor led) {
		if (visit (led)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : led.inputVar)
				vars.add(v.name);
			
			block.add(new ExternalSetFunctionCall(led, "setLEDCollor", vars));

			led.getContinuation().accept(this);
			endVisit (led);
		}
	}
	
	@Override
	public void visitSetLEDOn (SetLEDOn led) {
		if (visit (led)) {
			List<String> vars = new LinkedList<String>();
			for(Variable v : led.inputVar)
				vars.add(v.name);
			
			block.add(new ExternalSetFunctionCall(led, "setLEDOn", vars));

			led.getContinuation().accept(this);
			endVisit (led);
		}
	}
	
	private structured.Type getType(Type t) {
		structured.Type type;
		if (t instanceof TypeBool)
			type = new structured.TypeBool();
		else if (t instanceof TypeDouble)
			type = new structured.TypeDouble();
		else if (t instanceof TypeLong)
			type = new structured.TypeLong();
		else
			type = new structured.TypeString();
		
		return type;
	}	
	
	private intermediate.VarDecl getVarDeclByName(String name) {
		for (intermediate.VarDecl var : varsD) {
			if (var.var.name.equals(name)) return var;
		}
		
		return null;
	}
}