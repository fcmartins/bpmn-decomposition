package semant;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import intermediate.If;
import intermediate.Join;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import problems.BPMNError;
import problems.IfNotConverging;
import problems.MissingDefaultCommandError;
import common.BPMNParser;

public class FindIfJoinPairTest {
	
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
		
		Map<If,Join> pairs = new HashMap<If, Join>();
		p.accept(new FindIfJoinPair(errors, pairs));
		assertTrue(errors.isEmpty());
	}

	@Test
	public void testNoConverging() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/If-No-Converging.bpmn2", errors);
		assertTrue(errors.isEmpty());

		Map<If,Join> pairs = new HashMap<If, Join>();
		p.accept(new FindIfJoinPair(errors, pairs));
		assertEquals(1, errors.size());
		assertThat(errors.get(0), instanceOf(IfNotConverging.class));
		assertEquals("_E5A117B0-5F88-4C89-B44A-D234A1093F90", errors.get(0).element.getId());
	}

	@Test
	public void testNoConvergingWithNestedIf() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/If-With-Nested-No-Converging.bpmn2", errors);
		assertTrue(errors.isEmpty());

		Map<If,Join> pairs = new HashMap<If, Join>();
		p.accept(new FindIfJoinPair(errors, pairs));
		assertEquals(1, errors.size());
		assertThat(errors.get(0), instanceOf(IfNotConverging.class));
		assertEquals("_9FA476DB-2907-4ED1-8C98-DE52DF5981C1", errors.get(0).element.getId());
	}

}
