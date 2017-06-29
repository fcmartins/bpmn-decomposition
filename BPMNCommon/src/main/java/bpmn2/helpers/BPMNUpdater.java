package bpmn2.helpers;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import jaxb.Namespace;
import bpmn2.BPMNEdge;
import bpmn2.BPMNLabel;
import bpmn2.BPMNPlane;
import bpmn2.BPMNShape;
import bpmn2.Bounds;
import bpmn2.DiagramElement;
import bpmn2.Point;
import bpmn2.TArtifact;
import bpmn2.TAssociation;
import bpmn2.TBaseElement;
import bpmn2.TDataObject;
import bpmn2.TDefinitions;
import bpmn2.TFlowElement;
import bpmn2.TItemDefinition;
import bpmn2.TParticipant;
import bpmn2.TProcess;
import bpmn2.TProperty;
import bpmn2.TSequenceFlow;
import bpmn2.TTask;
import exception.BPMNError;

public class BPMNUpdater {

	/**
     * Every visual element that is drawn to the right of the line x = minimumX is advanced (in X) by advanceX.
     * It is necessary to avoid overlapping when inserting new elements into the model.
	 * @throws BPMNError 
     */
    public static void advanceEveryDiagramElementByX(TDefinitions definitions, TParticipant pool, double minimumX, double advanceX) throws BPMNError {
    	advanceDiagramElementByX(BPMNHelper.getDiagramElement(definitions, pool), minimumX, advanceX);
        TProcess process = BPMNHelper.getProcess(definitions, pool);
        for (JAXBElement<? extends TFlowElement> element : process.getFlowElement()) {
        	TFlowElement flowElement = element.getValue();
        	DiagramElement diagramElement = BPMNHelper.getDiagramElement(definitions, flowElement);
            advanceDiagramElementByX(diagramElement, minimumX, advanceX);
        }
    }
    
    private static void advanceDiagramElementByX(DiagramElement diagramElement, double minimumX, double advanceX) {
        if (diagramElement instanceof BPMNShape) {
            BPMNShape shape = (BPMNShape) diagramElement;

            if (shape.getBounds().getX() > minimumX) {
                shape.getBounds().setX(shape.getBounds().getX() + advanceX);
                
                if (shape.getBPMNLabel() != null)
                	advanceDiagramElementByX(shape.getBPMNLabel(), minimumX, advanceX);
            }
        } else if (diagramElement instanceof BPMNEdge) {
            BPMNEdge edge = (BPMNEdge) diagramElement;

            List<Point> waypoints = edge.getWaypoint();
            for (Point point : waypoints) {
                if (point.getX() > minimumX)
                    point.setX(point.getX() + advanceX);
            }
        }
        else if (diagramElement instanceof BPMNLabel) {
            BPMNLabel label = (BPMNLabel) diagramElement;

            if (label.getBounds() != null && label.getBounds().getX() > minimumX) {
                label.getBounds().setX(label.getBounds().getX() + advanceX);
            }
        }
    }

	public static void increasePoolWidth(TDefinitions definitions, TParticipant pool, int width) throws BPMNError {
		DiagramElement diagramElement = BPMNHelper.getDiagramElement(definitions, pool);
		BPMNShape shape = (BPMNShape) diagramElement;
		shape.getBounds().setWidth(shape.getBounds().getWidth() + width);
	}

	public static void linkBPMNPlaneToProcess(TDefinitions definitions, TProcess centralProcess) {
		BPMNPlane plane = BPMNHelper.getPlane(definitions);
		plane.setBpmnElement(new QName(Namespace.ROOT.getNamespaceURI(), centralProcess.getId()));
	}

	public static void linkPoolToProcess(TParticipant centralPool, TProcess centralProcess) {
		centralPool.setProcessRef(new QName(Namespace.ROOT.getNamespaceURI(), centralProcess.getId()));
	}
	
	public static void linkProcessToCollaboration(TProcess process, String collaborationId) {
		if (collaborationId == null) process.setDefinitionalCollaborationRef(null);
		else process.setDefinitionalCollaborationRef(new QName(Namespace.COLLABORATION.getNamespaceURI(), collaborationId));
	}
	
	public static void convertAllDataObjectItemSubjectRefToDroolsDataType(TDefinitions definitions) throws BPMNError {
		for (TDataObject dataObject : BPMNHelper.getAllDataObjects(definitions)) {
			convertDataObjectItemSubjectRefToDroolsDataType(definitions, dataObject);
		}
	}
	
	public static void convertDataObjectItemSubjectRefToDroolsDataType(TDefinitions definitions, TDataObject dataObject) throws BPMNError {
		if (dataObject.getItemSubjectRef() != null) {
			TItemDefinition itemDefinition = BPMNHelper.getItemDefinition(definitions, 
					dataObject.getItemSubjectRef().getLocalPart());
	
			if (itemDefinition.getStructureRef().getNamespaceURI().equals(Namespace.XS.getNamespaceURI())) {
				QName qname = new QName(Namespace.DROOLS.getNamespaceURI(), "datype");
				
				switch (itemDefinition.getStructureRef().getLocalPart()) {
				case "boolean":
					dataObject.getOtherAttributes().put(qname, "Boolean");
					break;
					
				case "short":
				case "int":
				case "integer":
					dataObject.getOtherAttributes().put(qname, "Integer");
					break;

				case "long":
					dataObject.getOtherAttributes().put(qname, "Long");
					break;
					
				case "decimal":
					dataObject.getOtherAttributes().put(qname, "Double");
					break;
					
				default:
					dataObject.getOtherAttributes().put(qname, "String");
					break;
				}
			}
		}
	}
	
	public static void setProcessAsExecutable(TProcess centralProcess, boolean executable) {
		centralProcess.setIsExecutable(executable);
	}
	
	public static void setProcessId(TDefinitions definitions, TProcess centralProcess, String id) {
		centralProcess.setId(id);
	}
	
	public static void setPropertyIdsToNames(TProcess process) {
		for (TProperty property : process.getProperty()) {
			property.setId(property.getName());
		}
	}
	
	public static void setProcessName(TProcess centralProcess, String name) {
		centralProcess.setName(name);
	}
	
	/**
	 * Transform every item definition to use structureRef specification namespace if not already.
	 */
	public static void transformAllItemDefinitions(TDefinitions definitions) {
		for (TItemDefinition itemDefinition : BPMNHelper.getAllItemDefinitions(definitions)) {
			if (itemDefinition.getStructureRef().getNamespaceURI().equals(Namespace.XS.getNamespaceURI())) {
				switch (itemDefinition.getStructureRef().getLocalPart()) {
				case "boolean":
					itemDefinition.setStructureRef(new QName(Namespace.ROOT.getNamespaceURI(), "Boolean"));
					break;
					
				case "short":
				case "int":
				case "integer":
					itemDefinition.setStructureRef(new QName(Namespace.ROOT.getNamespaceURI(), "Integer"));
					break;

				case "long":
					itemDefinition.setStructureRef(new QName(Namespace.ROOT.getNamespaceURI(), "Long"));
					break;
					
				case "decimal":
					itemDefinition.setStructureRef(new QName(Namespace.ROOT.getNamespaceURI(), "Double"));
					break;
					
				default:
					itemDefinition.setStructureRef(new QName(Namespace.ROOT.getNamespaceURI(), "String"));
					break;
				}
			}
		}
	}

    private static void updateEdge(TDefinitions definitions, TSequenceFlow sequenceFlow, 
    		TBaseElement source, TBaseElement target) throws BPMNError {
    	DiagramElement diagramSource = BPMNHelper.getDiagramElement(definitions, source);
    	DiagramElement diagramTarget = BPMNHelper.getDiagramElement(definitions, target);
    	
		DiagramElement diagramElement = BPMNHelper.getDiagramElement(definitions, sequenceFlow);
		BPMNEdge edge = (BPMNEdge) diagramElement;
		edge.setSourceElement(new QName(Namespace.ROOT.getNamespaceURI(), diagramSource.getId()));
		edge.setTargetElement(new QName(Namespace.ROOT.getNamespaceURI(), diagramTarget.getId()));
		
		// update waypoints
		Bounds sourceBounds = BPMNHelper.getBoundsOfElement(definitions, source);
		Bounds targetBounds = BPMNHelper.getBoundsOfElement(definitions, target);
		
		Point waypoint1 = new Point();
		waypoint1.setX(sourceBounds.getX() + sourceBounds.getWidth());
		waypoint1.setY(sourceBounds.getY() + sourceBounds.getHeight() / 2.0f);
		edge.getWaypoint().set(0, waypoint1);

		Point waypoint2 = new Point();
		waypoint2.setX(targetBounds.getX());
		waypoint2.setY(targetBounds.getY() + targetBounds.getHeight() / 2.0f);
		edge.getWaypoint().set(0, waypoint2);
	}
    
    public static void updateEdgesWithDiagramElementAsSource(TDefinitions definitions, DiagramElement oldDiagramElement, 
    		DiagramElement newDiagramElement) {
        BPMNPlane plane = definitions.getBPMNDiagram().get(0).getBPMNPlane();
		List<JAXBElement<? extends DiagramElement>> diagramElements = plane.getDiagramElement();
		Iterator<JAXBElement<? extends DiagramElement>> iterator = diagramElements.iterator();
        while (iterator.hasNext()) {
        	DiagramElement diagramElement = iterator.next().getValue();
            if (diagramElement instanceof BPMNEdge) {
            	BPMNEdge edge = (BPMNEdge) diagramElement;
            	if (edge.getTargetElement() != null && 
            			edge.getSourceElement().getLocalPart().equals(oldDiagramElement.getId())) {
	            	QName qname = new QName(Namespace.ROOT.getNamespaceURI(), newDiagramElement.getId());
	            	edge.setSourceElement(qname);
	            }
            }
        }
    }
    
    public static void updateEdgesWithDiagramElementAsTarget(TDefinitions definitions, DiagramElement oldDiagramElement, 
    		DiagramElement newDiagramElement) {
        BPMNPlane plane = definitions.getBPMNDiagram().get(0).getBPMNPlane();
		List<JAXBElement<? extends DiagramElement>> diagramElements = plane.getDiagramElement();
		Iterator<JAXBElement<? extends DiagramElement>> iterator = diagramElements.iterator();
        while (iterator.hasNext()) {
        	DiagramElement diagramElement = iterator.next().getValue();
            if (diagramElement instanceof BPMNEdge) {
            	BPMNEdge edge = (BPMNEdge) diagramElement;
            	if (edge.getTargetElement() != null && 
            			edge.getTargetElement().getLocalPart().equals(oldDiagramElement.getId())) {
	            	QName qname = new QName(Namespace.ROOT.getNamespaceURI(), newDiagramElement.getId());
	            	edge.setTargetElement(qname);
	            }
            }
        }
    }
    
    /**
     * Every sequence flow that has oldTask as its source is changed so it has newTask as the new source.
     * @throws BPMNError 
     */
    public static void updateSequenceFlowsWithTaskAsSource(TDefinitions definitions, TProcess process, TTask oldTask, TTask newTask) throws BPMNError {
        for (JAXBElement<? extends TFlowElement> element : process.getFlowElement()) {
            TFlowElement flowElement = element.getValue();
            if (flowElement instanceof TSequenceFlow) {
                TSequenceFlow sequenceFlow = (TSequenceFlow) flowElement;
                if (sequenceFlow.getSourceRef() == oldTask) {
                	sequenceFlow.setSourceRef(newTask);

                    // update diagram element
                	updateEdge(definitions, sequenceFlow, newTask, (TBaseElement) sequenceFlow.getTargetRef());
                }
            }
        }
    }

	/**
     * Every sequence flow that has oldTask as its target is changed so it has newTask as the new target.
	 * @throws BPMNError 
     */
    public static void updateSequenceFlowsWithTaskAsTarget(TDefinitions definitions, TProcess process, TTask oldTask, TTask newTask) throws BPMNError {
        for (JAXBElement<? extends TFlowElement> element : process.getFlowElement()) {
            TFlowElement flowElement = element.getValue();
            if (flowElement instanceof TSequenceFlow) {
                TSequenceFlow sequenceFlow = (TSequenceFlow) flowElement;
                if (sequenceFlow.getTargetRef() == oldTask) {
                	sequenceFlow.setTargetRef(newTask);

                    // update diagram element
                	updateEdge(definitions, sequenceFlow, (TBaseElement) sequenceFlow.getSourceRef(), newTask);
                }
            }
        }
    }

    /**
     * Every association that has oldTask as its source is changed so it has newTask as the new source.
     */
    public static void updateAssociationsWithTaskAsSource(TProcess process, TTask oldTask, TTask newTask) {
        for (JAXBElement<? extends TArtifact> element : process.getArtifact()) {
            TArtifact artifact = element.getValue();
            if (artifact instanceof TAssociation) {
                TAssociation association = (TAssociation) artifact;
                if (association.getSourceRef().getLocalPart().equals(oldTask.getId())) {
                    QName qname = new QName(Namespace.ROOT.getNamespaceURI(), newTask.getId());
                    association.setSourceRef(qname);
                }
            }
        }
    }

    /**
     * Every association that has oldTask as its source is changed so it has newTask as the new source.
     */
    public static void updateAssociationsWithTaskAsTarget(TProcess process, TTask oldTask, TTask newTask) {
        for (JAXBElement<? extends TArtifact> element : process.getArtifact()) {
            TArtifact artifact = element.getValue();
            if (artifact instanceof TAssociation) {
                TAssociation association = (TAssociation) artifact;
                if (association.getTargetRef().getLocalPart().equals(oldTask.getId())) {
                    QName qname = new QName(Namespace.ROOT.getNamespaceURI(), newTask.getId());
                    association.setTargetRef(qname);
                }
            }
        }
    }
	
}
