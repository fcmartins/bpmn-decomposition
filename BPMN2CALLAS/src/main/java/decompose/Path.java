package decompose;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import bpmn2.TEndEvent;


public class Path implements Iterable<Node>, Comparable<Path> {

	private List<Node> nodes;

	// for serialization purposes
	protected Path() {
	}
	
	/**
	 * Creates a new path from the given collection of nodes.
	 * 
	 * @param nodes
	 * @requires nodes.size() > 0
	 */
	public Path(Collection<Node> nodes) {
		this.nodes = new LinkedList<>(nodes);
	}

	public Iterator<Node> iterator() {
		return nodes.iterator();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("[");
		Iterator<Node> it = iterator();
		s.append(it.next()); // a path has always one node, at least!
		while (it.hasNext())
			s.append(", " + it.next());
		s.append("]");
		s.append("\n");
		return s.toString();
	}
	
	/**
	 * Compares two paths.
	 * 
	 * @param other
	 *            - The other path.
	 * 
	 * @return <ul>
	 *         <li>1 if the current path has more nodes than the other.</li>
	 *         <li>-1 if the other path has more nodes than the current path.</li>
	 *         <li>0 if they are equal.</li>
	 *         <li>-1 or 1 if the current node of the current path is greater or
	 *         lower than the current node of other path.</li>
	 *         </ul>
	 */
	@Override
	public int compareTo(Path other) {
		Iterator<Node> iterator = other.iterator();
		for (Node node : this)
			if (iterator.hasNext()) {
				Node otherNode = iterator.next();
				if (node.compareTo(otherNode) != 0)
					return node.compareTo(otherNode);
			} else
				return 1;
		return iterator.hasNext() ? -1 : 0;
	}

	public int size(){
		return nodes.size();
	}
	
	@Override
	public boolean equals(Object other){
		if (other == null){
	    	return false;
	    }
	    if (other == this){
	    	return true;
	    }
	    if (!(other instanceof Node)){
	    	return false;
	    }
	    Path otherPath = (Path)other;
	    Iterator<Node> iterator = otherPath.iterator();
	    for (Node node : this){
			if (iterator.hasNext()) {
				Node otherNode = iterator.next();
				if (node != otherNode)
					return false;
			} 
	    }
	    return true;
	}
	
	@Override
	public int hashCode(){
		int result = 31;
		
		for(Node node : nodes){
			result *= 13 + node.hashCode();
		}
		
		return result;
	}
	
	public  List<Node> getNodes(){
		return nodes;
	}
	
	public Node getInitialNode(){
		return nodes.get(0);
	}
	
	
	public boolean isSubpath(Path other){
		if(this.size() <= other.size()){
			
			List<Node> otherNodes = other.getNodes().subList(0, this.size());
			List<Node> thisNodes = this.getNodes();			
			
			for(int i = 0; i < this.size(); i++){
				if(thisNodes.get(i) != otherNodes.get(i)){
					
					if(i == thisNodes.size()-1 && thisNodes.get(i).getValue() instanceof TEndEvent){
						return true;
					}
								
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public void deleteNode(Node delete){
		
		for(int i = 0; i <nodes.size();i++){
			if(nodes.get(i) == delete){
				nodes.remove(i);
				return;
			}
		}
		
	}
	
}