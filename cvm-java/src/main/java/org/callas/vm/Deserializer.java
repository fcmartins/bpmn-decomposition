package org.callas.vm;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

public class Deserializer {

	private final DataInput in;

	/**
	 * Wraps the data input to read the contents from.
	 * 
	 * @param in
	 */
	public Deserializer(DataInput in) {
		this.in = in;
	}

	private String readString() throws IOException {
		byte[] buffer = new byte[in.readByte()];
		if (buffer.length > 0) {
			in.readFully(buffer);
		}
		return new String(buffer);
	}

	/**
	 * Reads a tagged value, which is given by a byte that represents a tag and
	 * then its representation.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Object readTaggedValue() throws IOException {
		byte tag = in.readByte();
		switch (tag) {
		case Tags.BOOL:
			return new Boolean(in.readBoolean());
		case Tags.BYTE:
			return new Byte(in.readByte());
		case Tags.DOUBLE:
			return new Double(in.readDouble());
		case Tags.INTEGER:
			return new Integer(in.readInt());
		case Tags.LONG:
			return new Long(in.readLong());
		case Tags.STRING:
			return new String(readString());
		case Tags.MODULE:
			return readModule();
		case Tags.MODULE_DECLARATION:
			return readModuleDeclaration();
		default:
			// flow never gets here
			throw new IOException("Unexpected value tag " + tag);
		}
	}

	private Module readModule() throws IOException {
		// read functions
		byte numberOfFunctions = in.readByte();
		Function[] functions = new Function[numberOfFunctions];
		for (int funcIndex = 0; funcIndex < numberOfFunctions; funcIndex++) {
			functions[funcIndex] = readFunction();
		}
		// build the module
		return new Module(functions);
	}

	protected Function readFunction() throws IOException {
		FunctionDeclaration dec = readFunctionDeclaration();
		byte freeVarsCount = in.readByte();
		Object[] freeVars = new Object[freeVarsCount];
		for (int index = 0; index < freeVarsCount; index++) {
			freeVars[index] = readTaggedValue();
		}
		// build the function
		return new Function(dec, freeVars);
	}

	private FunctionDeclaration readFunctionDeclaration() throws IOException {
		String name = readString();
		byte parametersCount = in.readByte();
		byte localsCount = in.readByte();
		byte[] byteCode = new byte[in.readInt()];
		in.readFully(byteCode);

		// extract the symbols
		byte numberOfSymbols = in.readByte();
		Object[] symbols = new Object[numberOfSymbols];
		for (int symIndex = 0; symIndex < numberOfSymbols; symIndex++) {
			symbols[symIndex] = readTaggedValue();
		}

		return new FunctionDeclaration(name, parametersCount, localsCount,
				byteCode, symbols);
	}

	/**
	 * Reads a passivated call from the network.
	 * 
	 * @return
	 * @throws IOException
	 */
	public Call readCall() throws IOException {
		String funcName = readString();
		byte numberOfParameters = in.readByte();
		// start collecting the data
		Object[] args = new Object[numberOfParameters];
		for (int i = 0; i < numberOfParameters; i++) {
			args[i] = readTaggedValue();
		}
		return new Call(funcName, args);
	}

	private ModuleDeclaration[] readModuleDeclarations() throws IOException {
		// extract the modules
		byte numberOfEntries = in.readByte();
		ModuleDeclaration[] entries = new ModuleDeclaration[numberOfEntries];
		for (int entryIndex = 0; entryIndex < numberOfEntries; entryIndex++) {
			entries[entryIndex] = readModuleDeclaration();
		}
		return entries;
	}

	public ModuleDeclaration readModuleDeclaration()
			throws IOException {
		byte numberOfFuncs = in.readByte();
		FunctionDeclaration funcs[] = new FunctionDeclaration[numberOfFuncs];
		byte[] sizes = new byte[numberOfFuncs];
		for (int i = 0; i < numberOfFuncs; i++) {
			sizes[i] = in.readByte();
			funcs[i] = readFunctionDeclaration();
		}
		ModuleDeclaration entry = new ModuleDeclaration(funcs, sizes);
		return entry;
	}

	/**
	 * Read an array of bytes and convert them to a Call object if possible.
	 * 
	 * @param data
	 *            The buffer that holds (at least) a serialized version of a
	 *            Call.
	 * @return A call object read from the provided array.
	 * @throws IOException
	 *             Thrown if the Call object cannot be parsed.
	 */
	public static Call fromBytesToCall(byte[] data) throws IOException {
		Deserializer deser = new Deserializer(new DataInputStream(
				new ByteArrayInputStream(data)));
		return deser.readCall();
	}
}
