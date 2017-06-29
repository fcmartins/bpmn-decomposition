package decompose;

import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import bpmn2.TExclusiveGateway;
import bpmn2.TFlowElement;
import bpmn2.TGateway;
import bpmn2.TInclusiveGateway;
import bpmn2.TReceiveTask;
import bpmn2.TSendTask;
import bpmn2.TSequenceFlow;

public class Graph {

	/**
	 * The set of nodes in the Graph.
	 */
	private Set<Node> nodes;

	/**
	 * A map of node edges in the Graph.
	 */
	private Map<Node, Set<Edge>> edges;

	/**
	 * The initial nodes of the Graph.
	 */
	private Set<Node> initialNodes;

	/**
	 * The final nodes of the Graph.
	 */
	private Set<Node> finalNodes;

	/**
	 * Creates a new Graph Object.
	 */
	public Graph() {
		nodes = new TreeSet<>();
		edges = new LinkedHashMap<>();
		initialNodes = new HashSet<>();
		finalNodes = new HashSet<>();
	}

	/**
	 * Creates a Node with the given value and adds it to the Graph.
	 * 
	 * @param flowElement
	 *            - The value of the Node.
	 * @return Node&lt;V&gt; - The new Node.
	 */
	public Node addNode(TFlowElement flowElement, String pool) {
		Node node = new Node(flowElement, pool);
		addNode(node);
		return node;
	}

	/**
	 * Adds a new Node to the Graph.
	 * 
	 * @param node
	 *            - The new Node.
	 */
	public void addNode(Node node) {
		if (!containsNode(node)) {
			nodes.add(node);
			edges.put(node, new LinkedHashSet<Edge>());
		}
	}

	/**
	 * Adds a Node to the Graph's initial nodes.
	 * 
	 * @param node
	 *            - The Node to add to the initial nodes.
	 */
	public void addInitialNode(Node node) {
		addNode(node);
		initialNodes.add(node);
	}

	/**
	 * Creates a Node with the given value and adds it to the Graph's initial
	 * nodes.
	 * 
	 * @param value
	 *            - The value of the Node.
	 * @return Node&lt;V&gt; - The new Node.
	 */
	public Node addInitialNode(TFlowElement value, String pool) {
		Node node = addNode(value, pool);
		addNode(node);
		initialNodes.add(node);
		return node;
	}

	/**
	 * Checks if the Node is an initial Graph Node.
	 * 
	 * @param node
	 *            - The Node to be verified.
	 * @return boolean - True if it is an initial Graph Node or False if not.
	 */
	public boolean isInitialNode(Node node) {
		return initialNodes.contains(node);
	}

	/**
	 * Adds a Node to the Graph final nodes.
	 * 
	 * @param node
	 *            - The Node to add to the Graph's final nodes.
	 */
	public void addFinalNode(Node node) {
		if (!containsNode(node))
			addNode(node);
		finalNodes.add(node);
	}

	/**
	 * Creates a Node with the given value and add it to the Graph's final
	 * nodes.
	 * 
	 * @param value
	 *            - The value of the Node.
	 * @return Node&lt;V&gt; - The new Node.
	 */
	public Node addFinalNode(TFlowElement flowElement, String pool) {
		Node node = getNode(flowElement);
		if (node == null)
			node = addNode(flowElement, pool);
		finalNodes.add(node);
		return node;
	}

	/**
	 * Checks if the Node is a final Graph Node.
	 * 
	 * @param node
	 *            - The Node to be verified.
	 * @return boolean - True if it is a final Graph Node or False if not.
	 */
	public boolean isFinalNode(Node node) {
		return finalNodes.contains(node);
	}

	/**
	 * Gets a Graph Node.
	 * 
	 * @param value
	 *            - The value of the Node.
	 * @return Node&lt;V&gt; - The Node with the given value.
	 */
	public Node getNode(TFlowElement flowElement) {
		for (Node node : nodes) {
			if (node.getValue() == flowElement)
				return node;
		}
		return null;
	}

	/**
	 * Adds a new Edge to the Graph.
	 * 
	 * @param edge
	 *            - The Edge to add.
	 */
	public void addEdge(Edge edge) {
		getNodeBeginEdges(edge.getBeginNode()).add(edge);
	}

	/**
	 * Creates a new Edge with the given nodes and adds it to the Graph.
	 * 
	 * @param from
	 *            - The Edge's begin Node.
	 * @param to
	 *            - The Edge's end Node.
	 * @return Edge&lt;V&gt; - The new Edge.
	 */
	public Edge addEdge(Node from, Node to) {
		Edge edge = new Edge(from, to);
		addEdge(edge);
		return edge;
	}

	public Edge addEdge(TFlowElement from, TFlowElement to) {
		Node f = getNode(from);
		Node t = getNode(to);
		Edge edge = new Edge(f, t);
		addEdge(edge);
		return edge;
	}

	public SequenceFlowEdge addSequenceFlowEdge(TFlowElement from, TFlowElement to) {
		Node f = getNode(from);
		Node t = getNode(to);
		SequenceFlowEdge edge = new SequenceFlowEdge(f, t);
		addEdge(edge);
		return edge;
	}

	public MessageFlowEdge addMessageFlowEdge(TFlowElement from, TFlowElement to) {
		Node f = getNode(from);
		Node t = getNode(to);
		MessageFlowEdge edge = new MessageFlowEdge(f, t);
		addEdge(edge);
		return edge;
	}

	public boolean messageFlowContainsNode(Deque<Node> pathNodes) {

		// Discards subpaths
		for (Edge edge : getNodeEndEdges(pathNodes.getFirst()))
			if (edge instanceof MessageFlowEdge)
				return false;

		for (Node node : pathNodes) {
			for (Edge edge : getNodeBeginEdges(node)) {
				// Checks if there is a beginning and an end in a message flow
				// in a given path
				if (edge instanceof MessageFlowEdge && pathNodes.contains(edge.getEndNode())) {
					return true;
				}
			}
		}

		return false;
	}
	
	/*
	 * Checks if a given path contains two or more gateways
	 */
	public boolean containsGateway(Deque<Node> pathNodes) {
		for (Node node : pathNodes) {		
			if(node.getValue() instanceof TGateway ||
					node.getValue() instanceof TExclusiveGateway ||
					node.getValue() instanceof TInclusiveGateway){
				return true;
			}
				
		}

		return false;
	}

	/*
	 * Returns true if there arent any communication tasks in a given path
	 */
	public boolean noCommunicationNodes(Deque<Node> pathNodes) {
			
		for (Node node : pathNodes) {
			if(node.getValue() instanceof TSendTask ||
					node.getValue() instanceof TReceiveTask){
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the set of Edges for the given Node, where the given Node is the
	 * Edge begin Node.
	 * 
	 * @param node
	 *            - The Node to get the edges.
	 * @return Set&lt;Edge&lt;V&gt;&gt; - The set of edges for the Node.
	 */
	public Set<Edge> getNodeBeginEdges(Node node) {
		Set<Edge> nodeEdges = edges.get(node);
		if (nodeEdges == null) {
			addNode(node);
			nodeEdges = edges.get(node);
		}
		return nodeEdges;
	}

	/**
	 * Gets the set of edges for the Node, where the given Node is the Edge end
	 * Node.
	 * 
	 * @param node
	 *            - The Node to get the Edges.
	 * @return Set&lt;Edge&lt;V&gt;&gt; - The set of Edges for the node.
	 */
	public Set<Edge> getNodeEndEdges(Node node) {
		Set<Edge> nodeEndEdges = new HashSet<>();
		for (Set<Edge> edgesNode : edges.values())
			for (Edge edge : edgesNode)
				if (edge.getEndNode() == node)
					nodeEndEdges.add(edge);

		return nodeEndEdges;
	}

	/**
	 * Removes the given Edge.
	 * 
	 * @param edge
	 *            - The Edge to remove.
	 */
	public void removeEdge(Edge edge) {
		getNodeBeginEdges(edge.getBeginNode()).remove(edge);
	}

	/**
	 * Removes the given Node.
	 * 
	 * @param node
	 *            - The Node to remove.
	 */
	public void removeNode(Node node) {
		nodes.remove(node);
		initialNodes.remove(node);
		finalNodes.remove(node);
		edges.remove(node);
	}

	/**
	 * Gets the initial nodes of of the Graph.
	 * 
	 * @return Iterable&lt;Node&lt;V&gt&gt; - The Graph initial nodes.
	 */
	public Iterable<Node> getInitialNodes() {
		return initialNodes;
	}

	/**
	 * Gets the final nodes of of the Graph.
	 * 
	 * @return Iterable&lt;Node&lt;V&gt;&gt; - The Graph final nodes.
	 */
	public Iterable<Node> getFinalNodes() {
		return finalNodes;
	}

	/**
	 * Checks if the Graph contains the given Node.
	 * 
	 * @param node
	 *            - The Node to be verified.
	 * @return boolean - True if Graph contains the Node and False if not.
	 */
	public boolean containsNode(Node node) {
		return nodes.contains(node);
	}

	/**
	 * Checks if the Graph contains the given Edge.
	 * 
	 * @param edge
	 *            - The Edge to be verified.
	 * @return boolean - True if Graph contains the Edge and False if not.
	 */
	public boolean containsEdge(Edge edge) {
		return getNodeBeginEdges(edge.getBeginNode()).contains(edge);
	}

	/**
	 * The Graph nodes.
	 * 
	 * @return Iterable&lt;Node&lt;V&gt;&gt; - The Graph nodes.
	 */
	public Iterable<Node> getNodes() {
		return nodes;
	}

	/**
	 * Sort the Graph nodes.
	 */
	public void sortNodes() {
		Set<Node> sorted = new TreeSet<>();
		for (Node node : nodes)
			sorted.add(node);
		nodes.clear();
		nodes = sorted;
	}

	/**
	 * The size of the Graph.
	 * 
	 * @return int - The Graph's number of nodes.
	 */
	public int size() {
		return nodes.size();
	}

	/**
	 * Checks if the given Path is a Path of the Graph.
	 * 
	 * @param path
	 *            - The Path to be verified.
	 * @return boolean - True if the Path is a Path of Graph and False if not.
	 */
	public boolean isPath(Path path) {
		Iterator<Node> iterator = path.iterator();
		if (!iterator.hasNext())
			return true;
		Node previousNode = iterator.next();
		if (!containsNode(previousNode))
			return false;
		while (iterator.hasNext()) {
			Node currentNode = iterator.next();
			Iterator<Edge> edgeIterator = getNodeBeginEdges(previousNode).iterator();
			boolean found = false;
			while (edgeIterator.hasNext() && !found) {
				if (edgeIterator.next().getEndNode() == currentNode)
					found = true;
			}
			if (!found)
				return false;
			previousNode = currentNode;
		}
		return true;
	}

	/**
	 * Visits the Graph.
	 * 
	 * @param visitor
	 *            - The visitor to apply.
	 */
	public void accept(IGraphVisitor visitor) {
		visitor.visitGraph(this);
	}

	/**
	 * The Graph representation.
	 * 
	 * @return String - The Graph representation.
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("[");
		Iterator<Node> it = nodes.iterator();
		if (!nodes.isEmpty()) { // a path has always one node, at least!
			while (it.hasNext()) {
				TFlowElement flowElement = it.next().getValue();
				if (flowElement instanceof TSequenceFlow) {
					s.append(" -> ");
				}else {
					s.append(flowElement.getName());
				}
			}
		}
		s.append("]");
		return s.toString();
	}

}