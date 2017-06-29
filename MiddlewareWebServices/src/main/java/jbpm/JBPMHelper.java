package jbpm;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jaxb.MyType;

import org.kie.remote.client.api.RemoteRuntimeEngineFactory;
import org.kie.remote.client.jaxb.ConversionUtil;
import org.kie.remote.client.jaxb.JaxbCommandsRequest;
import org.kie.remote.client.jaxb.JaxbCommandsResponse;
import org.kie.remote.jaxb.gen.JaxbStringObjectPairArray;
import org.kie.remote.jaxb.gen.SetProcessInstanceVariablesCommand;
import org.kie.remote.jaxb.gen.SignalEventCommand;
import org.kie.remote.services.ws.command.generated.CommandWebService;
import org.kie.remote.services.ws.command.generated.CommandWebServiceException;
import org.kie.services.client.serialization.jaxb.impl.JaxbCommandResponse;
import org.kie.services.client.serialization.jaxb.rest.JaxbExceptionResponse;

public class JBPMHelper {
    private CommandWebService service;
    private String deploymentId;

    public JBPMHelper(URL applicationUrl, String deploymentId) {
        this(applicationUrl, "krisv", "krisv", deploymentId);
    }

    public JBPMHelper(URL applicationUrl, String user, String password, String deploymentId) {
        this.deploymentId = deploymentId;
        service = createWebserviceClient(applicationUrl, user, password, deploymentId);
    }

    /**
     * Create the webservice client
     *
     * @param applicationUrl Something like "http://localhost:8080/jbpm-console/"
     * @param user           The user doing the webservice requests
     * @param password       The user's password
     * @param deploymentId   The deployment id that the request will interact with
     * @return A {@link CommandWebService} client instance
     */
    private static CommandWebService createWebserviceClient(URL applicationUrl,
                                                            String user, String password, String deploymentId) {
        return RemoteRuntimeEngineFactory.newCommandWebServiceClientBuilder()
                .addServerUrl(applicationUrl)
                .addUserName(user)
                .addPassword(password)
                .addDeploymentId(deploymentId)
                .addExtraJaxbClasses(MyType.class)
                .addTimeout(30)
                .buildBasicAuthClient();
    }

    public boolean sendSignal(long processInstanceId, String eventType, Object value) {
        SignalEventCommand command = new SignalEventCommand();
        command.setProcessInstanceId(processInstanceId);
        command.setEventType(eventType);
        command.setEvent(value);

        try {
            JaxbCommandsRequest req = new JaxbCommandsRequest(deploymentId, command);
            JaxbCommandsResponse res = service.execute(req);

            System.out.println("Sent successfully command to jBPM");

            // check response
            List<JaxbCommandResponse<?>> responses = res.getResponses();
            if (responses.isEmpty()) {
                // success
                System.out.println(String.format("sendSignal(%s): success", eventType));
            } else {
                System.out.println(String.format("sendSignal(%s): error", eventType));

                JaxbCommandResponse<?> response = responses.get(0);
                if (response instanceof JaxbExceptionResponse) {
                    JaxbExceptionResponse exception = (JaxbExceptionResponse) response;
                    System.out.println(String.format("\terror: " + exception.getMessage()));
                } else {
                    System.out.println(String.format("\tunknown result"));
                }

                return false;
            }

            return true;
        } catch (CommandWebServiceException e) {
            System.out.println("JBPM remote API failed");
            e.printStackTrace();
        }

        return false;
    }

    public boolean setVariables(long processInstanceId, Map<String, Object> variablesMap) {
        try {
            JaxbStringObjectPairArray variables = ConversionUtil
                    .convertMapToJaxbStringObjectPairArray(transform(variablesMap));

            SetProcessInstanceVariablesCommand command = new SetProcessInstanceVariablesCommand();
            command.setProcessInstanceId(processInstanceId);
            command.setVariables(variables);

            JaxbCommandsRequest req = new JaxbCommandsRequest(deploymentId, command);
            JaxbCommandsResponse res = service.execute(req);

            System.out.println("Sent successfully command to jBPM");

            // check response
            List<JaxbCommandResponse<?>> responses = res.getResponses();
            if (responses.isEmpty()) {
                // success
                System.out.println(String.format("setVariables(%s): success", variables));
                return true;
            } else {
                System.out.println(String.format("setVariable(%s): error", variables));

                JaxbCommandResponse<?> response = responses.get(0);
                if (response instanceof JaxbExceptionResponse) {
                    JaxbExceptionResponse exception = (JaxbExceptionResponse) response;
                    System.out.println(String.format("\terror: " + exception.getMessage()));
                } else {
                    System.out.println(String.format("\tunknown result"));
                }

                return false;
            }
        } catch (IOException | CommandWebServiceException e) {
            System.out.println("JBPM remote API failed");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Obtain an equivalent map prepared for jaxb.
     */
    private static Map<String, Object> transform(Map<String, Object> variablesMap) throws IOException {
        Map<String, Object> variables = new HashMap<>();
        for (Map.Entry<String, Object> variable : variablesMap.entrySet()) {
            MyType myType = new MyType();
            myType.setObject(variable.getValue());
            variables.put(variable.getKey(), myType);
        }

        return variables;
    }
}
