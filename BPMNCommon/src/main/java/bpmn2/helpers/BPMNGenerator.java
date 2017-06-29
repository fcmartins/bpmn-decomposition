package bpmn2.helpers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import jaxb.JaxbHelper;
import jaxb.Namespace;
import bpmn2.BPMNEdge;
import bpmn2.BPMNShape;
import bpmn2.Bounds;
import bpmn2.Point;
import bpmn2.TAssignment;
import bpmn2.TBaseElement;
import bpmn2.TDataInput;
import bpmn2.TDataInputAssociation;
import bpmn2.TDataOutput;
import bpmn2.TDataOutputAssociation;
import bpmn2.TDataState;
import bpmn2.TFormalExpression;
import bpmn2.TInputOutputSpecification;
import bpmn2.TInputSet;
import bpmn2.TItemDefinition;
import bpmn2.TOutputSet;
import bpmn2.TProperty;
import bpmn2.TSequenceFlow;
import bpmn2.TTask;

/**
 * This class generates elements from the specification.
 *
 */
public class BPMNGenerator {
	
	public static void configureTaskInput(TTask task, Map<String, Object> input) {
		String taskID = task.getId();
		
        TInputOutputSpecification ioSpecification = task.getIoSpecification();
        
		// data inputs
        Map<String, TDataInput> dataInputMap = new HashMap<>();
        List<TDataInput> dataInputs = new LinkedList<>();
        for (String parameter : input.keySet()) {
            QName itemSubjectRef = new QName(Namespace.ROOT.getNamespaceURI(), taskID + "_" + parameter + "InputXItem");
            TDataInput dataInput = generateDataInput(taskID + "_" + parameter + "InputX", parameter, itemSubjectRef);
            dataInputs.add(dataInput);
            dataInputMap.put(parameter, dataInput);
        }
        ioSpecification.getDataInput().addAll(dataInputs);

        // input set
        TInputSet inputSet = BPMNGenerator.generateInputSet();
        List<JAXBElement<Object>> dataInputRefs = new LinkedList<>();
        for (String parameter : input.keySet()) {
            dataInputRefs.add(generateDataInputRef(taskID + "_" + parameter + "InputX"));
        }
        inputSet.getDataInputRefs().addAll(dataInputRefs);
        ioSpecification.getInputSet().add(inputSet);

        // data input associations
        List<TDataInputAssociation> dataInputAssociations = new LinkedList<>();
        for (String parameter : input.keySet()) {
            String from = input.get(parameter) != null ? input.get(parameter).toString() : null;
            dataInputAssociations.add(generateDataInputAssociation(from,
                    dataInputMap.get(parameter)));
        }
        task.getDataInputAssociation().addAll(dataInputAssociations);
	}
	
	public static void configureTaskOutput(TTask task, Map<TDataOutput, TProperty> output) {
		String taskID = task.getId();
		
        TInputOutputSpecification ioSpecification = task.getIoSpecification();
        
		// data outputs
        Map<String, TDataOutput> dataOutputMap = new HashMap<>();
        List<TDataOutput> dataOutputs = new LinkedList<>();
        for (TDataOutput dataOutput : output.keySet()) {
        	String name = dataOutput.getName();
        	TDataOutput dataOutput1 = generateDataOutput(taskID + "_" + name + "OutputX", name, 
        			new QName(Namespace.ROOT.getNamespaceURI(), dataOutput.getItemSubjectRef().getLocalPart()));
        	dataOutputs.add(dataOutput1);
        	dataOutputMap.put(name, dataOutput1);
        }
        ioSpecification.getDataOutput().addAll(dataOutputs);

        // output set
        TOutputSet outputSet = BPMNGenerator.generateOutputSet();
        List<JAXBElement<Object>> dataOutputRefs = new LinkedList<>();
        Map<String, JAXBElement<Object>> dataOutputRefMap = new HashMap<>();
        for (TDataOutput dataOutput : output.keySet()) {
        	String name = dataOutput.getName();
        	JAXBElement<Object> dataOutputRef = generateDataOutputRef(taskID + "_" + name + "OutputX");
        	dataOutputRefs.add(dataOutputRef);
        	dataOutputRefMap.put(name, dataOutputRef);
        }
        outputSet.getDataOutputRefs().addAll(dataOutputRefs);
        ioSpecification.getOutputSet().add(outputSet);

        // data output associations
        List<TDataOutputAssociation> dataOutputAssociations = new LinkedList<>();
        for (TDataOutput dataOutput : output.keySet()) {
        	String name = dataOutput.getName();
        	dataOutputAssociations.add(generateDataOutputAssociation(
            		generateSourceRef(dataOutputMap.get(name).getId()),
                    output.get(dataOutput)));
        }
        task.getDataOutputAssociation().addAll(dataOutputAssociations);
	}

	public static TAssignment generateAssignment(TFormalExpression from, TFormalExpression to) {
        TAssignment assignment = new TAssignment();
        assignment.setFrom(from);
        assignment.setTo(to);

        return assignment;
    }

    public static TDataInput generateDataInput(String id, String name, QName itemSubjectRef) {
        TDataInput dataInput = new TDataInput();
        dataInput.setId(id);
        dataInput.setName(name);
        if (itemSubjectRef != null) dataInput.setItemSubjectRef(itemSubjectRef);

        return dataInput;
    }

    public static TDataOutput generateDataOutput(String id, String name, QName itemSubjectRef) {
    	TDataOutput dataOutput = new TDataOutput();
    	dataOutput.setId(id);
    	dataOutput.setName(name);
        if (itemSubjectRef != null) dataOutput.setItemSubjectRef(itemSubjectRef);

        return dataOutput;
    }

    public static TDataInputAssociation generateDataInputAssociation(String from, TDataInput target) {
        TDataInputAssociation dataInputAssociation = new TDataInputAssociation();
        dataInputAssociation.setId(generateRandomId());
        dataInputAssociation.setTargetRef(target);
        dataInputAssociation.getAssignment().add(generateAssignment(generateFormalExpression(from),
                generateFormalExpression(target.getId())));

        return dataInputAssociation;
    }

    public static TDataOutputAssociation generateDataOutputAssociation(JAXBElement<Object> source, TProperty target) {
    	TDataOutputAssociation dataOutputAssociation = new TDataOutputAssociation();
        dataOutputAssociation.setId(generateRandomId());
        dataOutputAssociation.setTargetRef(target);
        dataOutputAssociation.getSourceRef().add(source);
//        dataOutputAssociation.getAssignment().add(generateAssignment(
//        		generateFormalExpression(source),
//        		generateFormalExpression(target.getId())));

        return dataOutputAssociation;
    }

    public static JAXBElement<Object> generateDataInputRef(Object value) {
        return JaxbHelper
                .toJAXBElement(Namespace.BPMN2, "dataInputRefs", value);
    }

    public static JAXBElement<Object> generateDataOutputRef(Object value) {
        return JaxbHelper
                .toJAXBElement(Namespace.BPMN2, "dataOutputRefs", value);
    }
    
    public static TDataState generateDataState(String value) {
    	TDataState dataState = new TDataState();
    	dataState.setId(generateRandomId());
    	dataState.setName(value);
    	return dataState;
    }

    public static BPMNEdge generateEdge(TBaseElement element, Bounds sourceBounds, Bounds targetBounds) {
        BPMNEdge edge = new BPMNEdge();
        
        edge.setId(generateRandomId());

        // link to bpmn element
        edge.setBpmnElement(new QName(Namespace.ROOT.getNamespaceURI(), element.getId()));

        List<Point> waypoints = new LinkedList<>();

        Point p1 = new Point();
        p1.setX(sourceBounds.getX() + sourceBounds.getWidth());
        p1.setY(sourceBounds.getY() + sourceBounds.getHeight() / 2);
        waypoints.add(p1);

        Point p2 = new Point();
        p2.setX(targetBounds.getX());
        p2.setY(targetBounds.getY() + targetBounds.getHeight() / 2);
        waypoints.add(p2);

        edge.getWaypoint().addAll(waypoints);

        return edge;
    }

    public static TFormalExpression generateFormalExpression(Serializable content) {
        TFormalExpression formalExpression = new TFormalExpression();
        formalExpression.getContent().add(content);

        return formalExpression;
    }

    public static TInputSet generateInputSet() {
        TInputSet inputSet = new TInputSet();
        inputSet.setId(generateRandomId());

        return inputSet;
    }

    public static TItemDefinition generateItemDefinition(String id, String structureRef) {
        // <bpmn2:itemDefinition id="__6DEDB2A8-F02B-4421-9F63-DDF436AE2D36_ParametersInputXItem" structureRef="String"/>

        TItemDefinition itemDefinition = new TItemDefinition();
        itemDefinition.setId(id);
        itemDefinition.setStructureRef(new QName(Namespace.ROOT.getNamespaceURI(), structureRef));

        return itemDefinition;
    }

    public static TOutputSet generateOutputSet() {
    	TOutputSet outputSet = new TOutputSet();
        outputSet.setId(generateRandomId());

        return outputSet;
    }

    public static Point generatePoint(double x, double y) {
        Point point = new Point();
        point.setX(x);
        point.setY(y);

        return point;
    }

    public static TProperty generateProperty(String id, String itemSubjectRef) {
        TProperty property = new TProperty();
        property.setId(id);
        property.setItemSubjectRef(new QName(Namespace.ROOT.getNamespaceURI(), itemSubjectRef));

        return property;
    }
    
    public static TProperty generatePropertyWithDataState(String name, String value) {
        TProperty property = new TProperty();
        property.setId(generateRandomId());
        property.setName(name);
        property.setDataState(generateDataState(value));

        return property;
    }
    
    public static String generateRandomId() {
    	// example of ID: _6DEDB2A8-F02B-4421-9F63-DDF436AE2D36
        return "_" + UUID.randomUUID().toString().toUpperCase();
    }

    public static TSequenceFlow generateSequenceFlow(TBaseElement source, TBaseElement target) {
        TSequenceFlow sequenceFlow = new TSequenceFlow();
        sequenceFlow.setId(generateRandomId());
        sequenceFlow.setSourceRef(source);
        sequenceFlow.setTargetRef(target);

        return sequenceFlow;
    }

    public static BPMNShape generateShape(String bpmnElementId, Bounds bounds) {
        BPMNShape shape = new BPMNShape();

        // link to bpmn element
        shape.setId("BPMNShape_" + bpmnElementId);
        shape.setBpmnElement(new QName(Namespace.ROOT.getNamespaceURI(), bpmnElementId));

        // bounds
        shape.setBounds(bounds);

        return shape;
    }

    public static JAXBElement<Object> generateSourceRef(Object value) {
        return JaxbHelper
                .toJAXBElement(Namespace.BPMN2, "sourceRef", value);
    }
    
    public static TTask generateTask(String taskName, 
			Map<String, Object> input, Map<TDataOutput, TProperty> output) {
    	if (input == null) input = new HashMap<String, Object>();
    	if (output == null) output = new HashMap<TDataOutput, TProperty>();
    	
        String taskID = generateRandomId();

        TTask task = new TTask();
        task.setName(taskName);
        task.setId(taskID);

        // I/O specification
        task.setIoSpecification(new TInputOutputSpecification());
        configureTaskInput(task, input);
        configureTaskOutput(task, output);

        return task;
    }
	
}
