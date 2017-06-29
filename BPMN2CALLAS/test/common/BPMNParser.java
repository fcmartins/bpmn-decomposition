package common;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import main.LoadXML;
import problems.BPMNError;
import translate.BPMN2Intermediate;
import bpmn2.TProcess;

public class BPMNParser {
	
	public intermediate.Process getProcess(String filePath, List<BPMNError> errors) {
		LoadXML load = new LoadXML();
		TProcess procSpec = load.XMLUnmarshal(filePath);
		assertNotNull(procSpec);
		
		intermediate.Process process;
		try {
			process = new BPMN2Intermediate(procSpec, errors).translate().get(0);
		} catch (exception.BPMNError e) {
			e.printStackTrace();
			process = null;
		}
		assertNotNull(process);
		
		return process;
	}
	
}
