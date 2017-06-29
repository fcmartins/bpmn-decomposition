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
import problems.MissingDefaultCommandError;

import common.BPMNParser;

public class ValidateDefaultCommandVisitorTest {
	
	private BPMNParser parser;
	
	@Before
	public void before() {
		parser = new BPMNParser();
	}
	
	@Test
	public void testOk() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/valid/Simple-If.bpmn2", errors);
		assertTrue(errors.isEmpty());
		
		p.accept(new ValidateDefaultCommandVisitor(errors));
		assertTrue(errors.isEmpty());
	}

	@Test
	public void testNoDefaultBranch() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/If-No-Default-Branch.bpmn2", errors);
		assertTrue(errors.isEmpty());
		
		p.accept(new ValidateDefaultCommandVisitor(errors));
		assertEquals(1, errors.size());
		assertThat(errors.get(0), instanceOf(MissingDefaultCommandError.class));
		assertEquals("_81142942-31D0-4E21-8123-CB718401CACD", errors.get(0).element.getId());
	}

}
