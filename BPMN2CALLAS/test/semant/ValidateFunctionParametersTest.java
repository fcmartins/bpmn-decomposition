package semant;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import problems.BPMNError;
import problems.InputTypeMismatchError;
import problems.MissingInputVarError;
import problems.MissingOutputVarError;
import problems.OutputTypeMismatchError;
import common.BPMNParser;

public class ValidateFunctionParametersTest {
	
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

		p.accept(new ValidateFunctionParameters(errors, p.getGlobVars().values()));
		assertTrue(errors.isEmpty());
	}

	@Test
	public void testNoInputVariable() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/No-Input-Variable.bpmn2", errors);
		assertTrue(errors.isEmpty());

		p.accept(new ValidateFunctionParameters(errors, p.getGlobVars().values()));
		assertEquals(1, errors.size());
		assertThat(errors.get(0), instanceOf(MissingInputVarError.class));
		assertEquals("_95EF859B-0B6D-4EC0-905F-9DA51E1483AC", errors.get(0).element.getId());
	}

	@Test
	public void testInvalidInputType() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/Invalid-Input-Type.bpmn2", errors);
		assertTrue(errors.isEmpty());

		p.accept(new ValidateFunctionParameters(errors, p.getGlobVars().values()));
		assertEquals(1, errors.size());
		assertThat(errors.get(0), instanceOf(InputTypeMismatchError.class));
		assertEquals("_C354F41A-106D-4202-87DB-4C7CB42C393F", errors.get(0).element.getId());
	}

	@Test
	public void testNoOutputVariable() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/No-Output-Variable.bpmn2", errors);
		assertTrue(errors.isEmpty());

		p.accept(new ValidateFunctionParameters(errors, p.getGlobVars().values()));
		assertEquals(1, errors.size());
		assertThat(errors.get(0), instanceOf(MissingOutputVarError.class));
		assertEquals("_DD927B95-D407-41F3-8ABE-3FF69E96DACA", errors.get(0).element.getId());
	}

	@Test
	public void testInvalidOutputType() {
		List<BPMNError> errors = new LinkedList<BPMNError>();
		intermediate.Process p = parser.getProcess("BPMNFiles/invalid/Invalid-Output-Type.bpmn2", errors);
		assertTrue(errors.isEmpty());

		p.accept(new ValidateFunctionParameters(errors, p.getGlobVars().values()));
		assertEquals(1, errors.size());
		assertThat(errors.get(0), instanceOf(OutputTypeMismatchError.class));
		assertEquals("_CB371A1F-4EF1-4A9A-97D3-4945D3E8E812", errors.get(0).element.getId());
	}

}
