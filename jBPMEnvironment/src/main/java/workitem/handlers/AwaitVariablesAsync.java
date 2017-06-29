package workitem.handlers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jaxb.MyType;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.EmptyContext;

import common.JBPMUtils;

public class AwaitVariablesAsync implements Runnable {

	private AwaitVariablesTaskWorkItemHandler handler;
	private WorkItem workItem;
	private WorkItemManager manager;
	private KieSession ksession;
	
	public AwaitVariablesAsync(AwaitVariablesTaskWorkItemHandler handler,
							   WorkItem workItem, WorkItemManager manager, KieSession ksession) {
		this.handler = handler;
		this.workItem = workItem;
		this.manager = manager;
		this.ksession = ksession;
	}

	@Override
	public void run() {
		try {
			final String deploymentId = JBPMUtils.getDeploymentId(ksession);
			long processInstanceId = workItem.getProcessInstanceId();

			String variables = (String) workItem
					.getParameter("Variables");

			if (variables == null || variables.equals(""))
				throw new IllegalStateException("Invalid parameters");

//			communicationId = URLDecoder.decode(communicationId, "UTF-8");
			variables = URLDecoder.decode(variables, "UTF-8");

			String[] variablesTransformed = variables.split("\\|");
			System.out.println("Processing variables: "
					+ Arrays.deepToString(variablesTransformed));
			Map<String, Object> results = new HashMap<>();
			for (String variableName : variablesTransformed) {
//				String completeName = communicationId + "_" + variableName;
				String completeName = variableName;
				System.out.println("Variable name: " + completeName);

				MyType myType = (MyType) JBPMUtils.getVariableValueBlocking(ksession,
						processInstanceId, completeName);
				System.out.println("MyType: " + myType);
				Object value = myType.getObject();
				
				System.out.println("Value: " + value + " (" + value.getClass() + ")");

				JBPMUtils.setVariable(ksession, processInstanceId,
						variableName, value);
//				JBPMUtils.setVariable(ksession, processInstanceId,
//						completeName, null);
				results.put(variableName, value);
			}

			// for some reason it doesn't work inside a new thread
//			manager.completeWorkItem(workItem.getId(), null);
			
			// but this works, as 
			// http://stackoverflow.com/questions/10714598/processing-a-task-in-an-asynchronous-workitemhandler
			RuntimeManager manager = RuntimeManagerRegistry.get().getManager(deploymentId);
			RuntimeEngine engine = manager.getRuntimeEngine(EmptyContext.get());
            engine.getKieSession().getWorkItemManager().completeWorkItem(workItem.getId(), results);
            manager.disposeRuntimeEngine(engine);
		} catch (InterruptedException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
//			manager.abortWorkItem(workItem.getId());
			handler.abortWorkItem(workItem, manager);
		}
	}

}
