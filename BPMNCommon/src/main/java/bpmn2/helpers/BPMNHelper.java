package bpmn2.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import bpmn2.BPMNEdge;
import bpmn2.BPMNPlane;
import bpmn2.BPMNShape;
import bpmn2.Bounds;
import bpmn2.DiagramElement;
import bpmn2.Point;
import bpmn2.TArtifact;
import bpmn2.TAssociation;
import bpmn2.TBaseElement;
import bpmn2.TCatchEvent;
import bpmn2.TCollaboration;
import bpmn2.TDataInput;
import bpmn2.TDataInputAssociation;
import bpmn2.TDataObject;
import bpmn2.TDataOutput;
import bpmn2.TDataOutputAssociation;
import bpmn2.TDefinitions;
import bpmn2.TEvent;
import bpmn2.TFlowElement;
import bpmn2.TItemDefinition;
import bpmn2.TMessageFlow;
import bpmn2.TParticipant;
import bpmn2.TProcess;
import bpmn2.TProperty;
import bpmn2.TRootElement;
import bpmn2.TSequenceFlow;
import bpmn2.TTask;
import exception.BPMNError;

/**
 * This class has methods to help when handling with BPMN models.
 * Every method has no side-effects.
 *
 */
public class BPMNHelper {

    public static Bounds cloneAndTranslate(Bounds bounds, Point translation) {
        Bounds newBounds = new Bounds();
        newBounds.setX(bounds.getX() + translation.getX());
        newBounds.setY(bounds.getY() + translation.getY());
        newBounds.setWidth(bounds.getWidth());
        newBounds.setHeight(bounds.getHeight());

        return newBounds;
    }
    
    public static List<TAssociation> getAllAssociations(TDefinitions definitions) {
    	List<TAssociation> associations = new LinkedList<TAssociation>();
    	List<TProcess> processes = getProcesses(definitions);
    	for (TProcess process : processes) {
    		for (JAXBElement<? extends TArtifact> element : process.getArtifact()) {
    			TArtifact artifact = element.getValue();
    			if (artifact instanceof TAssociation)
    				associations.add((TAssociation) artifact);
    		}
    	}
    	
    	return associations;
    }
    
    public static List<TCatchEvent> getAllCatchEvents(TDefinitions definitions) {
    	List<TCatchEvent> events = new LinkedList<TCatchEvent>();
    	List<TProcess> processes = getProcesses(definitions);
    	for (TProcess process : processes) {
    		for (JAXBElement<? extends TFlowElement> element : process.getFlowElement()) {
    			TFlowElement flowElement = element.getValue();
    			if (flowElement instanceof TCatchEvent)
    				events.add((TCatchEvent) flowElement);
    		}
    	}
    	
    	return events;
    }
    
    public static List<TDataObject> getAllDataObjects(TDefinitions definitions) {
    	List<TDataObject> dataObjects = new LinkedList<TDataObject>();
    	List<TProcess> processes = getProcesses(definitions);
    	for (TProcess process : processes) {
    		for (JAXBElement<? extends TFlowElement> element : process.getFlowElement()) {
    			TFlowElement flowElement = element.getValue();
    			if (flowElement instanceof TDataObject)
    				dataObjects.add((TDataObject) flowElement);
    		}
    	}
    	
    	return dataObjects;
    }
    
    public static List<DiagramElement> getAllDiagramElements(TDefinitions definitions) {
    	List<DiagramElement> elements = new LinkedList<DiagramElement>();
    	for (JAXBElement<? extends DiagramElement> element : definitions.getBPMNDiagram().get(0).getBPMNPlane().getDiagramElement()) {
    		elements.add(element.getValue());
    	}
    	
    	return elements;
    }
    
    public static List<TItemDefinition> getAllItemDefinitions(TDefinitions definitions) {
    	List<TItemDefinition> itemDefinitions = new LinkedList<TItemDefinition>();
    	for (JAXBElement<? extends TRootElement> element : definitions.getRootElement()) {
    		TRootElement rootElement = element.getValue();
    		if (rootElement instanceof TItemDefinition)
    			itemDefinitions.add((TItemDefinition) rootElement);
    	}
    	
    	return itemDefinitions;
    }
    
    public static List<TFlowElement> getAllFlowElements(TDefinitions definitions) {
    	List<TFlowElement> elements = new LinkedList<TFlowElement>();
    	List<TProcess> processes = getProcesses(definitions);
    	for (TProcess process : processes) {
    		for (JAXBElement<? extends TFlowElement> element : process.getFlowElement()) {
    			TFlowElement flowElement = element.getValue();
    			elements.add(flowElement);
    		}
    	}
    	
    	return elements;
    }
    
    /**
     * Obtains the bounds of a certain element given the model.
     */
    public static Bounds getBoundsOfElement(TDefinitions definitions, TBaseElement element) throws BPMNError {
        List<JAXBElement<? extends DiagramElement>> diagramElements = definitions.getBPMNDiagram().get(0)
                .getBPMNPlane().getDiagramElement();

        for (JAXBElement<? extends DiagramElement> diagramElementJaxb : diagramElements) {
            DiagramElement diagramElement = diagramElementJaxb.getValue();
            if (diagramElement instanceof BPMNShape) {
                BPMNShape shape = (BPMNShape) diagramElement;
                if (shape.getBpmnElement().getLocalPart().equals(element.getId())) {
                    return shape.getBounds();
                }
            }
        }

        throw new BPMNError("Every visual element should have a corresponding diagram description");
    }

    public static TCollaboration getCollaboration(TDefinitions definitions) throws BPMNError {
        return getCollaboration(definitions, true);
    }

    public static TCollaboration getCollaboration(TDefinitions definitions, boolean throwOnFailure) throws BPMNError {
        List<JAXBElement<? extends TRootElement>> rootElements = definitions.getRootElement();
        for (JAXBElement<? extends TRootElement> element : rootElements) {
            TRootElement rootElement = element.getValue();
            if (rootElement instanceof TCollaboration)
                return (TCollaboration) rootElement;
        }

        if (throwOnFailure) throw new BPMNError("Model does not represent a collaboration");
        return null;
    }

    /**
     * Obtains the constant or variable name that the input parameter with name parameterName of the task maps to.
     */
    public static Object getConstantOrVariableNameMappedToInputParameter(TTask task, String parameterName)
            throws BPMNError, UnsupportedEncodingException {
        for (TDataInputAssociation dataInputAssociation : task.getDataInputAssociation()) {
            if (((TDataInput) dataInputAssociation.getTargetRef()).getName().equals(parameterName)) {
                if (!dataInputAssociation.getSourceRef().isEmpty()) {
                    Object sourceRef = dataInputAssociation.getSourceRef().get(0).getValue();
                    if (sourceRef instanceof TProperty) {
                        // variable
                        TProperty property = (TProperty) sourceRef;
                        return property.getId();
                    } else {
                        // unknown
                        throw new BPMNError("Unknown data input specification model");
                    }
                } else {
                    // constant
                    Object constant = dataInputAssociation.getAssignment().get(0).getFrom().getContent().get(0);
                    if (constant instanceof String) constant = URLDecoder.decode((String) constant, "UTF-8");
                    return constant;
                }
            }
        }

        throw new BPMNError("Task (" + task.getName() + ") should specify value of input parameter (" + parameterName + ")");
    }
    
    public static DiagramElement getDiagramElement(TDefinitions definitions, TBaseElement element) throws BPMNError {
    	BPMNPlane plane = definitions.getBPMNDiagram().get(0).getBPMNPlane();
		List<JAXBElement<? extends DiagramElement>> diagramElements = plane.getDiagramElement();
		Iterator<JAXBElement<? extends DiagramElement>> iterator = diagramElements.iterator();
        while (iterator.hasNext()) {
        	DiagramElement diagramElement = iterator.next().getValue();
            if (diagramElement instanceof BPMNShape && 
            		((BPMNShape) diagramElement).getBpmnElement().getLocalPart().equals(element.getId()))
            	return diagramElement;
            else if (diagramElement instanceof BPMNEdge && 
            		((BPMNEdge) diagramElement).getBpmnElement().getLocalPart().equals(element.getId()))
            	return diagramElement;
        }
        
        throw new BPMNError("There is no diagram element for this element: " + element.getId());
    }
    
    public static TFlowElement getFlowElement(TDefinitions definitions, String elementId) throws BPMNError {
    	List<TParticipant> pools = getPools(definitions);
    	for (TParticipant pool : pools) {
    		TFlowElement element = getFlowElement(definitions, pool, elementId, false);
    		if (element != null) return element;
    	}
    	
    	throw new BPMNError("There is no element with ID: " + elementId);
    }
    
    public static TFlowElement getFlowElement(TDefinitions definitions, TParticipant pool, String elementId) throws BPMNError {
    	return getFlowElement(definitions, pool, elementId, true);
    }
    
    public static TFlowElement getFlowElement(TDefinitions definitions, TParticipant pool, String elementId,
    		boolean throwOnException) throws BPMNError {
    	TProcess process = getProcess(definitions, pool.getProcessRef().getLocalPart());
    		
    	for (JAXBElement<? extends TFlowElement> element : process.getFlowElement()) {
        	if (element.getValue().getId().equals(elementId))
        		return element.getValue();
        }

    	if (throwOnException) throw new BPMNError("There is no flow element with ID: " + elementId);
    	return null;
    }
    
    public static TItemDefinition getItemDefinition(TDefinitions definitions, String id) throws BPMNError {
    	for (JAXBElement<? extends TRootElement> element : definitions.getRootElement()) {
    		TRootElement rootElement = element.getValue();
    		if (rootElement instanceof TItemDefinition) {
    			TItemDefinition itemDefinition = (TItemDefinition) rootElement;
    			if (itemDefinition.getId().equals(id))
    				return itemDefinition;
    		}
    	}

    	throw new BPMNError("There is no item definition with ID: " + id);
    }
    
    public static TMessageFlow getMessageFlowWithTaskAsSource(TDefinitions definitions, TBaseElement element) throws BPMNError {
    	List<TMessageFlow> messageFlows = getCollaboration(definitions).getMessageFlow();
    	for (TMessageFlow messageFlow : messageFlows) {
    		if (messageFlow.getSourceRef().getLocalPart().equals(element.getId()))
    			return messageFlow;
    	}
    	
    	throw new BPMNError("There is no message flow originating from element: " + element.getId());
    }
    
    public static TMessageFlow getMessageFlowWithTaskAsTarget(TDefinitions definitions, TBaseElement element) throws BPMNError {
    	List<TMessageFlow> messageFlows = getCollaboration(definitions).getMessageFlow();
    	for (TMessageFlow messageFlow : messageFlows) {
    		if (messageFlow.getTargetRef().getLocalPart().equals(element.getId()))
    			return messageFlow;
    	}
    	
    	throw new BPMNError("There is no message flow targeting element: " + element.getId());
    }
    
    public static List<TBaseElement> getOutgoing(TDefinitions definitions, TBaseElement baseElement) {
    	List<TBaseElement> outgoing = new LinkedList<TBaseElement>();
    	List<TProcess> processes = getProcesses(definitions);
    	for (TProcess process : processes) {
    		for (JAXBElement<? extends TFlowElement> element : process.getFlowElement()) {
    			TFlowElement flowElement = element.getValue();
    			if (flowElement instanceof TSequenceFlow) {
    				TSequenceFlow sequenceFlow = (TSequenceFlow) flowElement;
    				if (sequenceFlow.getSourceRef() == baseElement)
    					outgoing.add((TBaseElement) sequenceFlow.getTargetRef());
    			}
    		}
    	}
    	
    	return outgoing;
    }

    public static BPMNPlane getPlane(TDefinitions definitions) {
        return definitions.getBPMNDiagram().get(0).getBPMNPlane();
    }
    
    public static TParticipant getPoolContainingElement(TDefinitions definitions, String elementId) throws BPMNError {
    	List<TParticipant> pools = getPools(definitions);
    	for (TParticipant pool : pools) {
    		if (pool.getId().equals(elementId)) return pool;
    		
    		TProcess process = getProcess(definitions, pool.getProcessRef().getLocalPart());
    		if (hasFlowElement(process, elementId)) return pool;
    	}
    	
    	throw new BPMNError("Flow element (" + elementId + ") does not occur in any pool");
    }

    public static List<TParticipant> getPools(TDefinitions definitions) throws BPMNError {
        return getCollaboration(definitions).getParticipant();
    }
    
    public static TProcess getProcess(TDefinitions definitions, String processId) throws BPMNError {
    	List<TProcess> processes = getProcesses(definitions);
    	for (TProcess process : processes) {
    		if (process.getId().equals(processId))
    			return process;
    	}
    	
    	throw new BPMNError("There is no process with ID: " + processId);
    }
    
    public static TProcess getProcess(TDefinitions definitions, TParticipant pool) throws BPMNError {
    	return getProcess(definitions, pool.getProcessRef().getLocalPart());
    }
    

    public static List<TProcess> getProcesses(TDefinitions definitions) {
        List<TProcess> processes = new LinkedList<>();

        List<JAXBElement<? extends TRootElement>> rootElements = definitions.getRootElement();
        for (JAXBElement<? extends TRootElement> element : rootElements) {
            TRootElement rootElement = element.getValue();
            if (rootElement instanceof TProcess) {
            	TProcess process = (TProcess) rootElement;
            	
//            	if (process.getDefinitionalCollaborationRef() != null)
//            		processes.add(process);
//            	else {
//            		System.out.println("Excluded potential process since it didn't belong to a collaboration!");
//            	}
            	processes.add(process);
            }
        }

        return processes;
    }
    
    public static List<TProcess> getProcesses(TDefinitions definitions, List<TParticipant> pools) throws BPMNError {
    	List<TProcess> processes = new LinkedList<TProcess>();
    	for (TParticipant pool : pools)
    		processes.add(getProcess(definitions, pool));
    	
    	return processes;
    }
    
    public static Object getProperty(TEvent element, String name) throws BPMNError {
    	return getProperty(element, name, true);
    }
    
    public static Object getProperty(TEvent element, String name, boolean throwOnFailure) throws BPMNError {
    	List<TProperty> properties = element.getProperty();
    	for (TProperty property : properties) {
    		if (property.getName().equals(name))
    			return property.getDataState().getName();
    	}
    	
    	if (throwOnFailure) throw new BPMNError("Element has no property called " + name);
    	return null;
    }
    
    public static TProperty getProperty(TProcess process, String id) throws BPMNError {
    	for (TProperty property : process.getProperty()) {
    		if (property.getId().equals(id))
    			return property;
    	}
    	
    	throw new BPMNError("Process has no property with id " + id);
    }
    
    public static TParticipant getSourcePoolOfMessageFlow(TDefinitions definitions, TMessageFlow messageFlow) throws BPMNError {
    	return getPoolContainingElement(definitions, messageFlow.getSourceRef().getLocalPart());
    }
    
    public static TParticipant getTargetPoolOfMessageFlow(TDefinitions definitions, TMessageFlow messageFlow) throws BPMNError {
    	return getPoolContainingElement(definitions, messageFlow.getTargetRef().getLocalPart());
    }
    
    /**
     * Obtains the array of constants/variable names that each input parameter maps to, in the order
     * specified by configurationTask.
     */
    public static Object[] getTaskInputParameters(TTask task, String[] parameterNames)
            throws BPMNError, UnsupportedEncodingException {
        List<Object> parameters = new LinkedList<>();
        for (String parameterName : parameterNames) {
            Object valueMappedToParameter = getConstantOrVariableNameMappedToInputParameter(task, parameterName);
            parameters.add(valueMappedToParameter);
        }

        return parameters.toArray();
    }
    
    public static TDataObject getTaskOutputDataObjectWithName(TDefinitions definitions, TCatchEvent task, String name)
            throws BPMNError, UnsupportedEncodingException {
    	for (TDataOutputAssociation association : task.getDataOutputAssociation()) {
    		Object object = association.getTargetRef();
    		if (object instanceof TDataObject) {
    			TDataObject dataObject = (TDataObject) object;
    			if (dataObject.getName().equals(name)) return dataObject;
    		}
    	}
    	
        throw new BPMNError("Task " + task.getId() + " has no output data object with name " + name);
    }

    /**
     * Obtains the array of the variable names to where the results of the execution of the task will be written,
     * given the configurationTask.
     */
    public static Object[] getTaskOutputVariables(TTask task, String[] variableNames)
            throws BPMNError, UnsupportedEncodingException {
        List<Object> variables = new LinkedList<>();
        for (String variableName : variableNames) {
            Object mappedToVariable = getVariableNameMappedFromOutputResult(task, variableName);
            variables.add(mappedToVariable);
        }

        return variables.toArray();
    }
    
    /**
     * Obtains the list of output parameter names of the task.
     */
    public static List<TDataOutput> getTaskDataOutputs(TTask task) throws BPMNError {
        List<TDataOutput> parameters = new LinkedList<>();
        for (TDataOutputAssociation dataOutputAssociation : task.getDataOutputAssociation()) {
           	Object sourceRef = dataOutputAssociation.getSourceRef().get(0).getValue();
           	if (sourceRef instanceof TDataOutput) {
           		TDataOutput dataOutput = (TDataOutput) sourceRef;
           		parameters.add(dataOutput);
           	}
        }

        return parameters;
    }
    
    public static String[] getTaskOutputSources(TTask task) throws BPMNError {
        List<String> variables = new LinkedList<>();
        for (TDataOutputAssociation dataOutputAssociation : task.getDataOutputAssociation()) {
            JAXBElement<Object> object = dataOutputAssociation.getSourceRef().get(0);
            Object sourceRef = object.getValue();
            if (sourceRef instanceof TDataOutput) {
            	TDataOutput dataOutput = (TDataOutput) sourceRef;
                variables.add(dataOutput.getName());
            }
        }

        String[] variableNames = new String[variables.size()];
        variables.toArray(variableNames);
        return variableNames;
    }

    /**
     * Obtains the list of output variables of the task.
     */
    public static String[] getTaskOutputVariables(TTask task) throws BPMNError {
        List<String> variables = new LinkedList<>();
        for (TDataOutputAssociation dataOutputAssociation : task.getDataOutputAssociation()) {
            Object targetRef = dataOutputAssociation.getTargetRef();
            if (targetRef instanceof TProperty) {
                TProperty property = (TProperty) targetRef;
                variables.add(property.getName());
            }
        }

        String[] variableNames = new String[variables.size()];
        variables.toArray(variableNames);
        return variableNames;
    }
    
    /**
     * Obtains the variable id where an output result will be persisted.
     */
    public static TProperty getPropertyMappedFromOutputResult(TTask task, TDataOutput dataOutput)
            throws BPMNError, UnsupportedEncodingException {
        for (TDataOutputAssociation dataOutputAssociation : task.getDataOutputAssociation()) {
            Object sourceRef = dataOutputAssociation.getSourceRef().get(0).getValue();
            if (sourceRef instanceof TDataOutput) {
                if (((TDataOutput) sourceRef).getName().equals(dataOutput.getName())) {
                    Object targetRef = dataOutputAssociation.getTargetRef();
                    if (targetRef instanceof TProperty) {
                        TProperty property = (TProperty) targetRef;
                        return property;
                    }
                }
            }
        }

        throw new BPMNError("Task (" + task.getName() + ") should specify the variable target of output variable (" +
        		dataOutput.getName() + ")");
    }

    /**
     * Obtains the variable name where an output result will be persisted.
     */
    public static Object getVariableNameMappedFromOutputResult(TTask task, String variableName)
            throws BPMNError, UnsupportedEncodingException {
        for (TDataOutputAssociation dataOutputAssociation : task.getDataOutputAssociation()) {
            Object sourceRef = dataOutputAssociation.getSourceRef().get(0).getValue();
            if (sourceRef instanceof TDataOutput) {
                TDataOutput dataOutput = (TDataOutput) sourceRef;

                if (dataOutput.getName().equals(variableName)) {
                    Object targetRef = dataOutputAssociation.getTargetRef();
                    if (targetRef instanceof TProperty) {
                        TProperty property = (TProperty) targetRef;
                        return property.getName();
                    }
                }
            }
        }

        throw new BPMNError("Task (" + task.getName() + ") should specify the variable target of output variable (" +
                variableName + ")");
    }
    
    public static boolean hasArtifact(TDefinitions definitions, String elementId) throws BPMNError {
    	boolean res = false;
    	
    	for (TProcess process : getProcesses(definitions)) {
    		res |= hasArtifact(process, elementId);
    	}
    	
    	return res;
    }
    
    public static boolean hasArtifact(TProcess process, String elementId) {
    	for (JAXBElement<? extends TArtifact> element : process.getArtifact()) {
    		if (element.getValue().getId().equals(elementId)) return true;
        }
    	
    	return false;
    }

    public static boolean hasCollaboration(TDefinitions definitions) throws BPMNError {
        return getCollaboration(definitions, false) != null;
    }
    
    public static boolean hasElement(TDefinitions definitions, String elementId) throws BPMNError {
    	boolean res = false;

    	res |= hasArtifact(definitions, elementId);
    	res |= hasFlowElement(definitions, elementId);
    	res |= hasMessageFlow(definitions, elementId);
    	res |= hasPool(definitions, elementId);
    	
    	return res;
    }
    
    public static boolean hasFlowElement(TDefinitions definitions, String elementId) throws BPMNError {
    	boolean res = false;
    	
    	for (TProcess process : getProcesses(definitions)) {
    		res |= hasFlowElement(process, elementId);
    	}
    	
    	return res;
    }
    
    public static boolean hasFlowElement(TProcess process, String elementId) {
    	for (JAXBElement<? extends TFlowElement> element : process.getFlowElement()) {
    		if (element.getValue().getId().equals(elementId)) return true;
        }
    	
    	return false;
    }
    
    public static boolean hasMessageFlow(TDefinitions definitions, String elementId) throws BPMNError {
    	TCollaboration collaboration = getCollaboration(definitions, false);
    	if (collaboration != null) {
	    	for (TMessageFlow messageFlow : collaboration.getMessageFlow()) {
	    		if (messageFlow.getId().equals(elementId)) return true;
	    	}
    	}
    	
    	return false;
    }

    public static boolean hasInputParameter(TTask task, String parameterName) {
        for (TDataInputAssociation dataInputAssociation : task.getDataInputAssociation()) {
            if (((TDataInput) dataInputAssociation.getTargetRef()).getName().equals(parameterName))
                return true;
        }

        return false;
    }
    
    public static boolean hasPool(TDefinitions definitions, String id) throws BPMNError {
    	if (hasCollaboration(definitions)) {
	    	List<TParticipant> pools = getPools(definitions);
		    for (TParticipant pool : pools) {
		    	if (pool.getId().equals(id)) return true;
		    }
    	}
    	
    	return false;
    }
    
    public static boolean hasProperty(TDefinitions definitions, String id) throws BPMNError {
    	for (TProcess process : getProcesses(definitions)) {
    		if (hasProperty(process, id)) return true;
    	}
    	
    	return false;
    }
    
    public static boolean hasProperty(TEvent element, String name) throws BPMNError {
    	List<TProperty> properties = element.getProperty();
    	for (TProperty property : properties) {
    		if (property.getName().equals(name))
    			return true;
    	}
    	
    	return false;
    }
    
    public static boolean hasProperty(TProcess process, String id) throws BPMNError {
    	for (TProperty property : process.getProperty()) {
    		if (property.getId().equals(id)) return true;
    	}
    	
    	return false;
    }
    
    public static boolean isValidAssociation(TDefinitions definitions, TAssociation association) throws BPMNError {
    	boolean res = true;
    	
    	res &= hasElement(definitions, association.getSourceRef().getLocalPart());
    	res &= hasElement(definitions, association.getTargetRef().getLocalPart());
    	
    	return res;
    }
    
    public static boolean isValidDiagramElement(TDefinitions definitions, DiagramElement element) throws BPMNError {
    	boolean res = false;
    	
    	if (element instanceof BPMNShape)
    		res = isValidBPMNShape(definitions, (BPMNShape) element);
    	else if (element instanceof BPMNEdge)
        	res = isValidBPMNEdge(definitions, (BPMNEdge) element);
    	
    	return res;
    }
    
    public static boolean isValidBPMNShape(TDefinitions definitions, BPMNShape shape) throws BPMNError {
    	boolean res = true;
    	
    	res &= hasElement(definitions, shape.getBpmnElement().getLocalPart());
    	
    	return res;
    }
    
    public static boolean isValidBPMNEdge(TDefinitions definitions, BPMNEdge edge) throws BPMNError {
    	boolean res = true;
    	
    	res &= hasElement(definitions, edge.getBpmnElement().getLocalPart());
    	
    	return res;
    }

}
