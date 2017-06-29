package semant;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import problems.BPMNError;
import problems.MissingReturnCommandError;

import common.BPMNParser;

public class ValidateExistingReturnCommandVisitorTest {
	
	private BPMNParser parser;
	
	@Before
	public void before() {
		parser = new BPMNParser();
	}
	
	@Test
	public void testOk() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/valid/Simple-GetTemp-LogDouble.bpmn2", errors);
		assertTrue(errors.isEmpty());
		
		p.accept(new ValidateExistingReturnCommandVisitor(errors));
		assertTrue(errors.isEmpty());
	}

	@Test
	public void testNoEndEvent() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/No-End-Event.bpmn2", errors);
		assertTrue(errors.isEmpty());
		
		p.accept(new ValidateExistingReturnCommandVisitor(errors));
		assertEquals(1, errors.size());
		assertThat(errors.get(0), instanceOf(MissingReturnCommandError.class));
	}

}
