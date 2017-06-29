package org.callas.absyn.processes;
import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.symbol.Symbol;
/**
 *Syntax Tree representation of a variable
 */
public class Variable extends CallasValue {
    /**
     * Variable name
     */
    public Symbol name;
     /**
     * @parame name Variable name
     */
    public Variable(SourceLocation loc, Symbol name) {
    	super(loc);
    	this.name=name;
    }
    
    public Variable(Symbol name) {
    	super();
    	this.name = name;
    }    
    
    public Variable(String name) {
    	this(Symbol.symbol(name));
    }
    
    public Variable(SourceLocation loc, String name) {
    	this(loc,Symbol.symbol(name));
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name.toString();
	}
}
