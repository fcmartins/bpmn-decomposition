package org.callas.parse;

import java.util.Map;
import java.util.TreeMap;

import org.callas.absyn.types.CodeType;
import org.callas.absyn.types.FunctionType;
import org.callas.absyn.types.RecursiveType;
import org.callas.core.IFileLoader;
import org.callas.util.TypeEquality;
import org.tyco.common.api.IParser;
import org.tyco.common.errorMsg.ErrorMessagesException;
import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.errorMsg.msgs.SyntacticError;
import org.tyco.common.symbol.Symbol;

/**
 * Parses the network interface.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class SensorTypeParser implements IFileAdapter<Map<Symbol, RecursiveType>, CodeType> {

	public CodeType adapt(String filename, Map<Symbol, RecursiveType> types)
			throws ErrorMessagesException {
		RecursiveType type = types.get(Symbol.symbol("Sensor"));
		if (type == null) {
			throw new ErrorMessagesException(new SyntacticError(new SourceLocation(filename),
					"Type 'Sensor' that declares the sensor type is not declared."));
		}
		CodeType sensorType = (CodeType) type.type;
		TypeEquality typeEq = new TypeEquality();
		Map<Symbol, FunctionType> funcs = new TreeMap<Symbol, FunctionType>(sensorType.functions);
		if (!funcs.containsKey(Symbol.symbol("init")))
			throw new ErrorMessagesException(new SyntacticError(new SourceLocation(filename),
			"Function 'init' in module Sensor is missing."));
		else if (!funcs.get(Symbol.symbol("init")).isNilType()) 
			throw new ErrorMessagesException(new SyntacticError(new SourceLocation(filename),
			"Function of signature 'Nil init' in module Sensor is missing."));
		return (CodeType) typeEq.unfold(new RecursiveType(type.variable, new CodeType(funcs)));
	}
	
	/**
	 * Creates the sensor code type parser.
	 * @param fileLoader
	 * @return
	 */
	public static final IParser<CodeType> createParser(IFileLoader fileLoader) {
		return new AdaptedParser<Map<Symbol, RecursiveType>, CodeType>(new TypeDefsParser(fileLoader), new SensorTypeParser());
	}
}
