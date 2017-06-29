package org.callas.absyn.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * The type of a process abstraction.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 2, 2008
 * 
 */
public class FunctionType extends CallasType {
	public final List<CallasType> parameters;
	public final CallasType result;

	public FunctionType(SourceLocation location, List<CallasType> arguments, CallasType result) {
		super(location);
		this.parameters = Collections.unmodifiableList(new ArrayList<CallasType>(arguments));
		this.result = result;
	}
	
	public FunctionType(CallasType result, CallasType...params) {
		this(null, Arrays.asList(params), result);
	}
	
	public boolean isNilType(){
		return this.result.toString().equals("rec Nil.{}");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String paramsString = parameters.toString();
		paramsString = paramsString.substring(1, paramsString.length() - 1);
		return "(" + paramsString + ")->(" + result + ")";
	}
}
