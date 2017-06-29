package decompose;

import java.util.HashSet;
import java.util.Set;

import bpmn2.TFlowElement;
import bpmn2.TReceiveTask;
import bpmn2.TSendTask;

public class Wsn {

	protected static final String processName = "WSN";
	protected final Set<TFlowElement> types;
	private GatewayTypes gateways;
		
	public Wsn(){
		gateways = new GatewayTypes();
		types = new HashSet<TFlowElement>() {
			private static final long serialVersionUID = -8523310797652121046L;
			{
				for(TFlowElement gateway : gateways.types){
					add(gateway);
				}
				add(new TSendTask());
				add(new TReceiveTask());
			}
		};
	}
		
}
