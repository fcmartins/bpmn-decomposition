package org.callas.absyn.processes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.callas.absyn.types.CodeType;
import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.symbol.Symbol;

/**
 * An anonymous module.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public class Code extends CallasValue {
	/**
	 * The empty module.
	 */
	public final static Code NIL = new Code();
	
	/**
	 * Where the processes are kept.
	 */
	public final Map<Symbol, ProcessAbstraction> processes;

	/**
	 * The type of this module.
	 */
	public final CodeType type;

	public Code(SourceLocation loc,
			Map<Symbol, ProcessAbstraction> processes, CodeType type) {
		super(loc);
		this.processes = Collections.unmodifiableMap(new TreeMap<Symbol, ProcessAbstraction>(processes));
		this.type = type;
	}

	public Code(Map<Symbol, ProcessAbstraction> processes,
			CodeType type) {
		super();
		this.processes = Collections.unmodifiableMap(new TreeMap<Symbol, ProcessAbstraction>(processes));
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public Code(SourceLocation loc) {
		this(loc, Collections.EMPTY_MAP,
				new CodeType(loc));
	}

	private Code() {
		this(new HashMap<Symbol, ProcessAbstraction>(),
				CodeType.NIL_TYPE);
	}

	@Override
	public String toString() {
		return processes + ":" + type;
	}
}