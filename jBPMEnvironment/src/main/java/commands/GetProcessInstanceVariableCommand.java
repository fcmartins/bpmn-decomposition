package commands;

import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.impl.KnowledgeCommandContext;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.ProcessInstance;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.kie.api.runtime.KieSession;
import org.kie.internal.command.Context;

public class GetProcessInstanceVariableCommand implements GenericCommand<Object> {

	private static final long serialVersionUID = -7849470173971664760L;
	
	private long processInstanceId;
	private String variableName;
	
	public GetProcessInstanceVariableCommand(long processInstanceId, String variableName) {
		this.processInstanceId = processInstanceId;
		this.variableName = variableName;
	}
	
	@Override
	public Object execute(Context context) {
		KieSession ksession = ((KnowledgeCommandContext) context).getKieSession();
		ProcessInstance pi = (ProcessInstance) ksession.getProcessInstance(processInstanceId);
		VariableScopeInstance vs = (VariableScopeInstance) pi.getContextInstance(VariableScope.VARIABLE_SCOPE);
		return vs.getVariable(variableName);
	}

}
