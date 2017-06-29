package org.callas.vm;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Utility class to write {@link Program}s and passivated {@link Call}s to a
 * data output.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * 
 */
public class Serializer {
	private final DataOutput out;

	/**
	 * Wraps a data output.
	 * 
	 * @param out
	 */
	public Serializer(DataOutput out) {
		this.out = out;
	}

	private void writeString(String data) throws IOException {
		out.writeByte(data.length());
		out.write(data.getBytes());
	}

	/**
	 * Writes a passivated call to the data output.
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void writeCall(Call message) throws IOException {
		if (message.arguments.length > Byte.MAX_VALUE) {
			throw new IOException("Too many arguments.");
		}
		writeString(message.functionName);
		out.writeByte((byte) message.arguments.length);

		// parameters
		for (int i = 0; i < message.arguments.length; i++) {
			writeTaggedValue(message.arguments[i]);
		}
	}

	private void writeTaggedValue(Object o) throws IOException {
		if (o instanceof String) {
			out.writeByte(Tags.STRING);
			writeString((String) o);
		} else if (o instanceof Long) {
			out.writeByte(Tags.LONG);
			out.writeLong(((Long) o).longValue());
		} else if (o instanceof Integer) {
			out.writeByte(Tags.INTEGER);
			out.writeInt(((Integer) o).intValue());
		} else if (o instanceof Byte) {
			out.writeByte(Tags.BYTE);
			out.writeByte(((Byte) o).byteValue());
		} else if (o instanceof Double) {
			out.writeByte(Tags.DOUBLE);
			out.writeDouble(((Double) o).doubleValue());
		} else if (o instanceof Boolean) {
			out.writeByte(Tags.BOOL);
			out.writeBoolean(((Boolean) o).booleanValue());
		} else if (o instanceof Module) {
			out.writeByte(Tags.MODULE);
			writeModule((Module) o);
		} else if (o instanceof ModuleDeclaration) {
			out.writeByte(Tags.MODULE_DECLARATION);
			writeModuleDeclaration((ModuleDeclaration) o);
		} else {
			// flow never gets here
			throw new IOException("Unsupported class of value " + o.getClass());
		}
	}

	private void writeModule(Module module) throws IOException {
		String[] names = module.getNames();
		// functions
		out.writeByte((byte) names.length);
		for (int i = 0; i < names.length; i++) {
			writeFunction(module.lookup(names[i]));
		}
	}

	protected void writeFunction(Function function) throws IOException {
		writeFunctionDeclaration(function.declaration);
		out.writeByte((byte) function.freeVariables.length);
		for (int i = 0; i < function.freeVariables.length; i++) {
			writeTaggedValue(function.freeVariables[i]);
		}
	}

	private void writeFunctionDeclaration(FunctionDeclaration funcDec)
			throws IOException {
		writeString(funcDec.name);
		out.writeByte(funcDec.parametersCount);
		out.writeByte(funcDec.localsCount);
		out.writeInt(funcDec.byteCode.length);
		out.write(funcDec.byteCode);
		out.writeByte((byte) funcDec.symbols.length);
		for (int i = 0; i < funcDec.symbols.length; i++) {
			writeTaggedValue(funcDec.symbols[i]);
		}
	}

	public void writeModuleDeclaration(ModuleDeclaration modDec)
			throws IOException {
		String[] funcNames = modDec.getNames();
		out.writeByte((byte) funcNames.length);
		for (int i = 0; i < funcNames.length; i++) {
			// write the number of environments
			String functionName = funcNames[i];
			out.writeByte((byte) modDec.getFreeVarsCount(functionName));
			// now write the function declaration
			writeFunctionDeclaration(modDec.lookup(functionName));
		}
	}

	/**
	 * Utility function to generate an array of bytes from a Call object.
	 * 
	 * @param call The object to serialize.
	 * @return The serialized data.
	 */
	public static byte[] fromCallToBytes(Call call) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Serializer ser = new Serializer(new DataOutputStream(out));
		try {
			ser.writeCall(call);
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not parse: " + call.toString());
		}
		return out.toByteArray();
	}
}
