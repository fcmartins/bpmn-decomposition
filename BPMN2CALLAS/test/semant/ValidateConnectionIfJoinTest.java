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
import problems.CycleFoundError;
import problems.IfHasDifferentJoinBranchesError;
import common.BPMNParser;

public class ValidateConnectionIfJoinTest {
	
	private BPMNParser parser;
	
	@Before
	public void before() {
		parser = new BPMNParser();
	}

	@Test
	public void testOk() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/valid/If-Nested-Multiple-Branch.bpmn2", errors);
		assertTrue(errors.isEmpty());
		
		Map<If, Join> map = new HashMap<If, Join>();
		p.accept(new FindIfJoinPair(errors, map));
		p.accept(new ValidateConnectionIfJoin(map, errors));
		assertTrue(errors.isEmpty());
	}

	@Test
	public void testBranchConvergingWrongGateway() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/If-Nested-Multiple-Branch-Wrong-Converging.bpmn2", errors);
		assertTrue(errors.isEmpty());

		Map<If, Join> map = new HashMap<If, Join>();
		p.accept(new FindIfJoinPair(errors, map));
		p.accept(new ValidateConnectionIfJoin(map, errors));
		assertEquals(1, errors.size());
		assertThat(errors.get(0), instanceOf(IfHasDifferentJoinBranchesError.class));
		assertEquals("_5475FC2F-6BE5-4669-BE66-90177849D81C", errors.get(0).element.getId());
	}

}
