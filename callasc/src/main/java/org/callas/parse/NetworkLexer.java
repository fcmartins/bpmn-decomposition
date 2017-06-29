package org.callas.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import java_cup.runtime.Symbol;

public class NetworkLexer extends PreNetworkLexer {
	
	private boolean hasFinished = false;
	
	private boolean lastWasNL = false;
	
	public NetworkLexer(InputStream in) {
		super(in);
	}

	public NetworkLexer(Reader in) {
		super(in);
	}

	@Override
	public Symbol next_token() throws IOException {
		if (hasFinished) {
			if (levels.size() <= 1) {
				return new java_cup.runtime.Symbol(NetworkSym.EOF);
			}
			levels.pop();
			return token(NetworkSym.DEDENT);
		}
		
		Symbol next_token = super.next_token();
		if (next_token.sym == NetworkSym.EOF) {
			hasFinished = true;
			return lastWasNL ? next_token() : token(NetworkSym.NL);
		}
		lastWasNL = next_token.sym == NetworkSym.NL;
		return next_token;
	}
}
