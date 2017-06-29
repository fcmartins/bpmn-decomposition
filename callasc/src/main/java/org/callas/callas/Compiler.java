package org.callas.callas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.callas.absyn.FileSection;
import org.callas.absyn.NetworkFile;
import org.callas.absyn.processes.CallasProcess;
import org.callas.external.Externs;
import org.callas.vm.CVMAPI;
import org.callas.vm.ast.CVMModule;
import org.tyco.common.api.ICompiler;
import org.tyco.common.api.IParser;
import org.tyco.common.api.ITypechecker;
import org.tyco.common.errorMsg.ErrorMessagesException;
import org.tyco.common.symtable.KeyNotFoundException;

/**
 * The command line interface for the compiler.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class Compiler {
	private CallasAPI api;
	private CVMAPI cvmApi = new CVMAPI();

	public Compiler(CallasAPI api) {
		this.api = api;
	}

	public void run(File file, boolean generateAsm)
			throws ErrorMessagesException, IOException {
//		URL fileurl = this.getClass().getClassLoader().getResource(file.toString());
		NetworkFile networkFile = api.createNetworkFileParser().parse(file.getAbsolutePath(), new FileInputStream(file));
		ITypechecker<NetworkFile, Void> typechecker = api.createFullTypechecker();
		typechecker.typecheck(networkFile);

		// use a set to get unique filenames (no need to compile the same source
		// twice)
		Set<String> files = new HashSet<String>();
		for (FileSection field : networkFile.getSensorSections()) {
			try {
				files.add(field.getValue("code"));
			} catch (KeyNotFoundException e) {
				// nothing to do here
			}
		}

		// for each filename, compile the file
		for (String code : files) {
			compile(code, generateAsm);
		}
	}

	private void compile(String filename,
			boolean generateAsm) throws ErrorMessagesException, IOException {
		ICompiler<CallasProcess, CVMModule> compiler = api.createCompiler();
		ICompiler<CVMModule, byte[]> assembler = cvmApi.createAssembler();

//		URL fileurl = this.getClass().getClassLoader().getResource(filename);
		CallasProcess proc = api.createProcessParser().parse(filename, new FileInputStream(filename));
		CVMModule program = compiler.compile(proc);
		int index = filename.lastIndexOf('.');
		if (index != -1) {
			filename = filename.substring(0, index);
		}
		final byte[] data;
		final String ext;
		if (generateAsm) {
			data = program.toString().getBytes();
			ext = ".calasm";
		} else {
			data = assembler.compile(program);
			ext = ".calbc";
		}
		FileOutputStream fileOutputStream = new FileOutputStream(new File(
				filename + ext));
		fileOutputStream.write(data);
		fileOutputStream.close();
	}

	/**
	 * If there is one parameter it is the filename, otherwise the source code
	 * is read through the standard input stream.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException {
		if (args.length != 2 && args.length != 3) {
			usage();
			return;
		}
		if (args.length == 3 && !args[0].equals("-asm")) {
			usage();
			return;
		}
		boolean generateAsm = args.length == 3;

		int offset = args.length == 3 ? 1 : 0;
		CallasAPI api = new CallasAPI();
		File file = new File(args[0 + offset]);
		try {
			IParser<Externs> externsParser = api.createExternsParser();
			String externsFname = args[1 + offset];
			FileInputStream externsStream = new FileInputStream(externsFname);
			Externs externs = externsParser.parse(externsFname, externsStream);
			api.useRegistry(externs);
			Compiler compiler = new Compiler(api);
			compiler.run(file, generateAsm);
		} catch (ErrorMessagesException e) {
			api.printErrors(System.err, e.getErrors());
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void usage() {
		System.err.println("Usage: callasc [-asm] <networkfile.calnet> <externs.caltype>");
		System.exit(-1);
	}

}
