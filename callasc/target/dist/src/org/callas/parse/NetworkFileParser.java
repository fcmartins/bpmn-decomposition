package org.callas.parse;

import java.io.InputStream;
import java.util.List;

import java_cup.runtime.lr_parser;

import org.callas.absyn.NetworkFile;
import org.callas.core.IFileLoader;
import org.tyco.common.errorMsg.ErrorMessage;

/**
 * Parses network file.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class NetworkFileParser extends CallasAbstractParser<NetworkFile> {

	public NetworkFileParser(IFileLoader fileLoader) {
		super(fileLoader);
	}

	@Override
	protected lr_parser createGrm(List<ErrorMessage> errors, String filename,
			InputStream inputStream) {
		return new NetworkGrm(errors, filename, inputStream, fileLoader);
	}
}
