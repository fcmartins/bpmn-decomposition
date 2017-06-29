package org.callas.parse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java_cup.runtime.lr_parser;

import org.callas.absyn.types.RecursiveType;
import org.callas.core.IFileLoader;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.symbol.Symbol;

class TypeDefsParser extends CallasAbstractParser<Map<Symbol, RecursiveType>> {
	public TypeDefsParser(IFileLoader fileLoader) {
		super(fileLoader);
	}

	private Map<Symbol, RecursiveType> types = new HashMap<Symbol, RecursiveType>();
	private String filename;

	@Override
	protected lr_parser createGrm(List<ErrorMessage> errors, String fname,
			InputStream stream) {
		this.filename = fname;
		ProcessGrm processGrm = new ProcessGrm(errors, filename, stream, fileLoader);
		types = processGrm.getTypes();
		return processGrm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.callas.parse.AbstractParser#adaptParsed(java.util.List,
	 * java.lang.Object)
	 */
	@Override
	protected Map<Symbol, RecursiveType> adaptParsed(List<ErrorMessage> errors,
			Object parsed) {
		return types;
	}
}
