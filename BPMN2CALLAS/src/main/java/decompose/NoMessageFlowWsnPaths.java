package decompose;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public class NoMessageFlowWsnPaths {
	private Graph graph;
	private Set<Path> messageFlowPaths;
	private Deque<Node> pathNodes;
	private final String wsn;

	public NoMessageFlowWsnPaths(Graph graph, String wsnName) {
		this.graph = graph;
		messageFlowPaths = new TreeSet<>();
		pathNodes = new LinkedList<>();
		wsn = wsnName;
	}

	public Set<Path> getTestRequirements() {
		for (Node node : graph.getInitialNodes()) {
			if (node.getPool().equals(wsn) && !isCommunicationNode(node)) {
				SimplePathCoverageVisitor visitor = new SimplePathCoverageVisitor(graph);
				node.accept(visitor);
			}
		}
		return messageFlowPaths;
	}

	/*
	 * Checks if a given initial node is the end node of a message flow edge
	 */
	private boolean isCommunicationNode(Node initialNode) {
		
		for (Edge edge : graph.getNodeEndEdges(initialNode))
			if (edge instanceof MessageFlowEdge)
				return true;
		
		return false;
	}

	private class SimplePathCoverageVisitor extends DepthFirstGraphVisitor {

		public SimplePathCoverageVisitor(Graph graph) {
			this.graph = graph;
			pathNodes.clear();
		}

		@Override
		public boolean visit(Node node) {
			if (pathNodes.contains(node))
				return false;
			pathNodes.addLast(node);
			if (graph.isFinalNode(node) && graph.noCommunicationNodes(pathNodes))
				addPath(pathNodes);
			return true;
		}

		private void addPath(Deque<Node> nodes) {
			Path toAdd = new Path(nodes);
			messageFlowPaths.add(toAdd);
		}

		@Override
		public void endVisit(Node node) {
			pathNodes.removeLast();
		}
	}
}