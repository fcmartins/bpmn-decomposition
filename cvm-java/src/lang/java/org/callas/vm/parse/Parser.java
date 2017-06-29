package org.callas.vm.parse;

import java.io.InputStream;
import java.util.List;

import java_cup.runtime.lr_parser;

import org.callas.vm.ast.CVMModule;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.util.AbstractParser;

public class Parser extends AbstractParser<CVMModule> {

	@Override
	protected lr_parser createGrm(List<ErrorMessage> errors, String filename,
			InputStream inputStream) {
		return new Grm(errors, filename, inputStream);
	}

}
