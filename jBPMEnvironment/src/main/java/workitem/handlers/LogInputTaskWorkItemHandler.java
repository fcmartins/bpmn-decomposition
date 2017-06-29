package workitem.handlers;

import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class LogInputTaskWorkItemHandler implements WorkItemHandler {

	private KieSession ksession;

	public LogInputTaskWorkItemHandler(KieSession ksession) {
		this.ksession = ksession;
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("Aborting due to failure");
//		ksession.abortProcessInstance(workItem.getProcessInstanceId());
//		manager.abortWorkItem(workItem.getId());
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("Preparing to log input variable");

		Map<String, Object> parameters = workItem.getParameters();

		if (!parameters.isEmpty()) {
			System.out.println("Parameters:");
			for (String parameterName : parameters.keySet()) {
				System.out.println("\t"
						+ parameterName
						+ "(type = "
						+ parameters.get(parameterName).getClass().getCanonicalName() + "): "
						+ parameters.get(parameterName).toString());
			}
		}
		else {
			System.out.println("No parameters!");
		}
		
		manager.completeWorkItem(workItem.getId(), null);
	}

}
