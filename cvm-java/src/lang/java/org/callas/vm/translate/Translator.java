package org.callas.vm.translate;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.callas.vm.FunctionDeclaration;
import org.callas.vm.ModuleDeclaration;
import org.callas.vm.Serializer;
import org.callas.vm.ast.CVMBBool;
import org.callas.vm.ast.CVMBDouble;
import org.callas.vm.ast.CVMBLong;
import org.callas.vm.ast.CVMBString;
import org.callas.vm.ast.CVMFunction;
import org.callas.vm.ast.CVMModule;
import org.callas.vm.ast.CVMStmt;
import org.callas.vm.ast.CVMValue;
import org.tyco.common.api.ICompiler;
import org.tyco.common.symbol.Symbol;

/**
 * Translates the abstract representation of the CVM bytecode into the CVM's
 * internal representation.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * 
 */
public class Translator implements ICompiler<CVMModule, byte[]> {

	private byte[] compileStmts(List<CVMStmt> stmts) {
		StmtTranslator trans = new StmtTranslator();
		List<Byte> result = new LinkedList<Byte>();
		for (CVMStmt stmt : stmts) {
			result.addAll(trans.compile(stmt));
		}
		return toArray(result);
	}

	private FunctionDeclaration compileFunc(String name, CVMFunction func) {
		Object[] syms = compileValues(func.constants);
		byte[] code = compileStmts(func.stmts);
		return new FunctionDeclaration(name, func.parametersCount,
				func.localsCount, code, syms);
	}

	private ModuleDeclaration compileModule(CVMModule mod) {
		byte[] freeVars = new byte[mod.functions.size()];
		FunctionDeclaration[] funcs = new FunctionDeclaration[mod.functions
				.size()];
		int index = 0;
		for (Entry<Symbol, CVMFunction> entry : mod.functions.entrySet()) {
			funcs[index] = compileFunc(entry.getKey().toString(), entry
					.getValue());
			freeVars[index] = entry.getValue().freeVarsCount;
			index++;
		}
		return new ModuleDeclaration(funcs, freeVars);
	}

	private Object[] compileValues(List<CVMValue> constants) {
		Object[] result = new Object[constants.size()];
		int index = 0;
		for (CVMValue val : constants) {
			result[index] = compileValue(val);
			index++;
		}
		return result;
	}

	private Object compileValue(CVMValue val) {
		if (val instanceof CVMBBool) {
			return new Boolean(((CVMBBool) val).value);
		} else if (val instanceof CVMBDouble) {
			return new Double(((CVMBDouble) val).value);
		} else if (val instanceof CVMBLong) {
			return new Long(((CVMBLong) val).value);
		} else if (val instanceof CVMBString) {
			return ((CVMBString) val).value;
		} else if (val instanceof CVMModule) {
			return compileModule((CVMModule) val);
		}
		throw new IllegalArgumentException("Value not supported: " + val);
	}

	private static byte[] toArray(List<Byte> code) {
		byte[] result = new byte[code.size()];
		int index = 0;
		for (Byte node : code) {
			result[index] = node;
			index++;
		}
		return result;
	}

	public byte[] compile(CVMModule from) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		DataOutput out = new DataOutputStream(outStream);
		Serializer ser = new Serializer(out);
		try {
			ser.writeModuleDeclaration(compileModule(from));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return outStream.toByteArray();
	}

}
