package decompose;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public class MessageFlowPaths {

	private Graph graph;
	private Set<Path> paths;
	private Deque<Node> pathNodes;

	public MessageFlowPaths(Graph graph) {
		this.graph = graph;
		paths = new TreeSet<>();
		pathNodes = new LinkedList<>();
	}

	public Set<Path> getTestRequirements() {
		for (Node node : graph.getInitialNodes()) {
			SimplePathCoverageVisitor visitor = new SimplePathCoverageVisitor(
					graph);
			node.accept(visitor);
		}
		return paths;
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
			if (graph.isFinalNode(node) && graph.messageFlowContainsNode(pathNodes))
				addPath(pathNodes);
			return true;
		}

		private void addPath(Deque<Node> nodes) {
			Path toAdd = new Path(nodes);
			paths.add(toAdd);
		}

		@Override
		public void endVisit(Node node) {
			pathNodes.removeLast();
		}
	}
}