package decompose;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import bpmn2.TActivity;
import bpmn2.TAssociation;
import bpmn2.TBoundaryEvent;
import bpmn2.TBusinessRuleTask;
import bpmn2.TCollaboration;
import bpmn2.TComplexGateway;
import bpmn2.TDataInputAssociation;
import bpmn2.TDataObject;
import bpmn2.TDataStoreReference;
import bpmn2.TEndEvent;
import bpmn2.TEventBasedGateway;
import bpmn2.TExclusiveGateway;
import bpmn2.TFlowElement;
import bpmn2.TGateway;
import bpmn2.TInclusiveGateway;
import bpmn2.TIntermediateCatchEvent;
import bpmn2.TIntermediateThrowEvent;
import bpmn2.TLane;
import bpmn2.TLaneSet;
import bpmn2.TManualTask;
import bpmn2.TMessageEventDefinition;
import bpmn2.TMessageFlow;
import bpmn2.TParallelGateway;
import bpmn2.TProcess;
import bpmn2.TReceiveTask;
import bpmn2.TScriptTask;
import bpmn2.TSendTask;
import bpmn2.TSequenceFlow;
import bpmn2.TServiceTask;
import bpmn2.TStartEvent;
import bpmn2.TTask;
import bpmn2.TUserTask;

public class ProcessTransformationHandler {

	private TCollaboration collaboration;
	private List<TProcess> processList;
	private TCollaboration newCollaboration;
	private List<TProcess> newProcessList;
	private Set<Path> candidatePaths;
	private List<Node> blackList;
	private List<AssociationPair<String, TAssociation>> associationPairList;

	private static final String generatedTaskName = "generatedTask_";
	private static final String generatedTaskId = "GeneratedTask_";
	private int generatedTaskCounter;

	private static final String generatedStartEventName = "GSM_";
	private static final String generatedStartEventId = "GeneratedStartEvent_";
	private int generatedStartEventCounter;

	private static final String generatedEndEventName = "GE_";
	private static final String generatedEndEventId = "GeneratedEndEvent_";
	private int generatedEndEventCounter;

	private static final String parallelGatewayName = "GPIF_";
	private static final String parallelGatewayId = "GeneratedParallelGateway_";
	private int parallelGatewayCounter;

	private static final String generatedMessageDefinitionEventId = "GeneratedMessageDefinitionEvent_";
	private int generatedStartMessageEventCounter;

	private static final String generatedSequenceFlowId = "GeneratedSequenceFlow_";
	private int generatedSequenceFlowCounter;

	private static final String generatedMessageFlowId = "GeneratedMessageFlow_";
	private int generatedMessageFlowCounter;

	private static final String generatedAssociationId = "GeneratedAssociation_";
	private int generatedAssociationCounter;

	private static final String namespaceURIModel = "http://www.omg.org/spec/BPMN/20100524/MODEL";

	public ProcessTransformationHandler(TCollaboration collaboration, List<TProcess> processList,
			Set<Path> transitivePaths, List<AssociationPair<String, TAssociation>> associationPairList) {
		this.collaboration = collaboration;
		this.processList = processList;
		this.candidatePaths = transitivePaths;
		this.associationPairList = associationPairList;
		newCollaboration = new TCollaboration();
		newProcessList = new ArrayList<>();
		blackList = new ArrayList<>();
		generatedStartEventCounter = 1;
		generatedEndEventCounter = 1;
		parallelGatewayCounter = 1;
		generatedTaskCounter = 1;
		generatedStartMessageEventCounter = 1;
		generatedSequenceFlowCounter = 1;
		generatedMessageFlowCounter = 1;
		generatedAssociationCounter = 1;
	}

	private boolean messageFlowAlreadyExists(TMessageFlow messageFlow) {
		for (TMessageFlow mf : collaboration.getMessageFlow()) {
			if (mf.getId().equals(messageFlow.getId())) {
				return true;
			}
		}
		return false;
	}

	private boolean associationAlreadyExists(TAssociation association) {
		for (int a = 0; a < associationPairList.size(); a++) {
			if (association.getId().equals(associationPairList.get(a).getAssociation().getId())) {
				return true;
			}
		}
		return false;
	}

	private boolean associationAlreadyExists(String source, String target){
		for(int a = 0; a < associationPairList.size(); a++){
			TAssociation association = associationPairList.get(a).getAssociation();
			
			if(association.getSourceRef().getLocalPart().equals(source) && association.getTargetRef().getLocalPart().equals(target)){
				return true;
			}
		}
		return false;
	}
	
	private boolean flowElementAlreadyExists(TFlowElement flowElement) {
		for (TProcess process : processList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				if (process.getFlowElement().get(i).getValue().getId().equals(flowElement.getId())) {
					return true;
				}
			}
		}

		return false;
	}

	private TMessageFlow createMessageFlow(String source, String target) {
		TMessageFlow messageFlow = new TMessageFlow();
		do {
			messageFlow.setId(generatedMessageFlowId + generatedMessageFlowCounter);
			generatedMessageFlowCounter++;
		} while (messageFlowAlreadyExists(messageFlow));
		messageFlow.setSourceRef(new QName(source));
		messageFlow.setTargetRef(new QName(target));
		return messageFlow;
	}

	private TAssociation createAssociation(String source, String target) {
		TAssociation association = new TAssociation();
		do {
			association.setId(generatedAssociationId + generatedAssociationCounter);
			generatedAssociationCounter++;
		} while (associationAlreadyExists(association));
		association.setSourceRef(new QName("", source));
		association.setTargetRef(new QName("", target));
		return association;
	}

	private TSequenceFlow createSequenceFlow() {
		TSequenceFlow sequenceFlow = new TSequenceFlow();
		do {
			sequenceFlow.setId(generatedSequenceFlowId + generatedSequenceFlowCounter);
			generatedSequenceFlowCounter++;
		} while (flowElementAlreadyExists(sequenceFlow));
		return sequenceFlow;
	}

	private TStartEvent createStartEvent() {
		TStartEvent startEvent = new TStartEvent();
		do {
			startEvent.setName(generatedStartEventName + generatedStartEventCounter);
			startEvent.setId(generatedStartEventId + generatedStartEventCounter);
			generatedStartEventCounter++;
		} while (flowElementAlreadyExists(startEvent));
		return startEvent;
	}

	private TEndEvent createEndEvent() {
		TEndEvent endEvent = new TEndEvent();
		do {
			endEvent.setName(generatedEndEventName + generatedEndEventCounter);
			endEvent.setId(generatedEndEventId + generatedEndEventCounter);
			generatedEndEventCounter++;
		} while (flowElementAlreadyExists(endEvent));
		return endEvent;
	}

	private TSendTask createSendTask() {
		TSendTask sendTask = new TSendTask();
		do {
			sendTask.setName(generatedTaskName + generatedTaskCounter);
			sendTask.setId(generatedTaskId + generatedTaskCounter);
			generatedTaskCounter++;
		} while (flowElementAlreadyExists(sendTask));
		return sendTask;
	}

	private TParallelGateway createParalelGateway() {
		TParallelGateway parallelGateway = new TParallelGateway();
		do {
			parallelGateway.setName(parallelGatewayName + parallelGatewayCounter);
			parallelGateway.setId(parallelGatewayId + parallelGatewayCounter);
			parallelGatewayCounter++;
		} while (flowElementAlreadyExists(parallelGateway));
		return parallelGateway;
	}

	private TMessageEventDefinition createMessageEventDefinition() {
		TMessageEventDefinition messageEventDefinition = new TMessageEventDefinition();
		messageEventDefinition.setId(generatedMessageDefinitionEventId + generatedStartMessageEventCounter);
		generatedStartMessageEventCounter++;
		return messageEventDefinition;
	}

	/*
	 * Initializes the new collaboration
	 */
	private void initializeNewCollaboration() {
		newCollaboration.setExtensionElements(collaboration.getExtensionElements());
		newCollaboration.setId(collaboration.getId());
		newCollaboration.setIsClosed(collaboration.isIsClosed());
		newCollaboration.setName(collaboration.getName());
		newCollaboration.getParticipant().addAll(collaboration.getParticipant());
	}

	/*
	 * Initializes the new process list
	 */
	private void initializeNewProcessesList() {
		for (TProcess process : processList) {
			TProcess newProcess = new TProcess();
			newProcess.setAuditing(process.getAuditing());
			newProcess.setDefinitionalCollaborationRef(process.getDefinitionalCollaborationRef());
			newProcess.setExtensionElements(process.getExtensionElements());
			newProcess.setId(process.getId());
			newProcess.setIoSpecification(process.getIoSpecification());
			newProcess.setIsClosed(process.isIsClosed());
			newProcess.setIsExecutable(process.isIsExecutable());
			newProcess.setMonitoring(process.getMonitoring());
			newProcess.setName(process.getName());
			newProcess.setProcessType(process.getProcessType());

			for (int i = 0; i < process.getArtifact().size(); i++) {
				newProcess.getArtifact().add(process.getArtifact().get(i));
			}

			for (TLaneSet laneSet : process.getLaneSet()) {
				TLaneSet newLaneSet = new TLaneSet();
				newLaneSet.setId(laneSet.getId());
				newLaneSet.setName(laneSet.getName());
				for (int i = 0; i < laneSet.getLane().size(); i++) {
					TLane newLane = new TLane();
					newLane.setId(laneSet.getLane().get(i).getId());
					newLane.setName(laneSet.getLane().get(i).getName());
					newLaneSet.getLane().add(newLane);
				}
				newProcess.getLaneSet().add(newLaneSet);
			}

			newProcessList.add(newProcess);
		}
	}

	private boolean isInTransitivePath(TFlowElement tFlowElement) {
		for (Path path : candidatePaths) {
			for (Node node : path.getNodes()) {
				if (node.getValue() == tFlowElement)
					return true;
			}
		}
		return false;
	}

	private void addUnmodifiedActivitiesAndSequenceFlowsToNewProcessList() {
		/*
		 * Adds all unmodified activities
		 */
		for (TProcess process : processList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				TFlowElement flowElement = process.getFlowElement().get(i).getValue();
				if (!flowElement.getClass().getName().equals("bpmn2.TSequenceFlow")) {
					if (!isInTransitivePath(flowElement)) {
						addNodeToNewProcessList(new Node(flowElement, process.getName()));
					}
				}
			}
		}

		/*
		 * Adds the corresponding sequence flows
		 */

		for (TProcess process : processList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				if (process.getFlowElement().get(i).getValue().getClass().getName().equals("bpmn2.TSequenceFlow")) {
					TSequenceFlow sequenceFlow = (TSequenceFlow) process.getFlowElement().get(i).getValue();

					for (TProcess newProcess : newProcessList) {

						int found = 0;

						for (int j = 0; j < newProcess.getFlowElement().size(); j++) {
							TFlowElement flowElement = newProcess.getFlowElement().get(j).getValue();
							if (flowElement == sequenceFlow.getSourceRef()
									|| flowElement == sequenceFlow.getTargetRef()) {
								found++;
								if (found == 2) {
									addNodeToNewProcessList(new Node(sequenceFlow, process.getName()));
									found = 0;
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	/*
	 * Deletes all outdated flow nodes
	 */
	private void deletePreviousInOutFlowNodes() {
		for (TProcess process : newProcessList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				TFlowElement flowElement = process.getFlowElement().get(i).getValue();
				if (!flowElement.getClass().getName().equals("bpmn2.TSequenceFlow")
						&& !flowElement.getClass().getName().equals("bpmn2.TDataStoreReference")
						&& !flowElement.getClass().getName().equals("bpmn2.TDataObject")) {
					try {
						Object object = flowElement;
						Class<?> clazz = object.getClass();
						Method m = clazz.getMethod("getIncoming");
						((List<QName>) m.invoke(object)).clear();
						m = clazz.getMethod("getOutgoing");
						((List<QName>) m.invoke(object)).clear();
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/*
	 * Adds a given sequence flow id to the corresponding incoming or outgoing
	 * node flow
	 */
	private void addNewInOutFlowNodes() {
		for (TProcess process : newProcessList) {
			for (JAXBElement<? extends TFlowElement> flowElement : process.getFlowElement()) {
				if (flowElement.getValue().getClass().getName().equals("bpmn2.TSequenceFlow")) {
					try {
						TSequenceFlow sequenceFlow = (TSequenceFlow) flowElement.getValue();

						Object source = sequenceFlow.getSourceRef();
						Object target = sequenceFlow.getTargetRef();

						if (!source.toString().contains("bpmn2.TDataStoreReference")
								&& !target.toString().contains("bpmn2.TDataStoreReference")
								&& !source.toString().contains("bpmn2.TDataObject")
								&& !target.toString().contains("bpmn2.TDataObject")) {

							Class<?> clazz = source.getClass();
							Method m = clazz.getMethod("getOutgoing");
							((List<QName>) m.invoke(source)).add(new QName(sequenceFlow.getId()));

							clazz = target.getClass();
							m = clazz.getMethod("getIncoming");
							((List<QName>) m.invoke(target)).add(new QName(sequenceFlow.getId()));
						}
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void addNodeToNewProcessList(Node node) {
		if (!isInNewProcessList(node.getValue())) {
			for (TProcess process : newProcessList) {

				if (process.getName().equals(node.getPool())) {

					switch (node.getValue().getClass().getName()) {
					case "bpmn2.TStartEvent":
						process.getFlowElement().add(
								new JAXBElement<TStartEvent>(new QName(namespaceURIModel, "startEvent"),
										TStartEvent.class, (TStartEvent) node.getValue()));
						break;
					case "bpmn2.TEndEvent":
						process.getFlowElement().add(
								new JAXBElement<TEndEvent>(new QName(namespaceURIModel, "endEvent"), TEndEvent.class,
										(TEndEvent) node.getValue()));
						break;
					case "bpmn2.TIntermediateCatchEvent":
						process.getFlowElement().add(
								new JAXBElement<TIntermediateCatchEvent>(new QName(namespaceURIModel,
										"intermediateCatchEvent"), TIntermediateCatchEvent.class,
										(TIntermediateCatchEvent) node.getValue()));
						break;
					case "bpmn2.TIntermediateThrowEvent":
						process.getFlowElement().add(
								new JAXBElement<TIntermediateThrowEvent>(new QName(namespaceURIModel,
										"intermediateThrowEvent"), TIntermediateThrowEvent.class,
										(TIntermediateThrowEvent) node.getValue()));
						break;
					case "bpmn2.TBoundaryEvent":
						process.getFlowElement().add(
								new JAXBElement<TBoundaryEvent>(new QName(namespaceURIModel, "boundaryEvent"),
										TBoundaryEvent.class, (TBoundaryEvent) node.getValue()));
						break;
					case "bpmn2.TTask":
						process.getFlowElement().add(
								new JAXBElement<TTask>(new QName(namespaceURIModel, "task"), TTask.class, (TTask) node
										.getValue()));
						break;
					case "bpmn2.TManualTask":
						process.getFlowElement().add(
								new JAXBElement<TManualTask>(new QName(namespaceURIModel, "manualTask"),
										TManualTask.class, (TManualTask) node.getValue()));
						break;
					case "bpmn2.TUserTask":
						process.getFlowElement().add(
								new JAXBElement<TUserTask>(new QName(namespaceURIModel, "userTask"), TUserTask.class,
										(TUserTask) node.getValue()));
						break;
					case "bpmn2.TScriptTask":
						process.getFlowElement().add(
								new JAXBElement<TScriptTask>(new QName(namespaceURIModel, "scriptTask"),
										TScriptTask.class, (TScriptTask) node.getValue()));
						break;
					case "bpmn2.TBusinessRuleTask":
						process.getFlowElement().add(
								new JAXBElement<TBusinessRuleTask>(new QName(namespaceURIModel, "businessRuleTask"),
										TBusinessRuleTask.class, (TBusinessRuleTask) node.getValue()));
						break;
					case "bpmn2.TServiceTask":
						process.getFlowElement().add(
								new JAXBElement<TServiceTask>(new QName(namespaceURIModel, "serviceTask"),
										TServiceTask.class, (TServiceTask) node.getValue()));
						break;
					case "bpmn2.TSendTask":
						process.getFlowElement().add(
								new JAXBElement<TSendTask>(new QName(namespaceURIModel, "sendTask"), TSendTask.class,
										(TSendTask) node.getValue()));
						break;
					case "bpmn2.TReceiveTask":
						process.getFlowElement().add(
								new JAXBElement<TReceiveTask>(new QName(namespaceURIModel, "receiveTask"),
										TReceiveTask.class, (TReceiveTask) node.getValue()));
						break;
					case "bpmn2.TGateway":
						process.getFlowElement().add(
								new JAXBElement<TGateway>(new QName(namespaceURIModel, "gateway"), TGateway.class,
										(TGateway) node.getValue()));
						break;
					case "bpmn2.TExclusiveGateway":
						process.getFlowElement().add(
								new JAXBElement<TExclusiveGateway>(new QName(namespaceURIModel, "exclusiveGateway"),
										TExclusiveGateway.class, (TExclusiveGateway) node.getValue()));
						break;
					case "bpmn2.TInclusiveGateway":
						process.getFlowElement().add(
								new JAXBElement<TInclusiveGateway>(new QName(namespaceURIModel, "inclusiveGateway"),
										TInclusiveGateway.class, (TInclusiveGateway) node.getValue()));
						break;
					case "bpmn2.TParallelGateway":
						process.getFlowElement().add(
								new JAXBElement<TParallelGateway>(new QName(namespaceURIModel, "parallelGateway"),
										TParallelGateway.class, (TParallelGateway) node.getValue()));
						break;
					case "bpmn2.TEventBasedGateway":
						process.getFlowElement().add(
								new JAXBElement<TEventBasedGateway>(new QName(namespaceURIModel, "eventBasedGateway"),
										TEventBasedGateway.class, (TEventBasedGateway) node.getValue()));
						break;
					case "bpmn2.TComplexGateway":
						process.getFlowElement().add(
								new JAXBElement<TComplexGateway>(new QName(namespaceURIModel, "complexGateway"),
										TComplexGateway.class, (TComplexGateway) node.getValue()));
						break;
					case "bpmn2.TSequenceFlow":
						process.getFlowElement().add(
								new JAXBElement<TSequenceFlow>(new QName(namespaceURIModel, "sequenceFlow"),
										TSequenceFlow.class, (TSequenceFlow) node.getValue()));
						break;
					case "bpmn2.TDataObject":
						process.getFlowElement().add(
								new JAXBElement<TDataObject>(new QName(namespaceURIModel, "dataObject"),
										TDataObject.class, (TDataObject) node.getValue()));
						break;
					case "bpmn2.TDataStoreReference":
						process.getFlowElement().add(
								new JAXBElement<TDataStoreReference>(
										new QName(namespaceURIModel, "dataStoreReference"), TDataStoreReference.class,
										(TDataStoreReference) node.getValue()));
						break;
					default:
						System.err.println("UNKNOW NODE TYPE IN addNodeToNewProcessList");
					}
				}
			}
		}
	}

	/*
	 * Searches for a sequence flow that contains the source reference of a
	 * given object
	 */
	private boolean sourceRefNotFound(TFlowElement flowElement) {
		if (!flowElement.getClass().getName().equals("bpmn2.TEndEvent") && !FlowElementUtil.isDataType(flowElement)) {
			for (TProcess process : newProcessList) {
				for (int i = 0; i < process.getFlowElement().size(); i++) {
					if (process.getFlowElement().get(i).getValue().getClass().getName().equals("bpmn2.TSequenceFlow")) {
						TSequenceFlow sf = (TSequenceFlow) process.getFlowElement().get(i).getValue();

						if (sf.getSourceRef() == flowElement) {
							return false;
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	/*
	 * Searches for a sequence flow that contains the target reference of a
	 * given object
	 */
	private boolean targetRefNotFound(TFlowElement flowElement) {
		if (!flowElement.getClass().getName().equals("bpmn2.TStartEvent") && !FlowElementUtil.isDataType(flowElement)) {
			for (TProcess process : newProcessList) {
				for (int i = 0; i < process.getFlowElement().size(); i++) {
					if (process.getFlowElement().get(i).getValue().getClass().getName().equals("bpmn2.TSequenceFlow")) {
						TSequenceFlow sf = (TSequenceFlow) process.getFlowElement().get(i).getValue();

						if (sf.getTargetRef() == flowElement) {
							return false;
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	private boolean isInNewProcessList(TFlowElement flowElement) {
		for (TProcess process : newProcessList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				if (process.getFlowElement().get(i).getValue() == flowElement) {
					return true;
				}
			}
		}
		return false;
	}

	private void case1(Node receiveTask, Node sendTask, Node previousReceiveOtherPool, Node nextSendOtherPool) {

		Node nextSendSamePool = null;

		/*
		 * Keeps searching for the next node on the same pool till it reaches an
		 * end event
		 */
		Node previousReceiveOtherPoolNext = null;
		for (Node next : previousReceiveOtherPool.getNext()) {
			if (next.getPool().equals(previousReceiveOtherPool.getPool())) {
				previousReceiveOtherPoolNext = next;
			}
		}

		if (!previousReceiveOtherPoolNext.getValue().getClass().getName().equals("bpmn2.TEndEvent")
				|| previousReceiveOtherPoolNext.getValue().getClass().getName().equals("bpmn2.TReceiveTask")) {
			while (!previousReceiveOtherPoolNext.getValue().getClass().getName().equals("bpmn2.TEndEvent")
					&& !previousReceiveOtherPoolNext.getValue().getClass().getName().equals("bpmn2.TReceiveTask")) {
				for (Node next : previousReceiveOtherPool.getNext()) {
					if (next.getPool().equals(previousReceiveOtherPool.getPool())) {
						previousReceiveOtherPool = next;
						previousReceiveOtherPoolNext = previousReceiveOtherPool.getNext().iterator().next();
					}
				}
			}
		}

		for (Node next : sendTask.getNext()) {
			if (next.getPool().equals(sendTask.getPool())) {
				nextSendSamePool = next;
			}
		}

		if (previousReceiveOtherPool.getPool().equals(nextSendOtherPool.getPool())) {

			/*
			 * Deletes the sequence flow of the task that connected to an end
			 * event that wont be used anymore
			 */
			if (FlowElementUtil.isTask(previousReceiveOtherPool.getValue())
					&& previousReceiveOtherPoolNext.getValue().getClass().getName().equals("bpmn2.TEndEvent")) {
				for (TProcess process : newProcessList) {
					if (process.getName().equals(previousReceiveOtherPool.getPool())) {
						for (int i = 0; i < process.getFlowElement().size(); i++) {
							if (process.getFlowElement().get(i).getValue().getClass().getName()
									.equals("bpmn2.TSequenceFlow")) {
								TSequenceFlow sequenceFlow = (TSequenceFlow) process.getFlowElement().get(i).getValue();
								if (sequenceFlow.getTargetRef() == previousReceiveOtherPoolNext.getValue()) {
									process.getFlowElement().remove(i);
									break;
								}
							}
						}
					}
				}
			}
			TSequenceFlow connectPreviousReceiveToNextNextSend = createSequenceFlow();
			connectPreviousReceiveToNextNextSend.setSourceRef(previousReceiveOtherPool.getValue());
			connectPreviousReceiveToNextNextSend.setTargetRef(nextSendOtherPool.getValue());

			TSequenceFlow connectReceiveToNextSend = createSequenceFlow();
			connectReceiveToNextSend.setSourceRef(receiveTask.getValue());
			connectReceiveToNextSend.setTargetRef(nextSendSamePool.getValue());

			addNodeToNewProcessList(new Node(connectPreviousReceiveToNextNextSend, previousReceiveOtherPool.getPool()));
			addNodeToNewProcessList(new Node(connectReceiveToNextSend, receiveTask.getPool()));
		}
	}

	/*
	 * Moves the timer event to the correct pool, creates a sequence flow to
	 * unite the timer event with the next node of the message event and turns
	 * the receive task into a start message event definition
	 */
	private void case2(Node timerEvent, Node startMessageEvent, Node receiveTask) {

		timerEvent.setPool(startMessageEvent.getPool());
		addNodeToNewProcessList(timerEvent);

		TSequenceFlow sequenceFlow = createSequenceFlow();
		sequenceFlow.setSourceRef(timerEvent.getValue());
		sequenceFlow.setTargetRef(startMessageEvent.getNext().iterator().next().getValue());

		addNodeToNewProcessList(new Node(sequenceFlow, startMessageEvent.getPool()));

		// Create start message event definition
		TStartEvent startEvent = createStartEvent();
		TMessageEventDefinition messageDefinition = createMessageEventDefinition();
		JAXBElement<TMessageEventDefinition> jaxbMessage = new JAXBElement<TMessageEventDefinition>(new QName(
				namespaceURIModel, "messageEventDefinition"), TMessageEventDefinition.class, messageDefinition);
		List<JAXBElement<TMessageEventDefinition>> list = new ArrayList<JAXBElement<TMessageEventDefinition>>(
				Arrays.asList(jaxbMessage));
		startEvent.setEventDefinition(list);

		addNodeToNewProcessList(new Node(startEvent, receiveTask.getPool()));

		/*
		 * If the receive task was already added on the new process list it will
		 * delete the task and update the corresponding sequence flow
		 */
		if (isInNewProcessList(receiveTask.getValue())) {
			for (TProcess process : newProcessList) {
				if (process.getName().equals(receiveTask.getPool())) {
					for (int i = 0; i < process.getFlowElement().size(); i++) {
						TFlowElement flowElement = process.getFlowElement().get(i).getValue();
						if (flowElement == receiveTask.getValue()) {
							process.getFlowElement().remove(i);
							i--;
						} else if (flowElement.getClass().getName().equals("bpmn2.TSequenceFlow")) {
							TSequenceFlow sf = (TSequenceFlow) process.getFlowElement().get(i).getValue();
							if (sf.getSourceRef() == receiveTask.getValue()) {
								sf.setSourceRef(startEvent);
							}
						}
					}
				}
			}
		}

		// Updates the corresponding previous and next nodes to point to the new
		// start message definition
		receiveTask.setValue(startEvent);

	}

	private Node processSendTaskInGatewayBranch(Node previousGatewayNode, Node nextNodeOtherPool) {

		/*
		 * Removes the unnecessary communication
		 */

		blackList.add(nextNodeOtherPool);

		Node toConnect = nextNodeOtherPool.getNext().iterator().next();
		boolean endEventFound = false;

		if (previousGatewayNode.getPool().equals(toConnect.getPool())) {
			TSequenceFlow sequenceFlow = createSequenceFlow();
			sequenceFlow.setSourceRef(previousGatewayNode.getValue());
			sequenceFlow.setTargetRef(toConnect.getValue());
			addNodeToNewProcessList(new Node(sequenceFlow, previousGatewayNode.getPool()));
		}

		for (Node nextNode : nextNodeOtherPool.getNext()) {
			if (nextNode.getPool().equals(nextNodeOtherPool.getPool())) {
				nextNodeOtherPool = nextNode;
			}
		}

		// get the last task on the other pool
		while (!endEventFound) {
			for (Node nextNode : nextNodeOtherPool.getNext()) {
				if (nextNode.getPool().equals(nextNodeOtherPool.getPool())) {
					if (nextNode.getValue().getClass().getName().equals("bpmn2.TEndEvent")) {
						endEventFound = true;
					} else if (nextNode.getValue().getClass().getName().equals("bpmn2.TSendTask")) {
						blackList.add(nextNode);
						/*
						 * Connects tasks that were separated by a send task
						 */
						for (Node nextNodeSamePoolSendTask : nextNode.getNext()) {
							if (nextNodeSamePoolSendTask.getPool().equals(nextNode.getPool())) {
								if (FlowElementUtil.isTask(nextNodeSamePoolSendTask.getValue())) {
									for (Node previousNode : nextNodeSamePoolSendTask.getPrevious()) {
										if (previousNode.getPool().equals(nextNodeSamePoolSendTask.getPool())) {
											if (FlowElementUtil.isTask(previousNode.getValue())) {
												TSequenceFlow sequenceFlow = createSequenceFlow();
												sequenceFlow.setSourceRef(previousNode.getValue());
												sequenceFlow.setTargetRef(nextNodeSamePoolSendTask.getValue());
												addNodeToNewProcessList(new Node(sequenceFlow, nextNode.getPool()));

											}
										}
									}

								}
								if (!nextNodeSamePoolSendTask.getValue().getClass().getName().equals("bpmn2.TEndEvent")) {
									nextNodeOtherPool = nextNodeSamePoolSendTask;
								} else {
									endEventFound = true;
								}
							}
						}
					} else {
						if (FlowElementUtil.isTask(nextNode.getValue())) {
							nextNodeOtherPool = nextNode;
						}
					}
				}
			}
		}

		return nextNodeOtherPool;
	}

	/*
	 * Moves and adds the gateway to the correct pool, creates a sequence flow
	 * that connects the gateway to the previous node of the sendTask, creates
	 * and end event and connects it to the previous of the receive task,
	 */
	private void case3(Node previousSend, Node nextSend, Node receive, Node gateway) {

		gateway.setPool(previousSend.getPool());
		addNodeToNewProcessList(gateway);

		TParallelGateway parallelGateway = createParalelGateway();
		addNodeToNewProcessList(new Node(parallelGateway, gateway.getPool()));

		TSequenceFlow connectPreviousToParallelGateway = createSequenceFlow();
		connectPreviousToParallelGateway.setSourceRef(previousSend.getValue());
		connectPreviousToParallelGateway.setTargetRef(parallelGateway);
		addNodeToNewProcessList(new Node(connectPreviousToParallelGateway, gateway.getPool()));

		TSequenceFlow connectParallelGateway1 = createSequenceFlow();
		connectParallelGateway1.setSourceRef(parallelGateway);
		connectParallelGateway1.setTargetRef(nextSend.getValue());
		addNodeToNewProcessList(new Node(connectParallelGateway1, gateway.getPool()));

		TSequenceFlow connectParallelGateway2 = createSequenceFlow();
		connectParallelGateway2.setSourceRef(parallelGateway);
		connectParallelGateway2.setTargetRef(gateway.getValue());
		addNodeToNewProcessList(new Node(connectParallelGateway2, gateway.getPool()));

		/*
		 * Replaces the deleted receive task with an end event
		 */

		for (Node previous : receive.getPrevious()) {
			if (previous.getPool().equals(receive.getPool()) && isInNewProcessList(previous.getValue())) {
				TEndEvent endEvent = createEndEvent();
				addNodeToNewProcessList(new Node(endEvent, previous.getPool()));

				TSequenceFlow connectToEndEvent = createSequenceFlow();
				connectToEndEvent.setSourceRef(previous.getValue());
				connectToEndEvent.setTargetRef(endEvent);
				addNodeToNewProcessList(new Node(connectToEndEvent, previous.getPool()));
			}
		}

		// process the gateway paths

		for (Node firstNodeOnBranch : gateway.getNext()) {

			blackList.add(firstNodeOnBranch);

			if (FlowElementUtil.isTask(firstNodeOnBranch.getValue())) {
				// Create start message event definition
				TStartEvent startEvent = createStartEvent();
				TMessageEventDefinition messageDefinition = createMessageEventDefinition();
				JAXBElement<TMessageEventDefinition> jaxbMessage = new JAXBElement<TMessageEventDefinition>(new QName(
						namespaceURIModel, "messageEventDefinition"), TMessageEventDefinition.class, messageDefinition);
				List<JAXBElement<TMessageEventDefinition>> list = new ArrayList<JAXBElement<TMessageEventDefinition>>(
						Arrays.asList(jaxbMessage));
				startEvent.setEventDefinition(list);

				addNodeToNewProcessList(new Node(startEvent, firstNodeOnBranch.getPool()));

				TSequenceFlow connectStartEvent = createSequenceFlow();
				connectStartEvent.setSourceRef(startEvent);
				connectStartEvent.setTargetRef(firstNodeOnBranch.getValue());
				addNodeToNewProcessList(new Node(connectStartEvent, firstNodeOnBranch.getPool()));

				TSendTask sendTask = createSendTask();
				addNodeToNewProcessList(new Node(sendTask, gateway.getPool()));

				TSequenceFlow connectSendTask = createSequenceFlow();
				connectSendTask.setSourceRef(gateway.getValue());
				connectSendTask.setTargetRef(sendTask);
				addNodeToNewProcessList(new Node(connectSendTask, gateway.getPool()));

				TEndEvent endEvent = createEndEvent();
				addNodeToNewProcessList(new Node(endEvent, gateway.getPool()));

				TSequenceFlow connectEndEvent = createSequenceFlow();
				connectEndEvent.setSourceRef(sendTask);
				connectEndEvent.setTargetRef(endEvent);
				addNodeToNewProcessList(new Node(connectEndEvent, gateway.getPool()));

				newCollaboration.getMessageFlow().add(createMessageFlow(sendTask.getId(), startEvent.getId()));
			} else if (firstNodeOnBranch.getValue().getClass().getName().equals("bpmn2.TSendTask")) {

				Node nextOtherPool = null;
				Node nextSamePool = null;

				for (Node nextSendFromGateway : firstNodeOnBranch.getNext()) {
					if (!nextSendFromGateway.getPool().equals(firstNodeOnBranch.getPool())) {
						nextOtherPool = nextSendFromGateway;
					} else {
						nextSamePool = nextSendFromGateway;
					}
				}

				nextOtherPool = processSendTaskInGatewayBranch(gateway, nextOtherPool);

				treatDataElements(firstNodeOnBranch, nextOtherPool);

				boolean endEventFound = false;
				boolean taskFound = false;
				Node previousNodeInGatewayBranch = nextOtherPool;

				while (!endEventFound && !taskFound) {

					blackList.add(nextSamePool);

					if (nextSamePool.getValue().getClass().getName().equals("bpmn2.TEndEvent")) {
						endEventFound = true;
					} else if (nextSamePool.getValue().getClass().getName().equals("bpmn2.TIntermediateThrowEvent")
							|| nextSamePool.getValue().getClass().getName().equals("bpmn2.TIntermediateCatchEvent")) {

						nextSamePool.setPool(gateway.getPool());
						addNodeToNewProcessList(nextSamePool);

						TSequenceFlow sequenceFlow = createSequenceFlow();
						sequenceFlow.setSourceRef(previousNodeInGatewayBranch.getValue());
						sequenceFlow.setTargetRef(nextSamePool.getValue());
						addNodeToNewProcessList(new Node(sequenceFlow, gateway.getPool()));
						previousNodeInGatewayBranch = nextSamePool;
					} else if (nextSamePool.getValue().getClass().getName().equals("bpmn2.TSendTask")) {

						for (Node nextNode : nextSamePool.getNext()) {
							if (!nextNode.getPool().equals(firstNodeOnBranch.getPool())) {
								nextOtherPool = nextNode;
							}
						}

						nextOtherPool = processSendTaskInGatewayBranch(previousNodeInGatewayBranch, nextOtherPool);

						treatDataElements(nextSamePool, nextOtherPool);

						previousNodeInGatewayBranch = nextOtherPool;
					} else {
						if (FlowElementUtil.isTask(nextSamePool.getValue())) {
							taskFound = true;
							break;
						}
					}

					if (!taskFound) {
						for (Node nextNode : nextSamePool.getNext()) {
							if (nextNode.getPool().equals(nextSamePool.getPool())) {
								nextSamePool = nextNode;
							} else if (nextSamePool.getValue().getClass().getName()
									.equals("bpmn2.TIntermediateThrowEvent")
									|| nextSamePool.getValue().getClass().getName()
											.equals("bpmn2.TIntermediateCatchEvent")) {
								nextSamePool = nextNode;
							}
						}
					}
				}

				/*
				 * If there is a task on the path (nextSamePool) create a send
				 * task to connect to the task
				 */
				if (taskFound) {
					/*
					 * Gateway Pool
					 */
					TSendTask sendTask = createSendTask();
					addNodeToNewProcessList(new Node(sendTask, previousNodeInGatewayBranch.getPool()));

					TSequenceFlow connectToSend = createSequenceFlow();
					connectToSend.setSourceRef(previousNodeInGatewayBranch.getValue());
					connectToSend.setTargetRef(sendTask);
					addNodeToNewProcessList(new Node(connectToSend, gateway.getPool()));

					TEndEvent endEvent = createEndEvent();
					addNodeToNewProcessList(new Node(endEvent, previousNodeInGatewayBranch.getPool()));

					TSequenceFlow connectToEndEvent = createSequenceFlow();
					connectToEndEvent.setSourceRef(sendTask);
					connectToEndEvent.setTargetRef(endEvent);
					addNodeToNewProcessList(new Node(connectToEndEvent, gateway.getPool()));

					blackList.add(previousNodeInGatewayBranch.getNext().iterator().next());
					/*
					 * Other Pool
					 */
					// Create start message event definition
					TStartEvent startEvent = createStartEvent();
					TMessageEventDefinition messageDefinition = createMessageEventDefinition();
					JAXBElement<TMessageEventDefinition> jaxbMessage = new JAXBElement<TMessageEventDefinition>(
							new QName(namespaceURIModel, "messageEventDefinition"), TMessageEventDefinition.class,
							messageDefinition);
					List<JAXBElement<TMessageEventDefinition>> list = new ArrayList<JAXBElement<TMessageEventDefinition>>(
							Arrays.asList(jaxbMessage));
					startEvent.setEventDefinition(list);

					addNodeToNewProcessList(new Node(startEvent, firstNodeOnBranch.getPool()));

					TSequenceFlow connectToTask = createSequenceFlow();
					connectToTask.setSourceRef(startEvent);

					// connects directly to the task only if the previous node
					// is the send task that was deleted or a receive task
					if (nextSamePool.getPrevious().iterator().next().getValue().getClass().getName()
							.equals("bpmn2.TSendTask")
							|| nextSamePool.getPrevious().iterator().next().getValue().getClass().getName()
									.equals("bpmn2.TReceiveTask")) {
						connectToTask.setTargetRef(nextSamePool.getValue());
					} else {
						connectToTask.setTargetRef(nextSamePool.getPrevious().iterator().next().getValue());
					}

					addNodeToNewProcessList(new Node(connectToTask, nextSamePool.getPool()));

					newCollaboration.getMessageFlow().add(createMessageFlow(sendTask.getId(), startEvent.getId()));
				}

			}
		}
	}

	private int getIndexOfElementToBeRemovedFromNewProcessList(TFlowElement flowElement) {
		for (TProcess process : newProcessList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				if (process.getFlowElement().get(i).getValue() == flowElement) {
					return i;
				}
			}
		}
		return -1;
	}

	/*
	 * Removes all activities that are not going to be used
	 */
	private void garbageCollector() {
		for (TProcess process : newProcessList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {

				TFlowElement flowElement = process.getFlowElement().get(i).getValue();

				if (flowElement.getClass().getName().equals("bpmn2.TEndEvent") && targetRefNotFound(flowElement)) {
					process.getFlowElement().remove(i);
					i--;
				} else if (flowElement.getClass().getName().equals("bpmn2.TStartEvent")
						&& FlowElementUtil.isMessageEventDefinition(new Node(flowElement, process.getName()))) {
					boolean startEventFound = false;

					for (TMessageFlow messageFlow : newCollaboration.getMessageFlow()) {
						if (messageFlow.getSourceRef().toString().equals(flowElement.getId())
								|| messageFlow.getTargetRef().toString().equals(flowElement.getId())) {
							startEventFound = true;
						}
					}

					if (!startEventFound) {
						// find the sequence flow attached to the start message
						// definition event and deletes it
						for (int j = 0; j < process.getFlowElement().size(); j++) {
							if (process.getFlowElement().get(j).getValue().getClass().getName()
									.equals("bpmn2.TSequenceFlow")) {
								TSequenceFlow sequenceFlow = (TSequenceFlow) process.getFlowElement().get(j).getValue();
								if (sequenceFlow.getSourceRef() == flowElement) {
									process.getFlowElement().remove(j);
									break;
								}
							}
						}

						process.getFlowElement().remove(i);
						i--;
					}
				} else if (flowElement.getClass().getName().equals("bpmn2.TSequenceFlow")) {
					TSequenceFlow sequenceFlow = (TSequenceFlow) flowElement;

					/*
					 * Deletes sequence flows that point to missing elements
					 */
					if (!isInNewProcessList((TFlowElement) sequenceFlow.getSourceRef())
							|| !isInNewProcessList((TFlowElement) sequenceFlow.getTargetRef())) {
						process.getFlowElement().remove(i);
						i--;
						/*
						 * Deletes sequence flows that are connected to end
						 * events whose source is a task that already has a
						 * sequence flow attached
						 */
					} else if (sequenceFlow.getTargetRef().getClass().getName().equals("bpmn2.TEndEvent")) {
						if (FlowElementUtil.isTask((TFlowElement) sequenceFlow.getSourceRef())) {
							for (int j = 0; j < process.getFlowElement().size(); j++) {
								if (process.getFlowElement().get(j).getValue().getClass().getName()
										.equals("bpmn2.TSequenceFlow")) {
									TSequenceFlow sf = (TSequenceFlow) process.getFlowElement().get(j).getValue();

									if (sf.getSourceRef() == sequenceFlow.getSourceRef()
											&& sf.getTargetRef() != sequenceFlow.getTargetRef()) {
										process.getFlowElement().remove(i);
										i--;
										break;
									}
								}
							}
						}
					}
					/*
					 * Deletes gateways thats are directly connected to an end
					 * event
					 */
				} else {
					if (FlowElementUtil.isGateway(flowElement)) {
						// get all outgoing connections to the gateway
						List<TSequenceFlow> gatewayOutgoingSf = new ArrayList<>();
						List<TSequenceFlow> gatewayIncomingSf = new ArrayList<>();
						for (int j = 0; j < process.getFlowElement().size(); j++) {
							if (process.getFlowElement().get(j).getValue().getClass().getName()
									.equals("bpmn2.TSequenceFlow")) {
								TSequenceFlow sequenceFlow = (TSequenceFlow) process.getFlowElement().get(j).getValue();
								if (sequenceFlow.getSourceRef() == flowElement) {
									gatewayOutgoingSf.add(sequenceFlow);
								} else if (sequenceFlow.getTargetRef() == flowElement) {
									gatewayIncomingSf.add(sequenceFlow);
								}
							}
						}

						if (gatewayOutgoingSf.size() == 1) {
							if (gatewayIncomingSf.size() == 1
									&& gatewayOutgoingSf.get(0).getTargetRef().getClass().getName()
											.equals("bpmn2.TEndEvent")) {
								TSequenceFlow sequenceFlow = createSequenceFlow();
								// set previous element as new source
								sequenceFlow.setSourceRef(gatewayIncomingSf.get(0).getSourceRef());
								sequenceFlow.setTargetRef(gatewayOutgoingSf.get(0).getTargetRef());
								// add new sequence flow
								addNodeToNewProcessList(new Node(sequenceFlow, process.getName()));
								// delete old gateway and sequence flows
								process.getFlowElement().remove(i);
								process.getFlowElement().remove(
										getIndexOfElementToBeRemovedFromNewProcessList(gatewayIncomingSf.get(0)));
								process.getFlowElement().remove(
										getIndexOfElementToBeRemovedFromNewProcessList(gatewayOutgoingSf.get(0)));
								i = -1;
							}
						} else {
							// if one of the outgoing connections leads to
							// an end event connects the previous of the
							// gateway to another
							// outgoing connection
							if (gatewayIncomingSf.size() == 1) {
								for (int j = 0; j < gatewayOutgoingSf.size(); j++) {
									if (gatewayOutgoingSf.get(j).getTargetRef().getClass().getName()
											.equals("bpmn2.TEndEvent")) {
										for (int k = 0; k < gatewayOutgoingSf.size(); k++) {
											if (k != j) {
												TSequenceFlow sequenceFlow = createSequenceFlow();
												// set previous element as
												// new source
												sequenceFlow.setSourceRef(gatewayIncomingSf.get(0).getSourceRef());
												sequenceFlow.setTargetRef(gatewayOutgoingSf.get(k).getTargetRef());
												// add new sequence flow
												addNodeToNewProcessList(new Node(sequenceFlow, process.getName()));
												// delete old gateway, end
												// event, incoming sequence
												// flow and both outgoing
												// sequence flows
												process.getFlowElement().remove(i);
												process.getFlowElement()
														.remove(getIndexOfElementToBeRemovedFromNewProcessList(gatewayIncomingSf
																.get(0)));
												process.getFlowElement()
														.remove(getIndexOfElementToBeRemovedFromNewProcessList(gatewayOutgoingSf
																.get(k)));
												process.getFlowElement()
														.remove(getIndexOfElementToBeRemovedFromNewProcessList((TFlowElement) gatewayOutgoingSf
																.get(j).getTargetRef()));
												process.getFlowElement()
														.remove(getIndexOfElementToBeRemovedFromNewProcessList(gatewayOutgoingSf
																.get(j)));
												i = -1;
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void createMissingElements() {
		for (TProcess process : newProcessList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {

				TFlowElement flowElement = process.getFlowElement().get(i).getValue();

				if (!flowElement.getClass().getName().equals("bpmn2.TSequenceFlow")
						&& !flowElement.getClass().getName().equals("bpmn2.TEndEvent")
						&& !FlowElementUtil.isDataType(flowElement)) {
					if (sourceRefNotFound(flowElement)) {

						boolean targetFound = false;

						for (int j = 0; j < process.getFlowElement().size() && !targetFound; j++) {

							TFlowElement flowElement2 = process.getFlowElement().get(j).getValue();
							if (!flowElement2.getClass().getName().equals("bpmn2.TSequenceFlow")) {
								if (targetRefNotFound(flowElement2)) {
									TSequenceFlow sequenceFlow = createSequenceFlow();
									sequenceFlow.setSourceRef(flowElement);
									sequenceFlow.setTargetRef(flowElement2);
									addNodeToNewProcessList(new Node(sequenceFlow, process.getName()));
									targetFound = true;
								}
							}
						}

						if (!targetFound) {
							TEndEvent endEvent = createEndEvent();
							addNodeToNewProcessList(new Node(endEvent, process.getName()));
							TSequenceFlow sequenceFlow = createSequenceFlow();
							sequenceFlow.setSourceRef(flowElement);
							sequenceFlow.setTargetRef(endEvent);
							addNodeToNewProcessList(new Node(sequenceFlow, process.getName()));
						}
					}
				}
			}
		}
	}

	private void endStep() {
		for (Path path : candidatePaths) {

			for (int i = 0; i < path.getNodes().size(); i++) {

				Node node = path.getNodes().get(i);

				if (!blackList.contains(node) && !isInNewProcessList(node.getValue())) {

					Node previous = null;

					if (!node.getPrevious().isEmpty()) {
						previous = node.getPrevious().iterator().next();
					}

					if (node.getValue().getClass().getName().equals("bpmn2.TSendTask")) {
						for (Node nextNode : node.getNext()) {
							if (nextNode.getPool().equals(node.getPool())) {
								if (!nextNode.getValue().getClass().getName().equals("bpmn2.TEndEvent")) {
									TSequenceFlow sequenceFlow = createSequenceFlow();
									sequenceFlow.setSourceRef(node.getValue());
									sequenceFlow.setTargetRef(nextNode.getValue());
									addNodeToNewProcessList(new Node(sequenceFlow, node.getPool()));
								}
							} else {
								newCollaboration.getMessageFlow().add(
										createMessageFlow(node.getValue().getId(), nextNode.getValue().getId()));
							}
						}
					}

					if (node.getValue().getClass().getName().equals("bpmn2.TReceiveTask")) {
						for (Node prevNode : node.getPrevious()) {
							if (prevNode.getPool().equals(node.getPool())) {
								if (targetRefNotFound(node.getValue())) {
									TSequenceFlow sequenceFlow = createSequenceFlow();
									sequenceFlow.setSourceRef(prevNode.getValue());
									sequenceFlow.setTargetRef(node.getValue());
									addNodeToNewProcessList(new Node(sequenceFlow, node.getPool()));
								}
							}
						}
						previous = null;
					}

					addNodeToNewProcessList(node);

					/*
					 * Creates a sequence flow between the current node and the
					 * previous node
					 */
					if (previous != null && !blackList.contains(previous)) {
						if (previous.getPool().equals(node.getPool())) {
							if (targetRefNotFound(node.getValue())) {
								TSequenceFlow sequenceFlow = createSequenceFlow();
								sequenceFlow.setSourceRef(previous.getValue());
								sequenceFlow.setTargetRef(node.getValue());
								addNodeToNewProcessList(new Node(sequenceFlow, node.getPool()));
							}
						}
					}

					// Creates all sequence flows for a given gateway node
					if (FlowElementUtil.isGateway(node.getValue())) {
						for (Node nextNode : node.getNext()) {
							TSequenceFlow sequenceFlow = createSequenceFlow();
							sequenceFlow.setSourceRef(node.getValue());
							sequenceFlow.setTargetRef(nextNode.getValue());
							addNodeToNewProcessList(new Node(sequenceFlow, node.getPool()));
						}
					}
				}
			}
		}
	}

	private void deleteAssociationsToFlowElement(String processName, TFlowElement flowElement) {

		List<AssociationPair<String, TAssociation>> associationPairsToDeleteList = new ArrayList<>();

		for (int a = 0; a < associationPairList.size(); a++) {
			TAssociation association = associationPairList.get(a).getAssociation();

			if (association.getSourceRef().getLocalPart().equals(flowElement.getId())
					|| association.getTargetRef().getLocalPart().equals(flowElement.getId())) {
				associationPairsToDeleteList.add(new AssociationPair<String, TAssociation>(processName, association));
			}
		}

		associationPairList.removeAll(associationPairsToDeleteList);

		for (int a = 0; a < associationPairsToDeleteList.size(); a++) {
			removeAssociationFromProcess(processName, associationPairsToDeleteList.get(a).getAssociation());
		}
	}

	private boolean treatReceiveTaskAssociationsCase(Node receiveNode, Node gatewayNode, String newGatewayPool){
		boolean result = false;

		TReceiveTask receiveTask = (TReceiveTask) receiveNode.getValue();

		for (int o = 0; o < receiveTask.getDataOutputAssociation().size(); o++) {
			if(receiveTask.getDataOutputAssociation().get(o).getTargetRef().getClass().getName().equals("bpmn2.TDataObject")){
				TDataObject dataObject = (TDataObject) receiveTask.getDataOutputAssociation().get(o).getTargetRef();
				
				deleteAssociationsToFlowElement(receiveNode.getPool(), dataObject);
				removeFlowElementFromProcess(receiveNode.getPool(), dataObject);
				
				TAssociation newAssociation = createAssociation(dataObject.getId(), gatewayNode.getValue().getId());
				associationPairList.add(new AssociationPair<String, TAssociation>(newGatewayPool, newAssociation));
				
				addAssociationToProcess(newGatewayPool, newAssociation);
				addNodeToNewProcessList(new Node(dataObject, newGatewayPool));
				
				result = true;
			}
		}

		return result;
	}
	
	private boolean treatSendTaskAssociationsCase(Node sendNode, Node gatewayNode) {

		boolean result = false;

		for (int a = 0; a < associationPairList.size(); a++) {
			TAssociation association = associationPairList.get(a).getAssociation();

			if (association.getTargetRef().getLocalPart().equals(sendNode.getValue().getId())) {
				association.setTargetRef(new QName("", gatewayNode.getValue().getId()));
				result = true;
			}
		}

		TActivity activity = (TActivity) sendNode.getValue();

		for (int i = 0; i < activity.getDataInputAssociation().size(); i++) {
			if (activity.getDataInputAssociation().get(i).getSourceRef().get(0).getValue().getClass().getName()
					.equals("bpmn2.TDataObject")) {
				TDataObject dataObject = (TDataObject) activity.getDataInputAssociation().get(i).getSourceRef().get(0)
						.getValue();
				TAssociation newAssociation = createAssociation(dataObject.getId(), gatewayNode.getValue().getId());
				associationPairList.add(new AssociationPair<String, TAssociation>(sendNode.getPool(), newAssociation));
				addAssociationToProcess(sendNode.getPool(), newAssociation);
				result = true;
			}
		}

		activity.getDataInputAssociation().clear();
		activity.setIoSpecification(null);

		return result;
	}

	private boolean checkIfTaskPrecedesGateway(TActivity activity, Node gatewayNode){
		
		Node previous = null;
		
		for(Node previousNode : gatewayNode.getPrevious()){
			if(previousNode.getPool().equals(gatewayNode.getPool())){
				
				previous = previousNode;
				
				while(!previous.getValue().getClass().getName().equals("bpmn2.TStartEvent")){
					if(previous.getValue() == activity){
						return true;
					}
					
					for(Node prev : previous.getPrevious()){
						if(prev.getPool().equals(gatewayNode.getPool())){
							previous = prev;
						}
					}
				}
				
			}
		}
		
		return false;
	}
	
	private boolean checkUnmovableDataGatewayCondition(Node gatewayNode){
		
		for (int a = 0; a < associationPairList.size(); a++) {
			TAssociation association = associationPairList.get(a).getAssociation();

			if (association.getTargetRef().getLocalPart().equals(gatewayNode.getValue().getId())) {
				TFlowElement data = FlowElementUtil.getFlowElementFromId(newProcessList, association.getSourceRef().getLocalPart());
				
				if(data instanceof TDataObject){
					TDataObject dataObject = (TDataObject) data;
					
					for(TProcess process : newProcessList){
						if(process.getName().equals(gatewayNode.getPool())){
							for(int i = 0; i < process.getFlowElement().size(); i++){
								TFlowElement flowElement = process.getFlowElement().get(i).getValue();
								
								if(FlowElementUtil.isTask(flowElement)){
									TActivity activity = (TActivity) flowElement;
									
									for(int o = 0; o < activity.getDataOutputAssociation().size(); o++){
										if(activity.getDataOutputAssociation().get(o).getTargetRef() == dataObject && checkIfTaskPrecedesGateway(activity, gatewayNode)){
												return true;
											}
										}
									}
								}
							}
						}
					}
				}
			}

		return false;
	}
	
	private boolean treatDataElementsGateway(Node sendNode, Node receiveNode, Node gatewayNode) {

		if(checkUnmovableDataGatewayCondition(gatewayNode)){
			return false;
		}
		
		if (treatSendTaskAssociationsCase(sendNode, gatewayNode)) {

			TReceiveTask receiveTask = (TReceiveTask) receiveNode.getValue();

			for (int i = 0; i < receiveTask.getDataInputAssociation().size(); i++) {
				deleteAssociationsToFlowElement(receiveNode.getPool(), (TFlowElement) receiveTask
						.getDataInputAssociation().get(i).getSourceRef().get(0).getValue());
				removeFlowElementFromProcess(receiveNode.getPool(), (TFlowElement) receiveTask
						.getDataInputAssociation().get(i).getSourceRef().get(0).getValue());
			}

			for (int i = 0; i < receiveTask.getDataOutputAssociation().size(); i++) {
				deleteAssociationsToFlowElement(receiveNode.getPool(), (TFlowElement) receiveTask
						.getDataOutputAssociation().get(i).getTargetRef());
				removeFlowElementFromProcess(receiveNode.getPool(), (TFlowElement) receiveTask
						.getDataOutputAssociation().get(i).getTargetRef());
			}

			deleteDataAssociationsFromActivity((TActivity) receiveTask);
		} else if(treatReceiveTaskAssociationsCase(receiveNode, gatewayNode, sendNode.getPool())){
			
			deleteDataAssociationsFromActivity((TActivity) receiveNode.getValue());
			deleteAssociationsToFlowElement(receiveNode.getPool(), receiveNode.getValue());
	
		}

		
		return true;
	}

	private void stepByStep() {

		for (Path path : candidatePaths) {

			for (int i = 0; i < path.getNodes().size(); i++) {
				boolean caseFound = false;
				Node node = path.getNodes().get(i);

				// Check for case2
				if (FlowElementUtil.isTimerEvent(node)) {
					Node timerNext = node.getNext().iterator().next();

					if (timerNext.getValue().getClass().getName().equals("bpmn2.TSendTask")) {
						Node timerNextNext = null;
						Node startMessageEvent = null;

						for (Node next : timerNext.getNext()) {
							if (next.getPool().equals(timerNext.getPool())) {
								timerNextNext = next;
							} else {
								startMessageEvent = next;
							}
						}
						// case2
						if (timerNextNext.getValue().getClass().getName().equals("bpmn2.TReceiveTask")) {
							case2(node, startMessageEvent, timerNextNext);
							blackList.add(timerNext);
							blackList.add(startMessageEvent);
							caseFound = true;
						}
					} else {
						addNodeToNewProcessList(node);
					}
				} else {

					Node previous = null;

					if (!node.getPrevious().isEmpty()) {
						previous = node.getPrevious().iterator().next();
					}

					// Check for case1
					if (previous != null && previous.getValue().getClass().getName().equals("bpmn2.TReceiveTask")
							&& node.getValue().getClass().getName().equals("bpmn2.TSendTask")) {

						Node previousReceive = null;
						for (Node prevPrevious : previous.getPrevious()) {
							if (!prevPrevious.getPool().equals(previous.getPool())) {
								previousReceive = prevPrevious;
							}
						}
						Node nextSend = null;
						for (Node next : node.getNext()) {
							if (!next.getPool().equals(node.getPool())) {
								nextSend = next;
							}
						}

						if (previousReceive.getPool().equals(nextSend.getPool())) {
							blackList.add(node);

							for (Node next : node.getNext()) {
								if (!node.getPool().equals(next.getPool())) {
									blackList.add(next);

									for (Node nn : next.getNext()) {
										if (nn.getPool().equals(next.getPool())) {
											if (!treatDataElements(previousReceive, nn)) {
												treatDataElements(node, nn);
											}
											break;
										}
									}

								}
							}

							deleteDataAssociationsFromActivity((TActivity) previousReceive.getValue());
							deleteDataAssociationsFromActivity((TActivity) previous.getValue());
							deleteDataAssociationsFromActivity((TActivity) node.getValue());
							deleteAssociationsAndDataObjectsFromActivity((TActivity) previous.getValue());
							deleteAssociationsAndDataObjectsFromActivity((TActivity) node.getValue());

							case1(previous, node, previousReceive, nextSend);

							caseFound = true;
						}
					}

					// Check for case3
					if (!blackList.contains(node) && node.getValue().getClass().getName().equals("bpmn2.TSendTask")) {

						Node nextNodeSamePool = null;
						Node nextNodeOtherPool = null;

						for (Node nextNode : node.getNext()) {
							if (nextNode.getPool().equals(node.getPool())) {
								nextNodeSamePool = nextNode;
							} else {
								nextNodeOtherPool = nextNode;
							}
						}

						for (Node gateway : nextNodeOtherPool.getNext()) {
							if (gateway.getPool().equals(nextNodeOtherPool.getPool())
									&& FlowElementUtil.isGateway(gateway.getValue())) {

								if (nextNodeOtherPool.getValue().getClass().getName().equals("bpmn2.TReceiveTask")) {
									if (treatDataElementsGateway(node, nextNodeOtherPool, gateway)) {
										blackList.add(node);
										blackList.add(nextNodeOtherPool);
										blackList.add(gateway);
										case3(previous, nextNodeSamePool, nextNodeOtherPool, gateway);
										caseFound = true;
									}
								}
							}
						}

						if (!blackList.contains(node) && node.getValue().getClass().getName().equals("bpmn2.TSendTask")) {
							for (Node nextNode : node.getNext()) {
								if (nextNode.getPool().equals(node.getPool())) {
									if (!nextNode.getValue().getClass().getName().equals("bpmn2.TEndEvent")) {
										TSequenceFlow sequenceFlow = createSequenceFlow();
										sequenceFlow.setSourceRef(node.getValue());
										sequenceFlow.setTargetRef(nextNode.getValue());
										addNodeToNewProcessList(new Node(sequenceFlow, node.getPool()));
									}
								} else {
									newCollaboration.getMessageFlow().add(
											createMessageFlow(node.getValue().getId(), nextNode.getValue().getId()));
								}
							}
						}

						/*
						 * Create a sequence flow for every incoming connection
						 * and a sequence flow if the outgoing connection is a
						 * task
						 */
						if (!blackList.contains(node)
								&& node.getValue().getClass().getName().equals("bpmn2.TReceiveTask")) {
							for (Node prevNode : node.getPrevious()) {
								if (prevNode.getPool().equals(node.getPool())) {
									if (targetRefNotFound(node.getValue())) {
										TSequenceFlow sequenceFlow = createSequenceFlow();
										sequenceFlow.setSourceRef(prevNode.getValue());
										sequenceFlow.setTargetRef(node.getValue());
										addNodeToNewProcessList(new Node(sequenceFlow, node.getPool()));
									}
								}
							}
							previous = null;

							for (Node nextNode : node.getNext()) {
								if (nextNode.getPool().equals(node.getPool())) {
									if (FlowElementUtil.isTask(nextNode.getValue())) {
										TSequenceFlow sequenceFlow = createSequenceFlow();
										sequenceFlow.setSourceRef(node.getValue());
										sequenceFlow.setTargetRef(nextNode.getValue());
										addNodeToNewProcessList(new Node(sequenceFlow, node.getPool()));
									}
								}
							}
						}
					}

					if (!blackList.contains(node)) {

						addNodeToNewProcessList(node);

						/*
						 * Creates a sequence flow between the current node and
						 * the previous node
						 */
						if (previous != null && !blackList.contains(previous)) {
							if (previous.getPool().equals(node.getPool())) {
								if (targetRefNotFound(node.getValue())) {
									TSequenceFlow sequenceFlow = createSequenceFlow();
									sequenceFlow.setSourceRef(previous.getValue());
									sequenceFlow.setTargetRef(node.getValue());
									addNodeToNewProcessList(new Node(sequenceFlow, node.getPool()));
								}
							}
						}

						// Creates all sequence flows for a given gateway node
						if (FlowElementUtil.isGateway(node.getValue())) {
							for (Node nextNode : node.getNext()) {
								TSequenceFlow sequenceFlow = createSequenceFlow();
								sequenceFlow.setSourceRef(node.getValue());
								sequenceFlow.setTargetRef(nextNode.getValue());
								addNodeToNewProcessList(new Node(sequenceFlow, node.getPool()));
							}
						}
					}
				}
				if (caseFound) {
					endStep();
					return;
				}
			}
		}
	}

	private void removeAssociationFromProcess(String processName, TAssociation association) {
		for (TProcess process : newProcessList) {
			if (process.getName().equals(processName)) {
				for (int i = 0; i < process.getArtifact().size(); i++) {
					if (process.getArtifact().get(i).getValue() == association) {
						process.getArtifact().remove(i);
						return;
					}
				}
			}
		}
	}

	private void addAssociationToProcess(String processName, TAssociation association) {
		for (TProcess process : newProcessList) {
			if (process.getName().equals(processName)) {
				process.getArtifact().add(
						new JAXBElement<TAssociation>(new QName(namespaceURIModel, "association"), TAssociation.class,
								association));
				return;
			}
		}
	}

	private void removeFlowElementFromProcess(String processName, TFlowElement flowElement) {
		for (TProcess process : newProcessList) {
			if (process.getName().equals(processName)) {
				for (int i = 0; i < process.getFlowElement().size(); i++) {
					if (process.getFlowElement().get(i).getValue() == flowElement) {
						System.out.println("Deleted " + process.getFlowElement().get(i).getValue().getId()
								+ " from new process list.");
						process.getFlowElement().remove(i);
						return;
					}
				}
			}
		}
	}

	/*
	 * Moves data objects and associations to the pool of the element that is
	 * attached to its association
	 */
	private boolean treatDataElements(Node originalElement, Node elementToMoveData) {

		boolean result = false;
		boolean elementsFromDifferentPools = !originalElement.getPool().equals(elementToMoveData.getPool());
		/*
		 * Deals with associations
		 */

		List<AssociationPair<String, TAssociation>> modifiedAssociationPairList = new ArrayList<>();

		for (int a = 0; a < associationPairList.size(); a++) {

			TAssociation association = associationPairList.get(a).getAssociation();

			boolean associationTreated = false;

			if (association.getSourceRef().getLocalPart().equals(originalElement.getValue().getId())) {

				association.setSourceRef(new QName("", elementToMoveData.getValue().getId()));
				result = true;

				if (elementsFromDifferentPools) {
					for (int i = 0; i < newProcessList.size() && !associationTreated; i++) {

						TProcess process = newProcessList.get(i);

						for (int j = 0; j < process.getFlowElement().size() && !associationTreated; j++) {
							// Data Object found

							TFlowElement flowElement = process.getFlowElement().get(j).getValue();

							if (flowElement.getId().equals(association.getTargetRef().getLocalPart())) {

								// removes the data object from the old process
								process.getFlowElement().remove(j);
								// adds the data process to the new process
								addNodeToNewProcessList(new Node(flowElement, elementToMoveData.getPool()));
								// removes the association from the old process
								removeAssociationFromProcess(originalElement.getPool(), association);
								// adds the association to the new process
								addAssociationToProcess(elementToMoveData.getPool(), association);

								modifiedAssociationPairList.add(new AssociationPair<String, TAssociation>(
										elementToMoveData.getPool(), association));

								associationTreated = true;
							}
						}
					}
				}
				System.out.println("DATA OBJECT target = " + association.getTargetRef().getLocalPart());

			} else if (association.getTargetRef().getLocalPart().equals(originalElement.getValue().getId())) {

				association.setTargetRef(new QName("", elementToMoveData.getValue().getId()));
				result = true;
				if (elementsFromDifferentPools) {
					for (int i = 0; i < newProcessList.size() && !associationTreated; i++) {

						TProcess process = newProcessList.get(i);

						for (int j = 0; j < process.getFlowElement().size() && !associationTreated; j++) {
							// Data Object found

							TFlowElement flowElement = process.getFlowElement().get(j).getValue();

							if (flowElement.getId().equals(association.getSourceRef().getLocalPart())) {

								// removes the data object from the old process
								process.getFlowElement().remove(j);
								// adds the data process to the new process
								addNodeToNewProcessList(new Node(flowElement, elementToMoveData.getPool()));
								// removes the association from the old process
								removeAssociationFromProcess(originalElement.getPool(), association);
								// adds the association to the new process
								addAssociationToProcess(elementToMoveData.getPool(), association);

								modifiedAssociationPairList.add(new AssociationPair<String, TAssociation>(
										elementToMoveData.getPool(), association));

								associationTreated = true;
							}
						}
					}
				}
				System.out.println("DATA OBJECT source = " + association.getSourceRef().getLocalPart());
			}

		}

		/*
		 * Updates the associationMap with the new keys
		 */

		for (int i = 0; i < modifiedAssociationPairList.size(); i++) {
			for (int a = 0; a < associationPairList.size(); a++) {
				if (associationPairList.get(a).getAssociation() == modifiedAssociationPairList.get(i).getAssociation()) {
					associationPairList.remove(a);
					break;
				}

			}
		}

		associationPairList.addAll(modifiedAssociationPairList);

		/*
		 * Deals with data associations
		 */

		System.out.println("ORIGINAL ELEMENT = " + originalElement.getValue().getId());

		if (FlowElementUtil.isDataTask(originalElement.getValue())) {

			TActivity originalActivity = (TActivity) originalElement.getValue();
			System.out.println("ORIGINAL ACTIVITY = " + originalActivity.getId());

			if (!originalActivity.getDataInputAssociation().isEmpty()
					&& FlowElementUtil.isDataTask(elementToMoveData.getValue())) {

				List<List<JAXBElement<Object>>> dataToDelete = new ArrayList<>();
				TActivity elementToMoveActivity = (TActivity) elementToMoveData.getValue();
				System.out.println("ELEMENT TO MOVE ACTIVITY = " + elementToMoveActivity.getId());

				for (TDataInputAssociation dataInputAssociation : originalActivity.getDataInputAssociation()) {
					elementToMoveActivity.getDataInputAssociation().add(dataInputAssociation);
					dataToDelete.add(dataInputAssociation.getSourceRef());
				}
				elementToMoveActivity.setIoSpecification(originalActivity.getIoSpecification());
				result = true;
				/*
				 * Deletes the data from the old process if the activities
				 * belong to different pools and moves it to the new process
				 */

				if (elementsFromDifferentPools) {
					for (List<JAXBElement<Object>> deleteList : dataToDelete) {
						for (JAXBElement<Object> delete : deleteList) {
							removeFlowElementFromProcess(originalElement.getPool(), (TFlowElement) delete.getValue());
							addNodeToNewProcessList(new Node((TFlowElement) delete.getValue(),
									elementToMoveData.getPool()));
						}
					}
				}
			}

			System.out.println();
		}
		return result;
	}

	private void deleteAssociationsAndDataObjectsFromActivity(TActivity activity) {

		for (int a = 0; a < associationPairList.size(); a++) {

			TAssociation association = associationPairList.get(a).getAssociation();
			if (association.getSourceRef().getLocalPart().equals(activity.getId())) {
				for (TProcess process : newProcessList) {

					for (int j = 0; j < process.getFlowElement().size(); j++) {
						// Data Object found

						TFlowElement flowElement = process.getFlowElement().get(j).getValue();

						if (flowElement.getId().equals(association.getTargetRef().getLocalPart())) {

							// removes the data object from the old process
							process.getFlowElement().remove(j);
							// removes the association from the old process
							removeAssociationFromProcess(process.getName(), association);
							associationPairList.remove(a);
							return;
						}
					}
				}
			} else if (association.getTargetRef().getLocalPart().equals(activity.getId())) {
				for (TProcess process : newProcessList) {

					for (int j = 0; j < process.getFlowElement().size(); j++) {
						// Data Object found

						TFlowElement flowElement = process.getFlowElement().get(j).getValue();

						if (flowElement.getId().equals(association.getSourceRef().getLocalPart())) {

							// removes the data object from the old process
							process.getFlowElement().remove(j);
							// removes the association from the old process
							removeAssociationFromProcess(process.getName(), association);
							associationPairList.remove(a);
							return;
						}
					}
				}

			}
		}
	}

	private void deleteDataAssociationsFromActivity(TActivity activity) {
		activity.getDataInputAssociation().clear();
		activity.getDataOutputAssociation().clear();
		activity.setIoSpecification(null);
	}

	private void addFlowElementToLane(String flowElementId, String laneId) {
		for (TProcess process : newProcessList) {
			if (!process.getLaneSet().isEmpty()) {
				for (TLaneSet laneSet : process.getLaneSet()) {
					for (TLane lane : laneSet.getLane()) {
						if (lane.getId().equals(laneId)) {
							lane.getFlowNodeRef().add(
									new JAXBElement<Object>(new QName(namespaceURIModel, "flowNodeRef"), Object.class,
											flowElementId));
							return;
						}
					}
				}
			}
		}
	}

	private void addFlowElementsToLanes() {
		for (TProcess newProcess : newProcessList) {
			if (!newProcess.getLaneSet().isEmpty()) {
				for (TProcess process : processList) {
					if (newProcess.getName().equals(process.getName())) {
						for (TLaneSet laneSet : process.getLaneSet()) {
							for (TLane lane : laneSet.getLane()) {
								for (int i = 0; i < lane.getFlowNodeRef().size(); i++) {
									for (int n = 0; n < newProcess.getFlowElement().size(); n++) {

										TFlowElement flowElement = newProcess.getFlowElement().get(n).getValue();

										if (flowElement == lane.getFlowNodeRef().get(i).getValue()) {

											System.out.println("Flow Element " + flowElement.getId() + " added to "
													+ lane.getId());

											addFlowElementToLane(flowElement.getId(), lane.getId());
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private TSequenceFlow getSequenceFlowFromSource(TProcess currentProcess, String sourceId) {
		for (TProcess process : newProcessList) {
			if (currentProcess.getName().equals(process.getName())) {
				for (int i = 0; i < process.getFlowElement().size(); i++) {
					if (process.getFlowElement().get(i).getValue().getClass().getName().equals("bpmn2.TSequenceFlow")) {
						TSequenceFlow sequenceFlow = (TSequenceFlow) process.getFlowElement().get(i).getValue();
						TFlowElement source = (TFlowElement) sequenceFlow.getSourceRef();

						if (source.getId().equals(sourceId)) {
							return sequenceFlow;
						}

					}
				}
			}
		}
		return null;
	}

	private TSequenceFlow getSequenceFlowFromTarget(TProcess currentProcess, String targetId) {
		for (TProcess process : newProcessList) {
			if (currentProcess.getName().equals(process.getName())) {
				for (int i = 0; i < process.getFlowElement().size(); i++) {
					if (process.getFlowElement().get(i).getValue().getClass().getName().equals("bpmn2.TSequenceFlow")) {
						TSequenceFlow sequenceFlow = (TSequenceFlow) process.getFlowElement().get(i).getValue();
						TFlowElement target = (TFlowElement) sequenceFlow.getTargetRef();

						if (target.getId().equals(targetId)) {
							return sequenceFlow;
						}

					}
				}
			}
		}
		return null;
	}

	/*
	 * Finds the corresponding lane for a generated or modified flow element
	 */
	private void addGeneratedOrModifiedFlowElementsToLanes() {

		for (TProcess process : newProcessList) {
			if (!process.getLaneSet().isEmpty()) {
				for (int i = 0; i < process.getFlowElement().size(); i++) {
					TFlowElement flowElement = process.getFlowElement().get(i).getValue();

					if (LaneUtil.getFlowElementLaneId(process, flowElement.getId()) == null) {

						switch (flowElement.getClass().getName()) {

						case "bpmn2.TStartEvent":
							TStartEvent startEvent = (TStartEvent) flowElement;
							TSequenceFlow startEventSequenceFlow = getSequenceFlowFromSource(process,
									startEvent.getId());
							TFlowElement target = (TFlowElement) startEventSequenceFlow.getTargetRef();
							// maybe not necessary oldProcess
							addFlowElementToLane(startEvent.getId(),
									LaneUtil.getFlowElementLaneId(process, target.getId()));
							break;
						case "bpmn2.TSequenceFlow":
							break;
						default:
							if (!FlowElementUtil.isDataType(flowElement)) {
								TSequenceFlow sf = getSequenceFlowFromTarget(process, flowElement.getId());
								TFlowElement source = (TFlowElement) sf.getSourceRef();
								// maybe not necessary

								String flowElementLane = LaneUtil.getFlowElementLaneId(process, source.getId());
								while (flowElementLane == null) {
									sf = getSequenceFlowFromTarget(process, source.getId());
									source = (TFlowElement) sf.getSourceRef();
									flowElementLane = LaneUtil.getFlowElementLaneId(process, source.getId());
								}
								addFlowElementToLane(flowElement.getId(), flowElementLane);
							}
							break;
						}
					}
				}
			}
		}
	}

	protected void transform() {
		initializeNewCollaboration();
		initializeNewProcessesList();
		addUnmodifiedActivitiesAndSequenceFlowsToNewProcessList();
		stepByStep();
		garbageCollector();
		createMissingElements();
		addFlowElementsToLanes();
		addGeneratedOrModifiedFlowElementsToLanes();
		deletePreviousInOutFlowNodes();
		addNewInOutFlowNodes();
	}

	protected TCollaboration getNewCollaboration() {
		return newCollaboration;
	}

	protected List<TProcess> getNewProcessList() {
		return newProcessList;
	}

	protected List<AssociationPair<String, TAssociation>> getAssociationPairList() {
		return associationPairList;
	}
}