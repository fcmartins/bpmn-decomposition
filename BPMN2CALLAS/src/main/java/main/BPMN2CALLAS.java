package main;

import intermediate.Process;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import problems.BPMNError;
import structured.BlockCommand;
import structured.Command;
import structured.Function;
import structured.FunctionSignature;
import translate.BPMN2Intermediate;
import translate.CallasPrint;
import translate.TranslateResponsible;
import bpmn2.TProcess;

public class BPMN2CALLAS {

	public String toCallas(TProcess process, String wsnId, String gatewayIp, int gatewayPort, String sinkIp, int sinkPort,
			String broadcastIp, int nodePort) throws exception.BPMNError, IOException {
		List<BPMNError> errors = new LinkedList<BPMNError>();

		Validations val = new Validations(errors);

		// parse
		BPMN2Intermediate psxml = new BPMN2Intermediate(process, errors);
		// semantic analysis
		List<Process> ps = psxml.translate();
		if (val.hasErrors()) {
			val.printErrors();
			throw new IllegalStateException("Invalid model file");
		}

		System.out.flush();
		val.validate(ps);
		if (val.hasErrors()) {
			val.printErrors();
			throw new IllegalStateException("Invalid model file");
		}

		// need to check if any errors were found
		TranslateResponsible transRes = new TranslateResponsible(val.getMap(), psxml.getVarDecl());
		List<Command> structuredProcess = (List<Command>) transRes.translate(ps);

		StringBuilder result = new StringBuilder();
		CallasPrint cp = new CallasPrint(structuredProcess, result);
		
		List<FunctionSignature> fs = new LinkedList<FunctionSignature>();
		for (Command c : structuredProcess) fs.add(((Function)c).signature);
		
		BlockCommand blockCommand = ((Function)structuredProcess.get(0)).blockCommand;
/*		CompilerDealer compDealer = new CompilerDealer(blockCommand, wsnId);
		compDealer.createCallasFiles(result, fs, gatewayIp, gatewayPort, sinkIp, sinkPort, broadcastIp, nodePort);
		compDealer.createCompiler();
//		CompilerDealer compDealer2 = new CompilerDealer(null);
//		compDealer2.createCompiler();
		
*/		return result.toString();
	}

}
