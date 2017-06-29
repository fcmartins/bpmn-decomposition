package decompose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import bpmn2.BPMNDiagram;
import bpmn2.BPMNPlane;
import bpmn2.DiagramElement;
import bpmn2.TAssociation;
import bpmn2.TCollaboration;
import bpmn2.TDataStore;
import bpmn2.TDefinitions;
import bpmn2.TFlowElement;
import bpmn2.TIntermediateThrowEvent;
import bpmn2.TMessageFlow;
import bpmn2.TProcess;
import bpmn2.TSequenceFlow;

public class Decompose {

	private TDefinitions definitions;
	private TCollaboration collaboration;
	private List<TProcess> processList;
	private Graph graph;
	private Set<Path> allPaths;
	private Set<Path> sensorPaths;
	private Set<Path> candidatePaths;
	private TCollaboration newCollaboration;
	private List<TProcess> newProcessList;
	private List<TDataStore> dataStoreList;

	private List<AssociationPair<String,TAssociation>> associationPairList;
	private Wsn wsn;
	private static final String namespaceURIModel = "http://www.omg.org/spec/BPMN/20100524/MODEL";

	public Decompose(TDefinitions definitions) {
		this.definitions = definitions;
		collaboration = new TCollaboration();
		processList = new ArrayList<>();
		graph = new Graph();
		allPaths = new HashSet<>();
		sensorPaths = new HashSet<>();
		candidatePaths = new HashSet<>();
		newCollaboration = new TCollaboration();
		newProcessList = new ArrayList<>();
		dataStoreList = new ArrayList<>();
		associationPairList = new ArrayList<>();
		wsn = new Wsn();
	}

	/*
	 * Extracts all information about the collaboration diagram and processes
	 */
	private void initializeGenerateGraphModel() {
		for (int i = 0; i < definitions.getRootElement().size(); i++) {
			switch (definitions.getRootElement().get(i).getValue().getClass().getName()) {
			case "bpmn2.TProcess":
				
				TProcess process = (TProcess) definitions.getRootElement().get(i).getValue();
				
				processList.add(process);
				
				for(int a = 0; a < process.getArtifact().size(); a++){
					
					if(process.getArtifact().get(a).getValue() instanceof TAssociation){
						associationPairList.add(new AssociationPair<String,TAssociation>(process.getName(),(TAssociation) process.getArtifact().get(a).getValue()));
					}
				}
				
				break;
			case "bpmn2.TCollaboration":
				collaboration = (TCollaboration) definitions.getRootElement().get(i).getValue();
				break;
			case "bpmn2.TDataStore":
				dataStoreList.add((TDataStore) definitions.getRootElement().get(i).getValue());
				break;
			default:
				break;
			}
		}
	}
	
	private void addNodesToGraph() {
		for (TProcess process : processList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				TFlowElement flowElement = process.getFlowElement().get(i).getValue();

				switch (flowElement.getClass().getName()) {
				case "bpmn2.TStartEvent":
					graph.addInitialNode(flowElement, process.getName());
					break;
				case "bpmn2.TEndEvent":
					graph.addFinalNode(flowElement, process.getName());
					break;
				case "bpmn2.TSequenceFlow":
					break;
				default:
					graph.addNode(flowElement, process.getName());
					break;
				}
			}
		}
	}

	private void addSequenceFlowsToGraph() {
		for (TProcess process : processList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				TFlowElement flowElement = process.getFlowElement().get(i).getValue();

				if (flowElement instanceof TSequenceFlow) {
					TSequenceFlow sequenceFlow = (TSequenceFlow) flowElement;
					graph.addSequenceFlowEdge((TFlowElement) sequenceFlow.getSourceRef(),
							(TFlowElement) sequenceFlow.getTargetRef());
				}
			}
		}
	}

	private void addMessageFlowsToGraph() {
		TFlowElement from = null;
		TFlowElement to = null;
		for (TMessageFlow messageFlow : collaboration.getMessageFlow()) {
			for (Node node : graph.getNodes()) {
				if (node.getValue().getId().equals(messageFlow.getSourceRef().getLocalPart())) {
					from = node.getValue();
				} else if (node.getValue().getId().equals(messageFlow.getTargetRef().getLocalPart())) {
					to = node.getValue();
				}
				if (from != null && to != null) {
					graph.addMessageFlowEdge(from, to);
					from = null;
					to = null;
					break;
				}
			}
		}
	}

	private TFlowElement getFlowElementFromObject(Object object) {
		for (TProcess process : processList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				TFlowElement flowElement = process.getFlowElement().get(i).getValue();
				if (flowElement == object) {
					return flowElement;
				}
			}
		}
		return null;
	}

	private TFlowElement getFlowElementFromMessageFlow(String toFind) {
		for (TProcess process : processList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				TFlowElement flowElement = process.getFlowElement().get(i).getValue();
				if (flowElement.getId().equals(toFind)) {
					return flowElement;
				}
			}
		}
		return null;
	}

	/*
	 * For all paths that occur some sort of communication adds, for each node,
	 * the previous node and next node
	 */
	private void setNextPreviousToGraphNodes() {

		for (TProcess process : processList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				if (process.getFlowElement().get(i).getValue() instanceof TSequenceFlow) {
					TSequenceFlow sequenceFlow = (TSequenceFlow) process.getFlowElement().get(i).getValue();

					Node source = graph.getNode(getFlowElementFromObject(sequenceFlow.getSourceRef()));
					Node target = graph.getNode(getFlowElementFromObject(sequenceFlow.getTargetRef()));

					source.addNext(target);
					target.addPrevious(source);
				}
			}
		}

		for (TMessageFlow messageFlow : collaboration.getMessageFlow()) {
			Node source = graph.getNode(getFlowElementFromMessageFlow(messageFlow.getSourceRef().toString()));
			Node target = graph.getNode(getFlowElementFromMessageFlow(messageFlow.getTargetRef().toString()));

			source.addNext(target);
			target.addPrevious(source);
		}
	}

	public void generateGraphModel() {
		initializeGenerateGraphModel();
		addNodesToGraph();
		addSequenceFlowsToGraph();
		addMessageFlowsToGraph();
		setNextPreviousToGraphNodes();
	}

	

	private boolean isTaskToSensorPool(Node node) {

		if (node.getPool().equals(wsn.processName)) {
			return true;
		}

		if (node.getValue() instanceof TIntermediateThrowEvent
				|| node.getValue() instanceof bpmn2.TIntermediateCatchEvent) {
			return true;
		}

		if (FlowElementUtil.isTimerEvent(node)) {
			return true;
		}

		if (FlowElementUtil.isMessageEventDefinition(node)) {
			return true;
		}

		for (TFlowElement sensorType : wsn.types) {
			if (sensorType.getClass() == node.getValue().getClass()) {
				return true;
			}
		}

		return false;
	}

	private Set<Path> getSensorTaskPaths(Set<Path> paths) {
		Set<Path> sensorPaths = new HashSet<>();
		for (Path p : paths) {
			Deque<Node> pathToAdd = new LinkedList<>();

			for (Node node : p) {
				if (isTaskToSensorPool(node)) {
					pathToAdd.add(node);
				}
			}

			if (!pathToAdd.isEmpty()) {
				sensorPaths.add(new Path(pathToAdd));
			}
		}
		return sensorPaths;
	}

	private Collection<Path> getAllSubpaths(Set<Path> sensorPaths) {
		Collection<Path> subpaths = new HashSet<>();

		for (Path p : sensorPaths) {
			for (Path pp : sensorPaths) {
				if (p != pp && p.isSubpath(pp)) {
					subpaths.add(p);
				}
			}
		}
		return subpaths;
	}

	private Set<Path> removeSensorTaskSubPaths(Set<Path> sensorPaths) {
		Collection<Path> subpaths = getAllSubpaths(sensorPaths);
		sensorPaths.removeAll(subpaths);
		return sensorPaths;
	}

	private boolean isContainedPath(Path p, Path pp) {
		List<Node> n = p.getNodes();
		List<Node> nn = pp.getNodes();

		if (nn.size() > n.size()) {
			for (Node node : n) {
				for (Node otherNode : nn) {
					if (node != otherNode && nn.contains(node)) {
						return true;
					} else if (node != otherNode && !n.contains(otherNode)) {
						return false;
					}
				}
			}
		}
		return false;
	}

	private Set<Path> discardUnnecessaryPaths(Set<Path> noSubpaths) {

		Collection<Path> unnecessaryPaths = new HashSet<>();

		for (Path p : noSubpaths) {
			/*
			 * if (isCommunicationPathOnly(p)) { unnecessaryPaths.add(p); break;
			 * }
			 */

			for (Path pp : noSubpaths) {
				if (p != pp && isContainedPath(p, pp)) {
						unnecessaryPaths.add(p);
						break;
					}
			}
		}

		if (!unnecessaryPaths.isEmpty()) {
			noSubpaths.removeAll(unnecessaryPaths);
		}

		return noSubpaths;
	}

	public void candidatePaths() {

		Set<Path> noSubpaths;

		/*
		 * Get all the possible paths
		 */

		allPaths = new MessageFlowPaths(graph).getTestRequirements();

		System.out.println("##### ALL GRAPH PATHS WITH COMMUNICATION #####");
		System.out.println(allPaths);

		/*
		 * Identify subpaths that contain comunication tasks, sensor tasks or
		 * wsn tasks
		 */

		sensorPaths = getSensorTaskPaths(allPaths);

		System.out.println("##### ALL SENSOR TASK CANDIDATES #####");
		System.out.println(sensorPaths);

		/*
		 * Remove subpaths
		 */

		noSubpaths = removeSensorTaskSubPaths(sensorPaths);

		System.out.println("##### SENSOR TASK CANDIDATES WITH NO SUBPATHS #####");
		System.out.println(noSubpaths);

		/*
		 * Remove unnecessary paths
		 */

		candidatePaths = discardUnnecessaryPaths(noSubpaths);

		System.out.println("##### CANDIDATE PATHS #####");
		System.out.println(candidatePaths);

	}

	

	private void printProcesses(List<TProcess> processList) {
		StringBuilder result = new StringBuilder();
		for (TProcess process : processList) {
			System.out.println("\nProcess name = " + process.getName() + "\n");
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				TFlowElement flowElement = process.getFlowElement().get(i).getValue();

				if (flowElement instanceof TSequenceFlow) {
					TSequenceFlow sequenceFlow = (TSequenceFlow) flowElement;
					result.append(FlowElementUtil.printFlowElementName(new ArrayList<Object>(Arrays.asList(sequenceFlow.getSourceRef(),
							sequenceFlow, sequenceFlow.getTargetRef()))));
				}
			}
			System.out.println(result);
			result = new StringBuilder();
		}
	}

	private void printCollaboration(TCollaboration collaboration, List<TProcess> processList) {
		StringBuilder result = new StringBuilder();
		System.out.println("\n##### Collaboration ######");
		Object source = null;
		Object target = null;
		for (TMessageFlow messageFlow : collaboration.getMessageFlow()) {
			for (TProcess process : processList) {
				for (int i = 0; i < process.getFlowElement().size(); i++) {
					TFlowElement flowElement = process.getFlowElement().get(i).getValue();

					if (flowElement.getId().equals(messageFlow.getSourceRef().getLocalPart())) {
						source = flowElement;
					} else if (flowElement.getId().equals(messageFlow.getTargetRef().getLocalPart())) {
						target = flowElement;
					}
					if (source != null && target != null) {
						result.append(FlowElementUtil.printFlowElementName(new ArrayList<Object>(Arrays.asList(source, messageFlow, target))));
						source = null;
						target = null;
						break;
					}
				}
			}
		}
		System.out.println(result);
	}

	public void redefinePools() {
		if (!candidatePaths.isEmpty()) {

			ProcessTransformationHandler pth = new ProcessTransformationHandler(collaboration, processList,
					candidatePaths, associationPairList);
			pth.transform();
			newCollaboration = pth.getNewCollaboration();
			newProcessList = pth.getNewProcessList();
			associationPairList = pth.getAssociationPairList();

			System.out.println("##### Processes #####");
			printProcesses(newProcessList);
			printCollaboration(newCollaboration, newProcessList);

		} else {
			System.out
					.println("There is no Transitive Path avaiable! The current BPMN definition doesn't need to be changed.");
		}
	}

	public TDefinitions drawCollaboration() {

		if (candidatePaths.isEmpty()) {
			return definitions;
		}

		TDefinitions newDefinition = new TDefinitions();
		newDefinition.setExporter(definitions.getExporter());
		newDefinition.setExporterVersion(definitions.getExporterVersion());
		newDefinition.setExpressionLanguage(definitions.getExpressionLanguage());
		newDefinition.setId(definitions.getId());
		newDefinition.setName(definitions.getName());
		newDefinition.setTargetNamespace(definitions.getTargetNamespace());
		newDefinition.setTypeLanguage(definitions.getTypeLanguage());

		/*
		 * Initializes the root element with new collaboration, the new
		 * processes and data stores if they exist
		 */
		newDefinition.getRootElement().add(
				new JAXBElement<TCollaboration>(new QName(namespaceURIModel, "collaboration"), TCollaboration.class,
						newCollaboration));

		for (TProcess process : newProcessList) {
			newDefinition.getRootElement().add(
					new JAXBElement<TProcess>(new QName(namespaceURIModel, "process"), TProcess.class, process));
		}

		for (TDataStore dataStore : dataStoreList) {
			newDefinition.getRootElement()
					.add(new JAXBElement<TDataStore>(new QName(namespaceURIModel, "dataStore"), TDataStore.class,
							dataStore));
		}

		// To be added to the new definition
		BPMNDiagram newBpmnDiagram = new BPMNDiagram();
		newBpmnDiagram.setDocumentation(definitions.getBPMNDiagram().get(0).getDocumentation());
		newBpmnDiagram.setId(definitions.getBPMNDiagram().get(0).getId());
		newBpmnDiagram.setName(definitions.getBPMNDiagram().get(0).getName());
		newBpmnDiagram.setResolution(definitions.getBPMNDiagram().get(0).getResolution());
		newBpmnDiagram.getBPMNLabelStyle().addAll(definitions.getBPMNDiagram().get(0).getBPMNLabelStyle());

		// To be added to the new bpmn diagram
		BPMNPlane newBpmnPlane = new BPMNPlane();
		newBpmnPlane.setBpmnElement(definitions.getBPMNDiagram().get(0).getBPMNPlane().getBpmnElement());
		newBpmnPlane.setExtension(definitions.getBPMNDiagram().get(0).getBPMNPlane().getExtension());
		newBpmnPlane.setId(definitions.getBPMNDiagram().get(0).getBPMNPlane().getId());

		/*
		 * To be added to the new bpmn plane
		 */
		DrawHandler dh = new DrawHandler(newCollaboration, newProcessList, associationPairList);
		List<JAXBElement<? extends DiagramElement>> newDiagramElement = dh.draw();

		// Builds the final definition
		newBpmnPlane.getDiagramElement().addAll(newDiagramElement);
		newBpmnDiagram.setBPMNPlane(newBpmnPlane);
		newDefinition.getBPMNDiagram().add(newBpmnDiagram);
		return newDefinition;
	}

}