package bpmn2.helpers;

import java.util.Map;

import javax.xml.bind.JAXBElement;

import bpmn2.BPMNDiagram;
import bpmn2.BPMNEdge;
import bpmn2.BPMNShape;
import bpmn2.Bounds;
import bpmn2.DiagramElement;
import bpmn2.TDataOutput;
import bpmn2.TDefinitions;
import bpmn2.TFlowElement;
import bpmn2.TItemDefinition;
import bpmn2.TProcess;
import bpmn2.TProperty;
import bpmn2.TRootElement;
import bpmn2.TSequenceFlow;
import bpmn2.TTask;
import jaxb.JaxbHelper;
import jaxb.Namespace;
import exception.BPMNError;

/**
 * This class contains some methods to help when adding sections of a BPMN Model.
 * Every method manipulates directly with the corresponding XSD classes, so it can be 
 * directly marshaled to a file.
 *
 */
public class BPMNAdder {

    /**
     * Adds a visual element to the model.
     */
    public static void addDiagramElement(TDefinitions definitions,
                                          JAXBElement<? extends DiagramElement> diagramElement) {
        BPMNDiagram bpmnDiagram = definitions.getBPMNDiagram().get(0);
        bpmnDiagram.getBPMNPlane().getDiagramElement().add(diagramElement);
    }

    /**
     * Adds an edge to the model.
     */
    public static void addEdge(TDefinitions definitions, BPMNEdge edge) {
        JAXBElement<? extends DiagramElement> edgeJaxbElement = JaxbHelper
                .toJAXBElement(Namespace.BPMNDI, "BPMNEdge", edge);

        addDiagramElement(definitions, edgeJaxbElement);
    }

    /**
     * Adds an item definition to the model.
     */
    public static void addItemDefinition(TDefinitions definitions, TItemDefinition itemDefinition) {
        JAXBElement<? extends TRootElement> itemDefinitionJaxbElement = JaxbHelper
                .toJAXBElement(Namespace.BPMN2, "itemDefinition", itemDefinition);

        definitions.getRootElement().add(itemDefinitionJaxbElement);
    }

	/**
     * Adds a property to model.
     */
	public static void addProperty(TProcess process, TProperty property) throws BPMNError {
        process.getProperty().add(property);
    }
	
	/**
     * Adds a new sequence flow to the model.
     */
    public static void addSequenceFlow(TDefinitions definitions, TProcess process, TSequenceFlow sequenceFlow, BPMNEdge edge) throws BPMNError {
        process.getFlowElement().add(JaxbHelper.toJAXBElement(Namespace.BPMN2, "sequenceFlow",
                sequenceFlow));

        // add the edge
        addEdge(definitions, edge); 
    }

    /**
     * Adds a shape element to the model.
     */
    public static void addShape(TDefinitions definitions, BPMNShape shape) {
        JAXBElement<? extends DiagramElement> shapeJaxbElement = JaxbHelper
                .toJAXBElement(Namespace.BPMNDI, "BPMNShape", shape);

        addDiagramElement(definitions, shapeJaxbElement);
    }
    
    /**
     * Adds a task to process, including its visual shape and parameter definitions.
     */
    public static TTask addTask(TDefinitions definitions, TProcess process, String taskName,
                                 Map<String, Object> input, Map<TDataOutput, TProperty> output,
                                 Bounds bounds) throws BPMNError {
    	input.put("TaskName", taskName);
    	
        // add the task
        TTask task = BPMNGenerator.generateTask(taskName, input, output);
        BPMNAdder.addTaskElement(process, task);

        String taskId = task.getId();

        // add the shape
        BPMNAdder.addShape(definitions, BPMNGenerator.generateShape(taskId, bounds));

        // add the item definitions
        for (String parameter : input.keySet()) {
        	BPMNAdder.addItemDefinition(definitions, 
            		BPMNGenerator.generateItemDefinition(taskId + "_" + parameter + "InputXItem", "String"));
        }

        return task;
    }

    /**
     * Adds a task to model, does not include its visual shape and parameter definitions.
     */
    public static void addTaskElement(TProcess process, TTask task) throws BPMNError {
        JAXBElement<? extends TFlowElement> taskJaxbElement = JaxbHelper
                .toJAXBElement(Namespace.BPMN2, "task", task);

        process.getFlowElement().add(taskJaxbElement);
    }
	
}
