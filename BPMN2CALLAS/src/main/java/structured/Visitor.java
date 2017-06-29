package structured;

public interface Visitor {
	
	public boolean visit (AssignCommand ac);
	public void visitAssignCommand (AssignCommand ac);
	public void endVisit (AssignCommand ac);
	
	public boolean visit (BlockCommand bc);
	public void visitBlockCommand (BlockCommand bc);
	public void endVisit (BlockCommand bc);
	
	public boolean visit (Command c);
	public void visitCommand (Command c);
	public void endVisit (Command c);
	
	public boolean visit (Expression e);
	public void visitExpression (Expression e);
	public void endVisit (Expression e);
	
	public boolean visit (ExternalGetFunctionCall efc);
	public void visitExternalGetFunctionCall (ExternalGetFunctionCall efc);
	public void endVisit (ExternalGetFunctionCall efc);
	
	public boolean visit (ExternalSetFunctionCall efc);
	public void visitExternalSetFunctionCall (ExternalSetFunctionCall efc);
	public void endVisit (ExternalSetFunctionCall efc);
	
	public boolean visit (Function f);
	public void visitFunction (Function f);
	public void endVisit (Function f);

	public boolean visit (IfStructured ifs);
	public void visitIfStructured (IfStructured ifs);
	public void endVisit (IfStructured ifs);
	
	public boolean visit (SendFunctionCall sfc);
	public void visitSendFunctionCall (SendFunctionCall sfc);
	public void endVisit (SendFunctionCall sfc);
	
	public boolean visit (Type t);
	public void visitType (Type t);
	public void endVisit (Type t);
	
	public boolean visit (TypeBool tb);
	public void visitTypeBool (TypeBool tb);
	public void endVisit (TypeBool tb);
	
	public boolean visit (TypeDouble td);
	public void visitTypeDouble (TypeDouble td);
	public void endVisit (TypeDouble td);
	
	public boolean visit (TypeInteger ti);
	public void visitTypeInteger (TypeInteger ti);
	public void endVisit (TypeInteger ti);
	
	public boolean visit (TypeLong tl);
	public void visitTypeLong (TypeLong tl);
	public void endVisit (TypeLong tl);
	
	public boolean visit (TypeString ts);
	public void visitTypeString (TypeString ts);
	public void endVisit (TypeString ts);
	
	public boolean visit (Value v);
	public void visitValue (Value v);
	public void endVisit (Value v);
	
	public boolean visit (VarDecl vd);
	public void visitVarDecl (VarDecl vd);
	public void endVisit (VarDecl vd);
	
	public boolean visit (Variable var);
	public void visitVariable (Variable var);
	public void endVisit (Variable var);
}
