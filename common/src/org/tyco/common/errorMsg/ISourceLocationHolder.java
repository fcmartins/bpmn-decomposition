package org.tyco.common.errorMsg;

/**
 * Holds a source location.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Sep 10, 2008
 *
 */
public interface ISourceLocationHolder {
	/**
	 * The source location associated with this holder.
	 * @return The source location.
	 */
	SourceLocation getSourceLocation();
}
