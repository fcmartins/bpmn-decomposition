package org.callas.parse;

import java.io.InputStream;
import java.util.List;

import java_cup.runtime.lr_parser;

import org.callas.absyn.processes.CallasProcess;
import org.callas.core.IFileLoader;
import org.tyco.common.errorMsg.ErrorMessage;

/**
 * The default Callas process parser.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date May 29, 2009
 * 
 */
public class CallasProcessParser extends CallasAbstractParser<CallasProcess> {

	public CallasProcessParser(IFileLoader fileLoader) {
		super(fileLoader);
	}

	@Override
	protected lr_parser createGrm(List<ErrorMessage> errors, String filename,
			InputStream stream) {
		return new ProcessGrm(errors, filename, stream, fileLoader);
	}
}
