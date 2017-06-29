package main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import problems.BPMNError;

import common.BPMNParser;

public class ValidationsTest {
	
	private BPMNParser parser;
	
	@Before
	public void before() {
		parser = new BPMNParser();
	}

	@Test
	public void testGetTempLogDouble() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/valid/Simple-GetTemp-LogDouble.bpmn2", errors);
		assertTrue(errors.isEmpty());
		
		Validations val = new Validations(errors);
		val.validate(p);
		assertEquals(0, errors.size());
	}

	@Test
	public void testGetTempLogDoubleBlink() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/valid/Simple-GetTemp-LogDouble-Blink.bpmn2", errors);
		assertTrue(errors.isEmpty());
		
		Validations val = new Validations(errors);
		val.validate(p);
		assertEquals(0, errors.size());
	}

	@Test
	public void testIfMultipleBranch() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/valid/If-Multiple-Branch.bpmn2", errors);
		assertTrue(errors.isEmpty());
		
		Validations val = new Validations(errors);
		val.validate(p);
		assertEquals(0, errors.size());
	}

	@Test
	public void testIfNestedMultipleBranch() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/valid/If-Nested-Multiple-Branch.bpmn2", errors);
		assertTrue(errors.isEmpty());
		
		Validations val = new Validations(errors);
		val.validate(p);
		assertEquals(0, errors.size());
	}

}
