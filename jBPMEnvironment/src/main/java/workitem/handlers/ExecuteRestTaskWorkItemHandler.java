package workitem.handlers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Arrays;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import common.JBPMUtils;
import common.RestClient;

public class ExecuteRestTaskWorkItemHandler implements WorkItemHandler {

	private KieSession ksession;

	public ExecuteRestTaskWorkItemHandler(KieSession ksession) {
		this.ksession = ksession;
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("Aborting due to failure");
		ksession.abortProcessInstance(workItem.getProcessInstanceId());
//		manager.abortWorkItem(workItem.getId());
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		try {
			System.out.println("I'm sending a message!!!");

			long processInstanceId = workItem.getProcessInstanceId();

			String communicationId = (String) workItem
					.getParameter("CommunicationID");
			String method = (String) workItem.getParameter("Method");
			String url = (String) workItem.getParameter("URL");
			String parameters = (String) workItem.getParameter("Parameters");
			String headers = ((String) workItem.getParameter("Headers"));
			String outputVariables = ((String) workItem
					.getParameter("OutputVariables"));

			if (method == null || url == null)
				throw new IllegalStateException("Invalid parameters");

			if (communicationId == null)
				communicationId = "";
			if (parameters == null)
				parameters = "";
			if (headers == null)
				headers = "";
			if (outputVariables == null)
				outputVariables = "";

			communicationId = URLDecoder.decode(communicationId, "UTF-8");
			url = URLDecoder.decode(url, "UTF-8");
			parameters = URLDecoder.decode(parameters, "UTF-8");
			headers = URLDecoder.decode(headers, "UTF-8");
			outputVariables = URLDecoder.decode(outputVariables, "UTF-8");

			String[] headersTransformed = new String[0];
			if (!headers.equals(""))
				headersTransformed = headers.split("\\|");

			String[] outputVariablesTransformed = new String[0];
			if (!outputVariables.equals(""))
				outputVariablesTransformed = transformOutputVariables(
						outputVariables.split("\\|"), communicationId);

			Object[] paramsTemp = transform(parameters, processInstanceId);

			sendRestRequest(workItem, outputVariablesTransformed,
					communicationId, method, url, paramsTemp,
					headersTransformed);
					
			manager.completeWorkItem(workItem.getId(), null);
		} catch (IOException | JSONException | URISyntaxException e) {
			e.printStackTrace();
//			manager.abortWorkItem(workItem.getId());
			abortWorkItem(workItem, manager);
		}
	}

	private Object[] transform(String parameters, long processInstanceId) {
		if (parameters.equals(""))
			return new String[0];
		String[] tokens = parameters.split("\\|");
		Object[] transformed = new Object[tokens.length];
		int i = 0;
		for (String tok : tokens) {
			if (tok.equals("")) {
				transformed[i] = tok;
			} else if (NumberUtils.isNumber(tok)) {
				// a number
				transformed[i] = NumberUtils.createNumber(tok);
			} else if (tok.charAt(0) == '"'
					&& tok.charAt(tok.length() - 1) == '"') {
				// a string constant
				transformed[i] = tok.substring(1, tok.length() - 1);
			} else if (tok.equalsIgnoreCase("true") || tok.equalsIgnoreCase("false")) {
				// a boolean value
				transformed[i] = Boolean.valueOf(tok);
			}
			else {
				// a variable
				transformed[i] = JBPMUtils.getVariable(ksession,
						processInstanceId, tok);
			}

			i++;
		}

		return transformed;
	}

	private String[] transformOutputVariables(String[] initial,
			String communicationID) {
		String[] result = new String[initial.length];
		for (int i = 0; i < initial.length; i++) {
			result[i] = communicationID + "_" + initial[i];
		}

		return result;
	}

	private void sendRestRequest(WorkItem workItem,
			String[] outputVariablesTransformed, String communicationId,
			String method, String url, Object[] inputValues, String[] headers)
			throws IOException, URISyntaxException, JSONException {
		if (method.equals("POST")) {
			JSONObject jbpmObj = new JSONObject();
			jbpmObj.put("serverAddress", JBPMUtils.getServerAddress());
			jbpmObj.put("deploymentId", JBPMUtils.getDeploymentId(ksession));
			jbpmObj.put("processInstanceId", workItem.getProcessInstanceId());
			jbpmObj.put("outputVariables", outputVariablesTransformed);

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("inputValues", inputValues);
			jsonObj.put("jbpm", jbpmObj);

			RestClient.post(url, jsonObj.toString(), headers);
		} else {
			throw new IllegalArgumentException("Invalid HTTP method (expected POST)");
		}
	}

}
