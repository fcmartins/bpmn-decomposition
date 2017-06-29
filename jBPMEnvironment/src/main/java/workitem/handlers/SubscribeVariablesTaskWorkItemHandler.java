package workitem.handlers;

import common.JBPMUtils;
import common.RestClient;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SubscribeVariablesTaskWorkItemHandler implements WorkItemHandler {

    private KieSession ksession;

    public SubscribeVariablesTaskWorkItemHandler(KieSession ksession) {
        this.ksession = ksession;
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        System.out.println("Aborting due to failure");
        ksession.abortProcessInstance(workItem.getProcessInstanceId());
//		manager.abortWorkItem(workItem.getId());
    }

    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        try {
            System.out.println("I'm subscribing variables!!!");

            String url = (String) workItem.getParameter("URL");
            String wsnId = (String) workItem.getParameter("WSNID");
            String variables = (String) workItem.getParameter("Variables");
            String sensorDataValidityDateWindow = (String) workItem.getParameter("SensorDataValidityDateWindow");
            String sensorDataNthEntry = (String) workItem.getParameter("SensorDataNthEntry");

            if (wsnId == null || url == null || variables == null)
                throw new IllegalStateException("Invalid parameters");
            
            if (sensorDataValidityDateWindow == null || sensorDataValidityDateWindow.equals("")) 
            	sensorDataValidityDateWindow = "0";
            if (sensorDataNthEntry == null || sensorDataNthEntry.equals("")) 
            	sensorDataNthEntry = "0";

            url = URLDecoder.decode(url, "UTF-8");
            wsnId = URLDecoder.decode(wsnId, "UTF-8");
            variables = URLDecoder.decode(variables, "UTF-8");
            sensorDataValidityDateWindow = URLDecoder.decode(sensorDataValidityDateWindow, "UTF-8");
            sensorDataNthEntry = URLDecoder.decode(sensorDataNthEntry, "UTF-8");

            sendRestRequest(workItem, url, wsnId, variables.split("\\|"),
                    Long.parseLong(sensorDataValidityDateWindow), Long.parseLong(sensorDataNthEntry));

            manager.completeWorkItem(workItem.getId(), null);
        } catch (IOException | JSONException | URISyntaxException e) {
            e.printStackTrace();
//			manager.abortWorkItem(workItem.getId());
            abortWorkItem(workItem, manager);
        }
    }

    private void sendRestRequest(WorkItem workItem,
                                 String url, String wsnId, String[] variables,
                                 long sensorDataValidityDateWindow,
                                 long sensorDataNthEntry)
            throws IOException, URISyntaxException {

        JSONObject subscriberDataObj = new JSONObject();

        List<JSONObject> subscriptions = new LinkedList<>();
        for (String variable : variables) {
            JSONObject subscriptionObj = new JSONObject();
            subscriptionObj.put("jbpmServerAddress", JBPMUtils.getServerAddress());
            subscriptionObj.put("deploymentId", JBPMUtils.getDeploymentId(ksession));
            subscriptionObj.put("processInstanceId", workItem.getProcessInstanceId());
            subscriptionObj.put("wsnId", wsnId);
            subscriptionObj.put("variable", variable);
            subscriptionObj.put("sensorDataValidityDateWindow", sensorDataValidityDateWindow);
            subscriptionObj.put("sensorDataNthEntry", sensorDataNthEntry);

            JBPMUtils.setVariable(ksession, workItem.getProcessInstanceId(), variable, null);

            subscriptions.add(subscriptionObj);
        }

        subscriberDataObj.put("subscriptions", subscriptions);

        RestClient.post(url, subscriberDataObj.toString(), null);
    }

}
