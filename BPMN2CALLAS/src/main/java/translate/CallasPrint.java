package translate;

import java.util.List;

import structured.AssignCommand;
import structured.BlockCommand;
import structured.Command;
import structured.Expression;
import structured.ExternalGetFunctionCall;
import structured.ExternalSetFunctionCall;
import structured.Function;
import structured.IfStructured;
import structured.SendFunctionCall;
import structured.TimedFunction;
import structured.Type;
import structured.TypeBool;
import structured.TypeDouble;
import structured.TypeInteger;
import structured.TypeString;
import structured.Value;
import structured.VarDecl;
import structured.Variable;
import structured.VisitDepthFirstVisitor;

public class CallasPrint extends VisitDepthFirstVisitor {
	
	private int depth;
	private StringBuilder result;
	private String functionType;
	
	public CallasPrint (List<Command> functions, StringBuilder result) {
		depth = 1;
		this.result = result;
		result.append("module sensor of Sensor:\n");
		
		for (Command c : functions) {
			Function f = (Function) c;
			
			printSpaces();
			result.append("def " + f.signature.name  + "(self");
			for (VarDecl arg : f.signature.arguments) result.append("," + arg.var.name);		
			result.append("):\n");
			depth++;
			printSpaces();
			result.append("c1 = self.sinkIpPort()\n");
			depth--;
			f.accept(this);
			depth++;
			printSpaces();
			
			// for NIL return
			result.append("pass\n");
			result.append("\n");
			
			depth--;
		}
		
		// write timed invocation
		printSpaces();
		result.append("def timer_init(self):\n");
		depth++;
		for (Command c : functions) {
			Function f = (Function) c;
			
			if (f instanceof TimedFunction) {
				
				TimedFunction tf = (TimedFunction)f;
				printSpaces();
				result.append(f.signature.name + "(");
				
				boolean firstArg = true;
				for (VarDecl arg : f.signature.arguments) {
					if (firstArg) firstArg = false;
					else result.append(",");
					result.append(arg.var.name);
				}
				
				result.append(") every " + tf.getPeriodicity().name + "\n");
			}
		}
		printSpaces();
		result.append("pass\n");
	}
	
	public String getFunctionName() {
		return functionType;
	}
	
	
	@Override
	public boolean visit(AssignCommand ac) {
		return true;
	}

	@Override
	public void visitAssignCommand(AssignCommand ac) {
		if (visit (ac)) {
//			printSpaces();
//			result.append("# element:" + ac.id + "\n");
			printSpaces();
			functionType = new String(ac.lhs.type.toString());
			ac.lhs.accept(this);
			result.append(" = ");
			ac.rhs.accept(this);
			result.append("\n");
			endVisit (ac);
		}
	}

	@Override
	public void endVisit(AssignCommand ac) { }

	@Override
	public boolean visit(BlockCommand bc) {
		return true;
	}

	@Override
	public void visitBlockCommand(BlockCommand bc) {
		if (visit (bc)) {
			depth++;
			for (Command c : bc.commands)
				c.accept(this);
			depth--;
//			System.out.println();
			endVisit (bc);
		}
	}

	@Override
	public void endVisit(BlockCommand bc) {	
		
	}
	
	@Override
	public boolean visit(Command c) {
		return true;
	}

	@Override
	public void visitCommand(Command c) { }

	@Override
	public void endVisit(Command c) { }

	@Override
	public boolean visit(ExternalGetFunctionCall efc) {
		return true;
	}

	@Override
	public void visitExternalGetFunctionCall(ExternalGetFunctionCall efc) {
		if (visit (efc)) {			
			result.append("extern " + efc.functionName + "()");
			endVisit (efc);
		}
	}

	@Override
	public void endVisit(ExternalGetFunctionCall efc) { }

	@Override
	public boolean visit(ExternalSetFunctionCall efc) {
		return true;
	}

	@Override
	public void visitExternalSetFunctionCall(ExternalSetFunctionCall efc) {
		if (visit (efc)) {
			printSpaces();
			result.append("extern " + efc.functionName + "(");
			int size = efc.arguments.size();
			for (int i = 0; i < size; i++) {
				result.append(efc.arguments.get(i));
				if (i < size - 1) {
					result.append(",");
				}
			}
			result.append(")\n");
			endVisit (efc);
		}
	}

	@Override
	public void endVisit(ExternalSetFunctionCall efc) { }

	@Override
	public boolean visit(IfStructured ifs) {
		return true;
	}

	@Override
	public void visitIfStructured(IfStructured ifs) {
		if (visit (ifs)) {
//			printSpaces();
//			result.append("# element:" + ifs.id + "\n");
			printSpaces();
			result.append("if ");
			ifs.guard.accept(this);
			
			result.append(":\n");
			ifs.thenBranches.accept(this);
			depth++;
			printSpaces();
			result.append("pass\n");
			depth--;
			
			printSpaces();
			result.append("else");
			
			result.append(":\n");
			ifs.elseBranch.accept(this);
			depth++;
			printSpaces();
			result.append("pass\n");
			depth--;

			endVisit (ifs);
		}
	}

	@Override
	public void endVisit(IfStructured ifs) { }

	@Override
	public boolean visit(SendFunctionCall sfc) {
		return true;
	}

	@Override
	public void visitSendFunctionCall(SendFunctionCall sfc) {
		if (visit (sfc)) {
//			printSpaces();
//			result.append("# element:" + sfc.id + "\n");
			for (structured.VarDecl arg : sfc.arguments) {
				printSpaces();
				result.append("select c1 send " + "setVariable_" + arg.type + "(" );
				result.append("\"" + arg.var.name + "\"");
				result.append(", ");
				result.append(arg.var.name);
				result.append(")\n");
			}
			
			endVisit (sfc);
		}
	}

	@Override
	public void endVisit(SendFunctionCall sfc) { }

	@Override
	public boolean visit(TypeBool tb) {
		return true;
	}

	@Override
	public void visitTypeBool(TypeBool tb) {
		if (visit (tb)) {
			endVisit (tb);
		}
	}

	@Override
	public void endVisit(TypeBool tb) {	}

	@Override
	public boolean visit(TypeDouble td) {
		return true;
	}

	@Override
	public void visitTypeDouble(TypeDouble td) {
		if (visit (td)) {
			endVisit (td);
		}
	}

	@Override
	public void endVisit(TypeDouble td) { }

	@Override
	public boolean visit(TypeInteger ti) {
		return true;
	}

	@Override
	public void visitTypeInteger(TypeInteger ti) {
		if (visit (ti)) {
			endVisit (ti);
		}
	}

	@Override
	public void endVisit(TypeInteger ti) { }

	@Override
	public boolean visit(TypeString ts) {
		return true;
	}

	@Override
	public void visitTypeString(TypeString ts) {
		if (visit (ts)) {
			endVisit (ts);
		}
	}

	@Override
	public void endVisit(TypeString ts) { }

	@Override
	public boolean visit(Value v) {
		return true;
	}

	@Override
	public void visitValue(Value v) {
		if (visit (v)) {
			result.append(v.name);
			endVisit (v);
		}
	}

	@Override
	public void endVisit(Value v) {	}

	@Override
	public boolean visit(VarDecl vd) {
		return true;
	}

	@Override
	public void visitVarDecl(VarDecl vd) {
		if (visit (vd)) {
			vd.type.accept(this);
			vd.var.accept(this);
			endVisit (vd);
		}
	}

	@Override
	public void endVisit(VarDecl vd) { }

	@Override
	public boolean visit(Variable var) {
		return true;
	}

	@Override
	public void visitVariable(Variable var) {
		if (visit (var)) {
			result.append(var.name);
			endVisit (var);
		}
	}

	@Override
	public void endVisit(Variable var) { }

	@Override
	public boolean visit(Type t) {
		return true;
	}

	@Override
	public void visitType(Type t) { }

	@Override
	public void endVisit(Type t) { }

	@Override
	public boolean visit(Expression e) {
		return true;
	}

	@Override
	public void visitExpression(Expression e) {	}

	@Override
	public void endVisit(Expression e) { }
	
	
	private void printSpaces() {
		for (int i = 0; i < depth; i++)
			result.append("   ");
	}
}
