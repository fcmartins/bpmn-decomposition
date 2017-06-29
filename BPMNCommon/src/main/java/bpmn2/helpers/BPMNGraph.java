package bpmn2.helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import bpmn2.TBaseElement;
import bpmn2.TCatchEvent;
import bpmn2.TDefinitions;
import bpmn2.TEvent;
import exception.BPMNError;

public class BPMNGraph {
	
	private Map<TBaseElement, List<TBaseElement>> next;
	
	private BPMNGraph(TDefinitions definitions) throws BPMNError {
		next = new HashMap<TBaseElement, List<TBaseElement>>();
		
		Set<TBaseElement> visited = new HashSet<TBaseElement>();
		List<TCatchEvent> events = BPMNHelper.getAllCatchEvents(definitions);
		for (TEvent event : events) {
			Queue<TBaseElement> q = new LinkedList<TBaseElement>();
			q.offer(event);
			while (!q.isEmpty()) {
				TBaseElement element = q.poll();
				
				visited.add(element);
				next.put(element, new LinkedList<TBaseElement>());
				
				List<TBaseElement> outgoing = BPMNHelper.getOutgoing(definitions, element);
				for (TBaseElement baseElement : outgoing) {
					if (visited.contains(baseElement))
						throw new BPMNError("Model has a cycle involving " + baseElement.getId());
//					System.out.println(element.getId() + " --> " + baseElement.getId());
					addNext(element, baseElement);
					q.offer(baseElement);
				}
			}
		}
	}
	
	private void addNext(TBaseElement u, TBaseElement v) {
		if (!next.containsKey(u)) next.put(u, new LinkedList<TBaseElement>());
		next.get(u).add(v);
	}
	
	public boolean doesElementReachElement(TBaseElement u, TBaseElement v) {
		Queue<TBaseElement> q = new LinkedList<TBaseElement>();
		q.offer(u);
		while (!q.isEmpty()) {
			TBaseElement element = q.poll();
			
			if (element.getId().equals(v.getId())) return true;
			
			for (TBaseElement baseElement : next.get(element)) {
				q.offer(baseElement);
			}
		}
		
		return false;
	}
	
	
	public static class Builder {

		private TDefinitions definitions;
		
		public Builder() {
			
		}
		
		public Builder setBpmnModel(TDefinitions definitions) {
			this.definitions = definitions;
			return this;
		}
		
		public BPMNGraph build() throws BPMNError {
			return new BPMNGraph(definitions);
		}
		
	}

}
