package org.callas.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.callas.absyn.types.CallasType;
import org.callas.absyn.types.CodeType;
import org.callas.absyn.types.FunctionType;
import org.callas.absyn.types.RecursiveType;
import org.callas.core.IFileLoader;
import org.callas.external.Externs;
import org.callas.external.Nop;
import org.tyco.common.api.IParser;
import org.tyco.common.errorMsg.ErrorMessagesException;
import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.errorMsg.msgs.SyntacticError;
import org.tyco.common.symbol.Symbol;

/**
 * Converts the typedefs into a registry.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 13, 2009
 * 
 */
public class ExternsParser implements
		IFileAdapter<Map<Symbol, RecursiveType>, Externs> {

	public Externs adapt(String filename,
			Map<Symbol, RecursiveType> types) throws ErrorMessagesException {
		RecursiveType recursiveType = types.get(Symbol.symbol("Extern"));
		if (recursiveType == null) {
			throw new ErrorMessagesException(new SyntacticError(
					new SourceLocation(filename, null),
					"Type 'Extern' that declares external functions is not declared."));
		}
		CodeType type = (CodeType) recursiveType.type;
		Externs registry = new Externs();
		for (Entry<Symbol, FunctionType> entry : type.functions.entrySet()) {
			List<CallasType> params = new ArrayList<CallasType>(entry
					.getValue().parameters);
			params.remove(0);
			FunctionType newFunc = new FunctionType(entry.getValue()
					.getSourceLocation(), params, entry.getValue().result);
			registry.registerOperation(entry.getKey().toString(), new Nop(
					newFunc));
		}
		return registry;
	}

	public static IParser<Externs> createParser(
			IFileLoader fileLoader) {
		return new AdaptedParser<Map<Symbol, RecursiveType>, Externs>(
				new TypeDefsParser(fileLoader), new ExternsParser());
	}
}
