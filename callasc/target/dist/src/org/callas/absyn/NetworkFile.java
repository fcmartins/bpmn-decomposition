package org.callas.absyn;

import java.util.*;

import org.callas.core.IFileLoader;
import org.tyco.common.errorMsg.SourceLocation;

public class NetworkFile extends FileSection {
	private final List<FileSection> sensors;

	public NetworkFile(IFileLoader loader, String filename) {
		super(loader, new SourceLocation(filename, null), new LinkedHashMap<String, SourceValue<String>>());
		this.sensors = new LinkedList<FileSection>();
	}
	
	/*
	 * @return the sensors
	 */
	public List<FileSection> getSensorSections() {
		return sensors;
	}

	public void add(FileSection s) {
		sensors.add(s);
	}
}
