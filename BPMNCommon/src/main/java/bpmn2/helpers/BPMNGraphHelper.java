package bpmn2.helpers;

import java.util.List;

import bpmn2.TBaseElement;
import bpmn2.TCatchEvent;
import bpmn2.TDefinitions;
import exception.BPMNError;

public class BPMNGraphHelper {

	public static TCatchEvent getCatchEventThatLeadsToElement(TDefinitions definitions, 
			BPMNGraph graph, TBaseElement baseElement) throws BPMNError {
		List<TCatchEvent> catchEvents = BPMNHelper.getAllCatchEvents(definitions);
        for (TCatchEvent catchEvent : catchEvents) {
        	if (graph.doesElementReachElement(catchEvent, baseElement)) return catchEvent;
        }
        
        throw new BPMNError("No catch event leads to element " + baseElement.getId());
	}
	
}
