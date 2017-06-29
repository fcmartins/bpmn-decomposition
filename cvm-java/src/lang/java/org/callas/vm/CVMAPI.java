package org.callas.vm;

import org.callas.vm.ast.CVMModule;
import org.callas.vm.parse.Parser;
import org.callas.vm.translate.Translator;
import org.tyco.common.api.ICompiler;
import org.tyco.common.api.IParser;

public class CVMAPI {
	public IParser<CVMModule> createParser() {
		return new Parser();
	}
	
	public ICompiler<CVMModule, byte[]> createAssembler() {
		return new Translator();
	}
}
