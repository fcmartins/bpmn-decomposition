package main;

import intermediate.AbSyn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

//import org.callas.callas.CallasAPI;
//import org.callas.callas.Compiler;
//import org.callas.external.Externs;
import org.tyco.common.api.IParser;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.ErrorMessagesException;

import structured.BlockCommand;
import structured.Command;
import translate.CreateCallasFiles;
import bpmn2.TTask;

public class CompilerDealer {
	public final String mainFile, udpFile;
	public final BlockCommand block; 
	
	private CreateCallasFiles ccf;
	
	public CompilerDealer(BlockCommand b) {
		block = b;
		mainFile = "main.calnet";
		udpFile = "udp.caltype";
	}
	
	public void createCallasFiles(StringBuilder result, String functionName) 
			throws IOException {
		ccf = new CreateCallasFiles(result, functionName);
	}
	
	public void createCompiler() {
//		CallasAPI api = new CallasAPI();
//		File main = new File(mainFile);
//		try {
//			IParser<Externs> externsParser = api.createExternsParser();
//			FileInputStream externsStream = new FileInputStream(udpFile);
//			Externs externs = externsParser.parse(udpFile, externsStream);
//			api.useRegistry(externs);
//			
//			Compiler compiler = new Compiler(api);
//			compiler.run(main, false);
//		} catch (ErrorMessagesException e) {
////			for (ErrorMessage error : e.getErrors()) 
////				printError(error.location.line);
//			api.printErrors(System.err, e.getErrors());
//			System.exit(-1);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	private void printError(int line) {
		try {
			Scanner scn = new Scanner(new File("node.callas"));
			String elem = "";
			for(int i = 1; i < line; i++)
				elem = scn.nextLine();
			String id = elem.split(":")[1];
			Command c = StructuredIdentifier.getCommand(id, block);
			AbSyn inter = c.element;
			System.err.println("Error found in element " + 
			((TTask)inter.element).getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
