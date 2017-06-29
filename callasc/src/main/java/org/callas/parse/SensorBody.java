package org.callas.parse;

import java.util.LinkedHashMap;
import java.util.Map;

import org.callas.absyn.FileSection;
import org.callas.absyn.SourceValue;
import org.callas.core.IFileLoader;
import org.tyco.common.errorMsg.SourceLocation;

/**
 * Used internally by the networkf file parser to build sensors.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
class SensorBody {

	private final Map<String, SourceValue<String>> contents = new LinkedHashMap<String, SourceValue<String>>();

	private IFileLoader loader;
	
	/**
	 * @param loader
	 */
	public SensorBody(IFileLoader loader) {
		this.loader = loader;
	}

	/**
	 * Ones one more property.
	 * 
	 * @param k
	 * @param v
	 */
	public void put(SourceLocation loc, String k, String v) {
		contents.put(k, new SourceValue<String>(loc, v));
	}

	/**
	 * @return the contents
	 */
	public Map<String, SourceValue<String>> getContents() {
		return contents;
	}

	public FileSection create(
			SourceLocation location) {
		return new FileSection(loader, location, contents);
	}

}
