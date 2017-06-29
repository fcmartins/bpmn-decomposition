package common;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.drools.core.command.runtime.process.SetProcessInstanceVariablesCommand;
import org.kie.api.runtime.KieSession;

import commands.GetProcessInstanceVariableCommand;

public class JBPMUtils {
	
	private static final int SLEEP_WAIT_TIME = 10 * 1000;

//	private KieSession ksession;
////	private ExecutorService pool;
//
//	public JBPMUtils(KieSession ksession) {
//		this.ksession = ksession;
////		pool = Executors.newFixedThreadPool(10);
//	}

//	public Future<Object> getVariableValueAsync(final long processInstanceId,
//			final String variableName) {
//		return pool.submit(new Callable<Object>() {
//
//			@Override
//			public Object call() throws Exception {
//				Object value = getVariable(processInstanceId, variableName);
//				while (value == null) {
//					Thread.sleep(500);
//					value = getVariable(processInstanceId, variableName);
//				}
//
//				return value;
//			}
//
//		});
//	}

	public static Object getVariableValueBlocking(KieSession ksession, final long processInstanceId,
			final String variableName) throws InterruptedException {
		Object value = getVariable(ksession, processInstanceId, variableName);
		while (value == null) {
			Thread.sleep(SLEEP_WAIT_TIME);
			value = getVariable(ksession, processInstanceId, variableName);
		}

		return value;
	}

	public static Object getVariable(KieSession ksession, long processInstanceId, String name) {
		return ksession.execute(new GetProcessInstanceVariableCommand(processInstanceId, name));
	}

	public static void setVariable(KieSession ksession, long processInstanceId, String name, Object value) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(name, value);
		ksession.execute(new SetProcessInstanceVariablesCommand(processInstanceId, variables));
	}

	public static String getDeploymentId(KieSession ksession) {
		return (String) ksession.getEnvironment().get("deploymentId");
	}

	public static String getServerAddress() throws UnknownHostException {
		return "http://" + IPUtils.getOwnIpAddress() + ":"
				+ IPUtils.getOpenPort() + "/jbpm-console";
	}

}
