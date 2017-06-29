package main;

import intermediate.AbSyn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.callas.callas.CallasAPI;
import org.callas.callas.Compiler;
import org.callas.external.Externs;
import org.tyco.common.api.IParser;
import org.tyco.common.errorMsg.ErrorMessagesException;

import structured.BlockCommand;
import structured.Command;
import structured.FunctionSignature;
import translate.CreateCallasFiles;
import bpmn2.TTask;

public class CompilerDealer {
	public final String mainFile, udpFile;
	public final BlockCommand block; 
	private String wsnId;
	
	private CreateCallasFiles ccf;
	
	public CompilerDealer(BlockCommand b, String wsnId) {
		block = b;
		mainFile = "main_" + wsnId + ".calnet";
		udpFile = "udp_" + wsnId + ".caltype";
		this.wsnId = wsnId;
	}
	
	public void createCallasFiles(StringBuilder result, List<FunctionSignature> fs,
			String gatewayIp, int gatewayPort, String sinkIp, int sinkPort,
			String broadcastIp, int nodePort) 
			throws IOException {
		ccf = new CreateCallasFiles(result, fs, wsnId, gatewayIp, gatewayPort, sinkIp, sinkPort, broadcastIp, nodePort);
	}
	
	public void createCompiler() {
		CallasAPI api = new CallasAPI();
//		File main = new File(this.getClass().getClassLoader().getResource(mainFile).getFile());
		File main = new File(mainFile);
		try {
			IParser<Externs> externsParser = api.createExternsParser();
//			FileInputStream externsStream = new FileInputStream(this.getClass().getClassLoader().getResource(udpFile).getFile());
			FileInputStream externsStream = new FileInputStream(udpFile);
			Externs externs = externsParser.parse(udpFile, externsStream);
			api.useRegistry(externs);
			
			Compiler compiler = new Compiler(api);
			compiler.run(main, false);
		} catch (ErrorMessagesException e) {
//			for (ErrorMessage error : e.getErrors()) 
//				printError(error.location.line);
			api.printErrors(System.err, e.getErrors());
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
