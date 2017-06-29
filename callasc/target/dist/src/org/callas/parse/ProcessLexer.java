package org.callas.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import java_cup.runtime.Symbol;

public class ProcessLexer extends PreProcessLexer {
	
	private boolean hasFinished = false;
	
	private boolean lastWasNL = false;
	
	public ProcessLexer(InputStream in) {
		super(in);
	}

	public ProcessLexer(Reader in) {
		super(in);
	}

	@Override
	public Symbol next_token() throws IOException {
		if (hasFinished) {
			if (levels.size() <= 1) {
				return new java_cup.runtime.Symbol(ProcessSym.EOF);
			}
			levels.pop();
			return token(ProcessSym.DEDENT);
		}
		
		Symbol next_token = super.next_token();
		if (next_token.sym == ProcessSym.EOF) {
			hasFinished = true;
			return lastWasNL ? next_token() : token(ProcessSym.NL);
		}
		lastWasNL = next_token.sym == ProcessSym.NL;
		return next_token;
	}
}
