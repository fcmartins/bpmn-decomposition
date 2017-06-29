package org.callas.absyn;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * An AST node, containing the source code location ({@link SourceLocation}).
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Sep 15, 2008
 * 
 */
public abstract class Absyn {
	/**
	 * The location of this node in the source code.
	 */
	private SourceLocation sourceLocation;

	/**
	 * Creates a node by providing its source location.
	 * 
	 * @param sourceLocation
	 */
	public Absyn(SourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	/**
	 * Utility constructor that accepts a null location.
	 */
	public Absyn() {
		this.sourceLocation = null;
	}

	/**
	 * Returns the source location.
	 * 
	 * @return
	 */
	public SourceLocation getSourceLocation() {
		return sourceLocation;
	}

	/**
	 * Defines the new source location.
	 * 
	 * @param sourceLocation
	 */
	public void setSourceLocation(SourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof Absyn
				&& (this.toString().equals(other.toString()));
	}
}
