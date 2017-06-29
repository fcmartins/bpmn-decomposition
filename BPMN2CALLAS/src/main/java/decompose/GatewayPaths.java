package decompose;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import decompose.DepthFirstGraphVisitor;
import decompose.Graph;
import decompose.Node;
import decompose.Path;

public class GatewayPaths {

	private Graph graph;
	private Set<Path> messageFlowPaths;
	private Deque<Node> pathNodes;

	public GatewayPaths(Graph graph) {
		this.graph = graph;
		messageFlowPaths = new TreeSet<Path>();
		pathNodes = new LinkedList<Node>();
	}

	public Set<Path> getTestRequirements() {
		for (Node node : graph.getInitialNodes()) {
			SimplePathCoverageVisitor visitor = new SimplePathCoverageVisitor(graph);
			node.accept(visitor);
		}
		return messageFlowPaths;
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
			/*
			 * If a path contains gateways adds the path starting
			 * from the first gateway found
			 */
			if (graph.isFinalNode(node) && graph.containsGateway(pathNodes)) {

				Deque<Node> toAdd = new LinkedList<Node>();
				boolean gatewayFound = false;

				for (Node n : pathNodes) {
					if (n.getValue().getClass().getName().equals("bpmn2.TGateway")
							|| n.getValue().getClass().getName().equals("bpmn2.TExclusiveGateway")
							|| n.getValue().getClass().getName().equals("bpmn2.TInclusiveGateway")) {
						gatewayFound = true;
					}

					if (gatewayFound) {
						toAdd.add(n);
					}
				}
				addPath(toAdd);
			}
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