package main;

import intermediate.Process;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import jaxb.XMLMarshaller;

import org.xml.sax.SAXException;

import decompose.Decompose;
import problems.BPMNError;
import translate.BPMN2Intermediate;
import translate.TranslateResponsible;
import bpmn2.TDefinitions;

public class Main {

	// TODO: Ter parametro para decidir o que fazer

	private static BPMN2Intermediate psxml;
	private static LoadXML load;
	private static List<Process> ps;
	private static Validations val;
	private static TranslateResponsible transRes;
	private static List<BPMNError> errors;

	private static Decompose dec;

	static java.lang.Process process;

	public static void main(String[] args) throws IOException, InterruptedException, exception.BPMNError {
		if (args.length != 1) {
			System.out.println("You must enter the file name");
			System.exit(0);
		}
		errors = new LinkedList<BPMNError>();

		val = new Validations(errors);

		load = new LoadXML();

		// TProcess process = load.XMLUnmarshal(args[0]);

		System.out.println("############ " + args[0] + " ############");

		TDefinitions definitions = new XMLMarshaller().unmarshall(args[0], TDefinitions.class);

		//
		// BPMN2CALLAS bpmn2callas = new BPMN2CALLAS();
		// String result = bpmn2callas.toCallas(process, "", 1, "", 1);
		/*
		 * //parse psxml = new BPMN2Intermediate(load.XMLUnmarshal(args[0]),
		 * errors); //semantic analysis ps = psxml.translate();
		 * exitOnValidationErrors();
		 * 
		 * System.out.flush(); val.validate(ps); exitOnValidationErrors();
		 */

		dec = new Decompose(definitions);
		dec.generateGraphModel();
		dec.candidatePaths();
		dec.redefinePools();

		/*
		 * Draw the result of the pool redefinition
		 */
		
		try {
			new XMLMarshaller().marshall(dec.drawCollaboration(), "BPMNFiles"+File.separator+"result"+File.separator+"result.bpmn", TDefinitions.class);
			System.out.println("RESULT CREATED");
		} catch (SAXException e) {
			e.printStackTrace();
		}

		//
		// //need to check if any errors were found
		// transRes = new TranslateResponsible(val.getMap(),
		// psxml.getVarDecl());
		// Function structuredProcess =
		// (Function) transRes.translate(ps).pop();
		//
		// StringBuilder result = new StringBuilder();
		// CallasPrint cp = new CallasPrint(structuredProcess, result);
		// System.out.println(result);

		/**
		 * --------------------------------------------------------------------
		 * Call CALLAS Compiler
		 * --------------------------------------------------------------------
		 */
		// CompilerDealer compDealer = new
		// CompilerDealer(structuredProcess.blockCommand);
		// compDealer.createCallasFiles(result, cp.getFunctionName());
		// CompilerDealer compDealer = new CompilerDealer(null);
		// compDealer.createCompiler();

		// new ExecuteCallas().execNodes();

		// Runtime.getRuntime().exec("javac Stuff.java" );

		// java.lang.Process process = Runtime.getRuntime().exec("java Stuff > "
		// );

	}

	private static void exitOnValidationErrors() {
		if (val.hasErrors()) {
			val.printErrors();
			System.exit(1);
		}
	}

}
