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
import problems.CycleFoundError;

import common.BPMNParser;

public class ValidateCyclesVisitorTest {
	
	private BPMNParser parser;
	
	@Before
	public void before() {
		parser = new BPMNParser();
	}

	@Test
	public void testCycle() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/Cycle.bpmn2", errors);
		assertTrue(errors.isEmpty());
		
		p.accept(new ValidateCyclesVisitor(errors));
		assertEquals(1, errors.size());
		assertThat(errors.get(0), instanceOf(CycleFoundError.class));
		assertEquals("_F4EBC1ED-02C5-4735-AF5B-15A3F2D9A816", errors.get(0).element.getId());
	}

	@Test
	public void testNoCycle() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/valid/Simple.bpmn2", errors);
		assertTrue(errors.isEmpty());
		
		p.accept(new ValidateCyclesVisitor(errors));
		assertTrue(errors.isEmpty());
	}

}
