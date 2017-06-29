package translate;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import problems.BPMNError;
import problems.IfBranchEmptyNameError;
import problems.InvalidFunctionNameError;
import common.BPMNParser;

public class BPMN2IntermediateTest {
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
	}

	@Test
	public void testIfNoBranchNames() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/If-No-Branch-Names.bpmn2", errors);
		assertEquals(2, errors.size());
		
		assertThat(errors.get(0), instanceOf(IfBranchEmptyNameError.class));
		assertEquals("_CE5C1901-C337-4B9E-94B7-2A2AEBEB6831", errors.get(0).element.getId());
		
		assertThat(errors.get(1), instanceOf(IfBranchEmptyNameError.class));
		assertEquals("_646BFD0C-BAB9-4FB7-A7F9-02C895B470C6", errors.get(1).element.getId());
	}

	@Test
	public void testInvalidFunctionName() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/Invalid-Function-Name.bpmn2", errors);
		assertEquals(1, errors.size());
		assertThat(errors.get(0), instanceOf(InvalidFunctionNameError.class));
		assertEquals("_65FEE2CC-5ABD-49C5-A989-306471259A2C", errors.get(0).element.getId());
	}
	
}
