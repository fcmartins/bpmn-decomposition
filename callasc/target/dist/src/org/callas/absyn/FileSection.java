package org.callas.absyn;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.callas.core.IFileLoader;
import org.tyco.common.api.IParser;
import org.tyco.common.errorMsg.ErrorMessagesException;
import org.tyco.common.errorMsg.ISourceLocationHolder;
import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.errorMsg.msgs.SyntacticError;
import org.tyco.common.symtable.KeyNotFoundException;

public class FileSection implements ISourceLocationHolder {

	private SourceLocation location;
	
	private Map<String, SourceValue<String>> metadata;

	private IFileLoader loader;
	
	/**
	 * @param location
	 * @param metadata
	 */
	public FileSection(IFileLoader loader, SourceLocation location,
			Map<String, SourceValue<String>> metadata) {
		this.location = location;
		this.metadata = metadata;
		this.loader = loader;
	}

	public SourceLocation getSourceLocation() {
		return location;
	}
	
	public Iterable<Entry<String, SourceValue<String>>> getMetadata() {
		return metadata.entrySet();
	}
	
	public SourceValue<String> get(String key) throws KeyNotFoundException {
		SourceValue<String> sourceValue = metadata.get(key);
		if (sourceValue == null) {
			throw new KeyNotFoundException(key);
		}
		return sourceValue;
	}
	public String getValue(String key) throws KeyNotFoundException {
		return get(key).getValue();
	}

	public void put(SourceLocation loc, String k, String v) {
		metadata.put(k, new SourceValue<String>(loc, v));
	}

	private SourceValue<InputStream> getInputStream(String key) throws ErrorMessagesException, KeyNotFoundException {
		SourceValue<String> field = get(key);
		try {
			InputStream stream = loader.loadFile(getValue(key));
			return new SourceValue<InputStream>(field.getSourceLocation(), stream);
		} catch (IOException e) {
			throw new ErrorMessagesException(new SyntacticError(field
					.getSourceLocation(), "File not found: " + field.getValue()));
		}
	}

	public <T> T parseFieldWith(String key, IParser<T> parser) throws ErrorMessagesException, KeyNotFoundException {
		SourceValue<InputStream> stream = getInputStream(key);
		return parser.parse(get(key).getValue(), stream.getValue());
	}
}
