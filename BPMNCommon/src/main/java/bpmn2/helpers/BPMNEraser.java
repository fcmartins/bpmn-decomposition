package bpmn2.helpers;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import bpmn2.BPMNEdge;
import bpmn2.BPMNPlane;
import bpmn2.BPMNShape;
import bpmn2.DiagramElement;
import bpmn2.TArtifact;
import bpmn2.TAssociation;
import bpmn2.TBaseElement;
import bpmn2.TCollaboration;
import bpmn2.TDefinitions;
import bpmn2.TFlowElement;
import bpmn2.TMessageFlow;
import bpmn2.TParticipant;
import bpmn2.TProcess;
import bpmn2.TRootElement;
import exception.BPMNError;

/**
 * This class contains some methods to help when removing sections of a BPMN Model.
 * Every method manipulates directly with the corresponding XSD classes, so it can be 
 * directly marshaled to a file.
 *
 */
public class BPMNEraser {
	
	public static void removeAllProcesses(TDefinitions definitions, TProcess excludeProcess) throws BPMNError {
		for (TProcess process : BPMNHelper.getProcesses(definitions)) {
			if (!process.getId().equals(excludeProcess.getId()))
				removeProcess(definitions, process);
		}
	}
    
    public static void removeAssociation(TDefinitions definitions, TAssociation association) throws BPMNError {
    	for (TProcess process : BPMNHelper.getProcesses(definitions)) {
    		List<JAXBElement<? extends TArtifact>> artifacts = process.getArtifact();
    		Iterator<JAXBElement<? extends TArtifact>> iterator = artifacts.iterator();
    		while (iterator.hasNext()) {
    			TArtifact artifact = iterator.next().getValue();
    			
    			if (artifact.getId().equals(association.getId()))
    				iterator.remove();
    		}
    	}
    }
    
    public static void removeCollaboration(TDefinitions definitions) {
    	Iterator<JAXBElement<? extends TRootElement>> iterator = definitions.getRootElement().iterator();
        while (iterator.hasNext()) {
        	TRootElement rootElement = iterator.next().getValue();
            if (rootElement instanceof TCollaboration)
            	iterator.remove();
        }
    }
	
	public static void removeDiagramElement(TDefinitions definitions, String elementId) {
		BPMNPlane plane = definitions.getBPMNDiagram().get(0).getBPMNPlane();
		List<JAXBElement<? extends DiagramElement>> diagramElements = plane.getDiagramElement();
		Iterator<JAXBElement<? extends DiagramElement>> iterator = diagramElements.iterator();
        while (iterator.hasNext()) {
        	DiagramElement diagramElement = iterator.next().getValue();
            if (diagramElement instanceof BPMNShape && 
            		((BPMNShape) diagramElement).getId().equals(elementId))
            	iterator.remove();
            else if (diagramElement instanceof BPMNEdge && 
            		((BPMNEdge) diagramElement).getId().equals(elementId))
            	iterator.remove();
        }
	}
	
	public static void removeDiagramElementAssociatedWithElement(TDefinitions definitions, TBaseElement element) {
		BPMNPlane plane = definitions.getBPMNDiagram().get(0).getBPMNPlane();
		List<JAXBElement<? extends DiagramElement>> diagramElements = plane.getDiagramElement();
		Iterator<JAXBElement<? extends DiagramElement>> iterator = diagramElements.iterator();
        while (iterator.hasNext()) {
        	DiagramElement diagramElement = iterator.next().getValue();
            if (diagramElement instanceof BPMNShape && 
            		((BPMNShape) diagramElement).getBpmnElement().getLocalPart().equals(element.getId()))
            	iterator.remove();
            else if (diagramElement instanceof BPMNEdge && 
            		((BPMNEdge) diagramElement).getBpmnElement().getLocalPart().equals(element.getId()))
            	iterator.remove();
        }
	}
	
	public static void removeDiagramElementsRelatedToPool(TDefinitions definitions, TParticipant pool) throws BPMNError {
		removeDiagramElementAssociatedWithElement(definitions, pool);
		
		TProcess process = BPMNHelper.getProcess(definitions, pool.getProcessRef().getLocalPart());
		List<JAXBElement<? extends TFlowElement>> flowElements = process.getFlowElement();
		for (JAXBElement<? extends TFlowElement> element : flowElements) {
			TFlowElement flowElement = element.getValue();
			
			removeDiagramElementAssociatedWithElement(definitions, flowElement);
		}
	}
	
	/**
     * Removes a given flow element from the model.
     */
    public static void removeFlowElement(TDefinitions definitions, TProcess process, TFlowElement flowElementToRemove) throws BPMNError {
        Iterator<JAXBElement<? extends TFlowElement>> iterator = process.getFlowElement().iterator();
        while (iterator.hasNext()) {
            TFlowElement flowElement = iterator.next().getValue();
            if (flowElement.getId().equals(flowElementToRemove.getId())) {
                iterator.remove();
            }
        }
    }
	
	public static void removeInvalidAssociations(TDefinitions definitions) throws BPMNError {
		List<TAssociation> associations = BPMNHelper.getAllAssociations(definitions);
		for (TAssociation association : associations) {
			if (!BPMNHelper.isValidAssociation(definitions, association)) {
				removeAssociation(definitions, association);
			}
		}
	}
	
	public static void removeInvalidDiagramElements(TDefinitions definitions) throws BPMNError {
		List<DiagramElement> elements = BPMNHelper.getAllDiagramElements(definitions);
		for (DiagramElement element : elements) {
			if (!BPMNHelper.isValidDiagramElement(definitions, element)) {
				removeDiagramElement(definitions, element.getId());
			}
		}
	}
    
    public static void removeMessageFlow(TDefinitions definitions, TMessageFlow oldMessageFlow) throws BPMNError {
    	TCollaboration collaboration = BPMNHelper.getCollaboration(definitions);
		List<TMessageFlow> messageFlows = collaboration.getMessageFlow();
		
		Iterator<TMessageFlow> iterator = messageFlows.iterator();
		while (iterator.hasNext()) {
			TMessageFlow messageFlow = iterator.next();
			
			if (messageFlow.getId().equals(oldMessageFlow.getId()))
				iterator.remove();
		}
    }
	
	public static void removeMessageFlowsWithPoolAsSource(TDefinitions definitions, TParticipant pool) throws BPMNError {
		TCollaboration collaboration = BPMNHelper.getCollaboration(definitions);
		List<TMessageFlow> messageFlows = collaboration.getMessageFlow();
		
		Iterator<TMessageFlow> iterator = messageFlows.iterator();
		while (iterator.hasNext()) {
			TMessageFlow messageFlow = iterator.next();
			
			String elementId = messageFlow.getSourceRef().getLocalPart();
			if (BPMNHelper.getPoolContainingElement(definitions, elementId).getId().equals(pool.getId())) {
				iterator.remove();
				removeDiagramElementAssociatedWithElement(definitions, messageFlow);
			}
		}
	}
	
	public static void removeMessageFlowsWithPoolAsTarget(TDefinitions definitions, TParticipant pool) throws BPMNError {
		TCollaboration collaboration = BPMNHelper.getCollaboration(definitions);
		List<TMessageFlow> messageFlows = collaboration.getMessageFlow();
		
		Iterator<TMessageFlow> iterator = messageFlows.iterator();
		while (iterator.hasNext()) {
			TMessageFlow messageFlow = iterator.next();
			
			String elementId = messageFlow.getTargetRef().getLocalPart();
			if (BPMNHelper.getPoolContainingElement(definitions, elementId).getId().equals(pool.getId())) {
				iterator.remove();
				removeDiagramElementAssociatedWithElement(definitions, messageFlow);
			}
		}
	}

    public static void removeParticipant(TDefinitions definitions, TParticipant participant) throws BPMNError {
        BPMNHelper.getCollaboration(definitions).getParticipant().remove(participant);
    }

    public static void removePool(TDefinitions definitions, TParticipant pool) throws BPMNError {
    	TProcess process = BPMNHelper.getProcess(definitions, pool.getProcessRef().getLocalPart());
    	
    	// remove diagram elements of process
    	removeDiagramElementsRelatedToPool(definitions, pool);

        // remove message flows that originate from pool
    	removeMessageFlowsWithPoolAsSource(definitions, pool);

        // remove message flows that target pool
        removeMessageFlowsWithPoolAsTarget(definitions, pool);
    	
        // remove process tag
        removeProcess(definitions, process);

        // remove participant tag
        removeParticipant(definitions, pool);
    }
    
    public static void removePools(TDefinitions definitions, List<TParticipant> pools) throws BPMNError {
    	for (TParticipant pool : pools) {
    		removePool(definitions, pool);
    	}
    }

    public static void removeProcess(TDefinitions definitions, TProcess process) {
        Iterator<JAXBElement<? extends TRootElement>> iterator = definitions.getRootElement().iterator();
        while (iterator.hasNext()) {
            TRootElement rootElement = iterator.next().getValue();
            if (rootElement instanceof TProcess && rootElement.equals(process))
                iterator.remove();
        }
    }

    public static void removeProcesses(TDefinitions definitions, List<TProcess> processes) {
		for (TProcess process : processes) {
			removeProcess(definitions, process);
		}
	}
    
}
