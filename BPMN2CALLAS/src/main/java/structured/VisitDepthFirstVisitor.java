package structured;

public class VisitDepthFirstVisitor implements Visitor {

	@Override
	public boolean visit(AssignCommand ac) {
		return true;
	}

	@Override
	public void visitAssignCommand(AssignCommand ac) {
		if (visit (ac)) {
//			System.out.print(ac.lhs + " = " + ac.rhs);
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
//			System.out.println("{");
			for (Command c : bc.commands)
				c.accept(this);
//			System.out.println("}");
			endVisit (bc);
		}
	}

	@Override
	public void endVisit(BlockCommand bc) {	}
	
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
			endVisit (efc);
		}

	}

	@Override
	public void endVisit(ExternalSetFunctionCall efc) { }
	
	@Override
	public boolean visit(Function f) {
		return true;
	}

	@Override
	public void visitFunction(Function f) {
		if (visit (f)) {
			f.blockCommand.accept(this);
			endVisit (f);
		}
	}

	@Override
	public void endVisit(Function f) {	}

	@Override
	public boolean visit(IfStructured ifs) {
		return true;
	}

	@Override
	public void visitIfStructured(IfStructured ifs) {
		if (visit (ifs)) {
//			System.out.print("if (");
			ifs.guard.accept(this);
			
//			System.out.println(") then");
			ifs.thenBranches.accept(this);
			
//			System.out.println("else");
			ifs.elseBranch.accept(this);
			
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
	public boolean visit(TypeLong tl) {
		return true;
	}

	@Override
	public void visitTypeLong(TypeLong tl) {
		if (visit (tl)) {
			endVisit (tl);
		}
	}

	@Override
	public void endVisit(TypeLong tl) { }

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
	public void visitType(Type t) {
		if (visit (t)) {
			endVisit (t);
		}		
	}

	@Override
	public void endVisit(Type t) { }

	@Override
	public boolean visit(Expression e) {
		return true;
	}

	@Override
	public void visitExpression(Expression e) {
		if (visit (e)) {
			endVisit (e);
		}
	}

	@Override
	public void endVisit(Expression e) { }

}
