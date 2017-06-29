package workitem.handlers;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class AwaitVariablesTaskWorkItemHandler implements WorkItemHandler {

	private KieSession ksession;

	public AwaitVariablesTaskWorkItemHandler(KieSession ksession) {
		this.ksession = ksession;
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("Aborting due to failure");
//		ksession.abortProcessInstance(workItem.getProcessInstanceId());
//		manager.abortWorkItem(workItem.getId());
	}

	public void executeWorkItem(WorkItem workItem,
			WorkItemManager manager) {
		new Thread(new AwaitVariablesAsync(this, workItem, manager, ksession)).start();
	}

}
